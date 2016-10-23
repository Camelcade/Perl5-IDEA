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
 * Second level adapter, relexes lazy blocks if necessary
 */

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.LexerBase;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;

public class PerlSublexingLexerAdapter extends LexerBase implements PerlElementTypes
{
	private static final Logger LOG = Logger.getInstance(FlexAdapter.class);
	private static final int LAZY_BLOCK_MINIMAL_SIZE = 140;
	private static Map<IElementType, Integer> SUBLEXINGS_MAP = new THashMap<>();

	static
	{
		SUBLEXINGS_MAP.put(LP_STRING_QW, PerlLexer.STRING_LIST);
		SUBLEXINGS_MAP.put(LP_STRING_Q, PerlLexer.STRING_Q);
		SUBLEXINGS_MAP.put(LP_STRING_QQ, PerlLexer.STRING_QQ);
		SUBLEXINGS_MAP.put(LP_STRING_XQ, PerlLexer.STRING_QX);

		SUBLEXINGS_MAP.put(LP_REGEX, PerlLexer.MATCH_REGEX);
		SUBLEXINGS_MAP.put(LP_REGEX_X, PerlLexer.EXTENDED_MATCH_REGEX);
		SUBLEXINGS_MAP.put(LP_REGEX_REPLACEMENT, PerlLexer.REPLACEMENT_REGEX);
		SUBLEXINGS_MAP.put(LP_CODE_BLOCK, PerlLexer.YYINITIAL);
	}

	@Nullable
	private final Project myProject;
	private boolean myIsForcingSublexing;
	private boolean myIsSublexing = false;
	private PerlCodeMergingLexerAdapter myMainLexer;
	private PerlSublexingLexerAdapter mySubLexer;
	private int myTokenStart;
	private int myTokenEnd;
	private int myState;
	private IElementType myTokenType;

	public PerlSublexingLexerAdapter(@Nullable Project project, boolean allowToMergeCodeBlocks, boolean forceSublexing)
	{
		myMainLexer = new PerlCodeMergingLexerAdapter(project, allowToMergeCodeBlocks);
		myIsForcingSublexing = forceSublexing;
		myProject = project;
	}

	@Override
	public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState)
	{
		myMainLexer.start(buffer, startOffset, endOffset, initialState);
		myTokenStart = myTokenEnd = startOffset;
		myTokenType = null;
		myIsSublexing = false;
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
		return myMainLexer.getBufferSequence();
	}

	@Override
	public int getBufferEnd()
	{
		return myMainLexer.getBufferEnd();
	}


	@NotNull
	private PerlSublexingLexerAdapter getSubLexer()
	{
		if (mySubLexer == null)
		{
			mySubLexer = new PerlSublexingLexerAdapter(myProject, false, true);
		}
		return mySubLexer;
	}

	protected void locateToken()
	{
		if (myTokenType != null)
		{
			return;
		}

		try
		{
			if (myIsSublexing)
			{
				lexToken(mySubLexer);

				if (myTokenType != null)
				{
					myState = PerlLexer.PREPARSED_ITEMS;
					return;
				}

				// sublexing finished
				myIsSublexing = false;
			}

			lexToken(myMainLexer);

			Integer subLexingState = SUBLEXINGS_MAP.get(myTokenType);

			if (subLexingState == null || (myTokenEnd - myTokenStart > LAZY_BLOCK_MINIMAL_SIZE && !myIsForcingSublexing))
			{
				return;
			}

			// need to sublex
			LexerBase subLexer = getSubLexer();
			subLexer.start(getBufferSequence(), myTokenStart, myTokenEnd, subLexingState);
			myIsSublexing = true;
			myTokenType = null;
			locateToken();
		}
		catch (Exception | Error e)
		{
			LOG.error(myMainLexer.getClass().getName(), e);
			myTokenType = TokenType.WHITE_SPACE;
			myTokenEnd = getBufferEnd();
		}
	}

	private void lexToken(LexerBase lexer) throws IOException
	{
		myTokenType = lexer.getTokenType();
		myTokenStart = lexer.getTokenStart();
		myState = lexer.getState();
		myTokenEnd = lexer.getTokenEnd();
		lexer.advance();
	}
}