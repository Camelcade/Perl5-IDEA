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

import com.intellij.psi.TokenType;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;

public class RegexBlock implements PerlElementTypes
{
	public static final HashMap<String, List<Character>> allowedModifiers = new HashMap<String, List<Character>>();
	public static final List<Character> whiteSpaces = Arrays.asList(' ', '\n', '\t', '\f', '\r');

	static
	{
		allowedModifiers.put("s", Arrays.asList(ArrayUtils.toObject("msixpodualgcer".toCharArray())));
		allowedModifiers.put("m", Arrays.asList(ArrayUtils.toObject("msixpodualgc".toCharArray())));
		allowedModifiers.put("qr", Arrays.asList(ArrayUtils.toObject("msixpodual".toCharArray())));
	}

	protected int startOffset;

	//	public static final Pattern OCCURANCE_PATTERN = Pattern.compile("\\{\\d+(,\\d*)?\\}");
//	public static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\\\[xNogbB]\\{.*?\\}");
	protected int endOffset;
	protected CharSequence buffer;
	protected char charOpener;
	protected char charCloser;

	protected RegexBlock(CharSequence buffer, int startOffset, int endOffset, char charOpener, char charCloser)
	{
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.buffer = buffer;
		this.charOpener = charOpener;
		this.charCloser = charCloser;
	}

	public static boolean isWhiteSpace(char character)
	{
		return whiteSpaces.contains(character);
	}

	/**
	 * Parses guaranteed opened regex block
	 *
	 * @param buffer      Input characters stream
	 * @param startOffset Start parsing offset
	 * @param bufferSize  Buffer last offset
	 * @param openingChar Opener character
	 * @return Parsed regex block or null if failed
	 */
	public static RegexBlock parseBlock(CharSequence buffer, int startOffset, int bufferSize, char openingChar, boolean isSecondBlock)
	{
		char closingChar = getQuoteCloseChar(openingChar);

		boolean isEscaped = false;
		boolean isCharGroup = false;
		boolean isQuotesDiffers = closingChar != openingChar;

		int braceLevel = 0;
		int parenLevel = 0;
		int delimiterLevel = 0;

		RegexBlock newBlock = null;
		int currentOffset = startOffset;

		while (true)
		{
			if (currentOffset == bufferSize)
				break;

			char currentChar = buffer.charAt(currentOffset);

			if (delimiterLevel == 0 && braceLevel == 0 && !isCharGroup && !isEscaped && parenLevel == 0 && closingChar == currentChar)
			{
				newBlock = new RegexBlock(buffer, startOffset, currentOffset + 1, openingChar, closingChar);
				break;
			}

			if (!isSecondBlock)
			{
				if (!isEscaped && !isCharGroup && currentChar == '[')
					isCharGroup = true;
				else if (!isEscaped && isCharGroup && currentChar == ']')
					isCharGroup = false;

				// @todo this is buggy, sometimes bare is allowed. See example from `redo` doc
//				if (!isEscaped && !isCharGroup && currentChar == '{')
//					braceLevel++;
//				else if (!isEscaped && !isCharGroup && braceLevel > 0 && currentChar == '}')
//					braceLevel--;
//
//				if (!isEscaped && !isCharGroup && currentChar == '(')
//					parenLevel++;
//				else if (!isEscaped && !isCharGroup && parenLevel > 0 && currentChar == ')')
//					parenLevel--;
			}

			if (!isEscaped && isQuotesDiffers && !isCharGroup)
				if (currentChar == openingChar)
					delimiterLevel++;
				else if (currentChar == closingChar && delimiterLevel > 0)
					delimiterLevel--;

			isEscaped = !isEscaped && closingChar != '\\' && currentChar == '\\';

			currentOffset++;
		}
		return newBlock;
	}

	/**
	 * Choosing closing character by opening one
	 *
	 * @param charOpener - char with which sequence started
	 * @return - ending char
	 */
	public static char getQuoteCloseChar(char charOpener)
	{
		if (charOpener == '<')
			return '>';
		else if (charOpener == '{')
			return '}';
		else if (charOpener == '(')
			return ')';
		else if (charOpener == '[')
			return ']';
		else
			return charOpener;
	}

	public int getStartOffset()
	{
		return startOffset;
	}

	public int getEndOffset()
	{
		return endOffset;
	}

	public Character getOpeningQuote()
	{
		return charOpener;
	}

	public Character getClosingQuote()
	{
		return charCloser;
	}

	public boolean hasSameQuotes()
	{
		return charOpener == charCloser;
	}


	Collection<CustomToken> parseEval(PerlLexerAdapter subLexer)
	{
		ArrayList<CustomToken> tokens = new ArrayList<CustomToken>();
		subLexer.start(buffer, startOffset, endOffset - 1);
		while (subLexer.getTokenType() != null)
		{
			tokens.add(new CustomToken(subLexer.getTokenStart(), subLexer.getTokenEnd(), subLexer.getTokenType()));
			subLexer.advance();
		}

		tokens.add(new CustomToken(endOffset - 1, endOffset, REGEX_QUOTE_CLOSE));

		return tokens;
	}


