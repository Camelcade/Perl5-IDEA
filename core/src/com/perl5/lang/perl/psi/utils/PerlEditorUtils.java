/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.utils;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class PerlEditorUtils {
  private PerlEditorUtils() {
  }

  /**
   * True iff previous non-space token is one of the {@code elementTypes}
   */
  public static boolean isPreviousToken(@NotNull Editor editor, int offset, @NotNull TokenSet elementTypes) {
    HighlighterIterator highlighterIterator = ((EditorEx)editor).getHighlighter().createIterator(offset);
    while (!highlighterIterator.atEnd()) {
      highlighterIterator.retreat();
      IElementType tokenType = highlighterIterator.getTokenType();
      if (tokenType != TokenType.WHITE_SPACE) {
        return elementTypes.contains(tokenType);
      }
    }
    return false;
  }
}
