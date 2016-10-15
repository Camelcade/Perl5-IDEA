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

package com.perl5.lang.perl.lexer.adapters;

/**
 * Created by hurricup on 12.04.2015.
 */

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.LexerBase;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

public class PerlLexerAdapter extends LexerBase implements PerlElementTypes
{
	private static final Logger LOG = Logger.getInstance(FlexAdapter.class);
	private static Map<IElementType, Integer> SUBLEXINGS_MAP = new THashMap<>();

	static
	{
		SUBLEXINGS_MAP.put(LP_STRING_QW, PerlLexer.LEX_STRING_LIST);
		SUBLEXINGS_MAP.put(LP_STRING_Q, PerlLexer.LEX_STRING_CONTENT);
		SUBLEXINGS_MAP.put(LP_STRING_QQ, PerlLexer.LEX_STRING_CONTENT_QQ);
		SUBLEXINGS_MAP.put(LP_STRING_XQ, PerlLexer.LEX_STRING_CONTENT_XQ);

		SUBLEXINGS_MAP.put(LP_REGEX, PerlLexer.LEX_REGEX);
		SUBLEXINGS_MAP.put(LP_REGEX_X, PerlLexer.LEX_EXTENDED_REGEX);
		SUBLEXINGS_MAP.put(LP_CODE, PerlLexer.YYINITIAL);
	}

	private final FlexLexer myFlex;

	private IElementType myTokenType;
	private CharSequence myText;

	private int myTokenStart;
	private int myTokenEnd;

	private int myBufferEnd;
	private int myState;

	private boolean isMySubLexing = false;
	private PerlLexer mySubLexer;

	public PerlLexerAdapter()
	{
		myFlex = new PerlLexer();
	}

	public FlexLexer getFlex()
	{
		return myFlex;
	}

	@Override
	public void start(@NotNull final CharSequence buffer, int startOffset, int endOffset, final int initialState)
	{
		assert initialState == PerlLexer.YYINITIAL : "Attempt to reset to non-initial state";
		myText = buffer;
		myTokenStart = myTokenEnd = startOffset;
		myBufferEnd = endOffset;
		myFlex.reset(myText, startOffset, endOffset, initialState);
		myTokenType = null;
		isMySubLexing = false;
	}

	@Override
	public int getState()
	{
		locateToken();
		return isMySubLexing ? PerlLexer.LEX_PREPARSED_ITEMS : myState;
	}

	@NotNull
	private PerlLexer getSubLexer()
	{
		if (mySubLexer == null)
		{
			mySubLexer = new PerlLexer();
		}
		return mySubLexer;
	}

	@Override
	public IElementType getTokenType()
	{
		locateToken();
		return myTokenType;
	}

	@Override
	public int getTokenStart()
	{
		locateToken();
		return myTokenStart;
	}

	@Override
	public int getTokenEnd()
	{
		locateToken();
		return myTokenEnd;
	}

	@Override
	public void advance()
	{
		locateToken();
		myTokenType = null;
	}

	@NotNull
	@Override
	public CharSequence getBufferSequence()
	{
		return myText;
	}

	@Override
	public int getBufferEnd()
	{
		return myBufferEnd;
	}

	protected void locateToken()
	{
		if (myTokenType != null)
		{
			return;
		}

		try
		{
			if (isMySubLexing)
			{
				lexToken(getSubLexer());

				if (myTokenType != null)
				{
					return;
				}

				// sublexing finished
				isMySubLexing = false;
			}

			lexToken(myFlex);

			Integer subLexingState = SUBLEXINGS_MAP.get(myTokenType);
			// fixme add a thrashhold here for large LPE chunks
			if (subLexingState == null)
			{
				return;
			}

			// need to sublex
			PerlLexer subLexer = getSubLexer();
			subLexer.reset(myText, myTokenStart, myTokenEnd, subLexingState);
			isMySubLexing = true;
			myTokenType = null;
			locateToken();
		}
		catch (Exception | Error e)
		{
			LOG.error(myFlex.getClass().getName(), e);
			myTokenType = TokenType.WHITE_SPACE;
			myTokenEnd = myBufferEnd;
		}
	}

	private void lexToken(FlexLexer lexer) throws IOException
	{
		myTokenStart = lexer.getTokenEnd();
		myState = lexer.yystate();
		myTokenType = lexer.advance();
		myTokenEnd = lexer.getTokenEnd();
	}
}