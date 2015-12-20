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

package com.perl5.lang.perl.idea.editor;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;

/**
 * Created by hurricup on 10.06.2015.
 */
public class PerlQuoteHandler extends SimpleTokenSetQuoteHandler
{
	public static final TokenSet OPENING_QUOTES = TokenSet.create(PerlElementTypes.QUOTE_SINGLE_OPEN, PerlElementTypes.QUOTE_DOUBLE_OPEN, PerlElementTypes.QUOTE_TICK_OPEN);
	public static final TokenSet CLOSING_QUOTES = TokenSet.create(PerlElementTypes.QUOTE_SINGLE_CLOSE, PerlElementTypes.QUOTE_DOUBLE_CLOSE, PerlElementTypes.QUOTE_TICK_CLOSE);

	public PerlQuoteHandler()
	{
		super(OPENING_QUOTES);
	}

	@Override
	public boolean isClosingQuote(HighlighterIterator iterator, int offset)
	{
		return CLOSING_QUOTES.contains(iterator.getTokenType());
	}

	@Override
	public boolean isOpeningQuote(HighlighterIterator iterator, int offset)
	{
		return OPENING_QUOTES.contains(iterator.getTokenType());
	}

}
