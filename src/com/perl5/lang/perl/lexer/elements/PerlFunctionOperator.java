package com.perl5.lang.perl.lexer.elements;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.perl5.lang.perl.highlighter.PerlSyntaxHighlighter;
import com.perl5.utils.SelfStyled;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlFunctionOperator extends PerlFunction implements SelfStyled
{
	private static final TextAttributesKey[] attributesKeys = new TextAttributesKey[]{PerlSyntaxHighlighter.PERL_OPERATOR};
	@Override
	public TextAttributesKey[] getTextAttributesKey()
	{
		return attributesKeys;
	}

	public PerlFunctionOperator() {
		super("PERL_OPERATOR");
	}
}
