package com.perl5.lang.pod.lexer.elements;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.tree.ILazyParseableElementType;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.pod.highlighter.PodSyntaxHighlighter;
import com.perl5.utils.SelfStyled;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PodCode extends ILazyParseableElementType implements SelfStyled
{
	private static final TextAttributesKey[] attributesKeys = new TextAttributesKey[]{PodSyntaxHighlighter.POD_CODE};
	@Override
	public TextAttributesKey[] getTextAttributesKey()
	{
		return attributesKeys;
	}

	public PodCode() {
		super("POD_CODE", PerlLanguage.INSTANCE);
	}
}
