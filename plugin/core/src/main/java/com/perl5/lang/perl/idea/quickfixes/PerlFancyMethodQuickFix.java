/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.quickfixes;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.PerlMethod;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;


public class PerlFancyMethodQuickFix implements LocalQuickFix {
  String myNewForm;

  public PerlFancyMethodQuickFix(String newForm) {
    super();
    myNewForm = newForm;
  }

  @Override
  public @Nls @NotNull String getName() {
    return PerlBundle.message("perl.quickfix.fancy.method.prefix") + myNewForm + "()";
  }

  @Override
  public @NotNull String getFamilyName() {
    return PerlBundle.message("perl.quickfix.fancy.method.family");
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    PsiElement method = descriptor.getPsiElement();
    assert method instanceof PerlMethod;

    PsiElement callExpression = method.getParent();
    assert callExpression instanceof PerlSubCallElement;

    PerlNamespaceElement namespaceElement = ((PerlMethod)method).getNamespaceElement();
    assert namespaceElement != null;

    PerlSubNameElement subNameElement = ((PerlMethod)method).getSubNameElement();
    assert subNameElement != null;

    StringBuilder argsBuilder = new StringBuilder();
    PsiElement run = callExpression.getFirstChild();
    while (run != null) {
      if (run != method) {
        argsBuilder.append(run.getNode().getChars());
      }
      run = run.getNextSibling();
    }

    callExpression.replace(PerlElementFactory.createMethodCall(project,
                                                               namespaceElement.getText(),
                                                               subNameElement.getName(),
                                                               argsBuilder.toString()));
  }
}
