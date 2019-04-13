/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.findusages;

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mixins.PerlCallArgumentsMixin;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

public class PerlReadWriteAccessDetector extends ReadWriteAccessDetector {
  private static final TokenSet VARIABLE_RW_CONTAINERS = TokenSet.create(
    SUFF_PP_EXPR, PREF_PP_EXPR
  );
  private static final TokenSet ASSIGN_RW_OPERATORS = TokenSet.create(
    OPERATOR_POW_ASSIGN,
    OPERATOR_PLUS_ASSIGN,
    OPERATOR_MINUS_ASSIGN,
    OPERATOR_MUL_ASSIGN,
    OPERATOR_DIV_ASSIGN,
    OPERATOR_MOD_ASSIGN,
    OPERATOR_CONCAT_ASSIGN,
    OPERATOR_X_ASSIGN,
    OPERATOR_BITWISE_AND_ASSIGN,
    OPERATOR_BITWISE_OR_ASSIGN,
    OPERATOR_BITWISE_XOR_ASSIGN,
    OPERATOR_SHIFT_LEFT_ASSIGN,
    OPERATOR_SHIFT_RIGHT_ASSIGN,
    OPERATOR_AND_ASSIGN,
    OPERATOR_OR_ASSIGN,
    OPERATOR_OR_DEFINED_ASSIGN
  );
  private static final TokenSet VARIABLE_FALL_THROUGH_ELEMENTS = TokenSet.create(
    COMMA_SEQUENCE_EXPR, PARENTHESISED_EXPR,
    ARRAY_ELEMENT, ARRAY_SLICE,
    HASH_ELEMENT, HASH_SLICE
  );

  private static final Set<String> LIST_MODIFYING_SUBS = ContainerUtil.set(
    "CORE::push", "CORE::pop", "CORE::unshift", "CORE::shift", "CORE::splice"
  );

  @Override
  public boolean isReadWriteAccessible(@NotNull PsiElement element) {
    return element instanceof PerlSubDefinitionElement ||
           element instanceof PerlVariableDeclarationElement ||
           element instanceof PerlNamespaceDefinitionElement;
  }

  @Override
  public boolean isDeclarationWriteAccess(@NotNull PsiElement element) {
    return false;
  }

  @NotNull
  @Override
  public Access getReferenceAccess(@NotNull PsiElement referencedElement, @NotNull PsiReference reference) {
    if (referencedElement instanceof PerlNamespaceDefinitionElement || referencedElement instanceof PerlSubDefinitionElement) {
      return Access.Read;
    }
    return getExpressionAccess(reference.getElement());
  }

  @NotNull
  @Override
  public Access getExpressionAccess(@NotNull PsiElement expression) {
    if (expression instanceof PerlVariableNameElement) {
      return getVariableAccess(expression.getParent(), expression);
    }
    return Access.Read;
  }

  private static Access getVariableAccess(@Nullable PsiElement expression, @NotNull PsiElement originalElement) {
    if (expression == null) {
      return Access.Read;
    }
    PsiElement parentExpression = expression.getParent();
    IElementType parentExpressionType = PsiUtilCore.getElementType(parentExpression);

    if (VARIABLE_FALL_THROUGH_ELEMENTS.contains(parentExpressionType)) {
      return getVariableAccess(parentExpression, originalElement);
    }
    if (parentExpression instanceof PerlCallArgumentsMixin) {
      PsiElement firstArgument = expression;
      if (expression instanceof PsiPerlCommaSequenceExpr) {
        PsiElement[] children = expression.getChildren();
        firstArgument = children.length > 0 ? children[0] : null;
      }

      if (firstArgument != null && PsiTreeUtil.isAncestor(firstArgument, originalElement, false)) {
        return getVariableAccess(parentExpression, originalElement);
      }
    }

    if (parentExpressionType == REGEX_EXPR) {
      IElementType rightPartType = PsiUtilCore.getElementType(parentExpression.getLastChild());
      if (rightPartType == REPLACEMENT_REGEX || rightPartType == TR_REGEX) {
        return Access.ReadWrite;
      }
    }
    else if (VARIABLE_RW_CONTAINERS.contains(parentExpressionType)) {
      return Access.ReadWrite;
    }
    else if (parentExpressionType == ASSIGN_EXPR) {
      if (expression.equals(parentExpression.getLastChild())) {
        return Access.Read;
      }
      else if (expression.equals(parentExpression.getFirstChild())) {
        if (!ASSIGN_RW_OPERATORS.contains(PsiUtilCore.getElementType(PerlPsiUtil.getNextSignificantSibling(expression)))) {
          return Access.Write;
        }
      }
      return Access.ReadWrite;
    }
    else if (parentExpressionType == FOREACH_ITERATOR) {
      return Access.Write;
    }
    else if (parentExpressionType == DELETE_EXPR) {
      return Access.ReadWrite;
    }
    else if (parentExpression instanceof PsiPerlSubCallExpr) {
      PsiPerlMethod method = ((PsiPerlSubCallExpr)parentExpression).getMethod();
      if (method != null) {
        PerlSubNameElement subNameElement = method.getSubNameElement();
        if (subNameElement != null) {
          for (PsiReference reference : subNameElement.getReferences()) {
            PsiElement target = reference.resolve();
            if (target instanceof PerlSubDefinitionElement &&
                LIST_MODIFYING_SUBS.contains(((PerlSubDefinitionElement)target).getCanonicalName())) {
              return Access.ReadWrite;
            }
          }
        }
      }
    }

    return Access.Read;
  }
}
