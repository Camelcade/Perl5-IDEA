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

package com.perl5.lang.mojolicious.idea.editor.smartkeys;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtilEx;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.perl.idea.editor.smartkeys.PerlEditorUtil;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.NotNull;


public class MojoliciousSmartKeysUtil implements MojoliciousElementTypes, PerlElementTypes {
  private static final TokenSet CLOSE_TOKENS = TokenSet.create(
    MOJO_BLOCK_CLOSER,
    MOJO_BLOCK_NOSPACE_CLOSER,
    MOJO_BLOCK_EXPR_CLOSER,
    MOJO_BLOCK_EXPR_NOSPACE_CLOSER
  );

  public static boolean addCloseMarker(final @NotNull Editor editor, @NotNull String marker) {
    var caretOffset = editor.getCaretModel().getOffset();
    if (caretOffset < 2) {
      return false;
    }
    var highlighterIterator = editor.getHighlighter().createIterator(caretOffset - 2);
    var tokenType = highlighterIterator.getTokenType();
    if (tokenType != MOJO_BLOCK_OPENER && tokenType != MOJO_BLOCK_EXPR_OPENER && tokenType != MOJO_BLOCK_EXPR_ESCAPED_OPENER) {
      return false;
    }

    if (hasCloseMarkerAhead(highlighterIterator)) {
      return false;
    }
    EditorModificationUtilEx.insertStringAtCaret(editor, marker, false, false);
    return true;
  }

  private static boolean hasCloseMarkerAhead(@NotNull HighlighterIterator highlighterIterator) {
    while (true) {
      highlighterIterator.advance();
      if (highlighterIterator.atEnd()) {
        return false;
      }
      var currentToken = highlighterIterator.getTokenType();
      if (CLOSE_TOKENS.contains(currentToken)) {
        return true;
      }
      else if (currentToken == OPERATOR_LT_NUMERIC) {
        highlighterIterator.advance();
        if (!highlighterIterator.atEnd() &&
            (highlighterIterator.getTokenType() == SIGIL_HASH || highlighterIterator.getTokenType() == OPERATOR_MOD)) {
          return false;
        }
      }
    }
  }

  public static boolean addEndMarker(final @NotNull Editor editor, @NotNull String marker) {
    var caretOffset = editor.getCaretModel().getOffset();
    if (caretOffset < 5) {
      return false;
    }
    var highlighterIterator = editor.getHighlighter().createIterator(caretOffset - 2);
    var tokenType = highlighterIterator.getTokenType();
    if (tokenType != MOJO_BEGIN && !(tokenType == SUB_NAME && StringUtil.equals("begin", PerlEditorUtil.getTokenChars(highlighterIterator)))
        || hasEndMarkerAhead(highlighterIterator)) {
      return false;
    }

    EditorModificationUtilEx.insertStringAtCaret(editor, marker, false, false);
    return true;
  }

  private static boolean hasEndMarkerAhead(@NotNull HighlighterIterator highlighterIterator) {
    while (true) {
      highlighterIterator.advance();
      if (highlighterIterator.atEnd()) {
        return false;
      }
      if (highlighterIterator.getTokenType() == MOJO_END) {
        return true;
      }
    }
  }
}
