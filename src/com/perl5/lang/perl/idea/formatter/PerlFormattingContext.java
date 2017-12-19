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

import com.intellij.formatting.ASTBlock;
import com.intellij.formatting.Block;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.containers.FactoryMap;
import com.intellij.util.containers.MultiMap;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.formatter.blocks.PerlFormattingBlock;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.psi.PsiPerlStatementModifier;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.perl5.lang.perl.idea.formatter.PerlIndentProcessor.MULTI_PARAM_BLOCK_CONTAINERS;
import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.SAME_LINE;
import static com.perl5.lang.perl.parser.MooseParserExtension.MOOSE_RESERVED_TOKENSET;

public class PerlFormattingContext implements PerlFormattingTokenSets {
  public final static TokenSet BLOCK_OPENERS = TokenSet.create(
    LEFT_BRACE,
    LEFT_BRACKET,
    LEFT_PAREN
  );
  public final static TokenSet BLOCK_CLOSERS = TokenSet.create(
    RIGHT_BRACE,
    RIGHT_BRACKET,
    RIGHT_PAREN,

    SEMICOLON
  );
  private final CommonCodeStyleSettings mySettings;
  private final PerlCodeStyleSettings myPerlSettings;
  private final SpacingBuilder mySpacingBuilder;
  private final Map<PsiFile, List<TextRange>> myHeredocRangesMap = FactoryMap.create(file -> {
    if (!(file instanceof PerlFileImpl)) {
      return Collections.emptyList();
    }
    final Document document = file.getViewProvider().getDocument();
    if (document == null) {
      return Collections.emptyList();
    }
    List<TextRange> result = new ArrayList<>();
    file.accept(new PsiElementVisitor() {
      @Override
      public void visitElement(PsiElement element) {
        if (PsiUtilCore.getElementType(element) == HEREDOC_OPENER) {
          int startOffset = element.getNode().getStartOffset();
          result.add(TextRange.create(startOffset + 1, document.getLineEndOffset(document.getLineNumber(startOffset))));
        }
        else {
          element.acceptChildren(this);
        }
      }
    });
    return result;
  });
  /**
   * Elements that must have LF between them
   */
  public final static TokenSet LF_ELEMENTS = TokenSet.create(
    LABEL_DECLARATION,
    STATEMENT,
    FOR_COMPOUND,
    WHILE_COMPOUND,
    WHEN_COMPOUND,
    UNTIL_COMPOUND,
    IF_COMPOUND,
    USE_STATEMENT
  );
  private final static MultiMap<IElementType, IElementType> OPERATOR_COLLISIONS_MAP = new MultiMap<>();

