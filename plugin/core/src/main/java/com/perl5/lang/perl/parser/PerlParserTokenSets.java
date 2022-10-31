/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;
import static com.perl5.lang.perl.lexer.PerlTokenSets.HEREDOC_BODIES_TOKENSET;


public final class PerlParserTokenSets {
  // these tokens are not being marked as bad characters
  public static final TokenSet BAD_CHARACTER_FORBIDDEN_TOKENS = TokenSet.create(
    RESERVED_PACKAGE,
    RIGHT_BRACE,
    RIGHT_BRACE_SCALAR,
    RIGHT_BRACE_ARRAY,
    RIGHT_BRACE_HASH,
    RIGHT_BRACE_GLOB,
    RIGHT_BRACE_CODE,
    REGEX_QUOTE_CLOSE,
    SEMICOLON
  );

  // Contains tokens that can be freely consumed during statement recovery
  public static final TokenSet STATEMENT_RECOVERY_CONSUMABLE_TOKENS = TokenSet.orSet(
    HEREDOC_BODIES_TOKENSET,
    TokenSet.create(
      COLON,
      COMMENT_ANNOTATION,
      COMMENT_BLOCK,
      COMMENT_LINE,
      FORMAT,
      FORMAT_TERMINATOR,
      HANDLE,
      HEREDOC_END,
      HEREDOC_END_INDENTABLE,
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
      SUB_PROTOTYPE_TOKEN,
      VERSION_ELEMENT
    ));


  // Tokens which consumed and counted as semicolon
  public static final TokenSet CONSUMABLE_SEMI_TOKENS = TokenSet.create(
    SEMICOLON
  );

  // Tokens which makes semicolon optional, like block close brace
  public static final TokenSet UNCONSUMABLE_SEMI_TOKENS = TokenSet.create(
    RIGHT_BRACE,
    REGEX_QUOTE_CLOSE,
    RIGHT_BRACE_SCALAR,
    RIGHT_BRACE_HASH,
    RIGHT_BRACE_ARRAY,
    RIGHT_BRACE_GLOB,
    RIGHT_BRACE_CODE,
    TAG_END,
    TAG_DATA
  );
}
