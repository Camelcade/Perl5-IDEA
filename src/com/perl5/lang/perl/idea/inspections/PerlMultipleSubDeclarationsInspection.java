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

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.psi.PerlSubDeclarationElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 14.06.2015.
 */
public class PerlMultipleSubDeclarationsInspection extends PerlInspection {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {

      @Override
      public void visitSubDeclarationElement(@NotNull PerlSubDeclarationElement o) {
        Project project = o.getProject();
        String canonicalName = o.getCanonicalName();

        if (PerlSubUtil.getSubDeclarations(project, canonicalName, GlobalSearchScope.projectScope(project)).size() > 1) {
          registerProblem(holder, o.getNameIdentifier(), "Multiple subs declarations found");
        }
      }
    };
  }
}
