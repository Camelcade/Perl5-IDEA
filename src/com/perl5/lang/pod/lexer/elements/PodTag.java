package com.perl5.lang.pod.lexer.elements;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.perl5.lang.pod.highlighter.PodSyntaxHighlighter;
import com.perl5.utils.SelfStyled;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PodTag extends PodElement implements SelfStyled
{
	private static final TextAttributesKey[] attributesKeys = new TextAttributesKey[]{PodSyntaxHighlighter.POD_TAG};
	@Override
	public TextAttributesKey[] getTextAttributesKey()
	{
		return attributesKeys;
	}

	public PodTag() {
		super("POD_TAG");
	}
}
