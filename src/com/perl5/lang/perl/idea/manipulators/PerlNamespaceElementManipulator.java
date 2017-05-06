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
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.impl.PerlNamespaceElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 27.09.2015.
 */
public class PerlNamespaceElementManipulator extends AbstractElementManipulator<PerlNamespaceElement> {
  @Override
  public PerlNamespaceElement handleContentChange(@NotNull PerlNamespaceElement element, @NotNull TextRange range, String newContent)
    throws IncorrectOperationException {
    if (newContent.isEmpty()) {
      throw new IncorrectOperationException("You can't set empty package name");
    }

    return (PerlNamespaceElement)((PerlNamespaceElementImpl)element).replaceWithText(range.replace(element.getText(), newContent));
  }

  @NotNull
  @Override
  public TextRange getRangeInElement(@NotNull PerlNamespaceElement element) {
    return getRangeInString(element.getText());
  }

  @NotNull
  public static TextRange getRangeInString(CharSequence elementText) {
    int endOffset = elementText.length();
    while (endOffset > 0) {
      char currentChar = elementText.charAt(endOffset - 1);
      if (currentChar != '\'' && currentChar != ':') {
        break;
      }
      endOffset--;
    }

    return endOffset == 0 ? TextRange.EMPTY_RANGE : TextRange.create(0, endOffset);
  }
}
