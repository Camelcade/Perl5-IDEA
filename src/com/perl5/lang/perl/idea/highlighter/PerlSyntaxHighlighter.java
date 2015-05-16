package com.perl5.lang.perl.idea.highlighter;

import com.intellij.ide.highlighter.HtmlFileHighlighter;
import com.intellij.ide.highlighter.XmlFileHighlighter;
import com.intellij.lang.Language;
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.lang.xhtml.XHTMLLanguage;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.lexer.*;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.PerlTokenType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.idea.highlighter.PodSyntaxHighlighter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class PerlSyntaxHighlighter extends SyntaxHighlighterBase{

	public static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

	public static final TextAttributesKey PERL_BUILT_IN = createTextAttributesKey("PERL_BUILT_IN", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey PERL_DEPRECATED = createTextAttributesKey("PERL_DEPRECATED", DefaultLanguageHighlighterColors.KEYWORD);

	public static final TextAttributesKey PERL_VERSION = createTextAttributesKey("PERL_VERSION", DefaultLanguageHighlighterColors.NUMBER);

	public static final TextAttributesKey PERL_COMMENT = createTextAttributesKey("PERL_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
	public static final TextAttributesKey PERL_COMMENT_BLOCK = createTextAttributesKey("PERL_COMMENT_BLOCK", DefaultLanguageHighlighterColors.BLOCK_COMMENT);

	public static final TextAttributesKey PERL_MULTILINE_MARKER = createTextAttributesKey("PERL_MULTILINE_MARKER", DefaultLanguageHighlighterColors.BLOCK_COMMENT);

	public static final TextAttributesKey PERL_POD = createTextAttributesKey("PERL_POD", DefaultLanguageHighlighterColors.DOC_COMMENT);

	public static final TextAttributesKey PERL_INSTANCE_METHOD_CALL = createTextAttributesKey("PERL_INSTANCE_METHOD_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL);
	public static final TextAttributesKey PERL_STATIC_METHOD_CALL = createTextAttributesKey("PERL_STATIC_METHOD_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL);

	public static final TextAttributesKey PERL_PACKAGE = createTextAttributesKey("PERL_PACKAGE", DefaultLanguageHighlighterColors.CLASS_NAME);
	public static final TextAttributesKey PERL_FUNCTION = createTextAttributesKey("PERL_FUNCTION", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey PERL_OPERATOR = createTextAttributesKey("PERL_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
	public static final TextAttributesKey PERL_PACKAGE_PRAGMA = createTextAttributesKey("PERL_PACKAGE_PRAGMA", DefaultLanguageHighlighterColors.CLASS_NAME);

	public static final TextAttributesKey PERL_REGEX_QUOTE = createTextAttributesKey("PERL_REGEX_QUOTE", DefaultLanguageHighlighterColors.BRACKETS);
	public static final TextAttributesKey PERL_REGEX_TOKEN = createTextAttributesKey("PERL_REGEX_TOKEN", DefaultLanguageHighlighterColors.STRING);

	public static final TextAttributesKey PERL_SQ_STRING = createTextAttributesKey("PERL_SQ_STRING", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey PERL_DQ_STRING = createTextAttributesKey("PERL_DQ_STRING", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey PERL_DX_STRING = createTextAttributesKey("PERL_DX_STRING", DefaultLanguageHighlighterColors.STRING);

	public static final TextAttributesKey PERL_NUMBER = createTextAttributesKey("PERL_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
	public static final TextAttributesKey PERL_COMMA = createTextAttributesKey("PERL_COMMA", DefaultLanguageHighlighterColors.COMMA);
	public static final TextAttributesKey PERL_SEMICOLON = createTextAttributesKey("PERL_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
	public static final TextAttributesKey PERL_BRACE = createTextAttributesKey("PERL_BRACES", DefaultLanguageHighlighterColors.BRACES);
	public static final TextAttributesKey PERL_PAREN = createTextAttributesKey("PERL_PARENTESS", DefaultLanguageHighlighterColors.PARENTHESES);
	public static final TextAttributesKey PERL_BRACK = createTextAttributesKey("PERL_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);

	public static final TextAttributesKey PERL_SCALAR = createTextAttributesKey("PERL_SCALAR", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
	public static final TextAttributesKey PERL_ARRAY = createTextAttributesKey("PERL_ARRAY", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
	public static final TextAttributesKey PERL_HASH = createTextAttributesKey("PERL_HASH", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
	public static final TextAttributesKey PERL_GLOB = createTextAttributesKey("PERL_GLOB", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);

	public static final TextAttributesKey PERL_DEREFERENCE = createTextAttributesKey("PERL_DEREFERENCE", DefaultLanguageHighlighterColors.DOT);

	private static final HashMap<IElementType, TextAttributesKey[]> attributesMap = new HashMap<IElementType, TextAttributesKey[]>();

	static{
		attributesMap.put(PerlElementTypes.PERL_COMMENT, new TextAttributesKey[]{PERL_COMMENT});
		attributesMap.put(PerlElementTypes.PERL_COMMENT_BLOCK, new TextAttributesKey[]{PERL_COMMENT});

//		attributesMap.put(PerlElementTypes.PERL_STRING, new TextAttributesKey[]{PERL_SQ_STRING});
//		attributesMap.put(PerlElementTypes.PERL_DQ_STRING, new TextAttributesKey[]{PERL_DQ_STRING});
//		attributesMap.put(PerlElementTypes.PERL_DX_STRING, new TextAttributesKey[]{PERL_DX_STRING});

//		attributesMap.put(PerlElementTypes.PERL_STRING_MULTILINE, new TextAttributesKey[]{PERL_SQ_STRING});
//		attributesMap.put(PerlElementTypes.PERL_MULTILINE_DQ, new TextAttributesKey[]{PERL_DQ_STRING});
//		attributesMap.put(PerlElementTypes.PERL_MULTILINE_DX, new TextAttributesKey[]{PERL_DX_STRING});

//		attributesMap.put(PerlElementTypes.PERL_NUMBER, new TextAttributesKey[]{PERL_NUMBER});
//		attributesMap.put(PerlElementTypes.PERL_VERSION, new TextAttributesKey[]{PERL_VERSION});

//		attributesMap.put(PerlElementTypes.PERL_MULTILINE_MARKER, new TextAttributesKey[]{PERL_MULTILINE_MARKER});
//		attributesMap.put(PerlElementTypes.PERL_MULTILINE_MARKER_HTML, new TextAttributesKey[]{PERL_MULTILINE_MARKER});
//		attributesMap.put(PerlElementTypes.PERL_MULTILINE_MARKER_XML, new TextAttributesKey[]{PERL_MULTILINE_MARKER});
//		attributesMap.put(PerlElementTypes.PERL_MULTILINE_MARKER_XHTML, new TextAttributesKey[]{PERL_MULTILINE_MARKER});

//		attributesMap.put(PerlElementTypes.PERL_COMMA, new TextAttributesKey[]{PERL_COMMA});

		attributesMap.put(PerlElementTypes.PERL_REGEX_QUOTE, new TextAttributesKey[]{PERL_REGEX_QUOTE});
		attributesMap.put(PerlElementTypes.PERL_REGEX_TOKEN, new TextAttributesKey[]{PERL_REGEX_TOKEN});
		attributesMap.put(PerlElementTypes.PERL_REGEX_MODIFIER, new TextAttributesKey[]{PERL_OPERATOR, PERL_BUILT_IN});

		attributesMap.put(PerlElementTypes.PERL_SEMI, new TextAttributesKey[]{PERL_SEMICOLON});
		attributesMap.put(PerlElementTypes.PERL_LBRACE, new TextAttributesKey[]{PERL_BRACE});
		attributesMap.put(PerlElementTypes.PERL_RBRACE, new TextAttributesKey[]{PERL_BRACE});
		attributesMap.put(PerlElementTypes.PERL_LBRACK, new TextAttributesKey[]{PERL_BRACK});
		attributesMap.put(PerlElementTypes.PERL_RBRACK, new TextAttributesKey[]{PERL_BRACK});
		attributesMap.put(PerlElementTypes.PERL_LPAREN, new TextAttributesKey[]{PERL_PAREN});
		attributesMap.put(PerlElementTypes.PERL_RPAREN, new TextAttributesKey[]{PERL_PAREN});

		attributesMap.put(PerlElementTypes.PERL_OPERATOR, new TextAttributesKey[]{PERL_OPERATOR, PERL_BUILT_IN});
		attributesMap.put(PerlElementTypes.PERL_OPERATOR_UNARY, new TextAttributesKey[]{PERL_OPERATOR, PERL_BUILT_IN});
		attributesMap.put(PerlElementTypes.PERL_OPERATOR_FILETEST, new TextAttributesKey[]{PERL_OPERATOR, PERL_BUILT_IN});

		attributesMap.put(PerlElementTypes.PERL_KEYWORD, new TextAttributesKey[]{PERL_OPERATOR, PERL_BUILT_IN});
		attributesMap.put(PerlElementTypes.PERL_TAG, new TextAttributesKey[]{PERL_OPERATOR, PERL_BUILT_IN});

//		attributesMap.put(PerlElementTypes.PERL_PACKAGE, new TextAttributesKey[]{PERL_PACKAGE});
//		attributesMap.put(PerlElementTypes.PERL_PACKAGE_BUILT_IN, new TextAttributesKey[]{PERL_PACKAGE, PERL_BUILT_IN});
//		attributesMap.put(PerlElementTypes.PERL_PACKAGE_BUILT_IN_PRAGMA, new TextAttributesKey[]{PERL_PACKAGE_PRAGMA, PERL_BUILT_IN});
//		attributesMap.put(PerlElementTypes.PERL_PACKAGE_BUILT_IN_DEPRECATED, new TextAttributesKey[]{PERL_PACKAGE, PERL_BUILT_IN, PERL_DEPRECATED});

//		attributesMap.put(PerlElementTypes.PERL_FUNCTION, new TextAttributesKey[]{PERL_FUNCTION});
//		attributesMap.put(PerlElementTypes.PERL_FUNCTION_BUILT_IN, new TextAttributesKey[]{PERL_FUNCTION_BUILT_IN});
//		attributesMap.put(PerlElementTypes.PERL_FUNCTION_BUILT_IN_IMPLEMENTED, new TextAttributesKey[]{PERL_FUNCTION_BUILT_IN});

		attributesMap.put(PerlElementTypes.PERL_SCALAR, new TextAttributesKey[]{PERL_SCALAR});
		attributesMap.put(PerlElementTypes.PERL_ARRAY, new TextAttributesKey[]{PERL_ARRAY});
		attributesMap.put(PerlElementTypes.PERL_HASH, new TextAttributesKey[]{PERL_HASH});
		attributesMap.put(PerlElementTypes.PERL_GLOB, new TextAttributesKey[]{PERL_GLOB});

		attributesMap.put(PerlElementTypes.PERL_SCALAR_BUILT_IN, new TextAttributesKey[]{PERL_SCALAR, PERL_BUILT_IN});
		attributesMap.put(PerlElementTypes.PERL_ARRAY_BUILT_IN, new TextAttributesKey[]{PERL_ARRAY, PERL_BUILT_IN});
		attributesMap.put(PerlElementTypes.PERL_HASH_BUILT_IN, new TextAttributesKey[]{PERL_HASH, PERL_BUILT_IN});
		attributesMap.put(PerlElementTypes.PERL_GLOB_BUILT_IN, new TextAttributesKey[]{PERL_GLOB, PERL_BUILT_IN});

        attributesMap.put(PerlElementTypes.PERL_SCALAR_INDEX, new TextAttributesKey[]{PERL_SCALAR});
        attributesMap.put(PerlElementTypes.PERL_SIGIL_SCALAR, new TextAttributesKey[]{PERL_SCALAR});
        attributesMap.put(PerlElementTypes.PERL_SIGIL_SCALAR_INDEX, new TextAttributesKey[]{PERL_SCALAR});
		attributesMap.put(PerlElementTypes.PERL_SIGIL_ARRAY, new TextAttributesKey[]{PERL_ARRAY});
		attributesMap.put(PerlElementTypes.PERL_SIGIL_HASH, new TextAttributesKey[]{PERL_HASH});

//		attributesMap.put(PerlElementTypes.PERL_DEREFERENCE, new TextAttributesKey[]{PERL_DEREFERENCE});
	}

	private static final HashMap<Language, SyntaxHighlighterBase> highlightersMap = new HashMap<Language, SyntaxHighlighterBase>();

	static{
		highlightersMap.put(PodLanguage.INSTANCE, new PodSyntaxHighlighter());
		highlightersMap.put(HTMLLanguage.INSTANCE, new HtmlFileHighlighter());
		highlightersMap.put(XHTMLLanguage.INSTANCE, new HtmlFileHighlighter());
		highlightersMap.put(XMLLanguage.INSTANCE, new XmlFileHighlighter());
	}

	@NotNull
	@Override
	public Lexer getHighlightingLexer() {
		return new PerlHighlightningLexer();
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {

		TextAttributesKey[] tokenAttributes = null;

		if( !( tokenType instanceof PerlTokenType))
		{
			SyntaxHighlighterBase subHighlighter = highlightersMap.get(tokenType.getLanguage());

			if( subHighlighter!= null )
				tokenAttributes = subHighlighter.getTokenHighlights(tokenType);
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
