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

package com.perl5.lang.perl.idea.formatter;

import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;

/**
 * Created by hurricup on 06.09.2015.
 */
public interface PerlFormattingTokenSets extends PerlElementTypes
{
	public static final TokenSet CONDITION_LIKE_ELEMENTS = TokenSet.create(
			CONDITION_STATEMENT,
			FOR_ITERATOR,
			FOR_LIST_EPXR
	);

	public static final TokenSet BLOCK_CONTAINERS = TokenSet.create(
			CONDITIONAL_BLOCK,
			FOR_COMPOUND,
			FOREACH_COMPOUND,
			UNCONDITIONAL_BLOCK,
			CONTINUE_BLOCK,

			// fixme probably we should move them somewhere
			SUB_DEFINITION,
			NAMESPACE_DEFINITION
	);

	public static final TokenSet STATEMENT_MODIFIERS = TokenSet.create(
			IF_STATEMENT_MODIFIER,
			UNLESS_STATEMENT_MODIFIER,
			WHILE_STATEMENT_MODIFIER,
			UNTIL_STATEMENT_MODIFIER,
			FOR_STATEMENT_MODIFIER,
			FOREACH_STATEMENT_MODIFIER,
			WHEN_STATEMENT_MODIFIER
	);

	public static final TokenSet RESERVED_COMPOUND_CONDITIONAL = TokenSet.create(
			RESERVED_IF,
			RESERVED_UNLESS,
			RESERVED_GIVEN,
			RESERVED_WHILE,
			RESERVED_UNTIL,
			RESERVED_ELSIF,

			// these are redundant here, but used in statement modifiers
			RESERVED_FOR,
			RESERVED_FOREACH
	);

	public static final TokenSet RESERVED_TERMS_BLOCKS = TokenSet.create(
			RESERVED_DO,
			RESERVED_EVAL,
			RESERVED_SUB
	);


	public static final TokenSet OPERATORS_BITWISE = TokenSet.create(
			OPERATOR_BITWISE_OR,
			OPERATOR_BITWISE_XOR,
			OPERATOR_BITWISE_AND
	);

	public static final TokenSet OPERATORS_ADDITIVE = TokenSet.create(
			OPERATOR_PLUS,
			OPERATOR_MINUS
	);

	public static final TokenSet OPERATORS_MULTIPLICATIVE = TokenSet.create(
			OPERATOR_MUL,
			OPERATOR_DIV,
			OPERATOR_POW
	);

	public static final TokenSet OPERATORS_SHIFT = TokenSet.create(
			OPERATOR_SHIFT_LEFT,
			OPERATOR_SHIFT_RIGHT
	);

	public static final TokenSet OPERATORS_UNARY = TokenSet.create(
			OPERATOR_PLUS_PLUS,
			OPERATOR_NOT,
			OPERATOR_MINUS_MINUS,
			OPERATOR_BITWISE_NOT
	);

	public static final TokenSet OPERATORS_LOGICAL = TokenSet.create(
			OPERATOR_AND,
			OPERATOR_OR,
			OPERATOR_OR_DEFINED
	);

	public static final TokenSet OPERATORS_EQUALITY = TokenSet.create(
			OPERATOR_EQ_NUMERIC,
			OPERATOR_NE_NUMERIC
	);

	public static final TokenSet OPERATORS_RELATIONAL = TokenSet.create(
			OPERATOR_GE_NUMERIC,
			OPERATOR_LE_NUMERIC,
			OPERATOR_SMARTMATCH,
			OPERATOR_GT_NUMERIC,
			OPERATOR_LT_NUMERIC,
			OPERATOR_CMP_NUMERIC
	);

	public static final TokenSet OPERATORS_STR = TokenSet.create(
			OPERATOR_GE_STR,
			OPERATOR_LE_STR,
			OPERATOR_GT_STR,
			OPERATOR_LT_STR,
			OPERATOR_CMP_STR,
			OPERATOR_EQ_STR,
			OPERATOR_NE_STR,
			OPERATOR_OR_LP,
			OPERATOR_AND_LP,
			OPERATOR_NOT_LP,
			OPERATOR_XOR_LP
	);

	public static final TokenSet OPERATORS_ASSIGNMENT = TokenSet.create(
			OPERATOR_ASSIGN,
			OPERATOR_POW_ASSIGN,
			OPERATOR_PLUS_ASSIGN,
			OPERATOR_MINUS_ASSIGN,
			OPERATOR_MUL_ASSIGN,
			OPERATOR_DIV_ASSIGN,
			OPERATOR_MOD_ASSIGN,
			OPERATOR_CONCAT_ASSIGN,
			OPERATOR_X_ASSIGN,
			OPERATOR_BITWISE_AND_ASSIGN,
			OPERATOR_BITWISE_OR_ASSIGN,
			OPERATOR_BITWISE_XOR_ASSIGN,
			OPERATOR_SHIFT_LEFT_ASSIGN,
			OPERATOR_SHIFT_RIGHT_ASSIGN,
			OPERATOR_AND_ASSIGN,
			OPERATOR_OR_ASSIGN,
			OPERATOR_OR_DEFINED_ASSIGN
	);

	public static final TokenSet RESERVED_VARIABLE_DECLARATION = TokenSet.create(
			RESERVED_MY,
			RESERVED_OUR,
			RESERVED_LOCAL,
			RESERVED_STATE
	);

}