  static {
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_PLUS_PLUS, OPERATOR_PLUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_PLUS, OPERATOR_PLUS_PLUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_PLUS, OPERATOR_PLUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_MINUS_MINUS, OPERATOR_MINUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_MINUS, OPERATOR_MINUS_MINUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_MINUS, OPERATOR_MINUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_SMARTMATCH, OPERATOR_BITWISE_NOT);
  }

  public PerlFormattingContext(@NotNull CodeStyleSettings settings) {
    mySettings = settings.getCommonSettings(PerlLanguage.INSTANCE);
    myPerlSettings = settings.getCustomSettings(PerlCodeStyleSettings.class);
    mySpacingBuilder = createSpacingBuilder();
  }

  protected SpacingBuilder createSpacingBuilder() {
    return new SpacingBuilder(mySettings)
      .after(COMMENT_LINE).lineBreakOrForceSpace(true, false)
      // standard settings
      .around(OPERATOR_X).spaces(1)
      .before(OPERATOR_X_ASSIGN).spaces(1)
      .around(OPERATORS_ASSIGNMENT).spaceIf(mySettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
      .around(OPERATORS_EQUALITY).spaceIf(mySettings.SPACE_AROUND_EQUALITY_OPERATORS)
      .around(OPERATORS_RELATIONAL).spaceIf(mySettings.SPACE_AROUND_RELATIONAL_OPERATORS)
      .around(OPERATORS_LOGICAL).spaceIf(mySettings.SPACE_AROUND_LOGICAL_OPERATORS)
      .around(OPERATORS_BITWISE).spaceIf(mySettings.SPACE_AROUND_BITWISE_OPERATORS)

      .afterInside(OPERATOR_MINUS, PREFIX_UNARY_EXPR).spaceIf(mySettings.SPACE_AROUND_UNARY_OPERATOR)
      .afterInside(OPERATOR_PLUS, PREFIX_UNARY_EXPR).spaceIf(mySettings.SPACE_AROUND_UNARY_OPERATOR)

      .around(OPERATORS_ADDITIVE).spaceIf(mySettings.SPACE_AROUND_ADDITIVE_OPERATORS)
      .around(OPERATORS_MULTIPLICATIVE).spaceIf(mySettings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS)
      .around(OPERATORS_SHIFT).spaceIf(mySettings.SPACE_AROUND_SHIFT_OPERATORS)
      .around(OPERATORS_UNARY).spaceIf(mySettings.SPACE_AROUND_UNARY_OPERATOR)
      .aroundInside(OPERATORS_RANGE, FLIPFLOP_EXPR).spaceIf(myPerlSettings.SPACE_AROUND_RANGE_OPERATORS)

      .between(RESERVED_PACKAGE, PACKAGE).spaces(1)
      .between(PACKAGE, VERSION_ELEMENT).spaces(1)
      .between(RESERVED_FORMAT, SUB_NAME).spaces(1)

      .betweenInside(SUB_NAME, LEFT_PAREN, SUB_DEFINITION).spaceIf(mySettings.SPACE_BEFORE_METHOD_PARENTHESES)
      .betweenInside(SUB_NAME, LEFT_PAREN, SUB_DECLARATION).spaceIf(mySettings.SPACE_BEFORE_METHOD_PARENTHESES)
      .betweenInside(SUB_NAME, LEFT_PAREN, METHOD_DEFINITION).spaceIf(mySettings.SPACE_BEFORE_METHOD_PARENTHESES)
      .betweenInside(SUB_NAME, LEFT_PAREN, FUNC_DEFINITION).spaceIf(mySettings.SPACE_BEFORE_METHOD_PARENTHESES)

      .betweenInside(LEFT_PAREN, RIGHT_PAREN, SUB_DEFINITION).spaceIf(mySettings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, SUB_DECLARATION).spaceIf(mySettings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, METHOD_DEFINITION).spaceIf(mySettings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, FUNC_DEFINITION).spaceIf(mySettings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)

      .afterInside(LEFT_PAREN, SUB_DEFINITIONS_TOKENSET).spaceIf(mySettings.SPACE_WITHIN_METHOD_PARENTHESES)
      .beforeInside(RIGHT_PAREN, SUB_DEFINITIONS_TOKENSET).spaceIf(mySettings.SPACE_WITHIN_METHOD_PARENTHESES)

      .betweenInside(SUB_NAME, COLON, SUB_DEFINITION).spaceIf(myPerlSettings.SPACE_BEFORE_ATTRIBUTE)
      .betweenInside(SUB_NAME, COLON, SUB_DECLARATION).spaceIf(myPerlSettings.SPACE_BEFORE_ATTRIBUTE)
      .betweenInside(SUB_NAME, COLON, METHOD_DEFINITION).spaceIf(myPerlSettings.SPACE_BEFORE_ATTRIBUTE)
      .betweenInside(SUB_NAME, COLON, FUNC_DEFINITION).spaceIf(myPerlSettings.SPACE_BEFORE_ATTRIBUTE)

      .betweenInside(RIGHT_PAREN, COLON, SUB_DEFINITION).spaceIf(myPerlSettings.SPACE_BEFORE_ATTRIBUTE)
      .betweenInside(RIGHT_PAREN, COLON, SUB_DECLARATION).spaceIf(myPerlSettings.SPACE_BEFORE_ATTRIBUTE)
      .betweenInside(RIGHT_PAREN, COLON, METHOD_DEFINITION).spaceIf(myPerlSettings.SPACE_BEFORE_ATTRIBUTE)
      .betweenInside(RIGHT_PAREN, COLON, FUNC_DEFINITION).spaceIf(myPerlSettings.SPACE_BEFORE_ATTRIBUTE)

      .between(COLON, ATTRIBUTE).spaces(0)

      .afterInside(RESERVED_SUB, SUB_DEFINITION).spaces(1)
      .afterInside(RESERVED_SUB, SUB_DECLARATION).spaces(1)
      .afterInside(RESERVED_METHOD, METHOD_DEFINITION).spaces(1)
      .afterInside(RESERVED_FUNC, FUNC_DEFINITION).spaces(1)

      .between(NUMBER_CONSTANT, OPERATOR_CONCAT).spaces(1)
      .aroundInside(OPERATOR_CONCAT, ADD_EXPR).spaceIf(myPerlSettings.SPACE_AROUND_CONCAT_OPERATOR)

      .betweenInside(STRING_CONTENT, STRING_CONTENT, LP_STRING_QW).spaces(1)
      .betweenInside(STRING_CONTENT, STRING_CONTENT, STRING_LIST).spaces(1)

      .betweenInside(QUOTE_SINGLE_OPEN, QUOTE_SINGLE_CLOSE, STRING_LIST).spaces(0)
      .betweenInside(QUOTE_SINGLE_OPEN, STRING_CONTENT, STRING_LIST).spaceIf(myPerlSettings.SPACE_WITHIN_QW_QUOTES)
      .betweenInside(STRING_CONTENT, QUOTE_SINGLE_CLOSE, STRING_LIST).spaceIf(myPerlSettings.SPACE_WITHIN_QW_QUOTES)

      .betweenInside(LEFT_PAREN, RIGHT_PAREN, PARENTHESISED_EXPR).spaces(0)
      .afterInside(LEFT_PAREN, PARENTHESISED_EXPR).spaceIf(mySettings.SPACE_WITHIN_PARENTHESES)
      .beforeInside(RIGHT_PAREN, PARENTHESISED_EXPR).spaceIf(mySettings.SPACE_WITHIN_PARENTHESES)

      .betweenInside(BLOCK_LEFT_BRACES, BLOCK_RIGHT_BRACES, BLOCK).spaces(0)
      .afterInside(BLOCK_LEFT_BRACES, BLOCK).spaceIf(mySettings.SPACE_WITHIN_BRACES)
      .beforeInside(BLOCK_RIGHT_BRACES, BLOCK).spaceIf(mySettings.SPACE_WITHIN_BRACES)

      .after(VARIABLE_LEFT_BRACES).spaces(0)
      .before(VARIABLE_RIGHT_BRACES).spaces(0)

      .betweenInside(LEFT_BRACE, RIGHT_BRACE, ANON_HASH).spaces(0)
      .afterInside(LEFT_BRACE, ANON_HASH).spaceIf(myPerlSettings.SPACES_WITHIN_ANON_HASH)
      .beforeInside(RIGHT_BRACE, ANON_HASH).spaceIf(myPerlSettings.SPACES_WITHIN_ANON_HASH)

      .betweenInside(LEFT_BRACKET, RIGHT_BRACKET, ANON_ARRAY).spaces(0)
      .afterInside(LEFT_BRACKET, ANON_ARRAY).spaceIf(myPerlSettings.SPACES_WITHIN_ANON_ARRAY)
      .beforeInside(RIGHT_BRACKET, ANON_ARRAY).spaceIf(myPerlSettings.SPACES_WITHIN_ANON_ARRAY)

      .around(ARRAY_INDEX).spaces(0)
      .around(HASH_INDEX).spaces(0)
      .around(OPERATOR_DEREFERENCE).spaces(0)

      .before(CALL_ARGUMENTS).spaces(1)
      .before(PARENTHESISED_CALL_ARGUMENTS).spaces(0)
      .betweenInside(LEFT_PAREN, RIGHT_PAREN, PARENTHESISED_CALL_ARGUMENTS).spaces(0)
      .afterInside(LEFT_PAREN, PARENTHESISED_CALL_ARGUMENTS).spaceIf(mySettings.SPACE_WITHIN_METHOD_CALL_PARENTHESES)
      .beforeInside(RIGHT_PAREN, PARENTHESISED_CALL_ARGUMENTS).spaceIf(mySettings.SPACE_WITHIN_METHOD_CALL_PARENTHESES)

      .before(COMMA).spaceIf(mySettings.SPACE_BEFORE_COMMA)
      .after(COMMA).spaceIf(mySettings.SPACE_AFTER_COMMA)

      .afterInside(BLOCK, MULTI_PARAM_BLOCK_CONTAINERS).spaces(1)

      .beforeInside(COLON, TRENAR_EXPR).spaceIf(mySettings.SPACE_BEFORE_COLON)
      .afterInside(COLON, TRENAR_EXPR).spaceIf(mySettings.SPACE_AFTER_COLON)
      .beforeInside(QUESTION, TRENAR_EXPR).spaceIf(mySettings.SPACE_BEFORE_QUEST)
      .afterInside(QUESTION, TRENAR_EXPR).spaceIf(mySettings.SPACE_AFTER_QUEST)

      .betweenInside(LEFT_PAREN, SEMICOLON, FOR_ITERATOR).spaceIf(mySettings.SPACE_WITHIN_IF_PARENTHESES)
      .betweenInside(SEMICOLON, RIGHT_PAREN, FOR_ITERATOR).spaceIf(mySettings.SPACE_WITHIN_IF_PARENTHESES)

      .betweenInside(SEMICOLON, SEMICOLON, FOR_ITERATOR).spaces(0)
      .afterInside(SEMICOLON, FOR_ITERATOR).spaceIf(mySettings.SPACE_AFTER_SEMICOLON)
      .beforeInside(SEMICOLON, FOR_ITERATOR).spaceIf(mySettings.SPACE_BEFORE_SEMICOLON)

      .between(RESERVED_TERMS_BLOCKS, BLOCK).spaceIf(mySettings.SPACE_BEFORE_DO_LBRACE)

      .between(RESERVED_CONDITIONAL_BRANCH_KEYWORDS, CONDITIONAL_BLOCK).spaceIf(mySettings.SPACE_BEFORE_IF_PARENTHESES)
      .between(RESERVED_COMPOUND_CONDITIONAL, CONDITION_EXPR).spaceIf(mySettings.SPACE_BEFORE_IF_PARENTHESES)
      .between(FOR_OR_FOREACH, FOR_ITERATOR).spaceIf(mySettings.SPACE_BEFORE_IF_PARENTHESES)
      .between(FOR_OR_FOREACH, CONDITION_EXPR).spaceIf(mySettings.SPACE_BEFORE_IF_PARENTHESES)
      .between(FOREACH_ITERATOR, CONDITION_EXPR).spaces(1)
      .afterInside(FOR_OR_FOREACH, FOR_COMPOUND).spaces(1)

      .betweenInside(RESERVED_IF, PARENTHESISED_EXPR, IF_STATEMENT_MODIFIER).spaceIf(mySettings.SPACE_BEFORE_IF_PARENTHESES)
      .betweenInside(RESERVED_UNLESS, PARENTHESISED_EXPR, UNLESS_STATEMENT_MODIFIER).spaceIf(mySettings.SPACE_BEFORE_IF_PARENTHESES)
      .betweenInside(RESERVED_WHILE, PARENTHESISED_EXPR, WHILE_STATEMENT_MODIFIER).spaceIf(mySettings.SPACE_BEFORE_IF_PARENTHESES)
      .betweenInside(RESERVED_UNTIL, PARENTHESISED_EXPR, UNTIL_STATEMENT_MODIFIER).spaceIf(mySettings.SPACE_BEFORE_IF_PARENTHESES)
      .betweenInside(RESERVED_FOR, PARENTHESISED_EXPR, FOR_STATEMENT_MODIFIER).spaceIf(mySettings.SPACE_BEFORE_IF_PARENTHESES)
      .betweenInside(RESERVED_FOREACH, PARENTHESISED_EXPR, FOR_STATEMENT_MODIFIER).spaceIf(mySettings.SPACE_BEFORE_IF_PARENTHESES)
      .betweenInside(RESERVED_WHEN, PARENTHESISED_EXPR, WHEN_STATEMENT_MODIFIER).spaceIf(mySettings.SPACE_BEFORE_IF_PARENTHESES)

      .afterInside(RESERVED_IF, IF_STATEMENT_MODIFIER).spaces(1)
      .afterInside(RESERVED_UNLESS, UNLESS_STATEMENT_MODIFIER).spaces(1)
      .afterInside(RESERVED_WHILE, WHILE_STATEMENT_MODIFIER).spaces(1)
      .afterInside(RESERVED_UNTIL, UNTIL_STATEMENT_MODIFIER).spaces(1)
      .afterInside(FOR_OR_FOREACH, FOR_STATEMENT_MODIFIER).spaces(1)
      .afterInside(RESERVED_WHEN, WHEN_STATEMENT_MODIFIER).spaces(1)

      .between(LABEL_KEYWORDS, SEMICOLON).spaces(0)
      .after(LABEL_KEYWORDS).spaces(1)

      .afterInside(PERL_HANDLE_EXPR, PRINT_EXPR).spaces(1)

      .between(CUSTOM_EXPR_KEYWORDS, SEMICOLON).spaces(0)
      .between(CUSTOM_EXPR_KEYWORDS, PARENTHESISED_EXPR).spaces(0)
      .after(CUSTOM_EXPR_KEYWORDS).spaces(1)
      .after(MOOSE_RESERVED_TOKENSET).spaces(1)

      .after(QUOTE_LIKE_OPENERS).spaces(0)

      .betweenInside(PACKAGE, SEMICOLON, USE_STATEMENT).spaces(0)
      .betweenInside(VERSION_ELEMENT, SEMICOLON, USE_STATEMENT).spaces(0)
      .afterInside(PACKAGE, USE_STATEMENT).spaces(1)
      .afterInside(VERSION_ELEMENT, USE_STATEMENT).spaces(1)

      .betweenInside(PACKAGE, SEMICOLON, NO_STATEMENT).spaces(0)
      .betweenInside(VERSION_ELEMENT, SEMICOLON, NO_STATEMENT).spaces(0)
      .afterInside(PACKAGE, NO_STATEMENT).spaces(1)
      .afterInside(VERSION_ELEMENT, NO_STATEMENT).spaces(1)

      .beforeInside(BLOCK, SUB_DEFINITIONS_TOKENSET).spacing(
        mySettings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        mySettings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        myPerlSettings.BRACE_STYLE_SUB == SAME_LINE ? 0 : 1,
        false,
        0
      )

      .beforeInside(LP_CODE_BLOCK, SUB_DEFINITIONS_TOKENSET).spacing(
        mySettings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        mySettings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        myPerlSettings.BRACE_STYLE_SUB == SAME_LINE ? 0 : 1,
        false,
        0
      )

      .beforeInside(BLOCK, NAMESPACE_DEFINITION).spacing(
        mySettings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        mySettings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        myPerlSettings.BRACE_STYLE_NAMESPACE == SAME_LINE ? 0 : 1,
        false,
        0
      )

      .beforeInside(BLOCK, BLOCK_CONTAINERS_TOKENSET).spacing(
        mySettings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        mySettings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        myPerlSettings.BRACE_STYLE_COMPOUND == SAME_LINE ? 0 : 1,
        false,
        0
      )
      .between(RESERVED_ELSE, UNCONDITIONAL_BLOCK).spacing(
        mySettings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        mySettings.SPACE_BEFORE_IF_LBRACE ? 1 : 0,
        myPerlSettings.BRACE_STYLE_COMPOUND == SAME_LINE ? 0 : 1,
        false,
        0
      )

      .afterInside(LEFT_PAREN, CONDITION_LIKE_ELEMENTS).spaceIf(mySettings.SPACE_WITHIN_IF_PARENTHESES)
      .beforeInside(RIGHT_PAREN, CONDITION_LIKE_ELEMENTS).spaceIf(mySettings.SPACE_WITHIN_IF_PARENTHESES)

      .before(SECONDARY_COMPOUND_TOKENSET).spacing(
        mySettings.SPACE_BEFORE_ELSE_KEYWORD ? 1 : 0,
        mySettings.SPACE_BEFORE_ELSE_KEYWORD ? 1 : 0,
        myPerlSettings.ELSE_ON_NEW_LINE ? 1 : 0,
        false,
        0
      )

      // unconditional
      .before(SEMICOLON).spaces(0)
      .before(HEREDOC_END).none()
      .around(OPERATORS_STR).spaces(1)

      // perl specific
      .after(RESERVED_VARIABLE_DECLARATION).spaceIf(myPerlSettings.SPACE_AFTER_VARIABLE_DECLARATION_KEYWORD)
      .around(FAT_COMMA).spacing(1, 1, 0, true, 1)
      .beforeInside(STATEMENT_MODIFIERS, STATEMENT).spaces(1)
      ;
  }

  public CommonCodeStyleSettings getSettings() {
    return mySettings;
  }

  public PerlCodeStyleSettings getPerlSettings() {
    return myPerlSettings;
  }

  public SpacingBuilder getSpacingBuilder() {
    return mySpacingBuilder;
  }

  public PerlIndentProcessor getIndentProcessor() {
    return PerlIndentProcessor.INSTANCE;
  }

  /**
   * Checks if Heredoc is ahead of current block and it's not possible to insert newline
   *
   * @param node in question
   * @return check result
   */
  public boolean isNewLineForbiddenAt(@NotNull ASTNode node) {
    List<TextRange> heredocRanges = myHeredocRangesMap.get(node.getPsi().getContainingFile());

    int startOffset = node.getStartOffset();
    for (TextRange range : heredocRanges) {
      if (range.contains(startOffset)) {
        return true;
      }
    }

    return false;
  }

  @Nullable
  public Spacing getSpacing(@NotNull ASTBlock parent, Block child1, @NotNull Block child2) {
    if (child1 instanceof ASTBlock && child2 instanceof ASTBlock) {
      ASTNode parentNode = parent.getNode();
      IElementType parentNodeType = PsiUtilCore.getElementType(parentNode);
      ASTNode child1Node = ((ASTBlock)child1).getNode();
      IElementType child1Type = child1Node.getElementType();
      ASTNode child2Node = ((ASTBlock)child2).getNode();
      IElementType child2Type = child2Node.getElementType();

      if (parentNodeType == PARENTHESISED_EXPR &&
          (child1Type == LEFT_PAREN || child2Type == RIGHT_PAREN) &&
          parentNode.getPsi().getParent() instanceof PsiPerlStatementModifier) {
        return getSettings().SPACE_WITHIN_IF_PARENTHESES ?
               Spacing.createSpacing(1, 1, 0, true, 1) :
               Spacing.createSpacing(0, 0, 0, true, 1);
      }

      // fix for number/concat
      if (child2Type == OPERATOR_CONCAT) {
        ASTNode run = child1Node;
        while (run instanceof CompositeElement) {
          run = run.getLastChildNode();
        }

        if (run != null) {
          IElementType runType = run.getElementType();
          if (runType == NUMBER_SIMPLE || runType == NUMBER && StringUtil.endsWith(run.getText(), ".")) {
            return Spacing.createSpacing(1, 1, 0, true, 1);
          }
        }
      }

      // LF after opening brace and before closing need to check if here-doc opener is in the line
      if (LF_ELEMENTS.contains(child1Type) && LF_ELEMENTS.contains(child2Type)) {
        if (!isNewLineForbiddenAt(child1Node)) {
          return Spacing.createSpacing(0, 0, 1, true, 1);
        }
        else {
          return Spacing.createSpacing(1, Integer.MAX_VALUE, 0, true, 1);
        }
      }

      // small inline blocks
      if (parentNodeType == BLOCK && !inGrepMapSort(parentNode) && !blockHasLessChildrenThan(parentNode, 2) &&
          (BLOCK_OPENERS.contains(child1Type) && ((PerlFormattingBlock)child1).isFirst()
           || BLOCK_CLOSERS.contains(child2Type) && ((PerlFormattingBlock)child2).isLast()
          )
          && !isNewLineForbiddenAt(child1Node)
        ) {
        return Spacing.createSpacing(0, 0, 1, true, 1);
      }

      if (parentNodeType == PARENTHESISED_CALL_ARGUMENTS &&
          child2Type == RIGHT_PAREN &&
          PsiUtilCore.getElementType(PsiTreeUtil.getDeepestLast(child1Node.getPsi())) == RIGHT_PAREN
        ) {
        return Spacing.createSpacing(0, 0, 0, true, 0);
      }

      // hack for + ++/- --/~~ ~
      if ((child2Type == PREFIX_UNARY_EXPR || child2Type == PREF_PP_EXPR) && OPERATOR_COLLISIONS_MAP.containsKey(child1Type)) {
        IElementType rightSignType = PsiUtilCore.getElementType(child2Node.getFirstChildNode());
        if (OPERATOR_COLLISIONS_MAP.get(child1Type).contains(rightSignType)) {
          return Spacing.createSpacing(1, 1, 0, true, 1);
        }
      }
      // hack for ++ +/-- -
      else if (child1Type == SUFF_PP_EXPR && OPERATOR_COLLISIONS_MAP.containsKey(child2Type)) {
        IElementType leftSignType = PsiUtilCore.getElementType(child1Node.getLastChildNode());
        if (OPERATOR_COLLISIONS_MAP.get(child2Type).contains(leftSignType)) {
          return Spacing.createSpacing(1, 1, 0, true, 1);
        }
      }
    }
    return getSpacingBuilder().getSpacing(parent, child1, child2);
  }

  /**
   * Check if we are in grep map or sort
   *
   * @return check result
   */
  private static boolean inGrepMapSort(@NotNull ASTNode node) {
    ASTNode parent = node.getTreeParent();
    IElementType parentElementType;
    return parent != null &&
           ((parentElementType = parent.getElementType()) == GREP_EXPR || parentElementType == SORT_EXPR || parentElementType == MAP_EXPR);
  }

  /**
   * Checks if block contains more than specified number of meaningful children. Spaces and line comments are being ignored
   *
   * @return check result
   */
  private static boolean blockHasLessChildrenThan(@NotNull ASTNode node, int maxChildren) {
    int counter = -2; // for braces
    ASTNode childNode = node.getFirstChildNode();
    while (childNode != null) {
      IElementType nodeType = childNode.getElementType();
      if (nodeType != TokenType.WHITE_SPACE && nodeType != COMMENT_LINE && nodeType != SEMICOLON) {
        if (++counter >= maxChildren) {
          return false;
        }
      }
      childNode = childNode.getTreeNext();
    }
    return true;
  }
}
