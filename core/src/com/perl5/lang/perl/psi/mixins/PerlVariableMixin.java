/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.Processor;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlBuiltInVariable;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.util.PerlPackageUtil.MAIN_NAMESPACE_NAME;


public abstract class PerlVariableMixin extends PerlCompositeElementImpl implements PerlElementTypes, PerlVariable {
  public PerlVariableMixin(ASTNode node) {
    super(node);
  }

  @Nullable
  public String getExplicitNamespaceName() {
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

  @Nullable
  @Override
  public PerlVariableNameElement getVariableNameElement() {
    return findChildByClass(PerlVariableNameElement.class);
  }

  @NotNull
  @Override
  public PerlValue computePerlValue() {
    PerlVariableNameElement variableNameElement = getVariableNameElement();

    if (variableNameElement == null) {
      return UNKNOWN_VALUE;
    }
    PerlVariableDeclarationElement lexicalDeclaration = PerlResolveUtil.getLexicalDeclaration(this);
    if (lexicalDeclaration != null &&
        (lexicalDeclaration.isInvocantDeclaration() || lexicalDeclaration.isSelf())) {
      PerlSelfHinter selfHinter = PsiTreeUtil.getParentOfType(lexicalDeclaration, PerlSelfHinter.class);
      if (selfHinter != null) {
        return selfHinter.getSelfType();
      }
      return PerlScalarValue.create(PerlPackageUtil.getContextNamespaceName(lexicalDeclaration));
    }

    PerlValue inferredValue = PerlResolveUtil.inferVariableValue(this);
    if (!inferredValue.isEmpty()) {
      return inferredValue;
    }

    return lexicalDeclaration == null ? UNKNOWN_VALUE : lexicalDeclaration.getDeclaredValue();
  }

  @Override
  public PerlVariableType getActualType() {
    PsiElement variableContainer = this.getParent();

    if (this instanceof PsiPerlCodeVariable) {
      return PerlVariableType.SCALAR;
    }
    else if (
      variableContainer instanceof PsiPerlHashElement
      || variableContainer instanceof PsiPerlHashSlice
      || this instanceof PsiPerlHashVariable
      ) {
      return PerlVariableType.HASH;
    }
    else if (
      variableContainer instanceof PsiPerlArraySlice
      || variableContainer instanceof PsiPerlArrayElement
      || this instanceof PsiPerlArrayIndexVariable
      || this instanceof PsiPerlArrayVariable
      ) {
      return PerlVariableType.ARRAY;
    }
    else if (
      variableContainer instanceof PsiPerlDerefExpr
      || this instanceof PsiPerlScalarVariable
      ) {
      return PerlVariableType.SCALAR;
    }
    else {
      throw new RuntimeException("Can't be: could not detect actual type of myVariable: " + getText());
    }
  }

  /**
   * @deprecated should be detected from resolve result
   */
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

    // fixme this been used already
    for (PsiReference reference : variableNameElement.getReferences()) {
      if (reference instanceof PsiPolyVariantReference) {
        for (ResolveResult resolveResult : ((PsiPolyVariantReference)reference).multiResolve(false)) {
          PsiElement targetElement = resolveResult.getElement();
          if (targetElement instanceof PerlVariableDeclarationElement && ((PerlVariableDeclarationElement)targetElement).isDeprecated()) {
            return true;
          }
        }
      }
      else {
        PsiElement targetElement = reference.resolve();
        if (targetElement instanceof PerlVariableDeclarationElement && ((PerlVariableDeclarationElement)targetElement).isDeprecated()) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public String getName() {
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

    for (PsiReference psiReference : variableNameElement.getReferences()) {
      if (psiReference instanceof PsiPolyVariantReference) {
        for (ResolveResult resolveResult : ((PsiPolyVariantReference)psiReference).multiResolve(false)) {
          PsiElement element = resolveResult.getElement();
          if (element instanceof PerlVariableDeclarationElement) {
            return (PerlVariableDeclarationElement)element;
          }
        }
      }
      else {
        PsiElement target = psiReference.resolve();
        if (target != null) {
          return (PerlVariableDeclarationElement)target;
        }
      }
    }

    return null;
  }

  private boolean processContainingNamespaceItems(@NotNull Processor<String> processor) {
    String variableName = getName();
    if (StringUtil.isEmpty(variableName)) {
      return true;
    }

    String namespaceName = ObjectUtils.notNull(getExplicitNamespaceName(), PerlPackageUtil.getContextNamespaceName(this));
    return processor.process(PerlPackageUtil.join(namespaceName, variableName));
  }

  // fixme this need to be moved to PerlResolveUtil or Resolver
  @Override
  public List<PerlVariableDeclarationElement> getGlobalDeclarations() {
    List<PerlVariableDeclarationElement> result = new ArrayList<>();
    PerlVariableType myType = getActualType();

    PsiElement parent = getParent(); // wrapper if any

    processContainingNamespaceItems(canonicalName -> {
      if (myType == PerlVariableType.SCALAR) {
        for (PerlVariableDeclarationElement variable : PerlScalarUtil.getGlobalScalarDefinitions(getProject(), canonicalName)) {
          if (!variable.equals(parent)) {
            result.add(variable);
          }
        }
      }
      else if (myType == PerlVariableType.ARRAY) {
        for (PerlVariableDeclarationElement variable : PerlArrayUtil.getGlobalArrayDefinitions(getProject(), canonicalName)) {
          if (!variable.equals(parent)) {
            result.add(variable);
          }
        }
      }
      else if (myType == PerlVariableType.HASH) {
        for (PerlVariableDeclarationElement variable : PerlHashUtil.getGlobalHashDefinitions(getProject(), canonicalName)) {
          if (!variable.equals(parent)) {
            result.add(variable);
          }
        }
      }
      return true;
    });

    return result;
  }

  @Override
  public List<PerlGlobVariable> getRelatedGlobs() {
    List<PerlGlobVariable> result = new ArrayList<>();
    processContainingNamespaceItems(it -> {
      result.addAll(PerlGlobUtil.getGlobsDefinitions(getProject(), it));
      return true;
    });
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
