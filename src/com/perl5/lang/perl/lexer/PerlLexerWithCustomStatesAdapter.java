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

package com.perl5.lang.perl.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 20.12.2015.
 */
public class PerlLexerWithCustomStatesAdapter extends LexerBase
{
	private PerlLexerWithCustomStates myFlex = null;
	private IElementType myTokenType = null;
	private CharSequence myText;

	private int myEnd;
	private int myState;

	public PerlLexerWithCustomStatesAdapter(PerlLexerWithCustomStates lexer)
	{
		myFlex = lexer;
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

		if (startOffset == 0 && initialState == PerlLexerGenerated.YYINITIAL)    // beginning of doc
		{
			myFlex.setCustomState(myFlex.getInitialCustomState());
		}
		else if (initialState > 255)    // properly packed state
		{
			myFlex.setCustomState((int) (initialState / 255));
			initialState = initialState % 255;
		}
		else
		{
			throw new RuntimeException("Shouldn't be here, inproperly packed state");
		}

		myFlex.reset(myText, startOffset, endOffset, initialState);
		myTokenType = null;
	}

	@Override
	public int getState()
	{
		if (myTokenType == null)
		{
			locateToken();
		}
		return myState;
	}

	@Override
	public IElementType getTokenType()
	{
		if (myTokenType == null)
		{
			locateToken();
		}
		return myTokenType;
	}

	@Override
	public int getTokenStart()
	{
		if (myTokenType == null)
		{
			locateToken();
		}
		return myFlex.getTokenStart();
	}

	@Override
	public int getTokenEnd()
	{
		if (myTokenType == null)
		{
			locateToken();
		}
		return myFlex.getTokenEnd();
	}

	@Override
	public void advance()
	{
		if (myTokenType == null)
		{
			locateToken();
		}
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

	protected void compileState()
	{
		int customState = myFlex.getCustomState();
		int lexerState = myFlex.yystate();

		assert customState < 255;
		assert lexerState < 255;

		myState = customState * 255 + lexerState;
	}

	protected void locateToken()
	{
		if (myTokenType != null)
		{
			return;
		}
		try
		{
			compileState();
			myTokenType = myFlex.advance();
		}
		catch (IOException e)
		{ /*Can't happen*/ }
		catch (Error e)
		{
			// add lexer class name to the error
			final Error error = new Error(myFlex.getClass().getName() + ": " + e.getMessage());
			error.setStackTrace(e.getStackTrace());
			throw error;
		}
	}
}
