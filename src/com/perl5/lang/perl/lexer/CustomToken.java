package com.perl5.lang.perl.lexer;

/**
 * Created by hurricup on 02.05.2015.
 */

import com.intellij.psi.tree.IElementType;

/**
 * Class for parsed token type, stores start, end and token type
 */
class CustomToken{
	private int tokenStart;
	private int tokenEnd;
	private IElementType tokenType;

	/**
	 * Creates parsed token entry
	 * @param start	token start offset
	 * @param end token end offset
	 * @param type token type
	 */
	public CustomToken( int start, int end, IElementType type )
	{
		tokenStart = start;
		tokenEnd = end;
		tokenType = type;
	}

	public int getTokenStart()
	{
		return tokenStart;
	}

	public int getTokenEnd()
	{
		return tokenEnd;
	}

	public IElementType getTokenType()
	{
		return tokenType;
	}
}
