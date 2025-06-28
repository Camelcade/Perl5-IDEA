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
import com.intellij.psi.PsiElement;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.PsiPerlVariableDeclarationLocal;
import com.perl5.lang.perl.psi.impl.PerlBuiltInVariable;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;


public class PerlVariableShadowingInspection extends PerlVariableInspectionBase {
  @Override
  public void checkDeclaration(ProblemsHolder holder, PerlVariableDeclarationElement variableDeclarationWrapper) {
    PerlVariable variable = variableDeclarationWrapper.getVariable();
    PsiElement declarationContainer = variableDeclarationWrapper.getParent();

    if (!(declarationContainer instanceof PsiPerlVariableDeclarationLocal)) {
      PerlVariableDeclarationElement lexicalDeclaration = PerlResolveUtil.getLexicalDeclaration(variable);
      if (lexicalDeclaration instanceof PerlBuiltInVariable) {
        registerProblem(holder, variable,
                        PerlBundle.message("perl.inspection.shadows.builtin", lexicalDeclaration.getVariable().getLineNumber()));
      }
      else if (lexicalDeclaration instanceof PerlImplicitVariableDeclaration) {
        registerProblem(holder, variable,
                        PerlBundle.message("perl.inspection.shadows.implicit", lexicalDeclaration.getVariable().getLineNumber()));
      }
      else if (lexicalDeclaration != null) {
        registerProblem(holder, variable,
                        PerlBundle.message("perl.inspection.shadows.other", lexicalDeclaration.getVariable().getLineNumber()));
      }
    }
  }
}
