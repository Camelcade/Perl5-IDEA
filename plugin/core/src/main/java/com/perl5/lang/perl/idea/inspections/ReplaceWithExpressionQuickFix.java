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

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

class ReplaceWithExpressionQuickFix implements LocalQuickFix {
  private final @NotNull String myTargetKeyword;

  public ReplaceWithExpressionQuickFix(@NotNull String targetKeyword) {
    myTargetKeyword = targetKeyword;
  }

  @Override
  public @Nls @NotNull String getFamilyName() {
    return PerlBundle.message("perl.inspection.loop.control.convert", myTargetKeyword);
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    PsiElement targetElement = descriptor.getPsiElement();
    PerlFileImpl file = PerlElementFactory.createFile(targetElement.getProject(), myTargetKeyword);
    PsiElement statement = file.getFirstChild();
    if (statement == null) {
      return;
    }
    PsiElement expression = statement.getFirstChild();
    if (expression != null) {
      targetElement.replace(expression);
    }
  }
}
