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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.mixins.PerlSubDeclarationBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface PerlReturnExpr extends PsiElement {
  /**
   * @return PsiElement representing return value, if any
   */
  @Nullable
  default PsiPerlExpr getReturnValueExpr() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlExpr.class);
  }

  /**
   * @return PsiElement we are returning from
   */
  @NotNull
  default PsiElement getReturnScope() {
    return Objects.requireNonNull(PsiTreeUtil.getParentOfType(
      this,
      PerlSortExpr.class,
      PerlSubExpr.class,
      PerlEvalExpr.class,
      PerlSubDeclarationBase.class,
      PsiFile.class
    ));
  }
}
