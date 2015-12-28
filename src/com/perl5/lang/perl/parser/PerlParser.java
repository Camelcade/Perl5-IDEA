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

			RIGHT_BRACE,
			REGEX_QUOTE_CLOSE,

			BLOCK_NAME,

			RESERVED_IF,
			RESERVED_UNLESS,
			RESERVED_GIVEN,
			RESERVED_WHILE,
			RESERVED_UNTIL,
			RESERVED_WHEN,

			RESERVED_FOREACH,    // may have no opening paren after a keyword
			RESERVED_FOR,        // may have no opening paren after a keyword

			RESERVED_PACKAGE,
			RESERVED_USE,
			RESERVED_NO,

			RESERVED_DEFAULT    // has no opening paren
	);

	// stop tokens for block recovery
	TokenSet BLOCK_RECOVERY_TOKENS = TokenSet.create(
			RIGHT_BRACE
	);

	// Tokens which consumed and counted as semicolon
	TokenSet CONSUMABLE_SEMI_TOKENS = TokenSet.create(
			SEMICOLON
	);

	// Tokens which makes semicolon optional, like block close brace
	TokenSet UNCONSUMABLE_SEMI_TOKENS = TokenSet.create(
			RIGHT_BRACE,
			REGEX_QUOTE_CLOSE
	);

}
