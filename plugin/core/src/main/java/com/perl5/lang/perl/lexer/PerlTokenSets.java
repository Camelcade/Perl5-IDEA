/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.parser.PerlElementTypesGenerated;
import com.perl5.lang.perl.psi.utils.PerlAnnotations;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import static com.perl5.lang.perl.parser.MooseParserExtension.MOOSE_RESERVED_TOKENSET;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.NO_STATEMENT;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.USE_STATEMENT;


public final class PerlTokenSets {
  private PerlTokenSets() {
  }

  /**
   * Quote openers with three or four quotes
   */
  public static final TokenSet COMPLEX_QUOTE_OPENERS = TokenSet.create(RESERVED_S, RESERVED_TR, RESERVED_Y);

  public static final TokenSet SIMPLE_QUOTE_OPENERS =
    TokenSet.create(RESERVED_Q, RESERVED_QQ, RESERVED_QX, RESERVED_QW, RESERVED_QR, RESERVED_M);

  public static final TokenSet ALL_QUOTE_OPENERS = TokenSet.orSet(SIMPLE_QUOTE_OPENERS, COMPLEX_QUOTE_OPENERS);

  public static final TokenSet BITWISE_BINARY_OPERATORS_TOKENSET =
    TokenSet.create(OPERATOR_BITWISE_OR, OPERATOR_BITWISE_XOR, OPERATOR_BITWISE_AND);

  public static final TokenSet BITWISE_OPERATORS_TOKENSET = TokenSet.orSet(
    BITWISE_BINARY_OPERATORS_TOKENSET,
    TokenSet.create(OPERATOR_BITWISE_NOT)
  );

  public static final TokenSet BITWISE_ASSIGN_OPERATORS_TOKENSET = TokenSet.create(
    OPERATOR_BITWISE_AND_ASSIGN, OPERATOR_BITWISE_OR_ASSIGN, OPERATOR_BITWISE_XOR_ASSIGN);

  public static final TokenSet OPERATORS_TOKENSET = TokenSet.orSet(
    BITWISE_OPERATORS_TOKENSET,
    BITWISE_ASSIGN_OPERATORS_TOKENSET,
    TokenSet.create(
      OPERATOR_CMP_NUMERIC,
      OPERATOR_LT_NUMERIC,
      OPERATOR_GT_NUMERIC,

      OPERATOR_ISA,

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

      //			OPERATOR_HEREDOC, // this is an artificial operator, not the real one; fixme uncommenting breaks parsing of print $of <<EOM
      OPERATOR_SHIFT_LEFT,
      OPERATOR_SHIFT_RIGHT,

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


      OPERATOR_AND_LP,
      OPERATOR_OR_LP,
      OPERATOR_XOR_LP,
      OPERATOR_NOT_LP,

      COMMA,
      FAT_COMMA,

      OPERATOR_DEREFERENCE,

      OPERATOR_X,
      OPERATOR_FILETEST,

      // syntax operators
      OPERATOR_POW_ASSIGN,
      OPERATOR_PLUS_ASSIGN,
      OPERATOR_MINUS_ASSIGN,
      OPERATOR_MUL_ASSIGN,
      OPERATOR_DIV_ASSIGN,
      OPERATOR_MOD_ASSIGN,
      OPERATOR_CONCAT_ASSIGN,
      OPERATOR_X_ASSIGN,
      OPERATOR_SHIFT_LEFT_ASSIGN,
      OPERATOR_SHIFT_RIGHT_ASSIGN,
      OPERATOR_AND_ASSIGN,
      OPERATOR_OR_ASSIGN,
      OPERATOR_OR_DEFINED_ASSIGN,

      OPERATOR_GE_NUMERIC,
      OPERATOR_LE_NUMERIC,
      OPERATOR_EQ_NUMERIC,
      OPERATOR_NE_NUMERIC,
      OPERATOR_SMARTMATCH
    ));

  public static final TokenSet FUNCTION_LIKE_EXPR = TokenSet.create(
    GREP_EXPR,
    MAP_EXPR,
    SORT_EXPR,
    EXIT_EXPR,
    SCALAR_EXPR,
    KEYS_EXPR,
    VALUES_EXPR,
    REQUIRE_EXPR,
    UNDEF_EXPR,
    EACH_EXPR,
    DEFINED_EXPR,
    WANTARRAY_EXPR,
    DELETE_EXPR,
    SPLICE_EXPR,
    BLESS_EXPR,
    ARRAY_UNSHIFT_EXPR,
    ARRAY_PUSH_EXPR,
    ARRAY_SHIFT_EXPR,
    ARRAY_POP_EXPR
  );

