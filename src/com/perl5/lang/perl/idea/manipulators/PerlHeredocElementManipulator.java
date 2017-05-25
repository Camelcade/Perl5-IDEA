/*
 * Copyright 2015 Alexandr Evstigneev
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

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 10.06.2015.
 */
public class PerlHeredocElementManipulator extends PerlTextContainerManipulator<PerlHeredocElementImpl> {

  @Override
  public PerlHeredocElementImpl handleContentChange(@NotNull PerlHeredocElementImpl element, @NotNull TextRange range, String newContent)
    throws IncorrectOperationException {
    StringBuilder sb = new StringBuilder(newContent);
    if (element.getTextLength() == range.getEndOffset() && !StringUtil.endsWith(newContent, "\n")) {
      sb.append("\n");
    }

    int indentSize = element.getIndentSize();
    if (indentSize > 0) {
      CommonCodeStyleSettings.IndentOptions indentOptions =
        CodeStyleSettingsManager.getInstance(element.getProject()).getCurrentSettings().
          getCommonSettings(PerlLanguage.INSTANCE)
          .getIndentOptions();

      String indenter = StringUtil.repeat(indentOptions != null && indentOptions.USE_TAB_CHARACTER ? "\t" : " ", indentSize);
      int currentSourceOffset = 0;
      int currentLineStart = 0;
      int targetOffset = 0;
      sb.insert(targetOffset, indenter);
      targetOffset += indentSize;
      int sourceLength = newContent.length();
      while (currentSourceOffset < sourceLength) {
        if (newContent.charAt(currentSourceOffset++) == '\n' && currentSourceOffset < sourceLength) {
          int lineSize = currentSourceOffset - currentLineStart;
          if (lineSize > 1) {
            sb.insert(targetOffset + lineSize, indenter);
            targetOffset += lineSize + indentSize;
          }
          else {
            targetOffset += lineSize;
          }
          currentLineStart = currentSourceOffset;
        }
      }
    }

    return super.handleContentChange(element, range, sb.toString());
  }
}
