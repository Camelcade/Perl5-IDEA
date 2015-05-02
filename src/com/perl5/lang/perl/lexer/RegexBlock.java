package com.perl5.lang.perl.lexer;

/**
 * Created by hurricup on 02.05.2015.
 */
public class RegexBlock
{
	/**
	 * Parses character stream for one regex block
	 * @param buffer	character stream
	 * @param startOffset	parsing start offset
	 * @param bufferSize	buffer size
	 * @return	RegexBlock object
	 */
	public static RegexBlock getBlock(char[] buffer, int startOffset, int bufferSize)
	{
		char openingChar = buffer[startOffset];
		char closingChar = getQuoteCloseChar(openingChar);

		int currentOffset = startOffset+1;
		boolean isEscaped = false;

		RegexBlock newBlock = null;

		while(true)
		{
			char currentChar = buffer[currentOffset];

			if( currentOffset == bufferSize )
				break;
			else if( !isEscaped && closingChar == currentChar )
			{
				newBlock = new RegexBlock(buffer, startOffset, currentOffset);
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

	protected int startOffset;
	protected int endOffset;
	protected char[] buffer;

	protected RegexBlock(char[] buffer, int startOffset, int endOffset)
	{
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.buffer = buffer;
	}
}
