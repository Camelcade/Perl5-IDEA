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

package com.perl5.lang.mason.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.LexerBase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.lexer.PerlLexerGenerated;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 20.12.2015.
 * fixme this adapter repeats MojoLexerAdapter, need to DRY them
 */
public class MasonPerlLexerAdapter  extends LexerBase
{
	private MasonPerlLexer myFlex = null;
	private IElementType myTokenType = null;
	private CharSequence myText;

	private int myEnd;
	private int myState;

	public MasonPerlLexerAdapter(Project project)
	{
		myFlex = new MasonPerlLexer(project);
	}

	public FlexLexer getFlex()
	{
		return myFlex;
	}

	@Override
	public void start(@NotNull final CharSequence buffer, int startOffset, int endOffset, int initialState)
	{
		myText = buffer;
		myEnd = endOffset;

		if (startOffset == 0 && initialState == PerlLexerGenerated.YYINITIAL)
			myFlex.setMasonState(PerlLexerGenerated.LEX_HTML_BLOCK);
		else if (initialState > 255)
		{
			myFlex.setMasonState((int) (initialState / 255));
			initialState = initialState % 255;
		}
		else
			myFlex.setMasonState(PerlLexerGenerated.LEX_MOJO_PERL_BLOCK);

		myFlex.reset(myText, startOffset, endOffset, initialState);
		myTokenType = null;
	}

	@Override
	public int getState()
	{
		if (myTokenType == null) locateToken();
		return myState;
	}

	@Override
	public IElementType getTokenType()
	{
		if (myTokenType == null) locateToken();
		return myTokenType;
	}

	@Override
	public int getTokenStart()
	{
		if (myTokenType == null) locateToken();
		return myFlex.getTokenStart();
	}

	@Override
	public int getTokenEnd()
	{
		if (myTokenType == null) locateToken();
		return myFlex.getTokenEnd();
	}

	@Override
	public void advance()
	{
		if (myTokenType == null) locateToken();
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
		return myEnd;
	}

	protected void getCompiledState()
	{
		int masonState = myFlex.getMasonState();
		int lexerState = myFlex.yystate();

		assert masonState < 255;
		assert lexerState < 255;

		if (masonState == PerlLexer.LEX_MOJO_PERL_BLOCK)
			myState = lexerState;
		else
			myState = masonState * 255 + lexerState;
	}

	protected void locateToken()
	{
		if (myTokenType != null) return;
		try
		{
			getCompiledState();
			myTokenType = myFlex.advance();
		} catch (IOException e)
		{ /*Can't happen*/ } catch (Error e)
		{
			// add lexer class name to the error
			final Error error = new Error(myFlex.getClass().getName() + ": " + e.getMessage());
			error.setStackTrace(e.getStackTrace());
			throw error;
		}
	}
}
