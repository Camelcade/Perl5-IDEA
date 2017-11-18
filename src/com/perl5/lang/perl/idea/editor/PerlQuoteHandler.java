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

package com.perl5.lang.perl.idea.editor;

import com.intellij.codeInsight.editorActions.MultiCharQuoteHandler;
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlBaseLexer;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.PerlParserDefinition.LITERALS;
import static com.perl5.lang.perl.parser.PerlParserUtil.CLOSE_QUOTES;
import static com.perl5.lang.perl.parser.PerlParserUtil.OPEN_QUOTES;

/**
 * Created by hurricup on 10.06.2015.
 */
public class PerlQuoteHandler extends SimpleTokenSetQuoteHandler implements MultiCharQuoteHandler, PerlElementTypes {

  public PerlQuoteHandler() {
    super(LITERALS);
  }

  @Override
  public boolean isClosingQuote(HighlighterIterator iterator, int offset) {
    return CLOSE_QUOTES.contains(iterator.getTokenType());
  }

  @Override
  public boolean isOpeningQuote(HighlighterIterator iterator, int offset) {
    return OPEN_QUOTES.contains(iterator.getTokenType());
  }

  @Override
  public boolean hasNonClosedLiteral(Editor editor, HighlighterIterator iterator, int offset) {
    int start = iterator.getStart();
    try {
      Document doc = editor.getDocument();
      CharSequence chars = doc.getCharsSequence();

      while (!iterator.atEnd()) {
        IElementType tokenType = iterator.getTokenType();

        if (myLiteralTokenSet.contains(tokenType)) {
          if (isNonClosedLiteral(iterator, chars)) {
            return true;
          }
        }
        else if (!OPEN_QUOTES.contains(tokenType)) {
          return false;
        }
        iterator.advance();
      }
      return true;
    }
    finally {
      while (iterator.atEnd() || iterator.getStart() != start) {
        iterator.retreat();
      }
    }
  }

  protected boolean isNonClosedLiteral(HighlighterIterator iterator, CharSequence chars) {
    if (iterator.getStart() >= iterator.getEnd() - 1) {
      return true;
    }

    char nextChar = chars.charAt(iterator.getEnd() - 1);
    return nextChar != '\"' && nextChar != '\'' && nextChar != '`';
  }

  @Nullable
  @Override
  public CharSequence getClosingQuote(HighlighterIterator iterator, int offset) {
    CharSequence documentSequence = iterator.getDocument().getCharsSequence();
    return Character.toString(PerlBaseLexer.getQuoteCloseChar(documentSequence.charAt(offset - 1)));
  }
}
