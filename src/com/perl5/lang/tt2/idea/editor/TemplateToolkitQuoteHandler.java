/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.tt2.idea.editor;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;

/**
 * Created by hurricup on 12.06.2016.
 */
public class TemplateToolkitQuoteHandler extends SimpleTokenSetQuoteHandler implements TemplateToolkitElementTypes
{
	public static final TokenSet OPENING_QUOTES = TokenSet.create(
			TT2_DQ_OPEN,
			TT2_SQ_OPEN
	);
	public static final TokenSet CLOSING_QUOTES = TokenSet.create(
			TT2_DQ_CLOSE,
			TT2_SQ_CLOSE
	);

	public TemplateToolkitQuoteHandler()
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
