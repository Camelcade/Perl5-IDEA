/*
 * Copyright 2015 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.idea.highlighter;

import com.intellij.ide.highlighter.HtmlFileHighlighter;
import com.intellij.ide.highlighter.XmlFileHighlighter;
import com.intellij.lang.Language;
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.lang.xhtml.XHTMLLanguage;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.PerlElementType;
import com.perl5.lang.perl.PerlTokenType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.PodTokenType;
import com.perl5.lang.pod.idea.highlighter.PodSyntaxHighlighter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class PerlSyntaxHighlighter extends SyntaxHighlighterBase
{
	public static final TextAttributes BOLD = new TextAttributes(null, null, null, null, Font.BOLD);
	public static final TextAttributes ITALIC = new TextAttributes(null, null, null, null, Font.ITALIC);
	public static final TextAttributes BOLD_ITALIC = TextAttributes.merge(BOLD, ITALIC);
	public static final TextAttributes STROKE = new TextAttributes(null, null, null, EffectType.STRIKEOUT, Font.PLAIN);

	public static final HashSet<IElementType> RESERVED_SET = new HashSet<>(PerlLexer.reservedTokenTypes.values());
	public static final TokenSet OPERATORS_SET = TokenSet.create(
			PerlElementTypes.PERL_OPERATOR,
			PerlElementTypes.PERL_OPERATOR_DIV,
			PerlElementTypes.PERL_OPERATOR_MUL,
			PerlElementTypes.PERL_OPERATOR_AMP,
			PerlElementTypes.PERL_OPERATOR_FILETEST,
			PerlElementTypes.PERL_OPERATOR_NOT,
			PerlElementTypes.PERL_OPERATOR_UNARY,
			PerlElementTypes.PERL_OPERATOR_X
	);

	public static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

	public static final TextAttributesKey PERL_NUMBER = createTextAttributesKey("PERL_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
	public static final TextAttributesKey PERL_VERSION = createTextAttributesKey("PERL_VERSION", PERL_NUMBER);

	public static final TextAttributesKey PERL_COMMENT = createTextAttributesKey("PERL_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
	public static final TextAttributesKey PERL_COMMENT_BLOCK = createTextAttributesKey("PERL_COMMENT_BLOCK", DefaultLanguageHighlighterColors.BLOCK_COMMENT);

	public static final TextAttributesKey PERL_HANDLE = createTextAttributesKey("PERL_HANDLE", DefaultLanguageHighlighterColors.CONSTANT);

	public static final TextAttributesKey PERL_PACKAGE = createTextAttributesKey("PERL_PACKAGE", DefaultLanguageHighlighterColors.CLASS_NAME);
	public static final TextAttributesKey PERL_PACKAGE_DEFINITION = createTextAttributesKey("PERL_PACKAGE_DEFINITION", PERL_PACKAGE);

	public static final TextAttributesKey PERL_SUB = createTextAttributesKey("PERL_SUB", DefaultLanguageHighlighterColors.FUNCTION_CALL);
	public static final TextAttributesKey PERL_SUB_DEFINITION = createTextAttributesKey("PERL_SUB_DEFINITION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
	public static final TextAttributesKey PERL_SUB_DECLARATION = createTextAttributesKey("PERL_SUB_DECLARATION", PERL_SUB_DEFINITION);

	public static final TextAttributesKey PERL_LABEL = createTextAttributesKey("PERL_LABEL", DefaultLanguageHighlighterColors.LABEL);
	public static final TextAttributesKey PERL_BLOCK_NAME = createTextAttributesKey("PERL_BLOCK_NAME", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);
	public static final TextAttributesKey PERL_TAG = createTextAttributesKey("PERL_TAG", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);

	public static final TextAttributesKey PERL_KEYWORD = createTextAttributesKey("PERL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey PERL_OPERATOR = createTextAttributesKey("PERL_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
	public static final TextAttributesKey PERL_DEREFERENCE = createTextAttributesKey("PERL_DEREFERENCE", DefaultLanguageHighlighterColors.OPERATION_SIGN);

	public static final TextAttributesKey PERL_REGEX_QUOTE = createTextAttributesKey("PERL_REGEX_QUOTE", DefaultLanguageHighlighterColors.BRACKETS);
	public static final TextAttributesKey PERL_REGEX_TOKEN = createTextAttributesKey("PERL_REGEX_TOKEN", DefaultLanguageHighlighterColors.STRING);

	public static final TextAttributesKey PERL_ANNOTATION = createTextAttributesKey("PERL_ANNOTATION", DefaultLanguageHighlighterColors.METADATA);
	public static final TextAttributesKey PERL_SUB_ATTRIBUTE = createTextAttributesKey("PERL_SUB_ATTRIBUTE", DefaultLanguageHighlighterColors.METADATA);
	public static final TextAttributesKey PERL_SUB_PROTOTYPE_TOKEN = createTextAttributesKey("PERL_SUB_PROTOTYPE", DefaultLanguageHighlighterColors.PARAMETER);

	public static final TextAttributesKey PERL_SQ_STRING = createTextAttributesKey("PERL_SQ_STRING", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey PERL_DQ_STRING = createTextAttributesKey("PERL_DQ_STRING", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey PERL_DX_STRING = createTextAttributesKey("PERL_DX_STRING", DefaultLanguageHighlighterColors.STRING);

	public static final TextAttributesKey PERL_COMMA = createTextAttributesKey("PERL_COMMA", DefaultLanguageHighlighterColors.COMMA);
	public static final TextAttributesKey PERL_SEMICOLON = createTextAttributesKey("PERL_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON);
	public static final TextAttributesKey PERL_BRACE = createTextAttributesKey("PERL_BRACES", DefaultLanguageHighlighterColors.BRACES);
	public static final TextAttributesKey PERL_PAREN = createTextAttributesKey("PERL_PARENTESS", DefaultLanguageHighlighterColors.PARENTHESES);
	public static final TextAttributesKey PERL_BRACK = createTextAttributesKey("PERL_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS);
	public static final TextAttributesKey PERL_ANGLE = createTextAttributesKey("PERL_ANGLES", DefaultLanguageHighlighterColors.BRACKETS);

	public static final TextAttributesKey PERL_SCALAR = createTextAttributesKey("PERL_SCALAR", DefaultLanguageHighlighterColors.IDENTIFIER);
	public static final TextAttributesKey PERL_ARRAY = createTextAttributesKey("PERL_ARRAY", DefaultLanguageHighlighterColors.IDENTIFIER);
	public static final TextAttributesKey PERL_HASH = createTextAttributesKey("PERL_HASH", DefaultLanguageHighlighterColors.IDENTIFIER);
	public static final TextAttributesKey PERL_GLOB = createTextAttributesKey("PERL_GLOB", DefaultLanguageHighlighterColors.IDENTIFIER);

	private static final HashMap<IElementType, TextAttributesKey[]> attributesMap = new HashMap<>();

	static
	{
		attributesMap.put(PerlElementTypes.ANNOTATION_ABSTRACT_KEY, new TextAttributesKey[]{PERL_ANNOTATION});
		attributesMap.put(PerlElementTypes.ANNOTATION_DEPRECATED_KEY, new TextAttributesKey[]{PERL_ANNOTATION});
		attributesMap.put(PerlElementTypes.ANNOTATION_METHOD_KEY, new TextAttributesKey[]{PERL_ANNOTATION});
		attributesMap.put(PerlElementTypes.ANNOTATION_OVERRIDE_KEY, new TextAttributesKey[]{PERL_ANNOTATION});
		attributesMap.put(PerlElementTypes.ANNOTATION_PREFIX, new TextAttributesKey[]{PERL_ANNOTATION});
		attributesMap.put(PerlElementTypes.ANNOTATION_RETURNS_KEY, new TextAttributesKey[]{PERL_ANNOTATION});
		attributesMap.put(PerlElementTypes.ANNOTATION_UNKNOWN_KEY, new TextAttributesKey[]{PERL_ANNOTATION});

		attributesMap.put(PerlElementTypes.PERL_ARROW_COMMA, new TextAttributesKey[]{PERL_COMMA});

		attributesMap.put(PerlElementTypes.PERL_BLOCK_NAME, new TextAttributesKey[]{PERL_KEYWORD});

		attributesMap.put(PerlElementTypes.PERL_COLON, new TextAttributesKey[]{PERL_OPERATOR});
		attributesMap.put(PerlElementTypes.PERL_COMMA, new TextAttributesKey[]{PERL_COMMA});

		attributesMap.put(PerlElementTypes.PERL_COMMENT, new TextAttributesKey[]{PERL_COMMENT});
		attributesMap.put(PerlElementTypes.PERL_COMMENT_BLOCK, new TextAttributesKey[]{PERL_COMMENT});

		attributesMap.put(PerlElementTypes.PERL_DEREFERENCE, new TextAttributesKey[]{PERL_DEREFERENCE});

		attributesMap.put(PerlElementTypes.PERL_SUB, new TextAttributesKey[]{PERL_SUB});
		attributesMap.put(PerlElementTypes.PERL_SUB_PROTOTYPE_TOKEN, new TextAttributesKey[]{PERL_SUB_PROTOTYPE_TOKEN});
		attributesMap.put(PerlElementTypes.PERL_SUB_ATTRIBUTE, new TextAttributesKey[]{PERL_SUB_ATTRIBUTE});

		attributesMap.put(PerlElementTypes.PERL_HANDLE, new TextAttributesKey[]{PERL_HANDLE});

		attributesMap.put(PerlElementTypes.TEMPLATE_BLOCK_HTML, new TextAttributesKey[]{PERL_COMMENT});
		attributesMap.put(PerlElementTypes.PERL_HEREDOC_END, new TextAttributesKey[]{PERL_SQ_STRING});

		attributesMap.put(PerlElementTypes.PERL_STRING_CONTENT, new TextAttributesKey[]{PERL_SQ_STRING});

		attributesMap.put(PerlElementTypes.PERL_REGEX_QUOTE_CLOSE, new TextAttributesKey[]{PERL_REGEX_QUOTE});
		attributesMap.put(PerlElementTypes.PERL_REGEX_QUOTE_OPEN, new TextAttributesKey[]{PERL_REGEX_QUOTE});
		attributesMap.put(PerlElementTypes.PERL_REGEX_TOKEN, new TextAttributesKey[]{PERL_REGEX_TOKEN});
		attributesMap.put(PerlElementTypes.PERL_REGEX_MODIFIER, new TextAttributesKey[]{PERL_KEYWORD});

		attributesMap.put(PerlElementTypes.PERL_QUOTE, new TextAttributesKey[]{PERL_SQ_STRING});
		attributesMap.put(PerlElementTypes.PERL_SEMI, new TextAttributesKey[]{PERL_SEMICOLON});
		attributesMap.put(PerlElementTypes.PERL_LBRACE, new TextAttributesKey[]{PERL_BRACE});
		attributesMap.put(PerlElementTypes.PERL_RBRACE, new TextAttributesKey[]{PERL_BRACE});
		attributesMap.put(PerlElementTypes.PERL_LBRACK, new TextAttributesKey[]{PERL_BRACK});
		attributesMap.put(PerlElementTypes.PERL_RBRACK, new TextAttributesKey[]{PERL_BRACK});
		attributesMap.put(PerlElementTypes.PERL_LPAREN, new TextAttributesKey[]{PERL_PAREN});
		attributesMap.put(PerlElementTypes.PERL_RPAREN, new TextAttributesKey[]{PERL_PAREN});
		attributesMap.put(PerlElementTypes.PERL_LANGLE, new TextAttributesKey[]{PERL_ANGLE});
		attributesMap.put(PerlElementTypes.PERL_RANGLE, new TextAttributesKey[]{PERL_ANGLE});

		attributesMap.put(PerlElementTypes.PERL_NUMBER, new TextAttributesKey[]{PERL_NUMBER});
		attributesMap.put(PerlElementTypes.PERL_NUMBER_VERSION, new TextAttributesKey[]{PERL_VERSION});

		attributesMap.put(PerlElementTypes.PERL_OPERATOR, new TextAttributesKey[]{PERL_OPERATOR});

		attributesMap.put(PerlElementTypes.PERL_RESERVED, new TextAttributesKey[]{PERL_KEYWORD});

		attributesMap.put(PerlElementTypes.PERL_TAG, new TextAttributesKey[]{PERL_TAG});

		attributesMap.put(PerlElementTypes.PERL_PACKAGE, new TextAttributesKey[]{PERL_PACKAGE});
		attributesMap.put(PerlElementTypes.PERL_HANDLE, new TextAttributesKey[]{PERL_HANDLE});

		attributesMap.put(PerlElementTypes.PERL_SIGIL_SCALAR, new TextAttributesKey[]{PERL_SCALAR});
		attributesMap.put(PerlElementTypes.PERL_SIGIL_SCALAR_INDEX, new TextAttributesKey[]{PERL_SCALAR});
		attributesMap.put(PerlElementTypes.PERL_SIGIL_ARRAY, new TextAttributesKey[]{PERL_ARRAY});
		attributesMap.put(PerlElementTypes.PERL_SIGIL_HASH, new TextAttributesKey[]{PERL_HASH});
		attributesMap.put(PerlElementTypes.PERL_VARIABLE_NAME, new TextAttributesKey[]{DefaultLanguageHighlighterColors.IDENTIFIER});
	}


	private static final SyntaxHighlighter POD_SYNTAX_HIGHLIGHTER = new PodSyntaxHighlighter();

	@NotNull
	@Override
	public Lexer getHighlightingLexer()
	{
		return new PerlHighlightningLexer();

	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType)
	{

		TextAttributesKey[] attributesKeys;

		if( tokenType instanceof PodTokenType)
			attributesKeys = POD_SYNTAX_HIGHLIGHTER.getTokenHighlights(tokenType);
		else if(RESERVED_SET.contains(tokenType))
			attributesKeys = attributesMap.get(PerlElementTypes.PERL_RESERVED);
		else if(OPERATORS_SET.contains(tokenType))
			attributesKeys = attributesMap.get(PerlElementTypes.PERL_OPERATOR);
		else
			attributesKeys = attributesMap.get(tokenType);

		return attributesKeys == null
				? EMPTY_KEYS
				: attributesKeys;
	}
}
