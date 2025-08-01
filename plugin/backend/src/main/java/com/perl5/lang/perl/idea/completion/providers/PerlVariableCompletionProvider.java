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

package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessor;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessorImpl;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionUtil;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import com.perl5.lang.perl.util.PerlTimeLogger;
import org.jetbrains.annotations.NotNull;

public class PerlVariableCompletionProvider extends PerlCompletionProvider {
  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                @NotNull ProcessingContext context,
                                @NotNull CompletionResultSet resultSet) {
    if (!Registry.is("perl5.completion.var.without.sigil", true)) {
      return;
    }

    PsiElement subName = parameters.getPosition();
    PsiElement method = subName.getParent();

    String namespaceName = null;
    if (!(method instanceof PsiPerlMethod psiPerlMethod) || psiPerlMethod.isObjectMethod()) {
      return;
    }
    namespaceName = psiPerlMethod.getExplicitNamespaceName();
    if (StringUtil.isNotEmpty(namespaceName)) {
      resultSet = resultSet.withPrefixMatcher(PerlPackageUtilCore.join(namespaceName, resultSet.getPrefixMatcher().getPrefix()));
    }

    PerlVariableCompletionProcessor variableCompletionProcessor = new PerlVariableCompletionProcessorImpl(
      parameters, resultSet, subName, namespaceName, false, false, false);

    PerlTimeLogger logger = PerlTimeLogger.create(LOG);
    PerlVariableCompletionUtil.processVariables(variableCompletionProcessor, logger);
    variableCompletionProcessor.logStatus(getClass());
  }
}
