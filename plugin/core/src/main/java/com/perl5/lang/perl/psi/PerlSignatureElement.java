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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PerlSignatureElement extends PerlLexicalVariableDeclarationMarker {
  default @Nullable PsiElement getTypeConstraintElement() {
    PsiElement[] children = getChildren();
    if (children.length == 3) {
      return children[0];
    }
    if (children.length < 2) {
      return null;
    }
    return isDeclarationElement(children[1]) ? children[0] : null;
  }

  default @Nullable PsiElement getDeclarationElement() {
    PsiElement[] children = getChildren();
    if (children.length == 3) {
      return children[1];
    }
    if (children.length == 1) {
      return children[0];
    }
    return isDeclarationElement(children[0]) ? children[0] : children[1];
  }

  /**
   * @return true iff {@code psiElement} is variable declaration or ignore element
   */
  static boolean isDeclarationElement(@NotNull PsiElement psiElement) {
    return psiElement instanceof PerlVariableDeclaration || psiElement instanceof PsiPerlSubSignatureElementIgnore;
  }

  default @Nullable PsiElement getDefaultValueElement() {
    PsiElement[] children = getChildren();
    if (children.length == 3) {
      return children[2];
    }
    if (children.length < 2) {
      return null;
    }
    return isDeclarationElement(children[1]) ? null : children[1];
  }

  default boolean hasDefaultValueElement() {
    return getDefaultValueElement() != null;
  }

  default boolean hasDeclarationElement() {
    return getDeclarationElement() != null;
  }
}
