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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlSimpleCompletionProcessor;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionUtil;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlTimeLogger;
import org.jetbrains.annotations.NotNull;


public class PerlPackageSubCompletionProvider extends PerlCompletionProvider {
  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                @NotNull ProcessingContext context,
                                @NotNull CompletionResultSet result) {
    PsiElement method = parameters.getPosition().getParent();
    if (!(method instanceof PsiPerlMethod)) {
      LOG.warn("Expected PsiPerlMethod, got psiElement=[" + method.getClass() + "]; text=[" + method.getText() + "]");
      return;
    }
    String explicitNamespace = ((PsiPerlMethod)method).getExplicitNamespaceName();
    String currentPrefixMatcher = result.getPrefixMatcher().getPrefix();
    String newPrefixMathcer = explicitNamespace == null
                              ? currentPrefixMatcher
                              : PerlPackageUtil.join(explicitNamespace, currentPrefixMatcher);
    result = result.withPrefixMatcher(newPrefixMathcer);

    PerlSimpleCompletionProcessor completionProcessor = new PerlSimpleCompletionProcessor(parameters, result, parameters.getPosition());
    PerlTimeLogger logger = PerlTimeLogger.create(LOG);
    if (!((PsiPerlMethod)method).isObjectMethod()) {
      PerlPackageCompletionUtil.processAllNamespacesNames(completionProcessor, false, false);
      logger.debug("Processed all namespace names");
    }
    else {
      if (!StringUtil.equals(PerlPackageUtil.SUPER_NAMESPACE_FULL, newPrefixMathcer)) {
        PerlPackageCompletionUtil.processPackageLookupElementWithAutocomplete(
          null, PerlPackageUtil.SUPER_NAMESPACE_FULL, null, completionProcessor);
        logger.debug("Processed all package lookups with autocomplete");
      }
    }
    completionProcessor.logStatus(getClass());
  }
}
