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
import com.perl5.lang.perl.lexer.adapters.PerlHighlightingLexerAdapter;
import com.perl5.lang.perl.parser.moose.MooseElementTypes;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.idea.highlighter.PodSyntaxHighlighter;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;
import static com.perl5.lang.perl.lexer.PerlTokenSets.*;

public class PerlSyntaxHighlighter extends SyntaxHighlighterBase implements PerlElementTypes, MooseElementTypes
{
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
	public static final TextAttributesKey PERL_LABEL = createTextAttributesKey("PERL_LABEL", DefaultLanguageHighlighterColors.IDENTIFIER);
	public static final TextAttributesKey PERL_BLOCK_NAME = createTextAttributesKey("PERL_BLOCK_NAME", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);
	public static final TextAttributesKey PERL_TAG = createTextAttributesKey("PERL_TAG", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);
	public static final TextAttributesKey PERL_KEYWORD = createTextAttributesKey("PERL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey PERL_OPERATOR = createTextAttributesKey("PERL_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
	public static final TextAttributesKey PERL_DEREFERENCE = createTextAttributesKey("PERL_DEREFERENCE", DefaultLanguageHighlighterColors.OPERATION_SIGN);

	public static final TextAttributesKey PERL_REGEX_QUOTE = createTextAttributesKey("PERL_REGEX_QUOTE", DefaultLanguageHighlighterColors.BRACKETS);
	public static final TextAttributesKey PERL_REGEX_TOKEN = createTextAttributesKey("PERL_REGEX_TOKEN", DefaultLanguageHighlighterColors.STRING);
//	public static final TextAttributesKey PERL_REGEX_CHAR_CLASS = createTextAttributesKey("PERL_REGEX_CHAR_CLASS", DefaultLanguageHighlighterColors.KEYWORD);

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
	private static final Map<IElementType, TextAttributesKey> COLORS_MAP = new THashMap<>();
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
		safeMap(PERL_PACKAGE, PACKAGE, PACKAGE_PRAGMA_CONSTANT, QUALIFYING_PACKAGE);
		safeMap(PERL_SCALAR, SIGIL_SCALAR, LEFT_BRACE_SCALAR, RIGHT_BRACE_SCALAR, DEREF_SCALAR, SIGIL_SCALAR_INDEX, DEREF_SCALAR_INDEX, SCALAR_NAME);
		safeMap(PERL_ARRAY, SIGIL_ARRAY, LEFT_BRACE_ARRAY, RIGHT_BRACE_ARRAY, DEREF_ARRAY, ARRAY_NAME);
		safeMap(PERL_HASH, SIGIL_HASH, LEFT_BRACE_HASH, RIGHT_BRACE_HASH, DEREF_HASH, HASH_NAME);
		safeMap(PERL_GLOB, SIGIL_GLOB, LEFT_BRACE_GLOB, RIGHT_BRACE_GLOB, DEREF_GLOB, GLOB_NAME);
		safeMap(PERL_SUB, SIGIL_CODE, LEFT_BRACE_CODE, RIGHT_BRACE_CODE, DEREF_CODE);

		safeMap(PERL_COMMA, FAT_COMMA, COMMA);

		safeMap(BLOCK_NAME, PERL_KEYWORD);

		safeMap(COLON, PERL_OPERATOR);

		safeMap(OPERATOR_DEREFERENCE, PERL_DEREFERENCE);

		safeMap(HANDLE, PERL_HANDLE);

		safeMap(HEREDOC_END, PERL_SQ_STRING);

		safeMap(PERL_REGEX_QUOTE, REGEX_QUOTE_CLOSE, REGEX_QUOTE_OPEN, REGEX_QUOTE_OPEN_E, REGEX_QUOTE_E, REGEX_QUOTE);
		safeMap(REGEX_TOKEN, PERL_REGEX_TOKEN);
		safeMap(REGEX_MODIFIER, PERL_KEYWORD);

		safeMap(PERL_DQ_STRING, QUOTE_DOUBLE, QUOTE_DOUBLE_OPEN, QUOTE_DOUBLE_CLOSE, STRING_CONTENT_QQ);
		safeMap(PERL_SQ_STRING, QUOTE_SINGLE, QUOTE_SINGLE_OPEN, QUOTE_SINGLE_CLOSE, STRING_CONTENT);
		safeMap(PERL_DX_STRING, QUOTE_TICK, QUOTE_TICK_OPEN, QUOTE_TICK_CLOSE, STRING_CONTENT_XQ);

		safeMap(SEMICOLON, PERL_SEMICOLON);

		safeMap(PERL_BRACE, LEFT_BRACE, RIGHT_BRACE); //, REGEX_LEFT_BRACE, REGEX_RIGHT_BRACE
		safeMap(PERL_BRACK, LEFT_BRACKET, RIGHT_BRACKET); //, REGEX_LEFT_BRACKET, REGEX_RIGHT_BRACKET, REGEX_POSIX_LEFT_BRACKET, REGEX_POSIX_RIGHT_BRACKET
		safeMap(PERL_PAREN, LEFT_PAREN, RIGHT_PAREN); // , REGEX_LEFT_PAREN, REGEX_RIGHT_PAREN

		safeMap(PERL_NUMBER, NUMBER, NUMBER_SIMPLE);
		safeMap(NUMBER_VERSION, PERL_VERSION);

		safeMap(PERL_TAG, TAG, TAG_END, TAG_DATA);

		safeMap(PERL_COMMENT, COMMENT_LINE, COMMENT_BLOCK);

		safeMap(ATTRIBUTE_IDENTIFIER, PERL_SUB_ATTRIBUTE);
		safeMap(ANNOTATION_UNKNOWN_KEY, PERL_COMMENT);

		safeMap(PERL_SUB_BUILTIN, BUILTIN_ARGUMENTLESS, BUILTIN_UNARY, BUILTIN_LIST);
		safeMap(SUB_NAME, PERL_SUB);

		safeMap(KEYWORDS_TOKENSET, PERL_KEYWORD);
		safeMap(ANNOTATIONS_KEYS, PERL_ANNOTATION);
		safeMap(TokenSet.andNot(OPERATORS_TOKENSET, TokenSet.create(COMMA, FAT_COMMA, OPERATOR_DEREFERENCE)), PERL_OPERATOR);

//		safeMap(PERL_REGEX_CHAR_CLASS, REGEX_CHAR_CLASS, REGEX_POSIX_CLASS_NAME);
	}


	protected Project myProject;

	public PerlSyntaxHighlighter(Project project)
	{
		myProject = project;
	}

	protected static void safeMap(@NotNull final IElementType type, @NotNull final TextAttributesKey value)
	{
		safeMap(COLORS_MAP, type, value);
	}

	protected static void safeMap(@NotNull final TokenSet tokens, @NotNull final TextAttributesKey value)
	{
		safeMap(COLORS_MAP, tokens, value);
	}

	protected static void safeMap(@NotNull TextAttributesKey value, @NotNull IElementType... types)
	{
		for (IElementType type : types)
		{
			safeMap(type, value);
		}
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
		// fixme unify this somehow
		if (tokenType.getLanguage() == PodLanguage.INSTANCE)
		{
			return PodSyntaxHighlighter.getTokenAttributes(tokenType);
		}

		return pack(COLORS_MAP.get(tokenType));
	}
}
