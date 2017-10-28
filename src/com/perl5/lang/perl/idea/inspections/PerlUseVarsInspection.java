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

package com.perl5.lang.perl.idea.inspections;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.quickfixes.PerlUseVarsQuickFix;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlUseStatement;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.util.PerlPackageUtil.PACKAGE_VARS;

public class PerlUseVarsInspection extends PerlInspection {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {
      @Override
      public void visitUseStatement(@NotNull PsiPerlUseStatement o) {
        if (PACKAGE_VARS.equals(o.getPackageName())) {
          holder.registerProblem(
            o,
            PerlBundle.message("perl.inspection.use.vars"),
            ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
            new PerlUseVarsQuickFix(o)
          );
        }
      }
    };
  }
}
