package com.perl5.highlighter;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.lexer.PerlElementType;
import com.perl5.lang.lexer.PerlLexer;
import com.perl5.lang.parser.PerlElementTypes;
import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;

import java.awt.*;
import java.io.Reader;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class PerlSyntaxHighlighter extends SyntaxHighlighterBase {
	public static final TextAttributesKey SEPARATOR = createTextAttributesKey("SIMPLE_SEPARATOR", SyntaxHighlighterColors.OPERATION_SIGN);
	public static final TextAttributesKey KEY = createTextAttributesKey("SIMPLE_KEY", SyntaxHighlighterColors.KEYWORD);
	public static final TextAttributesKey VALUE = createTextAttributesKey("SIMPLE_VALUE", SyntaxHighlighterColors.STRING);
	public static final TextAttributesKey COMMENT = createTextAttributesKey("SIMPLE_COMMENT", SyntaxHighlighterColors.LINE_COMMENT);
	public static final TextAttributesKey PERL_FUNCTION = createTextAttributesKey("PERL_FUNCTION", SyntaxHighlighterColors. ); //HighlightInfoType.METHOD_CALL.getAttributesKey()

	static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("SIMPLE_BAD_CHARACTER",
			new TextAttributes(Color.RED, null, null, null, Font.BOLD));

	private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
	private static final TextAttributesKey[] SEPARATOR_KEYS = new TextAttributesKey[]{SEPARATOR};
	private static final TextAttributesKey[] KEY_KEYS = new TextAttributesKey[]{KEY};
	private static final TextAttributesKey[] VALUE_KEYS = new TextAttributesKey[]{VALUE};
	private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
	private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
	private static final TextAttributesKey[] PERL_FUNCTIONS = new TextAttributesKey[]{PERL_FUNCTION};

	@NotNull
	@Override
	public Lexer getHighlightingLexer() {
		return new FlexAdapter(new PerlLexer((Reader) null));
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
		if (tokenType.equals(TokenType.BAD_CHARACTER))
		{
			return BAD_CHAR_KEYS;
//		} else if (tokenType.equals(PerlElementTypes.SEPARATOR)) {
//			return SEPARATOR_KEYS;
//		} else if (tokenType.equals(PerlElementTypes.KEY)) {
//			return KEY_KEYS;
//		} else if (tokenType.equals(PerlElementTypes.VALUE)) {
//			return VALUE_KEYS;
		} else if (tokenType.equals(PerlElementTypes.PERL_COMMENT)) {
			return COMMENT_KEYS;
		} else if (tokenType.equals(PerlElementTypes.PERL_FUNCTION)) {
			return PERL_FUNCTIONS;
		} else if (tokenType.equals(PerlElementTypes.PERL_SYNTAX)) {
			return PERL_FUNCTIONS;
		} else {
			return EMPTY_KEYS;
		}
	}
}
