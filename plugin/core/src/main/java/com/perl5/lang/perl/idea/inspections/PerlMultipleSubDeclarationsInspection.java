/*
 * Copyright 2015-2024 Alexandr Evstigneev
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
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.PerlSubDeclarationElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;


public class PerlMultipleSubDeclarationsInspection extends PerlInspection {
  @Override
  public @NotNull PsiElementVisitor buildVisitor(final @NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {

      @Override
      protected boolean shouldVisitLightElements() {
        return true;
      }

      @Override
      public void visitSubDeclarationElement(@NotNull PerlSubDeclarationElement o) {
        Project project = o.getProject();
        String canonicalName = o.getCanonicalName();

        if (PerlSubUtil.getSubDeclarations(project, canonicalName, GlobalSearchScope.projectScope(project)).size() > 1) {
          registerProblem(holder, o.getNameIdentifier(), PerlBundle.message("inspection.message.multiple.subs.declarations.found"));
        }
      }
    };
  }
}
