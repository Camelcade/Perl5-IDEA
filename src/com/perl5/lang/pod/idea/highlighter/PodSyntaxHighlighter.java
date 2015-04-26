package com.perl5.lang.pod.idea.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.PerlTokenType;
import com.perl5.lang.pod.lexer.PodElementTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PodSyntaxHighlighter  extends SyntaxHighlighterBase
{
	public static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

	public static final TextAttributesKey POD_TAG = createTextAttributesKey("POD_TAG", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG);
	public static final TextAttributesKey POD_TEXT = createTextAttributesKey("POD_TEXT", DefaultLanguageHighlighterColors.DOC_COMMENT);
	public static final TextAttributesKey POD_CODE = createTextAttributesKey("POD_CODE", DefaultLanguageHighlighterColors.KEYWORD);

	public static final HashMap<IElementType,TextAttributesKey[]> attributesMap = new HashMap<IElementType,TextAttributesKey[]>();

	private static final PerlSyntaxHighlighter perlHilighter = new PerlSyntaxHighlighter();

	static{
		attributesMap.put(PodElementTypes.POD_TAG, new TextAttributesKey[]{PodSyntaxHighlighter.POD_TAG});
		attributesMap.put(PodElementTypes.POD_TEXT, new TextAttributesKey[]{PodSyntaxHighlighter.POD_TEXT});
		attributesMap.put(PodElementTypes.POD_NEWLINE, new TextAttributesKey[]{PodSyntaxHighlighter.POD_TEXT});
		attributesMap.put(PodElementTypes.POD_CODE, new TextAttributesKey[]{PodSyntaxHighlighter.POD_CODE});
	}

	@NotNull
	@Override
	public Lexer getHighlightingLexer() {
		return new PodHighlightingLexer();
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {

		TextAttributesKey[] tokenAttributes;

		if( tokenType instanceof PerlTokenType)
		{
			tokenAttributes = perlHilighter.getTokenHighlights(tokenType);
		}
		else
		{
			tokenAttributes = attributesMap.get(tokenType);
		}

		return tokenAttributes == null
				? EMPTY_KEYS
				: tokenAttributes;
	}
}
