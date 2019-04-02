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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.util.PerlArrayUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by hurricup on 30.04.2016.
 */
public interface PerlAssignExpression extends PsiPerlExpr {
  /**
   * Returns the leftmost side of assign expression
   *
   * @return left side
   */
  @NotNull
  default PsiElement getLeftSide() {
    return getFirstChild();
  }

  /**
   * Returns the rightmost side of assignment expression
   *
   * @return rightmost side or null if expression is incomplete
   */
  @Nullable
  default PsiElement getRightSide() {
    PsiElement lastChild = getLastChild();
    PsiElement firstChild = getFirstChild();

    if (lastChild == firstChild || lastChild == null || lastChild.getFirstChild() == null) {
      return null;
    }

    return lastChild;
  }

  /**
   * @return an assignment expression if {@code element} is a part of one.
   * Unwraps multi-variable declarations and passing through any empty wrappers, e.g. variable declaration
   */
  @Nullable
  @Contract("null->null")
  static PerlAssignExpression getAssignmentExpression(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }
    PerlAssignExpression assignExpression = PsiTreeUtil.getParentOfType(element, PerlAssignExpression.class);
    if (assignExpression == null) {
      return null;
    }

    return Arrays.stream(assignExpression.getChildren())
             .flatMap(it -> PerlArrayUtil.collectListElements(it).stream())
             .flatMap(it -> it instanceof PerlVariableDeclarationExpr ?
                            ((PerlVariableDeclarationExpr)it).getVariableDeclarationElementList().stream() :
                            Stream.of(it))
             .anyMatch(it -> it != null && it.getTextRange().equals(element.getTextRange())) ? assignExpression : null;
  }
}
