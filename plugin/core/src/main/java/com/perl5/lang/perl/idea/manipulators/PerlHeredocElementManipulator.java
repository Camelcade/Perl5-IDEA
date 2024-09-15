/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.manipulators;

import com.intellij.application.options.CodeStyle;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.NotNull;


public class PerlHeredocElementManipulator extends AbstractElementManipulator<PerlHeredocElementImpl> {

  @Override
  public PerlHeredocElementImpl handleContentChange(final @NotNull PerlHeredocElementImpl element,
                                                    final @NotNull TextRange range,
                                                    final String newContent)
    throws IncorrectOperationException {
    var contentRemoval = newContent.isEmpty();
    var replacementContent = newContent;
    var effectiveRange = range;
    if (range.isEmpty()) {
      // this handles empty heredoc update. We have a single empty shred pointing to the offset of closing \n
      effectiveRange = TextRange.create(range.getStartOffset(), element.getTextLength() - 1);
    }

    var elementText = element.getText();
    if (effectiveRange.getStartOffset() > 0) {
      var lineStart = getLineStartOffset(elementText, effectiveRange);
      if (lineStart < effectiveRange.getStartOffset()) {
        if (!contentRemoval) {
          var indent = elementText.substring(lineStart, effectiveRange.getStartOffset());
          replacementContent = prependLines(replacementContent, indent);
        }
        effectiveRange = TextRange.create(lineStart, effectiveRange.getEndOffset());
      }
    }
    else if (effectiveRange.getStartOffset() == 0) {
      replacementContent = prependLines(newContent, getIndenter(element.getProject(), element.getRealIndentSize()));
    }

    String newElementText = effectiveRange.replace(elementText, replacementContent);
    PerlHeredocElementImpl replacement = PerlElementFactory.createHeredocBodyReplacement(element, newElementText);

    return (PerlHeredocElementImpl)element.replace(replacement);
  }

  private static int getLineStartOffset(@NotNull String elementText, @NotNull TextRange effectiveRange) {
    var startOffset = effectiveRange.getStartOffset();
    if (startOffset == 0) {
      return startOffset;
    }
    if (elementText.charAt(startOffset - 1) == '\n') {
      return startOffset;
    }
    return StringUtil.skipWhitespaceBackward(elementText, startOffset - 1);
  }

  private static @NotNull String getIndenter(@NotNull Project project, int indentSize) {
    CommonCodeStyleSettings.IndentOptions indentOptions =
      CodeStyle.getSettings(project).getCommonSettings(PerlLanguage.INSTANCE).getIndentOptions();

    return StringUtil.repeat(indentOptions != null && indentOptions.USE_TAB_CHARACTER ? "\t" : " ", indentSize);
  }

  private static @NotNull String prependLines(@NotNull String newContent, @NotNull String prefix) {
    var result = new StringBuilder();
    StringUtil.split(newContent, "\n", false, true)
      .forEach(line -> {
        result.append(prefix);
        result.append(line);
      });
    return result.toString();
  }
}
