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

package com.perl5.lang.perl.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.PsiPerlMapExpr;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlBuiltInVariable extends PerlImplicitVariableDeclaration {
  public PerlBuiltInVariable(@NotNull PsiManager manager,
                             @NotNull String variableName) {
    super(manager, variableName, null, false, false, false, null);
  }

  @Nullable
  @Override
  public String getPackageName() {
    return PerlPackageUtil.MAIN_PACKAGE;
  }

  @Override
  public String toString() {
    return "Built-in: " + getVariableType().getSigil() + getVariableName();
  }

  @Override
  public boolean isBuiltIn() {
    return true;
  }

}
