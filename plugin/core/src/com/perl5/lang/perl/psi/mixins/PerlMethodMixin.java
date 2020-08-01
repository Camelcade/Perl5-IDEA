/*
 * Copyright 2015-2020 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.*;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.PACKAGE;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.QUALIFYING_PACKAGE;


public abstract class PerlMethodMixin extends PerlCompositeElementImpl implements PerlMethod {
  private static final Logger LOG = Logger.getInstance(PerlMethodMixin.class);

  public PerlMethodMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public @Nullable String getExplicitNamespaceName() {
    PerlNamespaceElement namespaceElement = getNamespaceElement();
    return namespaceElement != null ? namespaceElement.getCanonicalName() : null;
  }

  @Override
  public String getName() {
    PerlSubNameElement subNameElement = getSubNameElement();
    return subNameElement == null ? null : subNameElement.getText();
  }

  @Override
  public @NotNull PerlValue computePerlValue() {
    PerlValue subNameValue = PerlScalarValue.create(getName());
    if (subNameValue == UNKNOWN_VALUE) {
      return UNKNOWN_VALUE;
    }

    String explicitNamespaceName = getExplicitNamespaceName();
    boolean hasExplicitNamespace = StringUtil.isNotEmpty(explicitNamespaceName);
    PsiElement parentElement = getParent();
    boolean isNestedCall = PerlSubCallElement.isNestedCall(parentElement);

    List<PerlValue> callArguments;
    if (parentElement instanceof PerlMethodContainer) {
      callArguments = ContainerUtil.map(((PerlMethodContainer)parentElement).getCallArgumentsList(), PerlValuesManager::from);
    }
    else {
      // these are sort and code variable
      LOG.debug("Non-method container container: " + parentElement.getClass().getSimpleName());
      callArguments = Collections.emptyList();
    }

    PsiElement derefExpression = parentElement.getParent();
    if (isNestedCall && parentElement.getPrevSibling() != null) {
      boolean isSuper = PerlPackageUtil.isSUPER(explicitNamespaceName);
      if (hasExplicitNamespace && !isSuper) { // awkward $var->Foo::Bar::method->
        return new PerlCallStaticValue(PerlScalarValue.create(explicitNamespaceName), subNameValue, callArguments, true);
      }
      String superContext = isSuper ? PerlPackageUtil.getContextNamespaceName(this) : null;
      if (!(derefExpression instanceof PerlDerefExpression)) {
        LOG.warn("Expected deref expression, got " + derefExpression);
        return UNKNOWN_VALUE;
      }

      PsiElement previousValue = ((PerlDerefExpression)derefExpression).getPreviousElement(parentElement);
      if (previousValue == null) { // first in chain
        return new PerlCallStaticValue(
          PerlScalarValue.create(PerlPackageUtil.getContextNamespaceName(this)), subNameValue, callArguments, false);
      }
      return PerlCallObjectValue.create(PerlValuesManager.from(previousValue), subNameValue, callArguments, superContext);
    }
    else if (isObjectMethod() && hasExplicitNamespace) { // this is for a fancy call new Foo::Bar
      return PerlCallObjectValue.create(PerlScalarValue.create(explicitNamespaceName), subNameValue, callArguments, null);
    }
    else if (hasExplicitNamespace) {
      return new PerlCallStaticValue(PerlScalarValue.create(explicitNamespaceName), subNameValue, callArguments, true);
    }
    else {
      return new PerlCallStaticValue(
        PerlScalarValue.create(PerlPackageUtil.getContextNamespaceName(this)), subNameValue, callArguments, false);
    }
  }

  @Override
  public boolean isObjectMethod() {
    boolean hasExplicitNamespace = hasExplicitNamespace();
    boolean isNestedCall = PerlSubCallElement.isNestedCall(getParent());

    return !hasExplicitNamespace && isNestedCall            // part of ..->method()
           || hasExplicitNamespace && getFirstChild() instanceof PerlSubNameElement    // method Foo::Bar
           || hasExplicitNamespace        // SUPER::method
              && isNestedCall
              && getFirstChild() instanceof PerlNamespaceElement
              && ((PerlNamespaceElement)getFirstChild()).isSUPER()
      ;
  }

  @Override
  public @Nullable PerlNamespaceElement getNamespaceElement() {
    PsiElement childByType = findChildByType(QUALIFYING_PACKAGE);
    if (childByType == null) {
      childByType = findChildByType(PACKAGE);
    }
    return childByType == null ? null : (PerlNamespaceElement)childByType;
  }

  /**
   * fixme this not working with fallback tokens
   */
  @Override
  public @Nullable PerlSubNameElement getSubNameElement() {
    return findChildByClass(PerlSubNameElement.class);
  }
}
