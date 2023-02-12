/*
 * Copyright 2015-2023 Alexandr Evstigneev
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
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import com.intellij.util.Processor;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlGlobVariableElement;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import com.perl5.lang.perl.psi.impl.PerlBuiltInVariable;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.util.PerlPackageUtil.MAIN_NAMESPACE_NAME;


public abstract class PerlVariableMixin extends PerlCompositeElementImpl implements PerlElementTypes, PerlVariable {
  public PerlVariableMixin(ASTNode node) {
    super(node);
  }

  @Override
  public @Nullable String getExplicitNamespaceName() {
    PerlVariableNameElement variableNameElement = getVariableNameElement();
    if (variableNameElement == null) {
      return null;
    }

    assert variableNameElement instanceof LeafPsiElement;
    CharSequence variableName = ((LeafPsiElement)variableNameElement).getChars();

    Pair<TextRange, TextRange> qualifiedRanges = PerlPackageUtil.getQualifiedRanges(variableName);
    if (qualifiedRanges.first == null) {
      return null;
    }
    else if (qualifiedRanges.first == TextRange.EMPTY_RANGE) {
      return MAIN_NAMESPACE_NAME;
    }
    return PerlPackageUtil.getCanonicalNamespaceName(qualifiedRanges.first.subSequence(variableName).toString());
  }

  @Override
  public @Nullable PerlVariableNameElement getVariableNameElement() {
    return findChildByClass(PerlVariableNameElement.class);
  }

  @Override
  public @NotNull PerlVariableType getActualType() {
    IElementType parentElementType = PsiUtilCore.getElementType(getParent());

    if (parentElementType == HASH_ELEMENT || parentElementType == HASH_SLICE || parentElementType == HASH_HASH_SLICE) {
      return PerlVariableType.HASH;
    }
    else if (parentElementType == ARRAY_SLICE || parentElementType == ARRAY_ELEMENT || parentElementType == HASH_ARRAY_SLICE) {
      return PerlVariableType.ARRAY;
    }

    IElementType elementType = PsiUtilCore.getElementType(this);
    if (elementType == CODE_VARIABLE) {
      return PerlVariableType.SCALAR;
    }
    else if (elementType == ARRAY_INDEX_VARIABLE || elementType == ARRAY_VARIABLE) {
      return PerlVariableType.ARRAY;
    }
    else if (elementType == HASH_VARIABLE) {
      return PerlVariableType.HASH;
    }
    else if (elementType == SCALAR_VARIABLE) {
      return PerlVariableType.SCALAR;
    }
    throw new RuntimeException("Can't be: could not detect actual type of myVariable: " + getText());
  }

  /**
   * @deprecated should be detected from resolve result
   */
  @Deprecated
  @Override
  public boolean isBuiltIn() {
    return getLexicalDeclaration() instanceof PerlBuiltInVariable;
  }

  /**
   * @deprecated deprecation decided by declaration
   */
  @Deprecated
  @Override
  public boolean isDeprecated() {
    PsiElement parent = getParent();

    if (parent instanceof PerlVariableDeclarationElement) {
      return ((PerlVariableDeclarationElement)parent).isDeprecated();
    }

    PerlVariableNameElement variableNameElement = getVariableNameElement();
    if (variableNameElement == null) {
      return false;
    }

    Ref<Boolean> resultRef = Ref.create();
    PerlResolveUtil.processResolveTargets((targetElement, __) -> {
      if (targetElement instanceof PerlVariableDeclarationElement && ((PerlVariableDeclarationElement)targetElement).isDeprecated()) {
        resultRef.set(true);
        return false;
      }
      return true;
    }, variableNameElement);

    return !resultRef.isNull();
  }

  @Override
  public @Nullable String getName() {
    PerlVariableNameElement variableNameElement = getVariableNameElement();
    if (variableNameElement == null) {
      return null;
    }
    assert variableNameElement instanceof LeafPsiElement;
    CharSequence variableName = ((LeafPsiElement)variableNameElement).getChars();
    Pair<TextRange, TextRange> qualifiedRanges = PerlPackageUtil.getQualifiedRanges(variableName);
    assert qualifiedRanges.second != null;
    return qualifiedRanges.second.subSequence(variableName).toString();
  }

  /**
   * @deprecated semantic of this method is hardly understandable
   */
  @Deprecated
  @Override
  public PerlVariableDeclarationElement getLexicalDeclaration() {
    if (getExplicitNamespaceName() != null) {
      return null;
    }

    PerlVariableNameElement variableNameElement = getVariableNameElement();
    if (variableNameElement == null) {
      return null;
    }

    Ref<PerlVariableDeclarationElement> resultRef = Ref.create();
    PerlResolveUtil.processResolveTargets((target, __) -> {
      if (target instanceof PerlVariableDeclarationElement) {
        resultRef.set((PerlVariableDeclarationElement)target);
        return false;
      }
      return true;
    }, variableNameElement);

    return resultRef.get();
  }

  // fixme this need to be moved to PerlResolveUtil or Resolver
  @Override
  public @NotNull List<PerlVariableDeclarationElement> getGlobalDeclarations() {
    String variableName = getName();
    if (variableName == null) {
      return Collections.emptyList();
    }

    List<PerlVariableDeclarationElement> result = new ArrayList<>();
    PerlVariableType myType = getActualType();

    PsiElement parent = getParent(); // wrapper if any
    String namespaceName = ObjectUtils.notNull(getExplicitNamespaceName(), PerlPackageUtil.getContextNamespaceName(this));
    String fullQualifiedName = PerlPackageUtil.join(namespaceName, variableName);

    Processor<PerlVariableDeclarationElement> variableProcessor = it -> {
      // fixme we must check fqn, because atm implicit variables are processed without NS restriction
      if (!it.equals(parent) && fullQualifiedName.equals(it.getCanonicalName())) {
        result.add(it);
      }
      return true;
    };
    if (myType == PerlVariableType.SCALAR) {
      PerlScalarUtil.processDefinedGlobalScalars(getProject(), getResolveScope(), variableProcessor, true, namespaceName);
    }
    else if (myType == PerlVariableType.ARRAY) {
      PerlArrayUtil.processDefinedGlobalArrays(getProject(), getResolveScope(), variableProcessor, true, namespaceName);
    }
    else if (myType == PerlVariableType.HASH) {
      PerlHashUtil.processDefinedGlobalHashes(getProject(), getResolveScope(), variableProcessor, true, namespaceName);
    }

    return result;
  }

  @Override
  public @NotNull List<PerlGlobVariableElement> getRelatedGlobs() {
    String variableName = getName();
    if (variableName == null) {
      return Collections.emptyList();
    }
    String namespaceName = ObjectUtils.notNull(getExplicitNamespaceName(), PerlPackageUtil.getContextNamespaceName(this));
    String fullQualifiedName = PerlPackageUtil.join(namespaceName, variableName);

    List<PerlGlobVariableElement> result = new ArrayList<>();
    PerlGlobUtil.processDefinedGlobs(getProject(), getResolveScope(), fullQualifiedName::equals, result::add, true, namespaceName);
    return result;
  }

  @Override
  public int getLineNumber() {
    Document document = PsiDocumentManager.getInstance(getProject()).getCachedDocument(getContainingFile());
    return document == null ? 0 : document.getLineNumber(getTextOffset()) + 1;
  }

  @Override
  public boolean isDeclaration() {
    return getParent() instanceof PerlVariableDeclarationElement;
  }
}
