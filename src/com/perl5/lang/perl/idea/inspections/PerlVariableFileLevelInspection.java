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

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;

/**
 * Created by evstigneev on 14.12.2015.
 */
public class PerlVariableFileLevelInspection extends PerlVariableDeclarationInspection {

  @Override
  public void checkDeclaration(ProblemsHolder holder, PerlVariableDeclarationWrapper variableDeclarationWrapper) {
    if (variableDeclarationWrapper.isGlobalDeclaration() &&
        PerlVariableGlobalUnusedInspection.EXCLUSIONS.contains(variableDeclarationWrapper.getText())) {
      return;
    }

    if (PsiTreeUtil
          .getParentOfType(variableDeclarationWrapper, PerlSubDefinitionBase.class, PerlMethodDefinition.class, PerlFuncDefinition.class,
                           PsiPerlSubExpr.class) == null) {
      PerlVariable variable = variableDeclarationWrapper.getVariable();
      if (variable != null) {
        holder.registerProblem(
          variable,
          "File level variable:" + variable.getText(),
          ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
      }
    }
  }
}
