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

package com.perl5.lang.perl.parser;

import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;

/**
 * Created by hurricup on 28.12.2015.
 */
public interface PerlParser extends PerlElementTypes
{
	// these tokens are not being marked as bad characters
	TokenSet BAD_CHARACTER_FORBIDDEN_TOKENS = TokenSet.create(
			RESERVED_PACKAGE,
			RIGHT_BRACE,
			REGEX_QUOTE_CLOSE,
			SEMICOLON
	);

	// Contains tokens that can be freely consumed during statement recovery
	TokenSet STATEMENT_RECOVERY_CONSUMABLE_TOKENS = TokenSet.create(
			COLON,
			COMMENT_ANNOTATION,
			COMMENT_BLOCK,
			COMMENT_LINE,
			FORMAT,
			FORMAT_TERMINATOR,
			HANDLE,
			HEREDOC,
			HEREDOC_END,
			HEREDOC_QQ,
			HEREDOC_QX,
			OPERATOR_AND,
			OPERATOR_AND_ASSIGN,
			OPERATOR_ASSIGN,
			OPERATOR_BITWISE_AND_ASSIGN,
			OPERATOR_BITWISE_OR,
			OPERATOR_BITWISE_OR_ASSIGN,
			OPERATOR_BITWISE_XOR,
			OPERATOR_BITWISE_XOR_ASSIGN,
			OPERATOR_CMP_NUMERIC,
			COMMA,
			FAT_COMMA,
			OPERATOR_CONCAT,
			OPERATOR_CONCAT_ASSIGN,
			OPERATOR_DEREFERENCE,
			OPERATOR_DIV,
			OPERATOR_DIV_ASSIGN,
			OPERATOR_EQ_NUMERIC,
			OPERATOR_FLIP_FLOP,
			OPERATOR_GE_NUMERIC,
			OPERATOR_GT_NUMERIC,
			OPERATOR_LE_NUMERIC,
			OPERATOR_LT_NUMERIC,
			OPERATOR_MINUS_ASSIGN,
			OPERATOR_MOD_ASSIGN,
			OPERATOR_MUL_ASSIGN,
			OPERATOR_NE_NUMERIC,
			OPERATOR_NOT_RE,
			OPERATOR_OR,
			OPERATOR_OR_ASSIGN,
			OPERATOR_OR_DEFINED,
			OPERATOR_OR_DEFINED_ASSIGN,
			OPERATOR_PLUS_ASSIGN,
			OPERATOR_POW,
			OPERATOR_POW_ASSIGN,
			OPERATOR_RE,
			OPERATOR_SHIFT_LEFT,
			OPERATOR_SHIFT_LEFT_ASSIGN,
			OPERATOR_SHIFT_RIGHT,
			OPERATOR_SHIFT_RIGHT_ASSIGN,
			OPERATOR_SMARTMATCH,
			OPERATOR_X_ASSIGN,
			PARSABLE_STRING_USE_VARS,
			QUESTION,
			QUOTE_DOUBLE,
			QUOTE_DOUBLE_CLOSE,
			QUOTE_SINGLE,
			QUOTE_SINGLE_CLOSE,
			QUOTE_TICK,
			QUOTE_TICK_CLOSE,
			REGEX_MODIFIER,
			REGEX_QUOTE,
			REGEX_QUOTE_E,
			REGEX_QUOTE_OPEN_E,
			REGEX_TOKEN,
			RIGHT_ANGLE,
			RIGHT_BRACKET,
			RIGHT_PAREN,
			SUB,
			SUB_PROTOTYPE_TOKEN,
			VERSION_ELEMENT
	);


	// Tokens which consumed and counted as semicolon
	TokenSet CONSUMABLE_SEMI_TOKENS = TokenSet.create(
			SEMICOLON
	);

	// Tokens which makes semicolon optional, like block close brace
	TokenSet UNCONSUMABLE_SEMI_TOKENS = TokenSet.create(
			RIGHT_BRACE,
			REGEX_QUOTE_CLOSE,
			TAG_END,
			TAG_DATA
	);

	TokenSet ANON_HASH_TOKEN_SUFFIXES = TokenSet.create(
			RIGHT_BRACE
			, RIGHT_PAREN
			, RIGHT_BRACKET
			, SEMICOLON
			, COLON

			, OPERATOR_HELLIP,
			OPERATOR_FLIP_FLOP,
			OPERATOR_CONCAT,

			OPERATOR_AND,
			OPERATOR_OR,
			OPERATOR_OR_DEFINED,
			OPERATOR_NOT,

			COLON,

			OPERATOR_AND_LP,
			OPERATOR_OR_LP,
//			OPERATOR_XOR_LP,
			OPERATOR_NOT_LP,

			COMMA,
			FAT_COMMA,

			OPERATOR_DEREFERENCE
	);

}
