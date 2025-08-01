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

package com.perl5.lang.perl.idea.inspections;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.utils.PerlResolveUtilCore;
import org.jetbrains.annotations.NotNull;


public class PerlUnresolvedVariableInspection extends PerlInspection {
  @Override
  public @NotNull PsiElementVisitor buildVisitor(final @NotNull ProblemsHolder holder, boolean isOnTheFly) {

    return new PerlVisitor() {
      @Override
      public void visitPerlVariable(final @NotNull PerlVariable variable) {
        PsiElement parent = variable.getParent();

        if (parent instanceof PerlVariableDeclarationElement || variable.isBuiltIn()) {
          return;
        }

        PerlVariableNameElement variableNameElement = variable.getVariableNameElement();

        if (!PerlResolveUtilCore.isResolvable(variableNameElement)) {
          registerProblem(holder, variableNameElement, PerlBundle.message("inspection.message.unable.to.find.variable.declaration"));
        }
      }
    };
  }
}
