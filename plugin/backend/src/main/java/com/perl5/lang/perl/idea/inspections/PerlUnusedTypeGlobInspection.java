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

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.util.PerlGlobUtilCore;
import org.jetbrains.annotations.NotNull;


public class PerlUnusedTypeGlobInspection extends PerlInspection {
  @Override
  public @NotNull PsiElementVisitor buildVisitor(final @NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {
      @Override
      public void visitGlobVariable(@NotNull PsiPerlGlobVariable o) {
        if ((o.getExplicitNamespaceName() != null || !PerlGlobUtilCore.BUILT_IN.contains(o.getName())) &&
            ReferencesSearch.search(o, GlobalSearchScope.projectScope(o.getProject())).findFirst() == null) {
          holder.registerProblem(o, PerlBundle.message("inspection.message.unused.typeglob.alias"),
                                 ProblemHighlightType.LIKE_UNUSED_SYMBOL);
        }
      }
    };
  }
}
