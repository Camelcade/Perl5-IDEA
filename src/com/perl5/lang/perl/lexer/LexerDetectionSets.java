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

package com.perl5.lang.perl.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by hurricup on 23.06.2015.
 */
public interface LexerDetectionSets extends PerlElementTypes
{
	// pre-variable name tokens
	public static final TokenSet SIGILS_TOKENS = TokenSet.create(
			SIGIL_ARRAY,
			SIGIL_SCALAR,
			SIGIL_SCALAR_INDEX,
			OPERATOR_BITWISE_AND,    // code sigil
			OPERATOR_MOD,    // hash sigil
			OPERATOR_MUL    // glob sigil
	);

	// we should not check bareword for reserved tokens if following was before
	public static final TokenSet PRE_PACKAGE_TOKENS = TokenSet.create(
			RESERVED_USE,
			RESERVED_NO,
			RESERVED_PACKAGE,
			RESERVED_MY,
			RESERVED_OUR,
			RESERVED_STATE,
			RESERVED_LOCAL
	);

	// prefixes, disables identifier interpretation attempt
	public static final TokenSet IDENTIFIER_NEGATION_PREFIX = TokenSet.orSet(
			PRE_PACKAGE_TOKENS,
			TokenSet.create(
					OPERATOR_DEREFERENCE    // ->identifier
			)
	);

	// operators tokens (except commas)
	public static final TokenSet OPERATORS_TOKENSET = TokenSet.create(
			OPERATOR_CMP_NUMERIC,
			OPERATOR_LT_NUMERIC,
			OPERATOR_GT_NUMERIC,

			OPERATOR_CMP_STR,
			OPERATOR_LE_STR,
			OPERATOR_GE_STR,
			OPERATOR_EQ_STR,
			OPERATOR_NE_STR,
			OPERATOR_LT_STR,
			OPERATOR_GT_STR,

			OPERATOR_HELLIP,
			OPERATOR_FLIP_FLOP,
			OPERATOR_CONCAT,

			OPERATOR_PLUS_PLUS,
			OPERATOR_MINUS_MINUS,
			OPERATOR_POW,

			OPERATOR_RE,
			OPERATOR_NOT_RE,

			OPERATOR_HEREDOC,
			OPERATOR_SHIFT_LEFT,
			OPERATOR_SHIFT_RIGHT,

			OPERATOR_SMARTMATCH,

			OPERATOR_AND,
			OPERATOR_OR,
			OPERATOR_OR_DEFINED,
			OPERATOR_NOT,

			OPERATOR_ASSIGN,

			QUESTION,
			COLON,

			OPERATOR_REFERENCE,

			OPERATOR_DIV,
			OPERATOR_MUL,
			OPERATOR_MOD,
			OPERATOR_PLUS,
			OPERATOR_MINUS,

			OPERATOR_BITWISE_NOT,
			OPERATOR_BITWISE_AND,
			OPERATOR_BITWISE_OR,
			OPERATOR_BITWISE_XOR,

			OPERATOR_AND_LP,
			OPERATOR_OR_LP,
			OPERATOR_XOR_LP,
			OPERATOR_NOT_LP,

			OPERATOR_COMMA,
			OPERATOR_COMMA_ARROW,

			OPERATOR_DEREFERENCE,

			OPERATOR_X,
			OPERATOR_FILETEST
	);


	// tokens that preceeds regexp opener
	public static final TokenSet REGEXP_PREFIX = TokenSet.create(
			SEMICOLON,
			COLON,
			LEFT_PAREN,
			LEFT_BRACE,
			LEFT_BRACKET
//			SUB	// fixme this works with argumentless subs, not all of them
	);


	public static final HashSet<String> REGEXP_PREFIX_SUBS = new HashSet<>(Arrays.asList(
			"split",
			"grep"
	));


	public static final HashSet<IElementType> RESERVED_TOKENSET = new HashSet<>();


	// tokens which preceeds {bareword}
	public static final TokenSet PRE_BRACED_STRING_TOKENS = TokenSet.create(
			OPERATOR_DEREFERENCE,    // ..->{bareword}
			VARIABLE_NAME,        // $hash{bareword}
			RIGHT_BRACE,        // $hash{something}{bareword}
			RIGHT_BRACKET        // $array[something]{bareword}
	);


	// last token except comments and whitespaces
	public static final TokenSet UNSIGNIFICANT_TOKENS = TokenSet.create(
			TokenType.NEW_LINE_INDENT,
			TokenType.WHITE_SPACE,
			COMMENT_LINE,
			COMMENT_BLOCK,
			POD,
			HEREDOC,
			HEREDOC_END
	);



}
