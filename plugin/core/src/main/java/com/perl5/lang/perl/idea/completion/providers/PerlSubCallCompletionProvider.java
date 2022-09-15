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
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlCallStaticValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlCallValue;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlSimpleCompletionProcessor;
import com.perl5.lang.perl.idea.completion.util.PerlSubCompletionUtil;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.util.PerlTimeLogger;
import org.jetbrains.annotations.NotNull;


public class PerlSubCallCompletionProvider extends PerlCompletionProvider {
  @Override
  public void addCompletions(@NotNull CompletionParameters parameters,
                             @NotNull ProcessingContext context,
                             @NotNull CompletionResultSet resultSet) {
    PsiElement position = parameters.getPosition();
    PsiElement method = position.getParent();
    assert method instanceof PsiPerlMethod;

    PerlCallValue perlValue = PerlCallValue.from(method);
    if (perlValue == null) {
      return;
    }
    PerlSimpleCompletionProcessor completionProcessor = new PerlSimpleCompletionProcessor(parameters, resultSet, position);
    PerlTimeLogger logger = PerlTimeLogger.create(LOG);
    if (perlValue instanceof PerlCallStaticValue && !((PerlCallStaticValue)perlValue).hasExplicitNamespace()) {
      PerlSubCompletionUtil.processBuiltInSubsLookupElements(completionProcessor);
      logger.debug("Processed built-in subs");
    }
    PerlSubCompletionUtil.processSubsCompletionsForCallValue(completionProcessor, perlValue, perlValue instanceof PerlCallStaticValue);
    logger.debug("Processed subs for call value");
    completionProcessor.logStatus(getClass());
  }
}
