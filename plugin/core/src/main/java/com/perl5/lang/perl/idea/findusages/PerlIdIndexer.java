/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.findusages;

import com.intellij.lang.cacheBuilder.WordOccurrence;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.psi.impl.cache.impl.id.IdDataConsumer;
import com.intellij.psi.impl.cache.impl.id.IdIndexEntry;
import com.intellij.psi.impl.cache.impl.id.ScanningIdIndexer;
import com.intellij.psi.search.UsageSearchContext;
import com.intellij.util.Processor;
import com.intellij.util.indexing.FileContent;
import com.intellij.util.text.CharArrayUtil;
import com.perl5.lang.perl.PerlLanguage;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PerlIdIndexer extends ScanningIdIndexer {
  @Override
  protected PerlWordsScanner createScanner() {
    return new PerlWordsScanner();
  }

  @Override
  public @NotNull Map<IdIndexEntry, Integer> map(@NotNull FileContent inputData) {
    final IdDataConsumer consumer = new IdDataConsumer();
    var psiFile = inputData.getPsiFile();
    var perlPsi = psiFile.getViewProvider().getPsi(PerlLanguage.INSTANCE);
    if (perlPsi != null) {
      //noinspection deprecation
      createScanner().processWordsUsingPsi(psiFile, createProcessor(inputData.getContentAsText(), consumer));
    }
    return consumer.getResult();
  }

  /**
   * @deprecated available in IDEA since 2023.3
   * @see com.intellij.psi.impl.cache.impl.id.ScanningIdIndexer#createProcessor
   */
  private static @NotNull Processor<WordOccurrence> createProcessor(@NotNull CharSequence chars, @NotNull IdDataConsumer consumer) {
    final char[] charsArray = CharArrayUtil.fromSequenceWithoutCopying(chars);
    return new Processor<>() {
      @Override
      public boolean process(final WordOccurrence t) {
        if (charsArray != null && t.getBaseText() == chars) {
          consumer.addOccurrence(charsArray, t.getStart(), t.getEnd(), convertToMask(t.getKind()));
        }
        else {
          consumer.addOccurrence(t.getBaseText(), t.getStart(), t.getEnd(), convertToMask(t.getKind()));
        }
        return true;
      }

      private int convertToMask(final WordOccurrence.Kind kind) {
        if (kind == null) {
          return UsageSearchContext.ANY;
        }
        if (kind == WordOccurrence.Kind.CODE) return UsageSearchContext.IN_CODE;
        if (kind == WordOccurrence.Kind.COMMENTS) return UsageSearchContext.IN_COMMENTS;
        if (kind == WordOccurrence.Kind.LITERALS) return UsageSearchContext.IN_STRINGS;
        if (kind == WordOccurrence.Kind.FOREIGN_LANGUAGE) return UsageSearchContext.IN_FOREIGN_LANGUAGES;
        return 0;
      }
    };
  }
}
