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

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.NotNull;


public class PerlHeredocElementManipulator extends AbstractElementManipulator<PerlHeredocElementImpl> {

  @Override
  public PerlHeredocElementImpl handleContentChange(@NotNull PerlHeredocElementImpl element, @NotNull TextRange range, String newContent)
    throws IncorrectOperationException {
    var contentRemoval = newContent.isEmpty();
    if (element.getTextLength() == range.getEndOffset() && !contentRemoval && !StringUtil.endsWith(newContent, "\n")) {
      newContent += "\n";
    }

    var elementText = element.getText();
    if (range.getStartOffset() > 0) {
      var lineStart = StringUtil.skipWhitespaceBackward(elementText, range.getStartOffset() - 1);
      if (lineStart < range.getStartOffset()) {
        if (!contentRemoval) {
          var indent = elementText.substring(lineStart, range.getStartOffset());
          newContent = prependLines(newContent, indent);
        }
        range = TextRange.create(lineStart, range.getEndOffset());
      }
    }

    String newElementText = range.replace(elementText, newContent);
    PerlHeredocElementImpl replacement = PerlElementFactory.createHeredocBodyReplacement(element, newElementText);

    return (PerlHeredocElementImpl)element.replace(replacement);
  }

  private static @NotNull String prependLines(@NotNull String newContent, @NotNull String prefix) {
    return prefix + String.join(prefix, StringUtil.split(newContent, "\n", false, true));
  }
}
