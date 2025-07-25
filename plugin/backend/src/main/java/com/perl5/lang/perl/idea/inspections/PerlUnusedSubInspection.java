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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.PerlSubDeclarationElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.perl5.lang.perl.util.PerlSubUtilCore.SUB_DESTROY;


public class PerlUnusedSubInspection extends PerlInspection {
  private static final Set<String> EXCLUSIONS = Set.of(
    "BEGIN",
    "UNITCHECK",
    "CHECK",
    "INIT",
    "END",
    SUB_DESTROY
  );

  @Override
  public @NotNull PsiElementVisitor buildVisitor(final @NotNull ProblemsHolder holder, boolean isOnTheFly) {

    return new PerlVisitor() {
      @Override
      public void visitPerlSubDefinitionElement(@NotNull PerlSubDefinitionElement o) {
        PsiElement subNameElement = o.getNameIdentifier();
        if (subNameElement != null &&
            !EXCLUSIONS.contains(o.getName()) &&
            ReferencesSearch.search(o, GlobalSearchScope.projectScope(o.getProject())).findFirst() == null) {
          holder.registerProblem(subNameElement, PerlBundle.message("inspection.message.unused.sub.definition"),
                                 ProblemHighlightType.LIKE_UNUSED_SYMBOL);
        }
      }

      @Override
      protected boolean shouldVisitLightElements() {
        return true;
      }

      @Override
      public void visitSubDeclarationElement(@NotNull PerlSubDeclarationElement o) {
        PsiElement subNameElement = o.getNameIdentifier();
        if (subNameElement != null &&
            !EXCLUSIONS.contains(o.getName()) &&
            ReferencesSearch.search(o, GlobalSearchScope.projectScope(o.getProject())).findFirst() == null) {
          holder.registerProblem(subNameElement, PerlBundle.message("inspection.message.unused.sub.declaration"),
                                 ProblemHighlightType.LIKE_UNUSED_SYMBOL);
        }
      }
    };
  }
}
