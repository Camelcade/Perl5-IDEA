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
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionWithIdentifier;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 14.06.2015.
 */
public class PerlMultipleNamespaceDefinitionsInspection extends PerlInspection {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {

      @Override
      public void visitPerlNamespaceDefinitionWithIdentifier(@NotNull PerlNamespaceDefinitionWithIdentifier o) {
        Project project = o.getProject();
        String packageName = o.getPackageName();
        if (packageName != null &&
            !PerlPackageUtil.MAIN_PACKAGE.equals(packageName) &&
            PerlPackageUtil.getNamespaceDefinitions(project, o.getPackageName(), GlobalSearchScope.projectScope(project)).size() > 1 &&
            o.getNameIdentifier() != null
          ) {
          registerProblem(holder, o.getNameIdentifier(), "Multiple namespace definitions found");
        }
      }
    };
  }
}
