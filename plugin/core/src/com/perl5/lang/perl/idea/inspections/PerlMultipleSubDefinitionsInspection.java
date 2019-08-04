/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;


public class PerlMultipleSubDefinitionsInspection extends PerlInspection {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {
      @Override
      public void visitPerlSubDefinitionElement(@NotNull PerlSubDefinitionElement o) {
        Project project = o.getProject();
        String name = "Sub";

        String canonicalName = o.getCanonicalName();
        GlobalSearchScope searchScope;
        if (PerlPackageUtil.isMain(o.getNamespaceName()) && PerlSharedSettings.getInstance(project).SIMPLE_MAIN_RESOLUTION) {
          searchScope = GlobalSearchScope.fileScope(o.getContainingFile());
        }
        else {
          searchScope = GlobalSearchScope.projectScope(project);
        }
        if (PerlSubUtil.getSubDefinitions(project, canonicalName, searchScope).size() > 1) {
          registerProblem(holder, o.getNameIdentifier(),
                          PerlBundle.message("perl.inspection.multiple.sub.declarations", name.toLowerCase()));
        }
        super.visitPerlSubDefinitionElement(o);
      }

      @Override
      protected boolean shouldVisitLightElements() {
        return true;
      }
    };
  }
}
