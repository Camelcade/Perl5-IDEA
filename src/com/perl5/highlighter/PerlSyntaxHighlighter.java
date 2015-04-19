package com.perl5.highlighter;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.lexer.PerlLexer;
import com.perl5.lang.lexer.elements.PerlElement;
import com.perl5.lang.parser.PerlElementTypes;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class PerlSyntaxHighlighter extends SyntaxHighlighterBase {

	public static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

	public static final TextAttributesKey PERL_COMMENT = createTextAttributesKey("PERL_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
	public static final TextAttributesKey PERL_COMMENT_MULTILINE = createTextAttributesKey("PERL_COMMENT_MULTILINE", DefaultLanguageHighlighterColors.BLOCK_COMMENT);

	public static final TextAttributesKey PERL_INSTANCE_METHOD_CALL = createTextAttributesKey("PERL_INSTANCE_METHOD_CALL", DefaultLanguageHighlighterColors.INSTANCE_METHOD);
	public static final TextAttributesKey PERL_STATIC_METHOD_CALL = createTextAttributesKey("PERL_STATIC_METHOD_CALL", DefaultLanguageHighlighterColors.STATIC_METHOD);
	public static final TextAttributesKey PERL_FUNCTION = createTextAttributesKey("PERL_FUNCTION", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey PERL_SYNTAX = createTextAttributesKey("PERL_SYNTAX", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey PERL_SQ_STRING = createTextAttributesKey("PERL_SQ_STRING", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey PERL_DQ_STRING = createTextAttributesKey("PERL_DQ_STRING", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey PERL_NUMBER = createTextAttributesKey("PERL_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
	public static final TextAttributesKey PERL_COMMA = createTextAttributesKey("PERL_COMMA", DefaultLanguageHighlighterColors.COMMA);
	public static final TextAttributesKey PERL_SEMICOLON = createTextAttributesKey("PERL_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
	public static final TextAttributesKey PERL_BRACE = createTextAttributesKey("PERL_BRACES", DefaultLanguageHighlighterColors.BRACES);
	public static final TextAttributesKey PERL_PAREN = createTextAttributesKey("PERL_PARENTESS", DefaultLanguageHighlighterColors.PARENTHESES);
	public static final TextAttributesKey PERL_BRACK = createTextAttributesKey("PERL_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
	public static final TextAttributesKey PERL_PACKAGE = createTextAttributesKey("PERL_PACKAGE", DefaultLanguageHighlighterColors.CLASS_NAME);
	public static final TextAttributesKey PERL_SCALAR = createTextAttributesKey("PERL_SCALAR", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
	public static final TextAttributesKey PERL_ARRAY = createTextAttributesKey("PERL_ARRAY", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
	public static final TextAttributesKey PERL_HASH = createTextAttributesKey("PERL_HASH", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
	public static final TextAttributesKey PERL_GLOB = createTextAttributesKey("PERL_GLOB", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
	public static final TextAttributesKey PERL_DEREFERENCE = createTextAttributesKey("PERL_DEREFERENCE", DefaultLanguageHighlighterColors.CLASS_REFERENCE);

	private static final TextAttributesKey[] STATIC_METHOD_CALL_KEYS = new TextAttributesKey[]{PERL_STATIC_METHOD_CALL};
	private static final TextAttributesKey[] INSTANCE_METHOD_CALL_KEYS = new TextAttributesKey[]{PERL_INSTANCE_METHOD_CALL};
	private static final TextAttributesKey[] FUNCTION_KEYS = new TextAttributesKey[]{PERL_FUNCTION};
	private static final TextAttributesKey[] SYNTAX_KEYS = new TextAttributesKey[]{PERL_SYNTAX};
	private static final TextAttributesKey[] SQ_STRING_KEYS = new TextAttributesKey[]{PERL_SQ_STRING};
	private static final TextAttributesKey[] DQ_STRING_KEYS = new TextAttributesKey[]{PERL_DQ_STRING};
	private static final TextAttributesKey[] COMMA_KEYS = new TextAttributesKey[]{PERL_COMMA};
	private static final TextAttributesKey[] SEMICOLON_KEYS = new TextAttributesKey[]{PERL_SEMICOLON};
	private static final TextAttributesKey[] BRACE_KEYS = new TextAttributesKey[]{PERL_BRACE};
	private static final TextAttributesKey[] PAREN_KEYS = new TextAttributesKey[]{PERL_PAREN};
	private static final TextAttributesKey[] BRACK_KEYS = new TextAttributesKey[]{PERL_BRACK};
	private static final TextAttributesKey[] PACKAGE_KEYS = new TextAttributesKey[]{PERL_PACKAGE};
	private static final TextAttributesKey[] PERL_GLOB_KEYS = new TextAttributesKey[]{PERL_GLOB};
	private static final TextAttributesKey[] PERL_DEREFERENCE_KEYS = new TextAttributesKey[]{PERL_DEREFERENCE};

	@NotNull
	@Override
	public Lexer getHighlightingLexer() {
		return new FlexAdapter(new PerlLexer((Reader) null));
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
		if( tokenType instanceof SelfStyled )
		{
			return ((SelfStyled)tokenType).getTextAttributesKey();
		} else if (tokenType.equals(PerlElementTypes.PERL_PACKAGE)) {
			return PACKAGE_KEYS;
		} else if (tokenType.equals(PerlElementTypes.PERL_FUNCTION)) {
			return FUNCTION_KEYS;
		} else if (tokenType.equals(PerlElementTypes.PERL_STATIC_METHOD_CALL)) {
			return STATIC_METHOD_CALL_KEYS;
		} else if (tokenType.equals(PerlElementTypes.PERL_INSTANCE_METHOD_CALL)) {
			return INSTANCE_METHOD_CALL_KEYS;
		} else if (tokenType.equals(PerlElementTypes.PERL_SYNTAX)) {
			return SYNTAX_KEYS;
		} else if (tokenType.equals(PerlElementTypes.PERL_VARIABLE_GLOB)) {
			return PERL_GLOB_KEYS;
		} else if (
				tokenType.equals(PerlElementTypes.PERL_DEREFERENCE)
						|| tokenType.equals(PerlElementTypes.PERL_DEPACKAGE)
				) {
			return PERL_DEREFERENCE_KEYS;
		} else if (tokenType.equals(PerlElementTypes.PERL_SQ_STRING)) {
			return SQ_STRING_KEYS;
		} else if (tokenType.equals(PerlElementTypes.PERL_DQ_STRING)) {
			return DQ_STRING_KEYS;
		} else if (tokenType.equals(PerlElementTypes.PERL_COMMA)) {
			return COMMA_KEYS;
		} else if (tokenType.equals(PerlElementTypes.PERL_SEMI)) {
			return SEMICOLON_KEYS;
		} else if (tokenType.equals(PerlElementTypes.PERL_LBRACE) || tokenType.equals(PerlElementTypes.PERL_RBRACE)) {
			return BRACE_KEYS;
		} else if (tokenType.equals(PerlElementTypes.PERL_LBRACK) || tokenType.equals(PerlElementTypes.PERL_RBRACK)) {
			return BRACK_KEYS;
		} else if (tokenType.equals(PerlElementTypes.PERL_LPAREN) || tokenType.equals(PerlElementTypes.PERL_RPAREN))
		{
			return PAREN_KEYS;
		}
		else
		{
			return EMPTY_KEYS;
		}
	}
}
