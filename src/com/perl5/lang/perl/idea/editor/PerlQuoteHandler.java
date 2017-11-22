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

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.perl5.lang.perl.lexer.PerlElementTypes;

import static com.perl5.lang.perl.PerlParserDefinition.LITERALS;
import static com.perl5.lang.perl.parser.PerlParserUtil.CLOSE_QUOTES;
import static com.perl5.lang.perl.parser.PerlParserUtil.OPEN_QUOTES;

/**
 * Created by hurricup on 10.06.2015.
 */
public class PerlQuoteHandler extends SimpleTokenSetQuoteHandler implements PerlElementTypes {

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
    return true;
  }
}
