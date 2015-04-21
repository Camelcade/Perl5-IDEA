package com.perl5.lang.perl.lexer.elements;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.perl5.lang.perl.highlighter.PerlSyntaxHighlighter;
import com.perl5.utils.SelfStyled;

/**
 * Created by hurricup on 20.04.2015.
 * This class represents POD documentation
 */
public class PerlPod extends PerlElement implements SelfStyled
{
	private static final TextAttributesKey[] attributesKeys = new TextAttributesKey[]{PerlSyntaxHighlighter.PERL_POD};
	@Override
	public TextAttributesKey[] getTextAttributesKey()
	{
		return attributesKeys;
	}

	public PerlPod() {
		super("PERL_POD");
	}
}
