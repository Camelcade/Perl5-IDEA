/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

/**
 * Created by hurricup on 10.06.2015.
 */
public class PerlHeredocElementManipulator extends AbstractElementManipulator<PerlHeredocElementImpl> {

  @Override
  public PerlHeredocElementImpl handleContentChange(@NotNull PerlHeredocElementImpl element, @NotNull TextRange range, String newContent)
    throws IncorrectOperationException {
    StringBuilder sb = new StringBuilder(newContent);
    if (element.getTextLength() == range.getEndOffset() && !StringUtil.endsWith(newContent, "\n")) {
      sb.append("\n");
    }

    Project project = element.getProject();
    if (range.getStartOffset() > 0) {
      sb.insert(0, getIndenter(project, range.getStartOffset()));
      range = TextRange.from(0, range.getEndOffset());
    }

    normalizeIndentation(project, sb, element.getRealIndentSize());

    String newElementText = range.replace(element.getText(), sb.toString());
    PerlHeredocElementImpl replacement = PerlElementFactory.createHeredocBodyReplacement(element, newElementText);

    return (PerlHeredocElementImpl)element.replace(replacement);
  }

  @NotNull
  private static String getIndenter(@NotNull Project project, int indentSize) {
    CommonCodeStyleSettings.IndentOptions indentOptions =
      CodeStyle.getSettings(project).getCommonSettings(PerlLanguage.INSTANCE).getIndentOptions();

    return StringUtil.repeat(indentOptions != null && indentOptions.USE_TAB_CHARACTER ? "\t" : " ", indentSize);
  }

  private static void normalizeIndentation(@NotNull Project project, @NotNull StringBuilder sb, int indentSize) {
    int offset = 0;
    int currentLineStart = 0;
    int currentLineIndentSize = 0;
    boolean hasNonSpaces = false;

    while (offset < sb.length()) {
      char currentChar = sb.charAt(offset++);
      if (currentChar == '\n') {
        if (hasNonSpaces && currentLineIndentSize < indentSize) {
          int indentAdjustment = indentSize - currentLineIndentSize;
          sb.insert(currentLineStart, getIndenter(project, indentAdjustment));
          offset += indentAdjustment;
        }
        currentLineStart = offset;
        hasNonSpaces = false;
        currentLineIndentSize = 0;
      }
      else if (!hasNonSpaces) {
        if (Character.isWhitespace(currentChar)) {
          currentLineIndentSize++;
        }
        else {
          hasNonSpaces = true;
        }
      }
    }
  }
}
