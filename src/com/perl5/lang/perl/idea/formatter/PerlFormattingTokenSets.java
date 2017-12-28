/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
public interface PerlFormattingTokenSets extends PerlElementTypes {
  TokenSet FOR_OR_FOREACH = TokenSet.create(
    RESERVED_FOR, RESERVED_FOREACH
  );

  TokenSet CONDITION_LIKE_ELEMENTS = TokenSet.create(
    CONDITION_EXPR,
    FOR_ITERATOR
  );

  TokenSet SUB_DEFINITIONS_TOKENSET = TokenSet.create(
    METHOD_DEFINITION,
    FUNC_DEFINITION,
    SUB_EXPR,
    SUB_DEFINITION,
    SUB_DECLARATION
  );

  TokenSet VARIABLE_DECLARATIONS = TokenSet.create(
    VARIABLE_DECLARATION_GLOBAL,
    VARIABLE_DECLARATION_LEXICAL,
    VARIABLE_DECLARATION_LOCAL
  );

  TokenSet SUBS_OR_VARIABLES_DECLARATION = TokenSet.orSet(
    VARIABLE_DECLARATIONS, SUB_DEFINITIONS_TOKENSET
  );

  TokenSet SECONDARY_COMPOUND_TOKENSET = TokenSet.create(
    CONTINUE_BLOCK,
    RESERVED_ELSE,
    RESERVED_ELSIF,
    DEFAULT_COMPOUND,
    CATCH_EXPR,
    FINALLY_EXPR
  );

  TokenSet BLOCK_CONTAINERS_TOKENSET = TokenSet.create(
    CONDITIONAL_BLOCK,
    UNCONDITIONAL_BLOCK,
    FOR_COMPOUND,
    WHILE_COMPOUND,
    UNTIL_COMPOUND,
    GIVEN_COMPOUND,
    WHEN_COMPOUND,
    CONTINUE_BLOCK,
    DEFAULT_COMPOUND,

    TRY_EXPR,
    CATCH_EXPR,
    FINALLY_EXPR
  );

  TokenSet STATEMENT_MODIFIERS = TokenSet.create(
    IF_STATEMENT_MODIFIER,
    UNLESS_STATEMENT_MODIFIER,
    WHILE_STATEMENT_MODIFIER,
    UNTIL_STATEMENT_MODIFIER,
    FOR_STATEMENT_MODIFIER,
    WHEN_STATEMENT_MODIFIER
  );

  TokenSet RESERVED_CONDITIONAL_BRANCH_KEYWORDS = TokenSet.create(
    RESERVED_IF,
    RESERVED_UNLESS,
    RESERVED_ELSIF
  );

  TokenSet RESERVED_COMPOUND_CONDITIONAL = TokenSet.create(
    RESERVED_GIVEN,
    RESERVED_WHILE,
    RESERVED_UNTIL,
    RESERVED_WHEN,
    RESERVED_WHILE,

    // these are redundant here, but used in statement modifiers
    RESERVED_FOR,
    RESERVED_FOREACH
  );

  TokenSet RESERVED_TERMS_BLOCKS = TokenSet.create(
    RESERVED_DO,
    RESERVED_EVAL,
    RESERVED_SUB,
    RESERVED_MAP,
    RESERVED_GREP,
    RESERVED_SORT
  );


  TokenSet OPERATORS_BITWISE = TokenSet.create(
    OPERATOR_BITWISE_OR,
    OPERATOR_BITWISE_XOR,
    OPERATOR_BITWISE_AND
  );

  TokenSet OPERATORS_ADDITIVE = TokenSet.create(
    OPERATOR_PLUS,
    OPERATOR_MINUS
  );

  TokenSet OPERATORS_MULTIPLICATIVE = TokenSet.create(
    OPERATOR_MUL,
    OPERATOR_DIV,
    OPERATOR_POW,
    OPERATOR_MOD
  );

  TokenSet OPERATORS_SHIFT = TokenSet.create(
    OPERATOR_SHIFT_LEFT,
    OPERATOR_SHIFT_RIGHT
  );

  TokenSet OPERATORS_RANGE = TokenSet.create(
    OPERATOR_FLIP_FLOP,
    OPERATOR_HELLIP
  );

  TokenSet OPERATORS_UNARY = TokenSet.create(
    OPERATOR_PLUS_PLUS,
    OPERATOR_NOT,
    OPERATOR_MINUS_MINUS,
    OPERATOR_BITWISE_NOT
  );

