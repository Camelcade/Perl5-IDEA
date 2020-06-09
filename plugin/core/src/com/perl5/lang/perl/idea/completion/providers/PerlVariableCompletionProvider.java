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

package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.application.Experiments;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlCompletionProvider;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessor;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessorImpl;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionUtil;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.psi.PsiPerlPerlHandleExpr;
import org.jetbrains.annotations.NotNull;

public class PerlVariableCompletionProvider extends PerlCompletionProvider {
  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                @NotNull ProcessingContext context,
                                @NotNull CompletionResultSet resultSet) {
    if (!Experiments.getInstance().isFeatureEnabled("perl5.completion.var.without.sigil")) {
      return;
    }

    PsiElement subName = parameters.getPosition();
    PsiElement method = subName.getParent();

    boolean fullQualified = false;
    String namespaceName = null;
    if (method instanceof PsiPerlMethod) {
      if (((PsiPerlMethod)method).isObjectMethod()) {
        return;
      }
      fullQualified = ((PsiPerlMethod)method).hasExplicitNamespace();
      namespaceName = ((PsiPerlMethod)method).getExplicitNamespaceName();
    }
    else if (!(method instanceof PsiPerlPerlHandleExpr)) {
      return;
    }

    PerlVariableCompletionProcessor variableCompletionProcessor = new PerlVariableCompletionProcessorImpl(
      withFqnSafeMatcher(resultSet), subName, fullQualified, false, false);

    PerlVariableCompletionUtil.fillWithVariables(variableCompletionProcessor, fullQualified, namespaceName);
    variableCompletionProcessor.logStatus(getClass());
  }
}
