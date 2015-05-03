package com.perl5.lang.perl.lexer;

import com.google.common.primitives.Chars;

import java.util.*;

/**
 * Created by hurricup on 02.05.2015.
 */
public class RegexBlock implements PerlElementTypes
{
	public static final HashMap<String, List<Character>> allowedModifiers = new HashMap<String, List<Character>>();
	public static final List<Character> whiteSpaces = Chars.asList(' ', '\n', '\t', '\f', '\r');

	static
	{
		allowedModifiers.put("s", Chars.asList("msixpodualgcer".toCharArray()));
		allowedModifiers.put("m", Chars.asList("msixpodualgc".toCharArray()));
		allowedModifiers.put("qr", Chars.asList("msixpodual".toCharArray()));
	}

	public static boolean isWhiteSpace(char character)
	{
		return whiteSpaces.contains(character);
	}


	/**
	 * Parses character stream for one regex block with opening and closing quote
	 * @param buffer	character stream
	 * @param startOffset	parsing start offset
	 * @param bufferSize	buffer size
	 * @return	RegexBlock object
	 */
	public static RegexBlock parseBlock(CharSequence buffer, int startOffset, int bufferSize)
	{
		return parseBlock(buffer, startOffset, bufferSize, startOffset+1, buffer.charAt(startOffset), startOffset);

	}

	/**
	 * Parses character stream for one regex block with opening and closing quote
	 * @param buffer	character stream
	 * @param startOffset	parsing start offset
	 * @param bufferSize	buffer size
	 * @return	RegexBlock object
	 */
	public static RegexBlock parseBlock(CharSequence buffer, int startOffset, int bufferSize, char openingChar)
	{
		return parseBlock(buffer, startOffset, bufferSize, startOffset, openingChar, null);
	}

	/**
	 * Parses guaranteed opened regex block
	 * @param buffer               	Input characters stream
	 * @param startOffset          	Buffer start offset
	 * @param bufferSize			Buffer last offset
	 * @param currentOffset			Current parsing offset
	 * @param openingChar			Opener character
	 * @param openingCharOffset		Offset of the opening character
	 * @return						Parsed regex block or null if failed
	 */
	protected static RegexBlock parseBlock(CharSequence buffer, int startOffset, int bufferSize, int currentOffset, char openingChar, Integer openingCharOffset)
	{
		char closingChar = getQuoteCloseChar(openingChar);

		boolean isEscaped = false;

		RegexBlock newBlock = null;

		while(true)
		{
			if( currentOffset == bufferSize )
				break;

			char currentChar = buffer.charAt(currentOffset);

			if( !isEscaped && closingChar == currentChar )
			{
				newBlock = new RegexBlock(buffer, startOffset, currentOffset, openingCharOffset, currentOffset);
				break;
			}
			else
				isEscaped = !isEscaped && currentChar == '\\';

			currentOffset++;
		}

		return newBlock;

	}

	/**
	 * Choosing closing character by opening one
	 * @param charOpener - char with which sequence started
	 * @return - ending char
	 */
	public static char getQuoteCloseChar(char charOpener)
	{
		if( charOpener == '<' )
			return '>';
		else if( charOpener == '{' )
			return '}';
		else if( charOpener == '(' )
			return ')';
		else if( charOpener == '[' )
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

	protected int startOffset;
	protected int endOffset;
	protected CharSequence buffer;
	protected Integer openingCharOffset;
	protected Integer closingCharOffset;

	protected RegexBlock(CharSequence buffer, int startOffset, int endOffset, Integer openingCharOffset, Integer closingCharOffset)
	{
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.buffer = buffer;
		this.openingCharOffset = openingCharOffset;
		this.closingCharOffset = closingCharOffset;
	}

	public Character getOpeningQuote()
	{
		return openingCharOffset == null
				? null
				: buffer.charAt(openingCharOffset);
	}

	public Character getClosingQuote()
	{
		return closingCharOffset == null
				? null
				: buffer.charAt(closingCharOffset);
	}

	public boolean hasSameQuotes()
	{
		return
			openingCharOffset == null	// no opening char
			|| getOpeningQuote().equals(getClosingQuote()); // open and close are equal
	}


	/**
	 * Tokenizing entry point
	 * @param isExtended	flag that regular expression is extended with spaces and comments
	 * @return				ArrayList of tokens
	 */
	public Collection<CustomToken> tokenize(boolean isExtended)
	{
		return isExtended
				? tokenizeExtended()
				: tokenizeRegular();
	}

	/**
	 * Tokenizing regexp object with nested comments and spaces
	 * @return array of CustomTokens
	 */
	protected Collection<CustomToken> tokenizeExtended()
	{
		return tokenizeRegular();
	}

	/**
	 * Tokenizing regexp object without nested comments and spaces
	 * @return array of CustomTokens
	 */
	protected Collection<CustomToken> tokenizeRegular()
	{
		ArrayList<CustomToken> tokens = new ArrayList<CustomToken>();

		int currentOffset = startOffset;
		if( openingCharOffset != null ) // got opening quote
		{
			tokens.add(new CustomToken(currentOffset, currentOffset + 1, PERL_REGEX_QUOTE));
			currentOffset++;
		}

		while( currentOffset < endOffset )
		{
			tokens.add(new CustomToken(currentOffset, currentOffset + 1, PERL_REGEX_TOKEN));
			currentOffset++;
		}

		tokens.add(new CustomToken(currentOffset, currentOffset + 1, PERL_REGEX_QUOTE));

		return tokens;
	}

}
