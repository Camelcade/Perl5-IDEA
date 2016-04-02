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

	// stop tokens for statement recovery
	TokenSet STATEMENT_RECOVERY_TOKENS = TokenSet.create(
			SEMICOLON,

			POD,

			LEFT_PAREN,
			LEFT_BRACKET,
			LEFT_BRACE,
			RIGHT_BRACE,
			REGEX_QUOTE_CLOSE,

			TAG,
			TAG_END,
			TAG_DATA,

			BLOCK_NAME,

			RESERVED_IF,
			RESERVED_UNLESS,
			RESERVED_ELSIF,
			RESERVED_ELSE,
			RESERVED_RETURN,
			RESERVED_GIVEN,
			RESERVED_WHEN,
			RESERVED_DEFAULT,
			RESERVED_WHILE,
			RESERVED_UNTIL,
			RESERVED_FOR,
			RESERVED_FOREACH,
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

			RESERVED_QW,

			RESERVED_QQ,
			RESERVED_Q,
			RESERVED_QX,

			RESERVED_TR,
			RESERVED_Y,

			RESERVED_S,
			RESERVED_QR,
			RESERVED_M,

			IDENTIFIER,
			PACKAGE,
			PACKAGE_CORE_IDENTIFIER,
			PACKAGE_IDENTIFIER,
			PACKAGE_PRAGMA_CONSTANT,
			PACKAGE_PRAGMA_VARS,

			SIGIL_SCALAR,
			SIGIL_ARRAY,
			SIGIL_HASH,
			SIGIL_GLOB,
			SIGIL_CODE,

			RESERVED_MY,
			RESERVED_OUR,
			RESERVED_STATE,
			RESERVED_LOCAL
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

			OPERATOR_COMMA,
			OPERATOR_COMMA_ARROW,

			OPERATOR_DEREFERENCE
	);

}
