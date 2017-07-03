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

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import static com.intellij.codeInspection.ProblemHighlightType.*;

/**
 * Created by hurricup on 14.06.2015.
 */
public abstract class PerlInspection extends LocalInspectionTool {

  protected void registerProblem(ProblemsHolder holder, PsiElement element, String message) {
    doRegisterProblem(holder, element, message, GENERIC_ERROR_OR_WARNING);
  }

  protected void registerError(ProblemsHolder holder, PsiElement element, String message) {
    doRegisterProblem(holder, element, message, GENERIC_ERROR);
  }

  protected void markDeprecated(ProblemsHolder holder, PsiElement element, String message) {
    doRegisterProblem(holder, element, message, LIKE_DEPRECATED);
  }

  private void doRegisterProblem(@NotNull ProblemsHolder holder,
                                 @NotNull PsiElement element,
                                 @NotNull String message,
                                 @NotNull ProblemHighlightType highlightType) {
    TextRange range = ElementManipulators.getValueTextRange(element);
    if (!range.isEmpty()) {
      holder.registerProblem(element, message, highlightType, range);
    }
  }
}
