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
import com.intellij.psi.search.searches.ReferencesSearch;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;

import java.util.Set;


public class PerlUnusedGlobalVariableInspection extends PerlVariableInspectionBase {
  static final Set<String> EXCLUSIONS = Set.of(
    "@ISA",
    "@EXPORT_OK",
    "@EXPORT",

    "%EXPORT_TAGS",

    "$VERSION"
  );

  @Override
  public void checkDeclaration(ProblemsHolder holder, PerlVariableDeclarationElement variableDeclarationWrapper) {
    if (variableDeclarationWrapper.isGlobalDeclaration()) {
      if (EXCLUSIONS.contains(variableDeclarationWrapper.getText())) {
        return;
      }

      if (ReferencesSearch.search(variableDeclarationWrapper, variableDeclarationWrapper.getUseScope()).findFirst() == null) {
        PerlVariable variable = variableDeclarationWrapper.getVariable();
        holder.registerProblem(
          variable,
          PerlBundle.message("inspection.message.unused.global.variable", variable.getText()),
          ProblemHighlightType.LIKE_UNUSED_SYMBOL);
      }
    }
  }
}

