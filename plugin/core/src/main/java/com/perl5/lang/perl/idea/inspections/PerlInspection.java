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

package com.perl5.lang.perl.idea.inspections;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.codeInspection.ProblemHighlightType.*;


public abstract class PerlInspection extends LocalInspectionTool {

  protected void registerProblem(@NotNull ProblemsHolder holder,
                                 @Nullable PsiElement element,
                                 String message,
                                 LocalQuickFix... quickFixes) {
    doRegisterProblem(holder, element, message, GENERIC_ERROR_OR_WARNING, quickFixes);
  }

  protected void registerError(@NotNull ProblemsHolder holder, @Nullable PsiElement element, String message, LocalQuickFix... quickFixes) {
    doRegisterProblem(holder, element, message, GENERIC_ERROR, quickFixes);
  }

  protected void markDeprecated(@NotNull ProblemsHolder holder, @Nullable PsiElement element, String message, LocalQuickFix... quickFixes) {
    doRegisterProblem(holder, element, message, LIKE_DEPRECATED, quickFixes);
  }

  private void doRegisterProblem(@NotNull ProblemsHolder holder,
                                 @Nullable PsiElement element,
                                 @NotNull String message,
                                 @NotNull ProblemHighlightType highlightType,
                                 @NotNull LocalQuickFix... quickFixes) {
    if (element == null) {
      return;
    }
    TextRange range = ElementManipulators.getValueTextRange(element);
    if (!range.isEmpty()) {
      holder.registerProblem(element, message, highlightType, range, quickFixes);
    }
  }
}