  public static final TokenSet CUSTOM_EXPR_KEYWORDS = TokenSet.create(
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
    RESERVED_RETURN,
    RESERVED_EXIT,

    RESERVED_SHIFT,
    RESERVED_UNSHIFT,
    RESERVED_PUSH,
    RESERVED_POP,

    RESERVED_SCALAR,
    RESERVED_KEYS,
    RESERVED_VALUES,
    RESERVED_EACH,
    RESERVED_DEFINED,
    RESERVED_WANTARRAY,
    RESERVED_DELETE,
    RESERVED_SPLICE,
    RESERVED_BLESS
  );

  public static final TokenSet MODIFIERS_KEYWORDS_TOKENSET = TokenSet.create(
    RESERVED_IF,
    RESERVED_UNTIL,
    RESERVED_UNLESS,
    RESERVED_FOR,
    RESERVED_FOREACH,
    RESERVED_WHEN,
    RESERVED_WHILE
  );

  public static final TokenSet TAGS_TOKEN_SET = TokenSet.create(TAG_DATA, TAG_END, TAG, TAG_PACKAGE);

  public static final TokenSet COMPOUND_KEYWORDS_TOKENSET = TokenSet.orSet(
    MODIFIERS_KEYWORDS_TOKENSET,
    TokenSet.create(RESERVED_ELSIF, RESERVED_ELSE, RESERVED_GIVEN)
  );

  public static final TokenSet SWITCH_KEYWORDS_TOKENSET = TokenSet.create(RESERVED_GIVEN, RESERVED_WHEN, RESERVED_DEFAULT);

  public static final TokenSet SUB_MODIFIERS = TokenSet.create(RESERVED_MY, RESERVED_OUR, RESERVED_STATE, RESERVED_ASYNC);

  public static final TokenSet DEFAULT_KEYWORDS_TOKENSET = TokenSet.orSet(
    ALL_QUOTE_OPENERS,
    CUSTOM_EXPR_KEYWORDS,
    COMPOUND_KEYWORDS_TOKENSET,
    MODIFIERS_KEYWORDS_TOKENSET,
    SWITCH_KEYWORDS_TOKENSET,
    TokenSet.create(
      RESERVED_MY,
      RESERVED_FIELD,
      RESERVED_OUR,
      RESERVED_STATE,
      RESERVED_LOCAL,
      RESERVED_DEFAULT,
      RESERVED_CONTINUE,
      RESERVED_FORMAT,
      RESERVED_SUB,
      RESERVED_ASYNC,
      RESERVED_PACKAGE,
      RESERVED_CLASS,
      RESERVED_DO,
      RESERVED_EVAL,
      RESERVED_GOTO,
      RESERVED_REDO,
      RESERVED_NEXT,
      RESERVED_LAST,
      RESERVED_EXIT
    ));

  public static final TokenSet TRY_CATCH_KEYWORDS_TOKENSET = TokenSet.create(
    RESERVED_TRY,
    RESERVED_CATCH,
    RESERVED_FINALLY,
    RESERVED_CATCH_WITH,
    RESERVED_EXCEPT,
    RESERVED_OTHERWISE,
    RESERVED_CONTINUATION
  );

  public static final TokenSet METHOD_SIGNATURES_KEYWORDS_TOKENSET = TokenSet.create(RESERVED_METHOD, RESERVED_FUNC);

  public static final TokenSet FUNCTION_PARAMETERS_KEYWORDS_TOKENSET = TokenSet.create(
    RESERVED_AFTER_FP,
    RESERVED_BEFORE_FP,
    RESERVED_AROUND_FP,
    RESERVED_AUGMENT_FP,
    RESERVED_OVERRIDE_FP,
    RESERVED_METHOD_FP,
    RESERVED_FUN
  );

  public static final TokenSet KEYWORDS_TOKENSET = TokenSet.orSet(
    DEFAULT_KEYWORDS_TOKENSET,
    MOOSE_RESERVED_TOKENSET,
    METHOD_SIGNATURES_KEYWORDS_TOKENSET,
    FUNCTION_PARAMETERS_KEYWORDS_TOKENSET,
    TRY_CATCH_KEYWORDS_TOKENSET
  );

  public static final TokenSet ANNOTATIONS_KEYS = TokenSet.create(PerlAnnotations.TOKENS_MAP.values().toArray(IElementType.EMPTY_ARRAY));

