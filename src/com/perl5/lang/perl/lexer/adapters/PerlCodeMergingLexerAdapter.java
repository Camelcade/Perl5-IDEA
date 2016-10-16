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

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.LexerBase;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 16.10.2016.
 * First level adapter, working above Flex lexer. Merges code blocks into LP_CODE_BLOCK tokens
 * fixme would be better to use lookahead adapter here; currently small code blocks may be lexed twice
 */
public class PerlCodeMergingLexerAdapter extends LexerBase implements PerlElementTypes
{
	private static final Logger LOG = Logger.getInstance(FlexAdapter.class);
	private final FlexLexer myFlex;
	private int myBufferStart;
	private IElementType myTokenType;
	private CharSequence myText;

	private int myTokenStart;
	private int myTokenEnd;

	private int myBufferEnd;
	private int myState;

	private boolean myAllowToMergeCodeBlocks;

	public PerlCodeMergingLexerAdapter(boolean allowToMergeCodeBlocks)
	{
		myAllowToMergeCodeBlocks = allowToMergeCodeBlocks;
		myFlex = new PerlLexer();
	}

	public FlexLexer getFlex()
	{
		return myFlex;
	}

	@Override
	public void start(@NotNull final CharSequence buffer, int startOffset, int endOffset, final int initialState)
	{
		myText = buffer;
		myTokenStart = myTokenEnd = myBufferStart = startOffset;
		myBufferEnd = endOffset;
		myFlex.reset(myText, startOffset, endOffset, initialState);
		myTokenType = null;
	}

	@Override
	public int getState()
	{
		locateToken();
		return myState;
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
			myTokenStart = myFlex.getTokenEnd();
			myState = myFlex.yystate();
			myTokenType = myFlex.advance();
			myTokenEnd = myFlex.getTokenEnd();
			mergeCode();
		}
		catch (Exception | Error e)
		{
			LOG.error(myFlex.getClass().getName(), e);
			myTokenType = TokenType.WHITE_SPACE;
			myTokenEnd = myBufferEnd;
		}
	}

	protected void mergeCode() throws IOException
	{
		if (myTokenType != LEFT_BRACE_CODE)
		{
			return;
		}
		if (myTokenStart == myBufferStart || !myAllowToMergeCodeBlocks)    // block reparsing
		{
			myTokenType = LEFT_BRACE;
			return;
		}

		int bracesDepth = 0;
		while (true)
		{
			IElementType nextTokenType = myFlex.advance();
			if (nextTokenType == null)
			{
				break;
			}
			else if (nextTokenType == LEFT_BRACE || nextTokenType == LEFT_BRACE_CODE)
			{
				bracesDepth++;
			}
			else if (nextTokenType == RIGHT_BRACE)
			{
				if (bracesDepth == 0)
				{
					break;
				}
				bracesDepth--;
			}
		}
		myTokenEnd = myFlex.getTokenEnd();
		myTokenType = LP_CODE_BLOCK;
		myState = PerlLexer.YYINITIAL;
	}
}
