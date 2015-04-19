package com.perl5.lang.lexer.elements;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.perl5.highlighter.PerlSyntaxHighlighter;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlCommentMultiline extends PerlComment
{
	private static final TextAttributesKey[] attributesKeys = new TextAttributesKey[]{PerlSyntaxHighlighter.PERL_COMMENT_MULTILINE};

	@Override
	public TextAttributesKey[] getTextAttributesKey()
	{
		return attributesKeys;
	}

}
