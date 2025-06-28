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

package com.perl5.lang.perl.idea.completion.providers.processors;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPerlCompletionProcessor implements PerlCompletionProcessor {
  protected static final Logger LOG = Logger.getInstance(AbstractPerlCompletionProcessor.class);

  /**
   * @return true iff {@code suggestedName} matches current prefix matcher
   */
  @Override
  @Contract("null->false")
  public boolean matches(@Nullable String suggestedName) {
    ProgressManager.checkCanceled();
    return StringUtil.isNotEmpty(suggestedName) && getResultSet().getPrefixMatcher().prefixMatches(suggestedName);
  }

  /**
   * @return true iff we should go on, false if we should stop
   */
  @Override
  public final boolean process(@Nullable LookupElementBuilder lookupElement) {
    if (lookupElement != null) {
      addElement(lookupElement);
    }
    return result();
  }

  @Override
  public final boolean processSingle(@NotNull LookupElementBuilder lookupElement) {
    for (String string : lookupElement.getAllLookupStrings()) {
      if (matches(string)) {
        process(lookupElement);
        break;
      }
    }
    return result();
  }
}
