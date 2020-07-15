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
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlSimpleCompletionProcessor;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class PerlUnicodeNamesCompletionProvider extends PerlCompletionProvider {
  private static final Logger LOG = Logger.getInstance(PerlUnicodeNamesCompletionProvider.class);
  private int myCodePoint = 0;
  private int myDataSize = 0;

  private SoftReference<Map<String, String>> myNamesCacheRef = new SoftReference<>(new HashMap<>());

  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                @NotNull ProcessingContext context,
                                @NotNull CompletionResultSet result) {
    PerlSimpleCompletionProcessor completionProcessor = new PerlSimpleCompletionProcessor(
      parameters, result.caseInsensitive(), parameters.getPosition());

    Map<String, String> cache = myNamesCacheRef.get();
    if (cache == null) {
      LOG.debug("Cache been collected, re-init");
      cache = new HashMap<>();
      myCodePoint = 0;
      myNamesCacheRef = new SoftReference<>(cache);
    }

    fillCached(completionProcessor, cache);
    lazyCache(completionProcessor, cache);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Cached entries: ", cache.size(), "; data size, mb: ", (float)myDataSize / 1024 / 1024);
    }

    completionProcessor.logStatus(getClass());
  }

  private void fillCached(@NotNull PerlSimpleCompletionProcessor completionProcessor, @NotNull Map<String, String> cache) {
    for (Map.Entry<String, String> entry : cache.entrySet()) {
      if (completionProcessor.matches(entry.getKey()) && !completionProcessor.process(
        LookupElementBuilder.create(entry.getKey()).withTypeText(entry.getValue(), false))) {
        break;
      }
    }
  }

  private void lazyCache(@NotNull PerlSimpleCompletionProcessor completionProcessor, @NotNull Map<String, String> cache) {
    if (!completionProcessor.result()) {
      return;
    }
    for (; myCodePoint < Integer.MAX_VALUE; myCodePoint++) {
      ProgressManager.checkCanceled();
      if (!Character.isValidCodePoint(myCodePoint)) {
        continue;
      }
      String characterName = Character.getName(myCodePoint);
      if (!StringUtil.isNotEmpty(characterName)) {
        continue;
      }
      String charValue = Character.toString(myCodePoint);
      cache.put(characterName, charValue);
      myDataSize += characterName.length() + charValue.length();
      if (!completionProcessor.matches(characterName)) {
        continue;
      }
      if (!completionProcessor.process(
        LookupElementBuilder.create(characterName).withTypeText(charValue, false))) {
        LOG.debug("Stopped at ", myCodePoint);
        return;
      }
    }
  }
}
