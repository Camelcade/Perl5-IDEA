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

package com.perl5.lang.perl.idea.editor.smartkeys;

import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlEditorUtil {
  /**
   * Iterates back until atEnd or non-space token
   */
  @NotNull
  public static HighlighterIterator moveToPreviousMeaningfulToken(@NotNull HighlighterIterator iterator) {
    while (!iterator.atEnd()) {
      IElementType tokenType = iterator.getTokenType();
      if (tokenType != TokenType.WHITE_SPACE) {
        break;
      }
      iterator.retreat();
    }
    return iterator;
  }

  /**
   * Iterates forward until atEnd or non-space token
   */
  @NotNull
  public static HighlighterIterator moveToNextMeaningfulToken(@NotNull HighlighterIterator iterator) {
    iterator.advance();
    while (!iterator.atEnd()) {
      IElementType tokenType = iterator.getTokenType();
      if (tokenType != TokenType.WHITE_SPACE) {
        break;
      }
      iterator.advance();
    }
    return iterator;
  }

  /**
   * @return previous non-space token type
   */
  @Nullable
  public static IElementType getPreviousTokenType(@NotNull HighlighterIterator iterator) {
    moveToPreviousMeaningfulToken(iterator);
    return iterator.atEnd() ? null : iterator.getTokenType();
  }
}
