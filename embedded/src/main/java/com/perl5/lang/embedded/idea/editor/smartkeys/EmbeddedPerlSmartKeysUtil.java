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

package com.perl5.lang.embedded.idea.editor.smartkeys;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.embedded.psi.EmbeddedPerlElementTypes.EMBED_MARKER_CLOSE;
import static com.perl5.lang.embedded.psi.EmbeddedPerlElementTypes.EMBED_MARKER_OPEN;


public final class EmbeddedPerlSmartKeysUtil implements PerlElementTypes {

  private EmbeddedPerlSmartKeysUtil() {
  }

  public static void addCloseMarker(final @NotNull Editor editor, @NotNull String marker) {
    int offset = editor.getCaretModel().getOffset();

    if (offset >= 2) {
      var highlighter = editor.getHighlighter();
      var highlighterIterator = highlighter.createIterator(offset - 2);
      if (highlighterIterator.getTokenType() == EMBED_MARKER_OPEN && !hasCloseMarkerAhead(highlighterIterator)) {
        EditorModificationUtil.insertStringAtCaret(editor, marker, false, false);
      }
    }
  }

  private static boolean hasCloseMarkerAhead(@NotNull HighlighterIterator highlighterIterator) {
    while (true) {
      highlighterIterator.advance();
      if (highlighterIterator.atEnd()) {
        return false;
      }
      var currentToken = highlighterIterator.getTokenType();
      if (currentToken == EMBED_MARKER_CLOSE) {
        return true;
      }
      else if (currentToken == OPERATOR_LT_NUMERIC) {
        highlighterIterator.advance();
        if (!highlighterIterator.atEnd() && highlighterIterator.getTokenType() == QUESTION) {
          return false;
        }
      }
    }
  }
}
