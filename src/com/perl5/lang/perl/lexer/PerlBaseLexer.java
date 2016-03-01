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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.util.PerlPackageUtil;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 10.08.2015.
 */
public abstract class PerlBaseLexer implements FlexLexer, PerlElementTypes
{
	protected final static int EXT_NOTHING = -1;
	protected final static int EXT_IDENTIFIER = 0;
	protected final static int EXT_PACKAGE = 1;
	protected final static int EXT_PACKAGE_DISCLOSED = 2;
	private static final String BASIC_IDENTIFIER_PATTERN_TEXT = "[_\\p{L}\\d][_\\p{L}\\d]*"; // something strang in Java with unicode props; Added digits to opener for package Encode::KR::2022_KR;
	private static final String PACKAGE_SEPARATOR_PATTERN_TEXT =
			"(?:" +
					"(?:::)+'?" +
					"|" +
					"(?:::)*'" +
					")";
	public static final Pattern AMBIGUOUS_PACKAGE_PATTERN = Pattern.compile(
			"(" +
					PACKAGE_SEPARATOR_PATTERN_TEXT + "?" +        // optional opening separator,
					"(?:" +
					BASIC_IDENTIFIER_PATTERN_TEXT +
					PACKAGE_SEPARATOR_PATTERN_TEXT +
					")*" +
					")" +
					"(" +
					BASIC_IDENTIFIER_PATTERN_TEXT +
					")");
	public final Stack<Integer> stateStack = new Stack<Integer>();
	public final LinkedList<CustomToken> preparsedTokensList = new LinkedList<CustomToken>();
	protected final PerlTokenHistory myTokenHistory = new PerlTokenHistory();
	protected int bufferStart;

	// has identifier inside
	public IElementType adjustAndParsePackage()
	{
		int adjustResult = adjustUtfIdentifier();
		if (adjustResult == EXT_PACKAGE_DISCLOSED)
		{
			return parsePackageCanonical();
		}
		else
		{
			return parsePackage();
		}
	}

	public abstract IElementType parsePackage();

	public IElementType adjustAndParseBarewordMinus()
	{
		int adjustResult = adjustUtfIdentifier();

		if (!StringUtil.startsWith(yytext(), "-"))
		{
			if (adjustResult == EXT_PACKAGE_DISCLOSED)
			{
				return parsePackageCanonical();
			}
			else if (adjustResult == EXT_PACKAGE)
			{
				return parsePackage();
			}
		}
		return parseBarewordMinus();
	}

	public abstract IElementType parseBarewordMinus();

	public abstract void setTokenStart(int position);

	public abstract void setTokenEnd(int position);

	public abstract CharSequence getBuffer();

	public abstract int getBufferEnd();

	public abstract int getNextTokenStart();

	public abstract boolean isLastToken();

	public abstract int getRealLexicalState();

	public abstract CharSequence yytext();

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

	// only ::
	public IElementType adjustAndParsePackageShort()
	{
		int adjustResult = adjustUtfIdentifier();

		if (adjustResult == EXT_PACKAGE_DISCLOSED) // got ::smth::
		{
			return parsePackageCanonical();
		}
		else if (adjustResult == EXT_PACKAGE || adjustResult == EXT_IDENTIFIER) // got ::smth +
		{
			return parsePackage();
		}

		return PACKAGE_IDENTIFIER; // only ::
	}

	// ends with ::
	public IElementType adjustAndParsePackageCanonical()
	{
		int adjustResult = adjustUtfIdentifier();
		if (adjustResult == EXT_IDENTIFIER || adjustResult == EXT_PACKAGE)    // ends with identifier
		{
			return parsePackage();
		}
		else
		{
			return parsePackageCanonical();    // ends with ::
		}
	}

	public IElementType parsePackageCanonical()
	{
		String canonicalPackageName = PerlPackageUtil.getCanonicalPackageName(yytext().toString());
		if (canonicalPackageName.equals(PerlPackageUtil.CORE_PACKAGE))
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

	public void registerToken(IElementType tokenType, CharSequence tokenText)
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
		addPreparsedToken(getCustomToken(start, end, tokenType));
	}

	/**
	 * Adds preparsed token to the queue with consistency control
	 *
	 * @param token token to add
	 */
	protected void addPreparsedToken(CustomToken token)
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

	protected IElementType lexBadCharacter()
	{
		int tokenStart = getTokenStart();
		int bufferEnd = getBufferEnd();
		CharSequence buffer = getBuffer();

		if (tokenStart < bufferEnd)
		{
			if (isValidIdentifierStartCharacter(buffer.charAt(tokenStart)))
			{
				int adjustResult = adjustUtfIdentifier();

				if (adjustResult == EXT_PACKAGE_DISCLOSED)
				{
					return parsePackageCanonical();
				}
				else if (adjustResult == EXT_PACKAGE)
				{
					return parsePackage();
				}
				return IDENTIFIER;
			}
		}
		return TokenType.BAD_CHARACTER;
	}

	protected int adjustUtfIdentifier()
	{
		int bufferEnd = getBufferEnd();
		CharSequence buffer = getBuffer();
		int tokenEnd = getTokenEnd();
		int result = EXT_NOTHING;
		char currentChar;
		while (tokenEnd < bufferEnd)
		{
			if (isValidIdentifierCharacter(currentChar = buffer.charAt(tokenEnd)))
			{
				tokenEnd++;
				if (result == EXT_PACKAGE_DISCLOSED)
				{
					result = EXT_PACKAGE;
				}
				else if (result == EXT_NOTHING)
				{
					result = EXT_IDENTIFIER;
				}
			}
			else if (currentChar == ':' && tokenEnd + 1 < bufferEnd && buffer.charAt(tokenEnd + 1) == ':')
			{
				tokenEnd += 2;
				result = EXT_PACKAGE_DISCLOSED;
			}
			else if (currentChar == '\'' && tokenEnd + 1 < bufferEnd && isValidIdentifierCharacter(buffer.charAt(tokenEnd + 1)))
			{
				tokenEnd += 2;
				result = EXT_PACKAGE;
			}
			else
			{
				break;
			}
		}

		setTokenEnd(tokenEnd);
		return result;
	}

	public boolean isValidIdentifierCharacter(char character)
	{
		return character == '_' || Character.isLetterOrDigit(character);
	}

	public boolean isValidIdentifierStartCharacter(char character)
	{
		return character == '_' || Character.isLetter(character);
	}
}
