package com.perl5.lang.pod.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.lexer.PodLexerAdapter;
import com.perl5.utils.SelfStyled;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PodSyntaxHighlighter  extends SyntaxHighlighterBase
{
	public static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

	public static final TextAttributesKey POD_MARKER = createTextAttributesKey("POD_MARKER", DefaultLanguageHighlighterColors.LINE_COMMENT);
	public static final TextAttributesKey POD_TAG = createTextAttributesKey("POD_TAG", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG);
	public static final TextAttributesKey POD_CODE = createTextAttributesKey("POD_CODE", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey POD_TEXT = createTextAttributesKey("POD_TEXT", DefaultLanguageHighlighterColors.DOC_COMMENT);

	@NotNull
	@Override
	public Lexer getHighlightingLexer() {
		return new PodLexerAdapter();
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
		if( tokenType instanceof SelfStyled)
		{
			return ((SelfStyled)tokenType).getTextAttributesKey();
		}
		return EMPTY_KEYS;
	}

}
