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
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlCompletionProvider;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlSimpleCompletionProcessor;
import com.perl5.lang.perl.idea.completion.util.PerlSubCompletionUtil;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import org.jetbrains.annotations.NotNull;


public class PerlSubBuiltInCompletionProvider extends PerlCompletionProvider {
  @Override
  public void addCompletions(@NotNull CompletionParameters parameters,
                             @NotNull ProcessingContext context,
                             @NotNull CompletionResultSet resultSet) {
    PsiElement subNameElement = parameters.getPosition();
    PsiElement method = subNameElement.getParent();
    assert method instanceof PsiPerlMethod;

    if (((PsiPerlMethod)method).hasExplicitNamespace() || ((PsiPerlMethod)method).isObjectMethod()) {
      return;
    }

    PerlSimpleCompletionProcessor completionProcessor = new PerlSimpleCompletionProcessor(resultSet, subNameElement) {
      @Override
      public void addElement(@NotNull LookupElementBuilder lookupElement) {
        super.addElement(lookupElement.withBoldness(true));
      }
    };

    PerlImplicitDeclarationsService.getInstance(method.getProject()).processSubs(sub -> sub.isBuiltIn()
                                                                                        ? PerlSubCompletionUtil
                                                                                          .processSubDefinitionLookupElement(sub,
                                                                                                                             completionProcessor)
                                                                                        : completionProcessor.result());
    completionProcessor.logStatus(getClass());
  }
}
