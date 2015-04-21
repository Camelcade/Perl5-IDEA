package com.perl5.lang.perl.lexer.elements;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.perl5.lang.perl.highlighter.PerlSyntaxHighlighter;
import com.perl5.utils.SelfStyled;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlNumber extends PerlElement implements SelfStyled
{
	private static final TextAttributesKey[] attributesKeys = new TextAttributesKey[]{PerlSyntaxHighlighter.PERL_NUMBER};

	public PerlNumber() {
		super("PERL_NUMBER");
	}

	@Override
	public TextAttributesKey[] getTextAttributesKey()
	{
		return attributesKeys;
	}
}
