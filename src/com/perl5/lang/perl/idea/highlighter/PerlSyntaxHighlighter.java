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

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
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

		attributesMap.put(PerlElementTypes.OPERATOR_COMMA_ARROW, new TextAttributesKey[]{PERL_COMMA});
		attributesMap.put(PerlElementTypes.OPERATOR_COMMA, new TextAttributesKey[]{PERL_COMMA});

		attributesMap.put(PerlElementTypes.BLOCK_NAME, new TextAttributesKey[]{PERL_KEYWORD});

		attributesMap.put(PerlElementTypes.COLON, new TextAttributesKey[]{PERL_OPERATOR});

		attributesMap.put(PerlElementTypes.COMMENT_LINE, new TextAttributesKey[]{PERL_COMMENT});
		attributesMap.put(PerlElementTypes.COMMENT_BLOCK, new TextAttributesKey[]{PERL_COMMENT});

		attributesMap.put(PerlElementTypes.OPERATOR_DEREFERENCE, new TextAttributesKey[]{PERL_DEREFERENCE});

		attributesMap.put(PerlElementTypes.SUB, new TextAttributesKey[]{PERL_SUB});
		attributesMap.put(PerlElementTypes.SUB_PROTOTYPE_TOKEN, new TextAttributesKey[]{PERL_SUB_PROTOTYPE_TOKEN});
		attributesMap.put(PerlElementTypes.SUB_ATTRIBUTE, new TextAttributesKey[]{PERL_SUB_ATTRIBUTE});

		attributesMap.put(PerlElementTypes.HANDLE, new TextAttributesKey[]{PERL_HANDLE});

		attributesMap.put(PerlElementTypes.TEMPLATE_BLOCK_HTML, new TextAttributesKey[]{PERL_COMMENT});
		attributesMap.put(PerlElementTypes.HEREDOC_END, new TextAttributesKey[]{PERL_SQ_STRING});

		attributesMap.put(PerlElementTypes.STRING_CONTENT, new TextAttributesKey[]{PERL_SQ_STRING});

		attributesMap.put(PerlElementTypes.REGEX_QUOTE_CLOSE, new TextAttributesKey[]{PERL_REGEX_QUOTE});
		attributesMap.put(PerlElementTypes.REGEX_QUOTE_OPEN, new TextAttributesKey[]{PERL_REGEX_QUOTE});
		attributesMap.put(PerlElementTypes.REGEX_TOKEN, new TextAttributesKey[]{PERL_REGEX_TOKEN});
		attributesMap.put(PerlElementTypes.REGEX_MODIFIER, new TextAttributesKey[]{PERL_KEYWORD});

		attributesMap.put(PerlElementTypes.QUOTE, new TextAttributesKey[]{PERL_SQ_STRING});
		attributesMap.put(PerlElementTypes.QUOTE_DOUBLE, new TextAttributesKey[]{PERL_DQ_STRING});
		attributesMap.put(PerlElementTypes.QUOTE_SINGLE, new TextAttributesKey[]{PERL_SQ_STRING});
		attributesMap.put(PerlElementTypes.QUOTE_TICK, new TextAttributesKey[]{PERL_DX_STRING});

		attributesMap.put(PerlElementTypes.SEMICOLON, new TextAttributesKey[]{PERL_SEMICOLON});
		attributesMap.put(PerlElementTypes.LEFT_BRACE, new TextAttributesKey[]{PERL_BRACE});
		attributesMap.put(PerlElementTypes.RIGHT_BRACE, new TextAttributesKey[]{PERL_BRACE});
		attributesMap.put(PerlElementTypes.LEFT_BRACKET, new TextAttributesKey[]{PERL_BRACK});
		attributesMap.put(PerlElementTypes.RIGHT_BRACKET, new TextAttributesKey[]{PERL_BRACK});
		attributesMap.put(PerlElementTypes.LEFT_PAREN, new TextAttributesKey[]{PERL_PAREN});
		attributesMap.put(PerlElementTypes.RIGHT_PAREN, new TextAttributesKey[]{PERL_PAREN});

		attributesMap.put(PerlElementTypes.NUMBER, new TextAttributesKey[]{PERL_NUMBER});
		attributesMap.put(PerlElementTypes.NUMBER_VERSION, new TextAttributesKey[]{PERL_VERSION});

		// key for all operators
		attributesMap.put(PerlElementTypes.OPERATOR_MUL, new TextAttributesKey[]{PERL_OPERATOR});
		// key for all reserved
		attributesMap.put(PerlElementTypes.RESERVED_IF, new TextAttributesKey[]{PERL_KEYWORD});

		attributesMap.put(PerlElementTypes.TAG, new TextAttributesKey[]{PERL_TAG});

		attributesMap.put(PerlElementTypes.PACKAGE, new TextAttributesKey[]{PERL_PACKAGE});
		attributesMap.put(PerlElementTypes.HANDLE, new TextAttributesKey[]{PERL_HANDLE});

		attributesMap.put(PerlElementTypes.SIGIL_SCALAR, new TextAttributesKey[]{PERL_SCALAR});
		attributesMap.put(PerlElementTypes.SIGIL_SCALAR_INDEX, new TextAttributesKey[]{PERL_SCALAR});
		attributesMap.put(PerlElementTypes.SIGIL_ARRAY, new TextAttributesKey[]{PERL_ARRAY});
		attributesMap.put(PerlElementTypes.VARIABLE_NAME, new TextAttributesKey[]{DefaultLanguageHighlighterColors.IDENTIFIER});
	}


	private final SyntaxHighlighter POD_SYNTAX_HIGHLIGHTER;

	protected Project myProject;

	public PerlSyntaxHighlighter(Project project)
	{
		myProject = project;
		POD_SYNTAX_HIGHLIGHTER = new PodSyntaxHighlighter(project);
	}

	@NotNull
	@Override
	public Lexer getHighlightingLexer()
	{
		return new PerlHighlightningLexer(myProject);
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType)
	{
		if (tokenType instanceof PodTokenType)
			return POD_SYNTAX_HIGHLIGHTER.getTokenHighlights(tokenType);
		else if (attributesMap.containsKey(tokenType) )
			return attributesMap.get(tokenType);
		else if (PerlLexer.RESERVED_TOKENSET.contains(tokenType))
			return attributesMap.get(PerlElementTypes.RESERVED_IF);
		else if (PerlLexer.OPERATORS_TOKENSET.contains(tokenType))
			return attributesMap.get(PerlElementTypes.OPERATOR_MUL);

		return EMPTY_KEYS;
	}
}