	/**
	 * Tokenizing entry point
	 *
	 * @param isExtended flag that regular expression is extended with spaces and comments
	 * @return ArrayList of tokens
	 */
	public Collection<CustomToken> tokenize(PerlStringLexer stringLexer, boolean isExtended, boolean isSecondBlock)
	{
		return isExtended
				? tokenizeExtended(stringLexer, isSecondBlock)
				: tokenizeRegular(stringLexer, isSecondBlock);
	}

	/**
	 * Tokenizing regexp object with nested comments and spaces
	 *
	 * @return array of CustomTokens
	 */
	protected Collection<CustomToken> tokenizeExtended(PerlStringLexer stringLexer, boolean isSecondBlock)
	{
		ArrayList<CustomToken> tokens = new ArrayList<CustomToken>();

		int currentOffset = startOffset;
		boolean isEscaped = false;
		boolean isCharGroup = false;
		int regexEndOffset = endOffset - 1; // one for quote

		while (currentOffset < regexEndOffset)
		{
			char currentChar = buffer.charAt(currentOffset);
			int charsLeft = regexEndOffset - currentOffset;

			if (charsLeft > 3 && "(?#".equals(buffer.subSequence(currentOffset, currentOffset + 3).toString()))
			{
				int commentStart = currentOffset;
				currentOffset += 2;
				while (currentOffset < regexEndOffset && buffer.charAt(currentOffset) != ')')
					currentOffset++;
				if (currentOffset < regexEndOffset)
					currentOffset++;
				tokens.add(new CustomToken(commentStart, currentOffset, COMMENT_LINE));
			} else if (!isEscaped && isWhiteSpace(currentChar)) // whitespace here
			{
				int whiteSpaceStart = currentOffset;
				while (currentOffset < regexEndOffset && isWhiteSpace(buffer.charAt(currentOffset)))
					currentOffset++;
				tokens.add(new CustomToken(whiteSpaceStart, currentOffset, TokenType.WHITE_SPACE));
			} else if (!isEscaped && currentChar == '#') // comment here
			{
				int commentStart = currentOffset;
				while (currentOffset < regexEndOffset && buffer.charAt(currentOffset) != '\n')
					currentOffset++;
				tokens.add(new CustomToken(commentStart, currentOffset, COMMENT_LINE));
			} else
				tokens.add(new CustomToken(currentOffset, ++currentOffset, REGEX_TOKEN));

			if (!isSecondBlock)
				if (!isEscaped && !isCharGroup && currentChar == '[')
					isCharGroup = true;
				else if (!isEscaped && isCharGroup && currentChar == ']')
					isCharGroup = false;

			isEscaped = !isEscaped && currentChar == '\\';
		}

		tokens.add(new CustomToken(currentOffset, currentOffset + 1, REGEX_QUOTE_CLOSE));

		return tokens;
	}

	/**
	 * Tokenizing regexp object without nested comments and spaces
	 *
	 * @return array of CustomTokens
	 */
	protected Collection<CustomToken> tokenizeRegular(PerlStringLexer stringLexer, boolean isSecondBlock)
	{
		ArrayList<CustomToken> tokens = new ArrayList<CustomToken>();

		int currentOffset = startOffset;
		int blockStart = currentOffset;

		boolean isEscaped = false;
		boolean isCharGroup = false;
		int regexEndOffset = endOffset - 1; // one for quote


		while (currentOffset < regexEndOffset)
		{
			char currentChar = buffer.charAt(currentOffset);
			int charsLeft = regexEndOffset - currentOffset;

			if (!isEscaped && !isCharGroup && charsLeft > 3 && "(?#".equals(buffer.subSequence(currentOffset, currentOffset + 3).toString()))
			{
				if (currentOffset > blockStart)
				{
					stringLexer.reset(buffer, blockStart, currentOffset, 0);
					tokens.addAll(PerlLexer.lexString(stringLexer));
				}

				int commentStart = currentOffset;
				currentOffset += 2;
				while (currentOffset < regexEndOffset && buffer.charAt(currentOffset) != ')')
					currentOffset++;

				blockStart = currentOffset;
				if (currentOffset == regexEndOffset)
					tokens.add(new CustomToken(commentStart, currentOffset, COMMENT_LINE));
				else
					tokens.add(new CustomToken(commentStart, currentOffset + 1, COMMENT_LINE));
			}

			if (!isSecondBlock)
			{
				if (!isEscaped && !isCharGroup && currentChar == '[')
					isCharGroup = true;
				else if (!isEscaped && isCharGroup && currentChar == ']')
					isCharGroup = false;
			}

			isEscaped = !isEscaped && currentChar == '\\';

			currentOffset++;
		}

		if (currentOffset > blockStart)
		{
			stringLexer.reset(buffer, blockStart, currentOffset, 0);
			tokens.addAll(PerlLexer.lexString(stringLexer));
		}

		tokens.add(new CustomToken(currentOffset, currentOffset + 1, REGEX_QUOTE_CLOSE));

		return tokens;
	}

}
