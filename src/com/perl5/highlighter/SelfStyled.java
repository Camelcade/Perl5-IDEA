package com.perl5.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;

/**
 * Created by hurricup on 19.04.2015.
 * Implement this interface in an Element to provide fast coloring
 */
public interface SelfStyled
{
	public TextAttributesKey[] getTextAttributesKey();
}


