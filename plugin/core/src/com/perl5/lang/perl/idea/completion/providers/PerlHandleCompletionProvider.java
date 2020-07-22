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
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlSimpleCompletionProcessor;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessor;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessorImpl;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionUtil;
import com.perl5.lang.perl.idea.completion.util.PerlSubCompletionUtil;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionUtil;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PerlHandleCompletionProvider extends PerlCompletionProvider {
  // fixme this could be in CORE.xml file as a flag
  private static final Set<String> AUTO_VIVIFICATION_SUBS = ContainerUtil.newHashSet(
    "open", "opendir"
  );

  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                @NotNull ProcessingContext context,
                                @NotNull CompletionResultSet result) {
    PsiElement position = parameters.getPosition();
    if (!isApplicable(position)) {
      return;
    }

    PerlSimpleCompletionProcessor completionProcessor = new PerlSimpleCompletionProcessor(parameters, result, position);
    PerlPackageCompletionUtil.processAllNamespacesNames(completionProcessor, false, false);
    PerlSubCompletionUtil.processContextSubsLookupElements(completionProcessor);

    if (Experiments.getInstance().isFeatureEnabled("perl5.completion.var.without.sigil")) {
      PerlVariableCompletionProcessor variableCompletionProcessor = new PerlVariableCompletionProcessorImpl(
        completionProcessor, null, false, false, false);
      PerlVariableCompletionUtil.processVariables(variableCompletionProcessor);
    }

    completionProcessor.logStatus(getClass());
  }

  private boolean isApplicable(@NotNull PsiElement position) {
    PerlSubCallElement subCallElement = PsiTreeUtil.getParentOfType(position, PerlSubCallElement.class);
    if (subCallElement == null) {
      return true;
    }
    PsiPerlMethod method = subCallElement.getMethod();
    if (method == null || method.hasExplicitNamespace()) {
      return true;
    }

    PerlSubNameElement subNameElement = method.getSubNameElement();
    if (subNameElement == null) {
      return true;
    }
    return !AUTO_VIVIFICATION_SUBS.contains(subNameElement.getName());
  }
}
