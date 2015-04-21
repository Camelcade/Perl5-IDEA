package com.perl5.lang.perl.lexer.elements;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.perl5.lang.perl.highlighter.PerlSyntaxHighlighter;
import com.perl5.utils.SelfStyled;

/**
 * Created by hurricup on 19.04.2015.
 * Represents core functions
 */
public class PerlFunctionBuiltIn extends PerlFunction implements SelfStyled
{
	private static final TextAttributesKey[] attributesKeys = new TextAttributesKey[]{PerlSyntaxHighlighter.PERL_FUNCTION};
	@Override
	public TextAttributesKey[] getTextAttributesKey()
	{
		return attributesKeys;
	}

	public PerlFunctionBuiltIn() {
		super("PERL_FUNCTION");
	}


}
