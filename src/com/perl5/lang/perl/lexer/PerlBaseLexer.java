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

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.util.PerlPackageUtil;

import java.util.regex.Pattern;

/**
 * Created by hurricup on 10.08.2015.
 */
public abstract class PerlBaseLexer extends PerlProtoLexer implements PerlElementTypes
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


	// check that current token surrounded with braces
	protected boolean isBraced()
	{
		return getTokenHistory().getLastSignificantTokenType() == LEFT_BRACE && getNextNonSpaceCharacter() == '}';
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
				else
				{
					return parseBarewordMinus();
				}
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
