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

package com.perl5.lang.perl.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by hurricup on 24.03.2016.
 */
public abstract class PerlProtoLexer implements FlexLexer
{
	protected final LinkedList<CustomToken> preparsedTokensList = new LinkedList<CustomToken>();
	protected final Stack<Integer> stateStack = new Stack<Integer>();
	protected final PerlTokenHistory myTokenHistory = new PerlTokenHistory();

	public abstract void setTokenStart(int position);

	public abstract void setTokenEnd(int position);

	public abstract CharSequence getBuffer();

	public abstract int getBufferEnd();

	public abstract int getNextTokenStart();

	public abstract boolean isLastToken();

	public IElementType advance() throws IOException
	{
		IElementType tokenType = null;

		if (preparsedTokensList.size() > 0)
			tokenType = getPreParsedToken();
		else
			tokenType = perlAdvance();

		if (tokenType != null)
			registerToken(tokenType, yytext());

		return tokenType;
	}

	public abstract IElementType perlAdvance() throws IOException;

	public abstract int getRealLexicalState();

	public abstract CharSequence yytext();

	protected abstract int getPreparsedLexicalState();

	@Override
	public int yystate()
	{
		return preparsedTokensList.size() > 0 ? getPreparsedLexicalState() : getRealLexicalState();
	}


	/**
	 * Reading tokens from parsed queue, setting start and end and returns them one by one
	 *
	 * @return token type or null if queue is empty
	 */
	public IElementType getPreParsedToken()
	{
		return restoreToken(preparsedTokensList.removeFirst());
	}

	private IElementType restoreToken(CustomToken token)
	{
		setTokenStart(token.getTokenStart());
		setTokenEnd(token.getTokenEnd());
		return token.getTokenType();
	}

	public void pushState()
	{
		stateStack.push(getRealLexicalState());
	}

	public void popState()
	{
		yybegin(stateStack.pop());
	}

	public void registerToken(IElementType tokenType, CharSequence tokenText)
	{
		getTokenHistory().addToken(tokenType, tokenText);
	}

	public PerlTokenHistory getTokenHistory()
	{
		return myTokenHistory;
	}

	/**
	 * Adds preparsed token to the queue with consistency control
	 *
	 * @param start     token start
	 * @param end       token end
	 * @param tokenType token type
	 */
	protected void pushPreparsedToken(int start, int end, IElementType tokenType)
	{
		pushPreparsedToken(getCustomToken(start, end, tokenType));
	}

	/**
	 * Adds preparsed token to the beginning of the queue with consistency control
	 *
	 * @param start     token start
	 * @param end       token end
	 * @param tokenType token type
	 */
	protected void unshiftPreparsedToken(int start, int end, IElementType tokenType)
	{
		unshiftPreparsedToken(getCustomToken(start, end, tokenType));
	}

	/**
	 * Helper for creating custom token object
	 *
	 * @param start     token start
	 * @param end       token end
	 * @param tokenType token type
	 * @return custom token object
	 */
	protected CustomToken getCustomToken(int start, int end, IElementType tokenType)
	{
		return new CustomToken(start, end, tokenType);
	}

	/**
	 * Adds preparsed token to the queue with consistency control
	 *
	 * @param token token to add
	 */
	protected void pushPreparsedToken(CustomToken token)
	{
		assert preparsedTokensList.size() == 0 ||
				preparsedTokensList.getLast().getTokenEnd() == token.getTokenStart() :
				"Tokens size is " +
						preparsedTokensList.size() +
						" new token start is " +
						token.getTokenStart() +
						(preparsedTokensList.size() == 0 ? "" :
								" last token end is " +
										preparsedTokensList.getLast().getTokenEnd());

		preparsedTokensList.add(token);
	}

	/**
	 * Adds preparsed token to the queue with consistency control
	 *
	 * @param token token to add
	 */
	protected void unshiftPreparsedToken(CustomToken token)
	{
		assert preparsedTokensList.size() == 0 ||
				preparsedTokensList.getFirst().getTokenStart() == token.getTokenEnd() :
				"Tokens size is " +
						preparsedTokensList.size() +
						" new token end is " +
						token.getTokenEnd() +
						(preparsedTokensList.size() == 0 ? "" :
								" first start end is " +
										preparsedTokensList.getFirst().getTokenStart());

		preparsedTokensList.addFirst(token);
	}

	protected void resetInternals()
	{
		getTokenHistory().reset();
		preparsedTokensList.clear();
		stateStack.clear();
	}

	protected Character getNextSignificantCharacter()
	{
		int nextPosition = getNextSignificantCharacterPosition(getTokenEnd());
		return nextPosition > -1 ? getBuffer().charAt(nextPosition) : null;
	}

	protected int getNextSignificantCharacterPosition(int position)
	{
		int currentPosition = position;
		int bufferEnd = getBufferEnd();
		CharSequence buffer = getBuffer();

		while (currentPosition < bufferEnd)
		{
			char currentChar = buffer.charAt(currentPosition);
			if (currentChar == '#')
			{
				while (currentPosition < bufferEnd)
				{
					if (buffer.charAt(currentPosition) == '\n')
						break;
					currentPosition++;
				}
			}
			else if (!Character.isWhitespace(currentChar))
				return currentPosition;

			currentPosition++;
		}
		return -1;
	}

	protected char getNextCharacter()
	{
		return getSafeCharacterAt(getTokenEnd());
	}

	protected char getSafeCharacterAt(int offset)
	{
		if (offset < getBufferEnd())
		{
			return getBuffer().charAt(offset);
		}
		return 0;
	}

	protected int getNextNonSpaceCharacterPosition(int position)
	{
		int currentPosition = position;
		int bufferEnd = getBufferEnd();
		CharSequence buffer = getBuffer();

		while (currentPosition < bufferEnd)
		{
			if (!Character.isWhitespace(buffer.charAt(currentPosition)))
				return currentPosition;

			currentPosition++;
		}
		return -1;
	}

	protected char getNextNonSpaceCharacter()
	{
		return getNextNonSpaceCharacter(getTokenEnd());
	}

	protected char getNextNonSpaceCharacter(int nextPosition)
	{
		nextPosition = getNextNonSpaceCharacterPosition(nextPosition);
		return nextPosition > -1 ? getBuffer().charAt(nextPosition) : 0;    // not sure it's a good idea
	}

}
