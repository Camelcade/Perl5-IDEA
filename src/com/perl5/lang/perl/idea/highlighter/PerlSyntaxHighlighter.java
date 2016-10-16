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

import com.intellij.codeInsight.template.impl.TemplateColors;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.lexer.adapters.PerlHighlightingLexerAdapter;
import com.perl5.lang.perl.parser.moose.MooseElementTypes;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.idea.highlighter.PodSyntaxHighlighter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class PerlSyntaxHighlighter extends SyntaxHighlighterBase implements PerlElementTypes, MooseElementTypes
{
	public static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

	public static final TextAttributesKey EMBED_MARKER_KEY = createTextAttributesKey("PERL_EMBED_MARKER", DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR);
	public static final TextAttributesKey[] EMBED_MARKER_KEYS = new TextAttributesKey[]{EMBED_MARKER_KEY};

	public static final TextAttributesKey PERL_NUMBER = createTextAttributesKey("PERL_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
	public static final TextAttributesKey PERL_VERSION = createTextAttributesKey("PERL_VERSION", PERL_NUMBER);
	public static final TextAttributesKey PERL_COMMENT = createTextAttributesKey("PERL_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);

	public static final TextAttributesKey PERL_GLOB = createTextAttributesKey("PERL_GLOB", DefaultLanguageHighlighterColors.IDENTIFIER);
	public static final TextAttributesKey PERL_GLOB_BUILTIN = createTextAttributesKey("PERL_GLOB_BUILTIN", PERL_GLOB);

	public static final TextAttributesKey PERL_HANDLE = createTextAttributesKey("PERL_HANDLE", PERL_GLOB);
	public static final TextAttributesKey PERL_HANDLE_BUILTIN = createTextAttributesKey("PERL_HANDLE_BUILTIN", PERL_GLOB_BUILTIN);

	public static final TextAttributesKey PERL_PACKAGE = createTextAttributesKey("PERL_PACKAGE", DefaultLanguageHighlighterColors.CLASS_NAME);
	public static final TextAttributesKey PERL_PACKAGE_PRAGMA = createTextAttributesKey("PERL_PACKAGE_PRAGMA", PERL_PACKAGE);
	public static final TextAttributesKey PERL_PACKAGE_CORE = createTextAttributesKey("PERL_PACKAGE_CORE", PERL_PACKAGE);

	public static final TextAttributesKey PERL_PACKAGE_DEFINITION = createTextAttributesKey("PERL_PACKAGE_DEFINITION", PERL_PACKAGE);

	public static final TextAttributesKey PERL_SUB = createTextAttributesKey("PERL_SUB", DefaultLanguageHighlighterColors.FUNCTION_CALL);
	public static final TextAttributesKey PERL_SUB_BUILTIN = createTextAttributesKey("PERL_SUB_BUILTIN", PERL_SUB);

	public static final TextAttributesKey PERL_XSUB = createTextAttributesKey("PERL_XSUB", PERL_SUB);

	public static final TextAttributesKey PERL_SUB_DEFINITION = createTextAttributesKey("PERL_SUB_DEFINITION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
	public static final TextAttributesKey PERL_SUB_DECLARATION = createTextAttributesKey("PERL_SUB_DECLARATION", PERL_SUB_DEFINITION);
	public static final TextAttributesKey PERL_LABEL = createTextAttributesKey("PERL_LABEL", DefaultLanguageHighlighterColors.LABEL);
	public static final TextAttributesKey PERL_BLOCK_NAME = createTextAttributesKey("PERL_BLOCK_NAME", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);
	public static final TextAttributesKey PERL_TAG = createTextAttributesKey("PERL_TAG", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);

	public static final TextAttributesKey PERL_KEYWORD = createTextAttributesKey("PERL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey[] PERL_KEYWORD_KEYS = new TextAttributesKey[]{PERL_KEYWORD};

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
	public static final TextAttributesKey PERL_AUTOLOAD = createTextAttributesKey("PERL_AUTOLOAD", TemplateColors.TEMPLATE_VARIABLE_ATTRIBUTES);

	public static final TextAttributesKey PERL_SCALAR = createTextAttributesKey("PERL_SCALAR", DefaultLanguageHighlighterColors.IDENTIFIER);

	public static final TextAttributesKey PERL_SCALAR_BUILTIN = createTextAttributesKey("PERL_SCALAR_BUILTIN", PERL_SCALAR);
	public static final TextAttributesKey PERL_ARRAY = createTextAttributesKey("PERL_ARRAY", DefaultLanguageHighlighterColors.IDENTIFIER);
	public static final TextAttributesKey PERL_ARRAY_BUILTIN = createTextAttributesKey("PERL_ARRAY_BUILTIN", PERL_ARRAY);
	public static final TextAttributesKey PERL_HASH = createTextAttributesKey("PERL_HASH", DefaultLanguageHighlighterColors.IDENTIFIER);
	public static final TextAttributesKey PERL_HASH_BUILTIN = createTextAttributesKey("PERL_HASH_BUILTIN", PERL_HASH);

	public static final TextAttributesKey PERL_CONSTANT = createTextAttributesKey("PERL_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT);

	public static final HashMap<IElementType, TextAttributesKey[]> ATTRIBUTES_MAP = new HashMap<IElementType, TextAttributesKey[]>();
	private static final TokenSet KEYWORDS = TokenSet.create(
			RESERVED_MY,
			RESERVED_OUR,
			RESERVED_STATE,
			RESERVED_LOCAL,
			RESERVED_ELSIF,
			RESERVED_ELSE,
			RESERVED_GIVEN,
			RESERVED_DEFAULT,
			RESERVED_CONTINUE,
			RESERVED_FORMAT,
			RESERVED_SUB,
			RESERVED_PACKAGE,
			RESERVED_USE,
			RESERVED_NO,
			RESERVED_REQUIRE,
			RESERVED_UNDEF,
			RESERVED_PRINT,
			RESERVED_PRINTF,
			RESERVED_SAY,
			RESERVED_GREP,
			RESERVED_MAP,
			RESERVED_SORT,
			RESERVED_DO,
			RESERVED_EVAL,
			RESERVED_GOTO,
			RESERVED_REDO,
			RESERVED_NEXT,
			RESERVED_LAST,
			RESERVED_RETURN,

			RESERVED_Y,
			RESERVED_TR,
			RESERVED_Q,
			RESERVED_S,
			RESERVED_M,
			RESERVED_QW,
			RESERVED_QQ,
			RESERVED_QR,
			RESERVED_QX,

			RESERVED_TRY,
			RESERVED_CATCH,
			RESERVED_FINALLY,

			RESERVED_INNER,
			RESERVED_WITH,
			RESERVED_EXTENDS,
			RESERVED_META,
			RESERVED_OVERRIDE,
			RESERVED_AROUND,
			RESERVED_SUPER,
			RESERVED_AUGMENT,
			RESERVED_AFTER,
			RESERVED_BEFORE,
			RESERVED_HAS
	);
	public static TextAttributesKey UNUSED_DEPRECATED;

	static
	{
		EditorColorsScheme currentScheme = EditorColorsManager.getInstance().getGlobalScheme();
		UNUSED_DEPRECATED = TextAttributesKey.createTextAttributesKey("UNUSED_DEPRECATED",
				TextAttributes.merge(
						currentScheme.getAttributes(CodeInsightColors.NOT_USED_ELEMENT_ATTRIBUTES),
						currentScheme.getAttributes(CodeInsightColors.DEPRECATED_ATTRIBUTES)
				));
	}

	static
	{
		// key for all reserved
		ATTRIBUTES_MAP.put(RESERVED_IF, PERL_KEYWORD_KEYS);
		ATTRIBUTES_MAP.put(RESERVED_UNTIL, PERL_KEYWORD_KEYS);
		ATTRIBUTES_MAP.put(RESERVED_UNLESS, PERL_KEYWORD_KEYS);
		ATTRIBUTES_MAP.put(RESERVED_FOR, PERL_KEYWORD_KEYS);
		ATTRIBUTES_MAP.put(RESERVED_FOREACH, PERL_KEYWORD_KEYS);
		ATTRIBUTES_MAP.put(RESERVED_WHEN, PERL_KEYWORD_KEYS);


		ATTRIBUTES_MAP.put(SIGIL_SCALAR, new TextAttributesKey[]{PERL_SCALAR});
		ATTRIBUTES_MAP.put(SIGIL_SCALAR_INDEX, new TextAttributesKey[]{PERL_SCALAR});
		ATTRIBUTES_MAP.put(SCALAR_NAME, new TextAttributesKey[]{PERL_SCALAR});

		ATTRIBUTES_MAP.put(SIGIL_ARRAY, new TextAttributesKey[]{PERL_ARRAY});
		ATTRIBUTES_MAP.put(ARRAY_NAME, new TextAttributesKey[]{PERL_ARRAY});

		ATTRIBUTES_MAP.put(SIGIL_HASH, new TextAttributesKey[]{PERL_HASH});
		ATTRIBUTES_MAP.put(HASH_NAME, new TextAttributesKey[]{PERL_HASH});

		ATTRIBUTES_MAP.put(SIGIL_GLOB, new TextAttributesKey[]{PERL_GLOB});
		ATTRIBUTES_MAP.put(GLOB_NAME, new TextAttributesKey[]{PERL_GLOB});

		ATTRIBUTES_MAP.put(SIGIL_CODE, new TextAttributesKey[]{PERL_SUB});
		ATTRIBUTES_MAP.put(CODE_NAME, new TextAttributesKey[]{PERL_SUB});

		ATTRIBUTES_MAP.put(FAT_COMMA, new TextAttributesKey[]{PERL_COMMA});
		ATTRIBUTES_MAP.put(COMMA, new TextAttributesKey[]{PERL_COMMA});

		ATTRIBUTES_MAP.put(BLOCK_NAME, PERL_KEYWORD_KEYS);

		ATTRIBUTES_MAP.put(COLON, new TextAttributesKey[]{PERL_OPERATOR});

		ATTRIBUTES_MAP.put(OPERATOR_DEREFERENCE, new TextAttributesKey[]{PERL_DEREFERENCE});

		ATTRIBUTES_MAP.put(HANDLE, new TextAttributesKey[]{PERL_HANDLE});

		ATTRIBUTES_MAP.put(HEREDOC_END, new TextAttributesKey[]{PERL_SQ_STRING});

		ATTRIBUTES_MAP.put(REGEX_QUOTE_CLOSE, new TextAttributesKey[]{PERL_REGEX_QUOTE});
		ATTRIBUTES_MAP.put(REGEX_QUOTE_OPEN, new TextAttributesKey[]{PERL_REGEX_QUOTE});
		ATTRIBUTES_MAP.put(REGEX_QUOTE_OPEN_E, new TextAttributesKey[]{PERL_REGEX_QUOTE});
		ATTRIBUTES_MAP.put(REGEX_QUOTE_E, new TextAttributesKey[]{PERL_REGEX_QUOTE});
		ATTRIBUTES_MAP.put(REGEX_QUOTE, new TextAttributesKey[]{PERL_REGEX_QUOTE});
		ATTRIBUTES_MAP.put(REGEX_TOKEN, new TextAttributesKey[]{PERL_REGEX_TOKEN});
		ATTRIBUTES_MAP.put(REGEX_MODIFIER, PERL_KEYWORD_KEYS);

		ATTRIBUTES_MAP.put(QUOTE_DOUBLE, new TextAttributesKey[]{PERL_DQ_STRING});
		ATTRIBUTES_MAP.put(QUOTE_SINGLE, new TextAttributesKey[]{PERL_SQ_STRING});
		ATTRIBUTES_MAP.put(QUOTE_TICK, new TextAttributesKey[]{PERL_DX_STRING});
		ATTRIBUTES_MAP.put(QUOTE_DOUBLE_OPEN, new TextAttributesKey[]{PERL_DQ_STRING});
		ATTRIBUTES_MAP.put(QUOTE_SINGLE_OPEN, new TextAttributesKey[]{PERL_SQ_STRING});
		ATTRIBUTES_MAP.put(QUOTE_TICK_OPEN, new TextAttributesKey[]{PERL_DX_STRING});
		ATTRIBUTES_MAP.put(QUOTE_DOUBLE_CLOSE, new TextAttributesKey[]{PERL_DQ_STRING});
		ATTRIBUTES_MAP.put(QUOTE_SINGLE_CLOSE, new TextAttributesKey[]{PERL_SQ_STRING});
		ATTRIBUTES_MAP.put(QUOTE_TICK_CLOSE, new TextAttributesKey[]{PERL_DX_STRING});

		ATTRIBUTES_MAP.put(SEMICOLON, new TextAttributesKey[]{PERL_SEMICOLON});
		ATTRIBUTES_MAP.put(LEFT_BRACE, new TextAttributesKey[]{PERL_BRACE});
		ATTRIBUTES_MAP.put(RIGHT_BRACE, new TextAttributesKey[]{PERL_BRACE});
		ATTRIBUTES_MAP.put(LEFT_BRACKET, new TextAttributesKey[]{PERL_BRACK});
		ATTRIBUTES_MAP.put(RIGHT_BRACKET, new TextAttributesKey[]{PERL_BRACK});
		ATTRIBUTES_MAP.put(LEFT_PAREN, new TextAttributesKey[]{PERL_PAREN});
		ATTRIBUTES_MAP.put(RIGHT_PAREN, new TextAttributesKey[]{PERL_PAREN});

		ATTRIBUTES_MAP.put(NUMBER, new TextAttributesKey[]{PERL_NUMBER});
		ATTRIBUTES_MAP.put(NUMBER_SIMPLE, new TextAttributesKey[]{PERL_NUMBER});
		ATTRIBUTES_MAP.put(NUMBER_VERSION, new TextAttributesKey[]{PERL_VERSION});

		// key for all operators
		ATTRIBUTES_MAP.put(OPERATOR_MUL, new TextAttributesKey[]{PERL_OPERATOR});

		ATTRIBUTES_MAP.put(TAG, new TextAttributesKey[]{PERL_TAG});
		ATTRIBUTES_MAP.put(TAG_END, new TextAttributesKey[]{PERL_TAG});
		ATTRIBUTES_MAP.put(TAG_DATA, new TextAttributesKey[]{PERL_TAG});

		ATTRIBUTES_MAP.put(STRING_CONTENT, new TextAttributesKey[]{PERL_SQ_STRING});
		ATTRIBUTES_MAP.put(STRING_CONTENT_QQ, new TextAttributesKey[]{PERL_DQ_STRING});
		ATTRIBUTES_MAP.put(STRING_CONTENT_XQ, new TextAttributesKey[]{PERL_DX_STRING});

		ATTRIBUTES_MAP.put(COMMENT_LINE, new TextAttributesKey[]{PERL_COMMENT});
		ATTRIBUTES_MAP.put(COMMENT_BLOCK, new TextAttributesKey[]{PERL_COMMENT});
	}


	protected Project myProject;

	public PerlSyntaxHighlighter(Project project)
	{
		myProject = project;
	}

	@NotNull
	@Override
	public Lexer getHighlightingLexer()
	{
		return new PerlHighlightingLexerAdapter(myProject);
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType)
	{
		if (tokenType.getLanguage() == PodLanguage.INSTANCE)
		{
			return PodSyntaxHighlighter.getTokenAttributes(tokenType);
		}
		else if (KEYWORDS.contains(tokenType))
		{
			return PERL_KEYWORD_KEYS;
		}
		else if (ATTRIBUTES_MAP.containsKey(tokenType))
		{
			return ATTRIBUTES_MAP.get(tokenType);
		}
		else if (PerlLexer.OPERATORS_TOKENSET.contains(tokenType))
		{
			return ATTRIBUTES_MAP.get(OPERATOR_MUL);
		}

		return EMPTY_KEYS;
	}
}