  TokenSet OPERATORS_LOGICAL = TokenSet.create(
    OPERATOR_AND,
    OPERATOR_OR,
    OPERATOR_OR_DEFINED
  );

  TokenSet OPERATORS_EQUALITY = TokenSet.create(
    OPERATOR_EQ_NUMERIC,
    OPERATOR_NE_NUMERIC,
    OPERATOR_RE,
    OPERATOR_NOT_RE
  );

  TokenSet OPERATORS_RELATIONAL = TokenSet.create(
    OPERATOR_GE_NUMERIC,
    OPERATOR_LE_NUMERIC,
    OPERATOR_SMARTMATCH,
    OPERATOR_GT_NUMERIC,
    OPERATOR_LT_NUMERIC,
    OPERATOR_CMP_NUMERIC
  );

  TokenSet OPERATORS_STR = TokenSet.create(
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

  TokenSet BINARY_EXPRESSIONS = TokenSet.create(
    LP_OR_XOR_EXPR,
    LP_AND_EXPR,
    FLIPFLOP_EXPR,
    OR_EXPR,
    AND_EXPR,
    BITWISE_OR_XOR_EXPR,
    BITWISE_AND_EXPR,
    SHIFT_EXPR,
    ADD_EXPR,
    MUL_EXPR,
    POW_EXPR,
    EQUAL_EXPR,
    COMPARE_EXPR,
    REGEX_EXPR
  );

  TokenSet PARENTHESISED_LIKE_EXPRESSIONS = TokenSet.orSet(
    TokenSet.create(PARENTHESISED_EXPR),
    VARIABLE_DECLARATIONS
  );

  TokenSet OPERATORS_ASSIGNMENT = TokenSet.create(
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


  TokenSet BINARY_OPERATORS = TokenSet.orSet(
    OPERATORS_ADDITIVE,
    OPERATORS_BITWISE,
    OPERATORS_EQUALITY,
    OPERATORS_RELATIONAL,
    OPERATORS_LOGICAL,
    OPERATORS_RANGE,
    OPERATORS_MULTIPLICATIVE,
    OPERATORS_STR,
    OPERATORS_SHIFT
  );


  TokenSet RESERVED_VARIABLE_DECLARATION = TokenSet.create(
    RESERVED_MY,
    RESERVED_OUR,
    RESERVED_LOCAL,
    RESERVED_STATE
  );

  TokenSet LABEL_KEYWORDS = TokenSet.create(
    RESERVED_LAST,
    RESERVED_NEXT,
    RESERVED_REDO,
    RESERVED_GOTO
  );

  TokenSet CUSTOM_EXPR_KEYWORDS = TokenSet.create(
    RESERVED_GREP,
    RESERVED_MAP,
    RESERVED_SORT,

    RESERVED_SAY,
    RESERVED_PRINT,
    RESERVED_PRINTF,

    RESERVED_USE,
    RESERVED_NO,
    RESERVED_REQUIRE,

    RESERVED_UNDEF,
    RESERVED_RETURN
  );

  TokenSet QUOTE_LIKE_OPENERS = TokenSet.create(
    RESERVED_M,
    RESERVED_QR,
    RESERVED_S,
    RESERVED_TR,
    RESERVED_Y,
    RESERVED_Q,
    RESERVED_QQ,
    RESERVED_QX,
    RESERVED_QW,
    OPERATOR_HEREDOC
  );

  TokenSet VARIABLE_LEFT_BRACES = TokenSet.create(
    LEFT_BRACE_SCALAR,
    LEFT_BRACE_ARRAY,
    LEFT_BRACE_HASH,
    LEFT_BRACE_GLOB,
    LEFT_BRACE_CODE
  );

  TokenSet BLOCK_LEFT_BRACES = TokenSet.orSet(
    VARIABLE_LEFT_BRACES,
    TokenSet.create(LEFT_BRACE)
  );

  TokenSet VARIABLE_RIGHT_BRACES = TokenSet.create(
    RIGHT_BRACE_SCALAR,
    RIGHT_BRACE_ARRAY,
    RIGHT_BRACE_HASH,
    RIGHT_BRACE_GLOB,
    RIGHT_BRACE_CODE
  );

  TokenSet BLOCK_RIGHT_BRACES = TokenSet.orSet(
    VARIABLE_RIGHT_BRACES,
    TokenSet.create(RIGHT_BRACE)
  );
}
