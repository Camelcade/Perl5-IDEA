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
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.util.PerlPackageUtil;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 10.08.2015.
 */
public abstract class PerlBaseLexer implements FlexLexer, PerlElementTypes
{
	private static final String reBasicIdentifier = "[_a-zA-Z0-9][_a-zA-Z0-9]*"; // something strang in Java with unicode props; Added digits to opener for package Encode::KR::2022_KR;
	private static final String reSeparator =
			"(?:" +
					"(?:::)+'?" +
					"|" +
					"(?:::)*'" +
					")";
	public static final Pattern AMBIGUOUS_PACKAGE_RE = Pattern.compile(
			"(" +
					reSeparator + "?" +        // optional opening separator,
					"(?:" +
					reBasicIdentifier +
					reSeparator +
					")*" +
					")" +
					"(" +
					reBasicIdentifier +
					")"
	);
	public final Stack<Integer> stateStack = new Stack<Integer>();
	public final LinkedList<CustomToken> preparsedTokensList = new LinkedList<CustomToken>();
	protected final PerlTokenHistory myTokenHistory = new PerlTokenHistory();
	protected int bufferStart;

	public abstract IElementType parsePackage();

	public abstract IElementType parseBarewordMinus();

	public abstract void setTokenStart(int position);

	public abstract void setTokenEnd(int position);

	public abstract CharSequence getBuffer();

	public abstract int getBufferEnd();

	public abstract int getNextTokenStart();

	public abstract boolean isLastToken();

	public abstract CharSequence yytext();

	public IElementType advance() throws IOException
	{
		IElementType tokenType = null;

		if (preparsedTokensList.size() > 0)
			tokenType = getPreParsedToken();
		else
			tokenType = perlAdvance();

		if (tokenType != null)
			registerToken(tokenType, yytext().toString());

		return tokenType;
	}

	public abstract IElementType perlAdvance() throws IOException;


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
		stateStack.push(yystate());
	}

	public void popState()
	{
		yybegin(stateStack.pop());
	}

	public IElementType parsePackageCanonical()
	{
		String canonicalPackageName = PerlPackageUtil.getCanonicalPackageName(yytext().toString());
		if (canonicalPackageName.equals("CORE"))
			return PACKAGE_CORE_IDENTIFIER;
		return PACKAGE_IDENTIFIER;
	}

	public PerlTokenHistory getTokenHistory()
	{
		return myTokenHistory;
	}

	// check that current token surrounded with braces
	protected boolean isBraced()
	{
		return getTokenHistory().getLastSignificantTokenType() == LEFT_BRACE && getNextNonSpaceCharacter() == '}';
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
		int currentPosition = getTokenEnd();
		int bufferEnd = getBufferEnd();
		CharSequence buffer = getBuffer();
		if (currentPosition < bufferEnd)
		{
			return buffer.charAt(currentPosition);
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
		int nextPosition = getNextNonSpaceCharacterPosition(getTokenEnd());
		return nextPosition > -1 ? getBuffer().charAt(nextPosition) : 0;    // not sure it's a good idea
	}

	public void registerToken(IElementType tokenType, String tokenText)
	{
		getTokenHistory().addToken(tokenType, tokenText);
	}

	// fixme this must be done using skeleton
	public void resetInternals()
	{
		getTokenHistory().reset();
		preparsedTokensList.clear();
		bufferStart = getTokenStart();
	}

	public int getBufferStart()
	{
		return bufferStart;
	}

	/**
	 * Adds preparsed token to the queue with consistency control
	 *
	 * @param start     token start
	 * @param end       token end
	 * @param tokenType token type
	 */
	protected void addPreparsedToken(int start, int end, IElementType tokenType)
	{
		assert preparsedTokensList.size() == 0 || preparsedTokensList.getLast().getTokenEnd() == start : "Tokens size is " + preparsedTokensList.size() + " new token start is " + start +
				(preparsedTokensList.size() == 0 ? "" : " last token end is " + preparsedTokensList.getLast().getTokenEnd());
		preparsedTokensList.add(getCustomToken(start, end, tokenType));
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

}
