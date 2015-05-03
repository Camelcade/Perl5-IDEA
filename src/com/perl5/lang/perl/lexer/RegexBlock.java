package com.perl5.lang.perl.lexer;

import com.google.common.primitives.Chars;

import java.util.*;

/**
 * Created by hurricup on 02.05.2015.
 */
public class RegexBlock implements PerlElementTypes
{
	public static final HashMap<String, List<Character>> allowedModifiers = new HashMap<String, List<Character>>();

	static
	{
		allowedModifiers.put("s", Chars.asList("msixpodualgcer".toCharArray()));
		allowedModifiers.put("m", Chars.asList("msixpodualgc".toCharArray()));
		allowedModifiers.put("qr", Chars.asList("msixpodual".toCharArray()));
	}

	/**
	 * Parses character stream for one regex block
	 * @param buffer	character stream
	 * @param startOffset	parsing start offset
	 * @param bufferSize	buffer size
	 * @return	RegexBlock object
	 */
	public static RegexBlock parseBlock(CharSequence buffer, int startOffset, int bufferSize)
	{
		char openingChar = buffer.charAt(startOffset);
		Integer openingCharOffset = startOffset;
		char closingChar = getQuoteCloseChar(openingChar);

		int currentOffset = startOffset+1;
		boolean isEscaped = false;

		RegexBlock newBlock = null;

		while(true)
		{
			char currentChar = buffer.charAt(currentOffset);

			if( currentOffset == bufferSize )
				break;
			else if( !isEscaped && closingChar == currentChar )
			{
				newBlock = new RegexBlock(buffer, startOffset, currentOffset, openingCharOffset, currentOffset);
				System.out.println(buffer.subSequence(startOffset,currentOffset).toString());
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

	public boolean hasSameQuotes()
	{
		return buffer.charAt(startOffset) == buffer.charAt(endOffset);
	}

	public Collection<CustomToken> tokenizeExtended()
	{
		return tokenize();
	}

	/**
	 * Tokenizing regexp object without nested comments
	 * @return array of CustomTokens
	 */
	public Collection<CustomToken> tokenize()
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
