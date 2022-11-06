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
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlMethod;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.PACKAGE;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.QUALIFYING_PACKAGE;


public abstract class PerlMethodMixin extends PerlCompositeElementImpl implements PerlMethod {
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
