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
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlSimpleCompletionProcessor;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessor;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessorImpl;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionUtil;
import com.perl5.lang.perl.idea.completion.util.PerlSubCompletionUtil;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionUtil;
import org.jetbrains.annotations.NotNull;

public class PerlHandleCompletionProvider extends PerlCompletionProvider {
  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                @NotNull ProcessingContext context,
                                @NotNull CompletionResultSet result) {
    PerlSimpleCompletionProcessor completionProcessor = new PerlSimpleCompletionProcessor(result, parameters.getPosition());
    PerlPackageCompletionUtil.processAllNamespacesNamesWithAutocompletion(completionProcessor, true, true);
    PerlSubCompletionUtil.processBuiltInSubsLookupElements(completionProcessor);

    if (Experiments.getInstance().isFeatureEnabled("perl5.completion.var.without.sigil")) {
      PerlVariableCompletionProcessor variableCompletionProcessor = new PerlVariableCompletionProcessorImpl(
        completionProcessor, null, false, false, false);
      PerlVariableCompletionUtil.fillWithVariables(variableCompletionProcessor);
    }

    completionProcessor.logStatus(getClass());
  }
}
