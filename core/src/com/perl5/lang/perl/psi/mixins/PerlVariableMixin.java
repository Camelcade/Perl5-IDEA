/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.Processor;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlStaticValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlBuiltInVariable;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlUnknownValue.UNKNOWN_VALUE;
import static com.perl5.lang.perl.util.PerlPackageUtil.MAIN_PACKAGE;

/**
 * Created by hurricup on 24.05.2015.
 */
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
      return MAIN_PACKAGE;
    }
    return PerlPackageUtil.getCanonicalPackageName(qualifiedRanges.first.subSequence(variableName).toString());
  }

  @Nullable
  @Override
  public PerlVariableNameElement getVariableNameElement() {
    return findChildByClass(PerlVariableNameElement.class);
  }

  @NotNull
  @Override
  public PerlValue computePerlValue() {
    if (!(this instanceof PsiPerlScalarVariable)) {
      return UNKNOWN_VALUE;
    }

    PerlVariableNameElement variableNameElement = getVariableNameElement();

    if (variableNameElement != null) {
      // find lexically visible declaration and check type
      final PerlVariableDeclarationElement declarationWrapper = getLexicalDeclaration();
      if (declarationWrapper != null) {
        PerlValue declaredValue = declarationWrapper.getDeclaredValue();
        if (declarationWrapper instanceof PerlBuiltInVariable ||
            declarationWrapper instanceof PerlImplicitVariableDeclaration && !declaredValue.isEmpty()) {
          return declaredValue;
        }

        if (declarationWrapper.isInvocantDeclaration() || declarationWrapper.isSelf()) {
          PerlSelfHinter selfHinter = PsiTreeUtil.getParentOfType(declarationWrapper, PerlSelfHinter.class);
          if (selfHinter != null) {
            return selfHinter.getSelfType();
          }
          return PerlStaticValue.create(PerlPackageUtil.getContextNamespaceName(declarationWrapper));
        }

        // check explicit type in declaration
        if (!declaredValue.isEmpty()) {
          return declaredValue;
        }

        // fixme this is bad, because my $var1 && print $var1 will be valid, but it's not
        PerlLexicalScope perlLexicalScope = PsiTreeUtil.getParentOfType(declarationWrapper, PerlLexicalScope.class);
        assert perlLexicalScope != null : "Unable to find lexical scope for:" +
                                          declarationWrapper.getClass() +
                                          " at " +
                                          declarationWrapper.getTextOffset() +
                                          " in " +
                                          declarationWrapper.getContainingFile();

        Ref<PerlValue> resultRef = Ref.create();

        int startOffset = declarationWrapper.getTextRange().getStartOffset();
        int endOffset = getTextRange().getStartOffset();

        if (startOffset < endOffset) { // fixme #2016
          PerlPsiUtil.processElementsInRangeBackward(
            perlLexicalScope,
            new TextRange(startOffset, endOffset),
            element -> {
              if (element != PerlVariableMixin.this &&
                  element instanceof PsiPerlScalarVariable
              ) {
                PsiElement variableNameElement1 = ((PsiPerlScalarVariable)element).getVariableNameElement();

                if (variableNameElement1 != null &&
                    (element.getParent().equals(declarationWrapper) ||
                     variableNameElement1.getReference() != null && variableNameElement1.getReference().isReferenceTo(declarationWrapper))
                ) {
                  // found variable assignment
                  PerlAssignExpression assignmentExpression = PerlAssignExpression.getAssignmentExpression(element);
                  if (assignmentExpression != null) {
                    resultRef.set(PerlValue.from(PerlContextType.from(element), assignmentExpression.getRightPartOfAssignment(element)));
                    return false;
                  }
                }
              }
              return true;
            }
          );
        }

        if (!resultRef.isNull()) {
          return resultRef.get();
        }
      }

      // checking global declarations with explicit types
      for (PerlVariableDeclarationElement declaration : getGlobalDeclarations()) {
        PerlValue declaredValue = declaration.getDeclaredValue();
        if (!declaredValue.isEmpty()) {
          return declaredValue;
        }
      }
    }

    return UNKNOWN_VALUE;
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


  @Override
  public boolean isBuiltIn() {
    return getLexicalDeclaration() instanceof PerlBuiltInVariable;
  }

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

  // fixme this need to be improved very much
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

    PerlValue namespaceValue = PerlStaticValue.create(
      ObjectUtils.notNull(getExplicitNamespaceName(), PerlPackageUtil.getContextNamespaceName(this)));

    return namespaceValue
      .processNamespaceNames(getProject(), getResolveScope(), it -> processor.process(PerlPackageUtil.join(it, variableName)));
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