  public static final TokenSet STRING_CONTENT_TOKENSET = TokenSet.create(STRING_CONTENT, STRING_CONTENT_XQ, STRING_CONTENT_QQ);

  public static final TokenSet HEREDOC_BODIES_TOKENSET = TokenSet.create(HEREDOC, HEREDOC_QQ, HEREDOC_QX);

  public static final TokenSet QUOTE_MIDDLE = TokenSet.create(REGEX_QUOTE, REGEX_QUOTE_E);

  public static final TokenSet REGEX_QUOTE_OPEN = TokenSet.create(PerlElementTypesGenerated.REGEX_QUOTE_OPEN, REGEX_QUOTE_OPEN_E);

  public static final TokenSet OPEN_QUOTES = TokenSet.create(QUOTE_DOUBLE_OPEN, QUOTE_TICK_OPEN, QUOTE_SINGLE_OPEN);

  public static final TokenSet QUOTE_OPEN_ANY = TokenSet.orSet(REGEX_QUOTE_OPEN, OPEN_QUOTES, QUOTE_MIDDLE);

  public static final TokenSet CLOSE_QUOTES = TokenSet.create(QUOTE_DOUBLE_CLOSE, QUOTE_TICK_CLOSE, QUOTE_SINGLE_CLOSE);

  public static final TokenSet QUOTE_CLOSE_FIRST_ANY = TokenSet.orSet(
    TokenSet.create(REGEX_QUOTE_CLOSE),
    QUOTE_MIDDLE,
    CLOSE_QUOTES
  );

  public static final TokenSet QUOTE_CLOSE_PAIRED = TokenSet.orSet(
    CLOSE_QUOTES,
    TokenSet.create(REGEX_QUOTE_CLOSE)
  );

  public static final TokenSet SIGILS = TokenSet.create(SIGIL_SCALAR, SIGIL_ARRAY, SIGIL_HASH, SIGIL_GLOB, SIGIL_CODE, SIGIL_SCALAR_INDEX);

  public static final TokenSet STATEMENTS = TokenSet.create(STATEMENT, USE_STATEMENT, NO_STATEMENT);

  public static final TokenSet TRANSPARENT_ELEMENT_TYPES = TokenSet.create(PARSABLE_STRING_USE_VARS);

  public static final TokenSet BLOCK_LIKE_CONTAINERS = TokenSet.create(
    BLOCK, BLOCK_SCALAR, BLOCK_ARRAY, BLOCK_HASH, BLOCK_GLOB, BLOCK_CODE, BLOCK_BRACELESS
  );

  public static final TokenSet HEREDOC_ENDS = TokenSet.create(HEREDOC_END, HEREDOC_END_INDENTABLE);

  public static final TokenSet LOOP_CONTROL_KEYWORDS = TokenSet.create(RESERVED_NEXT, RESERVED_LAST, RESERVED_REDO);

  public static final TokenSet CAST_EXPRESSIONS = TokenSet.create(
    ARRAY_CAST_EXPR, CODE_CAST_EXPR, GLOB_CAST_EXPR, HASH_CAST_EXPR, SCALAR_INDEX_CAST_EXPR, SCALAR_CAST_EXPR);

  public static final TokenSet SLICES = TokenSet.create(HASH_SLICE, ARRAY_SLICE);

  public static final TokenSet REGEX_OPERATIONS = TokenSet.create(REPLACEMENT_REGEX, COMPILE_REGEX, MATCH_REGEX);

  public static final TokenSet VARIABLES =
    TokenSet.create(SCALAR_VARIABLE, ARRAY_VARIABLE, HASH_VARIABLE, CODE_VARIABLE, GLOB_VARIABLE, ARRAY_INDEX_VARIABLE);

  public static final TokenSet QUOTED_STRINGS = TokenSet.create(STRING_DQ, STRING_XQ, STRING_SQ);

  public static final TokenSet STRINGS = TokenSet.orSet(QUOTED_STRINGS, TokenSet.create(STRING_BARE));

  public static final TokenSet VARIABLE_DECLARATIONS =
    TokenSet.create(VARIABLE_DECLARATION_GLOBAL, VARIABLE_DECLARATION_LEXICAL, VARIABLE_DECLARATION_LOCAL);

  public static final TokenSet MODIFIER_DECLARATIONS_TOKENSET =
    TokenSet.create(AFTER_MODIFIER, BEFORE_MODIFIER, AROUND_MODIFIER, AUGMENT_MODIFIER);

  public static final TokenSet VARIABLE_NAMES = TokenSet.create(SCALAR_NAME, ARRAY_NAME, HASH_NAME, GLOB_NAME);

