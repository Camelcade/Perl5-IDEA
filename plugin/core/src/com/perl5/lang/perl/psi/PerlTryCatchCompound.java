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
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PerlTryCatchCompound extends PsiElement {
  @NotNull
  default PerlTryExpr getTryExpression() {
    PerlTryExpr tryExpr = PsiTreeUtil.getChildOfType(this, PerlTryExpr.class);
    assert tryExpr != null : "No try expression in " + getText();
    return tryExpr;
  }

  @Nullable
  default PerlCatchExpr getCatchExpression() {
    return PsiTreeUtil.getChildOfType(this, PerlCatchExpr.class);
  }
}
