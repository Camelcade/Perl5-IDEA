package com.perl5.lang.perl.parser;

import com.intellij.psi.tree.IElementType;

/**
 * Created by hurricup on 07.05.2015.
 * represents token data - type and text
 */
public class PerlTokenData
{
	protected IElementType tokenType;
	protected String tokenText;

	public PerlTokenData(IElementType type, String text)
	{
		tokenType = type;
		tokenText = text;
	}

	public IElementType getTokenType()
	{
		return tokenType;
	}

	public String getTokenText()
	{
		return tokenText;
	}

}
