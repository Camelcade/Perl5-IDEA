package com.perl5.lang.embedded.idea;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.embedded.EmbeddedPerlLexerAdapter;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.pod.idea.highlighter.PodHighlightingLexer;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Created by hurricup on 19.05.2015.
 */
public class EmbeddedPerlSyntaxHighlighter extends PerlSyntaxHighlighter implements PerlElementTypes
{
	public static final TextAttributesKey EMBED_MARKER = createTextAttributesKey("PERL_EMBED_MARKER", DefaultLanguageHighlighterColors.METADATA);

	TextAttributesKey[]	EMBED_MARKER_KEYS = new TextAttributesKey[]{EMBED_MARKER,PERL_BUILT_IN};

	@NotNull
	@Override
	public Lexer getHighlightingLexer()
	{
		return new EmbeddedPerlLexerAdapter();
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType)
	{
		if( tokenType == PerlElementTypes.EMBED_MARKER )
			return EMBED_MARKER_KEYS;
		else
			return super.getTokenHighlights(tokenType);
	}
}