  public static final TokenSet VARIABLE_OPEN_BRACES =
    TokenSet.create(LEFT_BRACE_SCALAR, LEFT_BRACE_ARRAY, LEFT_BRACE_HASH, LEFT_BRACE_GLOB, LEFT_BRACE_CODE);

  public static final TokenSet VARIABLE_CLOSE_BRACES = TokenSet.create(
    RIGHT_BRACE_SCALAR, RIGHT_BRACE_ARRAY, RIGHT_BRACE_HASH, RIGHT_BRACE_GLOB, RIGHT_BRACE_CODE);

  public static final TokenSet PRE_VARIABLE_NAME_TOKENS = TokenSet.orSet(SIGILS, VARIABLE_OPEN_BRACES);

  public static final TokenSet UNCHAINABLE_OPERATORS = TokenSet.create(OPERATOR_CMP_NUMERIC, OPERATOR_CMP_STR, OPERATOR_SMARTMATCH);

  public static final TokenSet STRING_CHAR_UNRENDERABLE_ALIASES = TokenSet.create(
    STRING_SPECIAL_FORMFEED,
    STRING_SPECIAL_BACKSPACE,
    STRING_SPECIAL_ALARM,
    STRING_SPECIAL_ESCAPE,
    STRING_SPECIAL_RANGE
  );

  public static final TokenSet STRING_CHAR_SIMPLE_ALIASES = TokenSet.orSet(STRING_CHAR_UNRENDERABLE_ALIASES, TokenSet.create(
    STRING_SPECIAL_TAB,
    STRING_SPECIAL_NEWLINE,
    STRING_SPECIAL_RETURN
  ));

  public static final TokenSet STRING_CHAR_OPERATORS = TokenSet.create(
    STRING_SPECIAL_LCFIRST,
    STRING_SPECIAL_TCFIRST,
    STRING_SPECIAL_LOWERCASE_START,
    STRING_SPECIAL_UPPERCASE_START,
    STRING_SPECIAL_FOLDCASE_START,
    STRING_SPECIAL_QUOTE_START,
    STRING_SPECIAL_MODIFIER_END
  );

  // these tokens are highlighted as special chars. Missing some stuff, like back-references, highlighted separately
  public static final TokenSet SPECIAL_STRING_TOKENS = TokenSet.orSet(
    STRING_CHAR_SIMPLE_ALIASES,
    STRING_CHAR_OPERATORS,
    TokenSet.create(
      STRING_SPECIAL_ESCAPE_CHAR,
      STRING_SPECIAL_SUBST,

      STRING_SPECIAL_HEX,
      STRING_SPECIAL_OCT,
      STRING_SPECIAL_OCT_AMBIGUOUS,

      STRING_SPECIAL_UNICODE,
      STRING_SPECIAL_UNICODE_CODE_PREFIX,

      STRING_SPECIAL_LEFT_BRACE,
      STRING_SPECIAL_RIGHT_BRACE
    ));

  public static final TokenSet PERL_PARAMETRIZED_STRING_SUBSTITUTIONS = TokenSet.create(UNICODE_CHAR, HEX_CHAR, OCT_CHAR);

  public static final TokenSet SUB_DEFINITIONS_TOKENSET = TokenSet.create(METHOD_DEFINITION, FUNC_DEFINITION, SUB_DEFINITION);

  public static final TokenSet COMPOUND_STATEMENTS_TOKENSET = TokenSet.orSet(
    MODIFIER_DECLARATIONS_TOKENSET,
    SUB_DEFINITIONS_TOKENSET,
    TokenSet.create(
      BLOCK_COMPOUND, NAMED_BLOCK,
      IF_COMPOUND, UNLESS_COMPOUND,
      GIVEN_COMPOUND, WHEN_COMPOUND, DEFAULT_COMPOUND,
      TRYCATCH_COMPOUND,
      SWITCH_COMPOUND, CASE_COMPOUND, CASE_DEFAULT,
      WHILE_COMPOUND, UNTIL_COMPOUND,
      FOR_COMPOUND, FOREACH_COMPOUND
    )
  );

  public static final TokenSet ELEMENTS_WITH_CUSTOM_DELIMITERS = TokenSet.orSet(
    REGEX_OPERATIONS, QUOTED_STRINGS, TokenSet.create(STRING_LIST, TR_REGEX)
  );

  public static final TokenSet PACKAGE_LIKE_TOKENS = TokenSet.create(PACKAGE, QUALIFYING_PACKAGE);
}
