/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

import com.intellij.formatting.SpacingBuilder;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.idea.formatter.PerlFormattingTokenSets.*;
import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.SAME_LINE;
import static com.perl5.lang.perl.lexer.PerlTokenSets.*;
import static com.perl5.lang.perl.parser.MooseParserExtension.MOOSE_RESERVED_TOKENSET;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.NO_STATEMENT;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.USE_STATEMENT;

public class PerlSpacingBuilderFactory {
  static @NotNull SpacingBuilder createSpacingBuilder(CommonCodeStyleSettings settings, PerlCodeStyleSettings perlSettings) {
    return new SpacingBuilder(settings)
      .beforeInside(COMMENT_LINE, COMMENT_ANNOTATION).spaces(1)
      .before(COMMENT_LINE).spaceIf(perlSettings.ALIGN_COMMENTS_ON_CONSEQUENT_LINES)
      .after(COMMENT_LINE).lineBreakOrForceSpace(true, false)
      // standard settings
      .around(OPERATOR_X).spaces(1)
      .before(OPERATOR_X_ASSIGN).spaces(1)
      .around(OPERATORS_ASSIGNMENT).spaceIf(settings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
      .around(OPERATORS_EQUALITY).spaceIf(settings.SPACE_AROUND_EQUALITY_OPERATORS)
      .around(OPERATORS_RELATIONAL).spaceIf(settings.SPACE_AROUND_RELATIONAL_OPERATORS)
      .around(OPERATORS_LOGICAL).spaceIf(settings.SPACE_AROUND_LOGICAL_OPERATORS)
      .around(BITWISE_BINARY_OPERATORS_TOKENSET).spaceIf(settings.SPACE_AROUND_BITWISE_OPERATORS)
      .after(OPERATOR_REFERENCE).spaces(0)

      .after(ANNOTATIONS_KEYS).spaces(1)
      .after(ANNOTATION_VARIABLE).spaces(1)
      .aroundInside(LEFT_BRACKET, COMPOSITE_TYPES).spaces(0)
      .aroundInside(RIGHT_BRACKET, COMPOSITE_TYPES).spaces(0)

      .between(FUNCTION_PARAMETERS_KEYWORDS_TOKENSET, SUB_NAME).spacing(1, 1, 0, false, 0)

      .afterInside(OPERATOR_MINUS, PREFIX_UNARY_EXPR).spaceIf(settings.SPACE_AROUND_UNARY_OPERATOR)
      .afterInside(OPERATOR_PLUS, PREFIX_UNARY_EXPR).spaceIf(settings.SPACE_AROUND_UNARY_OPERATOR)

      .around(OPERATORS_ADDITIVE).spaceIf(settings.SPACE_AROUND_ADDITIVE_OPERATORS)
      .around(OPERATORS_MULTIPLICATIVE).spaceIf(settings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS)
      .around(OPERATORS_SHIFT).spaceIf(settings.SPACE_AROUND_SHIFT_OPERATORS)
      .around(OPERATORS_UNARY).spaceIf(settings.SPACE_AROUND_UNARY_OPERATOR)
      .aroundInside(OPERATORS_RANGE, FLIPFLOP_EXPR).spaceIf(perlSettings.SPACE_AROUND_RANGE_OPERATORS)

      .between(RESERVED_PACKAGE, PACKAGE).spaces(1)
      .between(RESERVED_CLASS, PACKAGE).spaces(1)
      .between(PACKAGE, VERSION_ELEMENT).spaces(1)
      .between(ATTRIBUTES, VERSION_ELEMENT).spaces(1)
      .between(RESERVED_FORMAT, SUB_NAME).spaces(1)
      .betweenInside(SUB_NAME, PACKAGE, METHOD).spaces(1)

      .between(ATTRIBUTES, LEFT_PAREN).spaces(1)
      .beforeInside(LEFT_PAREN, SUB_OR_MODIFIER_DEFINITIONS_TOKENSET).spacing(
        settings.SPACE_BEFORE_METHOD_PARENTHESES ? 1 : 0,
        settings.SPACE_BEFORE_METHOD_PARENTHESES ? 1 : 0,
        0,
        false,
        0
      )
      .after(TYPE_SPECIFIER).spacing(1, 1, 0, false, 0)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, SUB_DEFINITION).spaceIf(settings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, SUB_DECLARATION).spaceIf(settings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, METHOD_DEFINITION).spaceIf(settings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, FUNC_DEFINITION).spaceIf(settings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, SUB_EXPR).spaceIf(settings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, FUN_EXPR).spaceIf(settings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, METHOD_EXPR).spaceIf(settings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, AFTER_MODIFIER).spaceIf(settings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, BEFORE_MODIFIER).spaceIf(settings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, AROUND_MODIFIER).spaceIf(settings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, AUGMENT_MODIFIER).spaceIf(settings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)

      .afterInside(LEFT_PAREN, SUB_OR_MODIFIER_DEFINITIONS_TOKENSET).spaceIf(settings.SPACE_WITHIN_METHOD_PARENTHESES)
      .beforeInside(RIGHT_PAREN, SUB_OR_MODIFIER_DEFINITIONS_TOKENSET).spaceIf(settings.SPACE_WITHIN_METHOD_PARENTHESES)

      .before(ATTRIBUTES).spaceIf(perlSettings.SPACE_BEFORE_ATTRIBUTE)
      .between(INVOCANTS_TOKENSET, COLON).spaces(1)
      .between(INVOCANTS_TOKENSET, SIGNATURE_ELEMENT).spaces(1)
      .betweenInside(COLON, VARIABLE_DECLARATION_ELEMENT, SIGNATURE_ELEMENT).spacing(0, 0, 0, false, 0)
      .beforeInside(COLON, INVOCANTS_TOKENSET).spacing(0, 0, 0, false, 0)
      .beforeInside(COLON, ATTRIBUTES).spaceIf(perlSettings.SPACE_BEFORE_ATTRIBUTE)
      .between(COLON, ATTRIBUTE).spacing(0, 0, 0, false, 0)
      .between(ATTRIBUTE, ATTRIBUTE).spaces(1)

      .between(SUB_MODIFIERS, RESERVED_SUB).spaces(1)
      .afterInside(RESERVED_SUB, SUB_DEFINITION).spacing(1, 1, 0, false, 0)
      .afterInside(RESERVED_SUB, SUB_DECLARATION).spacing(1, 1, 0, false, 0)
      .between(RESERVED_ASYNC, RESERVED_METHOD).spacing(1, 1, 0, false, 0)
      .afterInside(RESERVED_METHOD, METHOD_DEFINITION).spacing(1, 1, 0, false, 0)
      .afterInside(RESERVED_FUNC, FUNC_DEFINITION).spacing(1, 1, 0, false, 0)

      .between(NUMBER_CONSTANT, OPERATOR_CONCAT).spaces(1)
      .aroundInside(OPERATOR_CONCAT, ADD_EXPR).spaceIf(perlSettings.SPACE_AROUND_CONCAT_OPERATOR)

      .betweenInside(STRING_BARE, STRING_BARE, STRING_LIST).spaces(1)

      .betweenInside(QUOTE_SINGLE_OPEN, QUOTE_SINGLE_CLOSE, STRING_LIST).spaces(0)
      .afterInside(QUOTE_SINGLE_OPEN, STRING_LIST).spaceIf(perlSettings.SPACE_WITHIN_QW_QUOTES)
      .beforeInside(QUOTE_SINGLE_CLOSE, STRING_LIST).spaceIf(perlSettings.SPACE_WITHIN_QW_QUOTES)

      .betweenInside(LEFT_PAREN, RIGHT_PAREN, PARENTHESISED_EXPR).spaces(0)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, VARIABLE_DECLARATION_GLOBAL).spaces(0)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, VARIABLE_DECLARATION_LEXICAL).spaces(0)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, VARIABLE_DECLARATION_LOCAL).spaces(0)
      .afterInside(LEFT_PAREN, PARENTHESISED_LIKE_EXPRESSIONS).spaceIf(settings.SPACE_WITHIN_PARENTHESES)
      .beforeInside(RIGHT_PAREN, PARENTHESISED_LIKE_EXPRESSIONS).spaceIf(settings.SPACE_WITHIN_PARENTHESES)

      .betweenInside(BLOCK_LEFT_BRACES, BLOCK_RIGHT_BRACES, BLOCK).spaces(0)
      .afterInside(BLOCK_LEFT_BRACES, BLOCK).spaceIf(settings.SPACE_WITHIN_BRACES)
      .beforeInside(BLOCK_RIGHT_BRACES, BLOCK).spaceIf(settings.SPACE_WITHIN_BRACES)

      .after(VARIABLE_LEFT_BRACES).spaces(0)
      .before(VARIABLE_RIGHT_BRACES).spaces(0)

      .betweenInside(LEFT_BRACE, RIGHT_BRACE, ANON_HASH).spaces(0)
      .afterInside(LEFT_BRACE, ANON_HASH).spaceIf(perlSettings.SPACES_WITHIN_ANON_HASH)
      .beforeInside(RIGHT_BRACE, ANON_HASH).spaceIf(perlSettings.SPACES_WITHIN_ANON_HASH)

      .betweenInside(LEFT_BRACKET, RIGHT_BRACKET, ANON_ARRAY).spaces(0)
      .afterInside(LEFT_BRACKET, ANON_ARRAY).spaceIf(perlSettings.SPACES_WITHIN_ANON_ARRAY)
      .beforeInside(RIGHT_BRACKET, ANON_ARRAY).spaceIf(perlSettings.SPACES_WITHIN_ANON_ARRAY)

      .around(OPERATOR_DEREFERENCE).spaceIf(perlSettings.SPACE_AROUND_DEREFERENCE)
      .around(ARRAY_INDEX).spaces(0)
      .around(HASH_INDEX).spaces(0)

      .before(CALL_ARGUMENTS).spaces(1)
      .before(PARENTHESISED_CALL_ARGUMENTS).spaces(0)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, PARENTHESISED_CALL_ARGUMENTS).spaces(0)
      .afterInside(LEFT_PAREN, PARENTHESISED_CALL_ARGUMENTS).spaceIf(settings.SPACE_WITHIN_METHOD_CALL_PARENTHESES)
      .beforeInside(RIGHT_PAREN, PARENTHESISED_CALL_ARGUMENTS).spaceIf(settings.SPACE_WITHIN_METHOD_CALL_PARENTHESES)

      .afterInside(LEFT_PAREN, FUNCTION_LIKE_EXPR).spaceIf(settings.SPACE_WITHIN_METHOD_CALL_PARENTHESES)
      .beforeInside(RIGHT_PAREN, FUNCTION_LIKE_EXPR).spaceIf(settings.SPACE_WITHIN_METHOD_CALL_PARENTHESES)

      .beforeInside(COMMA, SIGNATURE_CONTENT).spacing(
        (settings.SPACE_BEFORE_COMMA ? 1 : 0),
        (settings.SPACE_BEFORE_COMMA ? 1 : 0),
        0,
        false,
        0
      )
      .beforeInside(COMMA, AROUND_SIGNATURE_INVOCANTS).spacing(
        (settings.SPACE_BEFORE_COMMA ? 1 : 0),
        (settings.SPACE_BEFORE_COMMA ? 1 : 0),
        0,
        false,
        0
      )
      .before(COMMA).spaceIf(settings.SPACE_BEFORE_COMMA)
      .after(COMMA).spaceIf(settings.SPACE_AFTER_COMMA)

      .afterInside(BLOCK, MULTI_PARAM_BLOCK_CONTAINERS).spaces(1)
      .afterInside(SUB_EXPR, SORT_EXPR).spaces(1)

      .beforeInside(COLON, TERNARY_EXPR).spaceIf(settings.SPACE_BEFORE_COLON)
      .afterInside(COLON, TERNARY_EXPR).spaceIf(settings.SPACE_AFTER_COLON)
      .beforeInside(QUESTION, TERNARY_EXPR).spaceIf(settings.SPACE_BEFORE_QUEST)
      .afterInside(QUESTION, TERNARY_EXPR).spaceIf(settings.SPACE_AFTER_QUEST)

      .betweenInside(LEFT_PAREN, SEMICOLON, FOR_COMPOUND).spaceIf(settings.SPACE_WITHIN_IF_PARENTHESES)
      .betweenInside(SEMICOLON, RIGHT_PAREN, FOR_COMPOUND).spaceIf(settings.SPACE_WITHIN_IF_PARENTHESES)

      .betweenInside(SEMICOLON, SEMICOLON, FOR_COMPOUND).spaces(0)
      .afterInside(SEMICOLON, FOR_COMPOUND).spaceIf(settings.SPACE_AFTER_SEMICOLON)
      .beforeInside(SEMICOLON, FOR_COMPOUND).spaceIf(settings.SPACE_BEFORE_SEMICOLON)

      .between(RESERVED_TERMS_BLOCKS, BLOCK).spaceIf(settings.SPACE_BEFORE_DO_LBRACE)
      .between(RESERVED_TERMS_BLOCKS, SUB_EXPR).spaceIf(settings.SPACE_BEFORE_DO_LBRACE)

      .between(RESERVED_CONDITIONAL_BRANCH_KEYWORDS, CONDITIONAL_BLOCK).spaceIf(settings.SPACE_BEFORE_IF_PARENTHESES)
      .between(RESERVED_COMPOUND_CONDITIONAL, CONDITION_EXPR).spaceIf(settings.SPACE_BEFORE_IF_PARENTHESES)
      .between(FOR_OR_FOREACH, LEFT_PAREN).spaceIf(settings.SPACE_BEFORE_IF_PARENTHESES)
      .between(FOR_OR_FOREACH, CONDITION_EXPR).spaceIf(settings.SPACE_BEFORE_IF_PARENTHESES)
      .between(FOREACH_ITERATOR, CONDITION_EXPR).spaces(1)
      .afterInside(FOR_OR_FOREACH, FOR_COMPOUND).spaces(1)
      .afterInside(FOR_OR_FOREACH, FOREACH_COMPOUND).spaces(1)

      .betweenInside(RESERVED_IF, PARENTHESISED_EXPR, IF_STATEMENT_MODIFIER).spaceIf(settings.SPACE_BEFORE_IF_PARENTHESES)
      .betweenInside(RESERVED_UNLESS, PARENTHESISED_EXPR, UNLESS_STATEMENT_MODIFIER).spaceIf(settings.SPACE_BEFORE_IF_PARENTHESES)
      .betweenInside(RESERVED_WHILE, PARENTHESISED_EXPR, WHILE_STATEMENT_MODIFIER).spaceIf(settings.SPACE_BEFORE_IF_PARENTHESES)
      .betweenInside(RESERVED_UNTIL, PARENTHESISED_EXPR, UNTIL_STATEMENT_MODIFIER).spaceIf(settings.SPACE_BEFORE_IF_PARENTHESES)
      .betweenInside(RESERVED_FOR, PARENTHESISED_EXPR, FOR_STATEMENT_MODIFIER).spaceIf(settings.SPACE_BEFORE_IF_PARENTHESES)
      .betweenInside(RESERVED_FOREACH, PARENTHESISED_EXPR, FOR_STATEMENT_MODIFIER).spaceIf(settings.SPACE_BEFORE_IF_PARENTHESES)
      .betweenInside(RESERVED_WHEN, PARENTHESISED_EXPR, WHEN_STATEMENT_MODIFIER).spaceIf(settings.SPACE_BEFORE_IF_PARENTHESES)

      .afterInside(RESERVED_IF, IF_STATEMENT_MODIFIER).spaces(1)
      .afterInside(RESERVED_UNLESS, UNLESS_STATEMENT_MODIFIER).spaces(1)
      .afterInside(RESERVED_WHILE, WHILE_STATEMENT_MODIFIER).spaces(1)
      .afterInside(RESERVED_UNTIL, UNTIL_STATEMENT_MODIFIER).spaces(1)
      .afterInside(FOR_OR_FOREACH, FOR_STATEMENT_MODIFIER).spaces(1)
      .afterInside(RESERVED_WHEN, WHEN_STATEMENT_MODIFIER).spaces(1)

      .between(LABEL_KEYWORDS, SEMICOLON).spaces(0)
      .after(LABEL_KEYWORDS).spaces(1)

      .afterInside(PERL_HANDLE_EXPR, COMMA_SEQUENCE_EXPR).spaces(1)

      .between(CUSTOM_EXPR_KEYWORDS, SEMICOLON).spaces(0)
      .between(RESERVED_RETURN, PARENTHESISED_EXPR).spaces(1)
      .between(CUSTOM_EXPR_KEYWORDS, PARENTHESISED_EXPR).spaces(0)
      .between(CUSTOM_EXPR_KEYWORDS, LEFT_PAREN).spaces(0)
      .after(CUSTOM_EXPR_KEYWORDS).spaces(1)
      .after(MOOSE_RESERVED_TOKENSET.get()).spaces(1)

      .between(OPERATOR_HEREDOC, STRING_BARE).spaces(0)
      .after(OPERATOR_HEREDOC).spaceIf(perlSettings.SPACE_AFTER_HEREDOC_OPERATOR)

      .betweenInside(PACKAGE, SEMICOLON, USE_STATEMENT).spaces(0)
      .betweenInside(VERSION_ELEMENT, SEMICOLON, USE_STATEMENT).spaces(0)
      .afterInside(PACKAGE, USE_STATEMENT).spaces(1)
      .afterInside(VERSION_ELEMENT, USE_STATEMENT).spaces(1)

      .betweenInside(PACKAGE, SEMICOLON, NO_STATEMENT).spaces(0)
      .betweenInside(VERSION_ELEMENT, SEMICOLON, NO_STATEMENT).spaces(0)
      .afterInside(PACKAGE, NO_STATEMENT).spaces(1)
      .afterInside(VERSION_ELEMENT, NO_STATEMENT).spaces(1)

      .beforeInside(BLOCK, SUB_OR_MODIFIER_DEFINITIONS_TOKENSET).spacing(
        settings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        settings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        perlSettings.BRACE_STYLE_SUB == SAME_LINE ? 0 : 1,
        false,
        0
      )

      .beforeInside(BLOCK, NAMESPACE_DEFINITION).spacing(
        settings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        settings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        perlSettings.BRACE_STYLE_NAMESPACE == SAME_LINE ? 0 : 1,
        false,
        0
      )

      .beforeInside(BLOCK, BLOCK_CONTAINERS_TOKENSET).spacing(
        settings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        settings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        perlSettings.BRACE_STYLE_COMPOUND == SAME_LINE ? 0 : 1,
        false,
        0
      )
      .beforeInside(SUB_EXPR, BLOCK_CONTAINERS_TOKENSET).spacing(
        settings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        settings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        perlSettings.BRACE_STYLE_COMPOUND == SAME_LINE ? 0 : 1,
        false,
        0
      )
      .between(RESERVED_ELSE, UNCONDITIONAL_BLOCK).spacing(
        settings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        settings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        perlSettings.BRACE_STYLE_COMPOUND == SAME_LINE ? 0 : 1,
        false,
        0
      )

      .afterInside(LEFT_PAREN, CONDITION_LIKE_ELEMENTS).spaceIf(settings.SPACE_WITHIN_IF_PARENTHESES)
      .beforeInside(RIGHT_PAREN, CONDITION_LIKE_ELEMENTS).spaceIf(settings.SPACE_WITHIN_IF_PARENTHESES)

      .before(SECONDARY_COMPOUND_TOKENSET).spacing(
        settings.SPACE_BEFORE_ELSE_KEYWORD ? 1 : 0,
        settings.SPACE_BEFORE_ELSE_KEYWORD ? 1 : 0,
        perlSettings.ELSE_ON_NEW_LINE ? 1 : 0,
        false,
        0
      )

      // unconditional
      .before(SEMICOLON).spaces(0)
      .before(HEREDOC_END).none()
      .around(OPERATORS_ALPHABETICAL).spaces(1)

      // perl specific
      .after(RESERVED_VARIABLE_DECLARATION).spaceIf(perlSettings.SPACE_AFTER_VARIABLE_DECLARATION_KEYWORD)
      .around(FAT_COMMA).spacing(1, 1, 0, true, 1)
      .beforeInside(STATEMENT_MODIFIERS, STATEMENT).spaces(1)
      ;
  }
}
