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

package com.perl5.lang.pod.elementTypes;

import com.intellij.lang.Language;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.lexer.adapters.PerlProgressAwareAdapter;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.lexer.PodElementTypes;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


public class PodTemplatingElementType extends TemplateDataElementType implements PerlElementTypes, PodElementTypes {
  public PodTemplatingElementType(@NonNls String debugName, Language language) {
    super(debugName, language, POD, POD_OUTER);
  }

  @Override
  protected CharSequence createTemplateText(@NotNull CharSequence sourceCode,
                                            @NotNull Lexer baseLexer,
                                            @NotNull RangeCollector rangeCollector) {
    if (doesSourceSeemsHasPod(sourceCode)) {
      return super.createTemplateText(sourceCode, baseLexer, rangeCollector);
    }
    rangeCollector.addOuterRange(TextRange.create(0, sourceCode.length()));
    return "";
  }

  /**
   * @return true iff {@code sourceCode} has pod-like opener.
   */
  private boolean doesSourceSeemsHasPod(@NotNull CharSequence sourceCode) {
    if (sourceCode.length() > 2 && sourceCode.charAt(0) == '=' && Character.isLetter(sourceCode.charAt(1))) {
      return true;
    }
    int startOffset = 0;
    while (true) {
      int possibleStart = StringUtil.indexOf(sourceCode, "\n=", startOffset);
      if (possibleStart > -1) {
        if (possibleStart < sourceCode.length() - 2 && Character.isLetter(sourceCode.charAt(possibleStart + 2))) {
          return true;
        }
        startOffset = possibleStart + 1;
      }
      else {
        return false;
      }
    }
  }

  /**
   * @return an optimized lexer (without sublexing) to distinct perl from pod
   */
  @Override
  protected Lexer createBaseLexer(TemplateLanguageFileViewProvider viewProvider) {
    if (viewProvider.getBaseLanguage() == PerlLanguage.INSTANCE) {
      return new PerlProgressAwareAdapter(new PerlLexer(null).withProject(viewProvider.getManager().getProject()));
    }
    return super.createBaseLexer(viewProvider);
  }

  @Override
  protected Language getTemplateFileLanguage(TemplateLanguageFileViewProvider viewProvider) {
    return PodLanguage.INSTANCE;
  }
}
