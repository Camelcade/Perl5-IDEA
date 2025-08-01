/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

import java.util.List;

public interface PerlTryCatchExpr extends PsiElement {
  default @NotNull PerlTryExpr getTryExpression() {
    PerlTryExpr tryExpr = PsiTreeUtil.getChildOfType(this, PerlTryExpr.class);
    assert tryExpr != null : "No try expression in " + getText();
    return tryExpr;
  }

  /**
   * @return list of catch and continuation expressions in natural order. Continuation is just a hacky catch for continuation exceptions
   */
  default @NotNull List<PerlCatchExpr> getCatchExpressions() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PerlCatchExpr.class);
  }

  default @NotNull List<PsiPerlFinallyExpr> getFinallyExpressions() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlFinallyExpr.class);
  }

  default @NotNull List<PsiPerlExceptExpr> getExceptExpressions() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlExceptExpr.class);
  }

  default @NotNull List<PsiPerlContinuationExpr> getContinuationExpressions() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlContinuationExpr.class);
  }

  default @NotNull List<PsiPerlOtherwiseExpr> getOtherwiseExpressions() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlOtherwiseExpr.class);
  }
}
