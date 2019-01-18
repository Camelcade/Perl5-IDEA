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

import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.idea.formatter.blocks.PerlAstBlock;
import com.perl5.lang.perl.idea.formatter.blocks.PerlSyntheticBlock;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.perlswitch.PerlSwitchElementTypes;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedNestedCallElementBase;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.idea.formatter.PerlFormattingContext.BLOCK_CLOSERS;
import static com.perl5.lang.perl.idea.formatter.PerlFormattingContext.BLOCK_OPENERS;
import static com.perl5.lang.perl.idea.formatter.PerlFormattingTokenSets.VARIABLE_DECLARATIONS;
import static com.perl5.lang.perl.lexer.PerlTokenSets.HEREDOC_BODIES_TOKENSET;

/**
 * Created by hurricup on 03.09.2015.
 */
public class PerlIndentProcessor implements PerlElementTypes, PerlSwitchElementTypes {
  public static final PerlIndentProcessor INSTANCE = new PerlIndentProcessor();

  // containers which has none indentation
  public static final TokenSet UNINDENTABLE_CONTAINERS = TokenSet.create(
    NAMESPACE_DEFINITION,
    NAMESPACE_CONTENT,
    SUB_DEFINITION,
    METHOD_DEFINITION,
    FUNC_DEFINITION,
    IF_COMPOUND,
    UNLESS_COMPOUND,
    UNTIL_COMPOUND,
    WHILE_COMPOUND,
    GIVEN_COMPOUND,
    WHEN_COMPOUND,
    DEFAULT_COMPOUND,
    TRYCATCH_COMPOUND,
    FOR_COMPOUND,
    FOREACH_COMPOUND,
    CONDITIONAL_BLOCK,
    CONTINUE_BLOCK,

    TRYCATCH_EXPR,
    TRY_EXPR,
    CATCH_EXPR,
    FINALLY_EXPR,
    EXCEPT_EXPR,
    OTHERWISE_EXPR,
    CONTINUATION_EXPR,

    // fixme see #745
    SWITCH_COMPOUND,
    CASE_COMPOUND,

    DO_EXPR,
    EVAL_EXPR,
    SUB_EXPR,
    PerlParserDefinition.FILE,

    LP_REGEX,
    LP_REGEX_REPLACEMENT,
    LP_REGEX_X,
    LP_REGEX_XX,
    LP_STRING_Q,
    LP_STRING_QQ,
    LP_STRING_QW,
    LP_STRING_XQ
  );

  static final TokenSet MULTI_PARAM_BLOCK_CONTAINERS = TokenSet.create(
    GREP_EXPR, MAP_EXPR, SORT_EXPR
  );

  public static final TokenSet BLOCK_LIKE_CONTAINERS = TokenSet.create(
    BLOCK
  );

  public static final TokenSet UNINDENTABLE_TOKENS = TokenSet.create(
    LP_STRING_QW,
    COMMA_SEQUENCE_EXPR,
    CALL_ARGUMENTS
  );

  private static final TokenSet FOR_ELEMENTS_TOKENSET = TokenSet.create(
    FOR_INIT, FOR_CONDITION, FOR_MUTATOR
  );

  /**
   * Tokens that must be suppressed for indentation
   */
  public static final TokenSet ABSOLUTE_UNINDENTABLE_TOKENS = TokenSet.create(
    HEREDOC_END,
    POD,
    FORMAT,
    FORMAT_TERMINATOR,
    TAG_DATA,
    TAG_END
  );

  public TokenSet getAbsoluteUnindentableTokens() {
    return ABSOLUTE_UNINDENTABLE_TOKENS;
  }

  public TokenSet getBlockLikeContainers() {
    return BLOCK_LIKE_CONTAINERS;
  }

  public TokenSet getUnindentableContainers() {
    return UNINDENTABLE_CONTAINERS;
  }

  public TokenSet getUnindentableTokens() {
    return UNINDENTABLE_TOKENS;
  }

  @NotNull
  public Indent getNodeIndent(@NotNull ASTNode node) {
    IElementType nodeType = node.getElementType();
    ASTNode parent = node.getTreeParent();
    ASTNode grandParent = parent != null ? parent.getTreeParent() : null;

    IElementType parentNodeType = parent != null ? parent.getElementType() : null;
    IElementType grandParentNodeType = grandParent != null ? grandParent.getElementType() : null;

    ASTNode prevSibling = FormatterUtil.getPreviousNonWhitespaceSibling(node);
    IElementType prevSiblingElementType = prevSibling != null ? prevSibling.getElementType() : null;

    ASTNode nextSibling = FormatterUtil.getNextNonWhitespaceSibling(node);
    IElementType nextSiblingElementType = nextSibling != null ? nextSibling.getElementType() : null;

    boolean isFirst = prevSibling == null;
    boolean isLast = nextSibling == null;

    if (parentNodeType == ATTRIBUTES) {
      return Indent.getContinuationIndent();
    }

    if (FOR_ELEMENTS_TOKENSET.contains(nodeType)) {
      return Indent.getContinuationIndent();
    }

    if (isFirst && BLOCK_OPENERS.contains(nodeType)
        || isLast && BLOCK_CLOSERS.contains(nodeType)
      ) {
      return Indent.getNoneIndent();
    }

    if (VARIABLE_DECLARATIONS.contains(parentNodeType) && ( nodeType == LEFT_PAREN || nodeType == RIGHT_PAREN )) {
      return Indent.getNoneIndent();
    }

    boolean forceFirstIndent = false;
    if (HEREDOC_BODIES_TOKENSET.contains(nodeType)) {
      PsiElement psi = node.getPsi();
      assert psi instanceof PerlHeredocElementImpl;
      if (!((PerlHeredocElementImpl)psi).isIndentable()) {
        return Indent.getAbsoluteNoneIndent();
      }
      forceFirstIndent = true;
    }

    if (getAbsoluteUnindentableTokens().contains(nodeType)) {
      return Indent.getAbsoluteNoneIndent();
    }

    if ((nodeType == BLOCK || nodeType == SUB_EXPR) && MULTI_PARAM_BLOCK_CONTAINERS.contains(parentNodeType)) {
      return Indent.getNoneIndent();
    }

    if (parent == null || grandParent == null && nodeType != HEREDOC_END_INDENTABLE && !HEREDOC_BODIES_TOKENSET.contains(nodeType)) {
      return Indent.getNoneIndent();
    }

    if (getUnindentableTokens().contains(nodeType) ||
        (nodeType instanceof PerlPolyNamedElementType && !(node.getPsi() instanceof PerlPolyNamedNestedCallElementBase))) {
      return Indent.getNoneIndent();
    }

    if (parentNodeType == STRING_LIST && ( nodeType == QUOTE_SINGLE_OPEN || nodeType == QUOTE_SINGLE_CLOSE )) {
      return Indent.getNoneIndent();
    }

    if (nodeType == STRING_CONTENT && ( parentNodeType == STRING_LIST || parentNodeType == LP_STRING_QW )) {
      return Indent.getContinuationIndent();
    }

    // defined by parent
    if (getUnindentableContainers().contains(parentNodeType)) {
      // a little magic for sub attributes
      if (parentNodeType == SUB_DEFINITION) {
        if (nodeType == COLON && nextSiblingElementType == ATTRIBUTE ||
            nodeType == ATTRIBUTE && prevSiblingElementType != COLON
          ) {
          return Indent.getContinuationIndent();
        }
      }

      return Indent.getNoneIndent();
    }

    if (PerlFormattingContext.COMMA_LIKE_SEQUENCES.contains(parentNodeType)) {
      return grandParentNodeType == STATEMENT ? Indent.getContinuationWithoutFirstIndent() : Indent.getContinuationIndent();
    }

    if (parentNodeType == CALL_ARGUMENTS) {
      return Indent.getContinuationIndent();
    }

    if (getBlockLikeContainers().contains(parentNodeType)) {
      return Indent.getNormalIndent();
    }

    return forceFirstIndent ? Indent.getContinuationIndent() : Indent.getContinuationWithoutFirstIndent();
  }

  @Nullable
  public Indent getChildIndent(@NotNull PerlAstBlock block, int newChildIndex) {
    IElementType elementType = block.getElementType();

    if (getUnindentableContainers().contains(elementType)) {
      return Indent.getNoneIndent();
    }

    if (getBlockLikeContainers().contains(elementType)) {
      return Indent.getNormalIndent();
    }

    if (block instanceof PerlSyntheticBlock && block.getSubBlocks().size() == newChildIndex) {
      ASTNode parentNode = block.getNode();
      ASTNode grandParentNode = parentNode.getTreeParent();
      return PsiUtilCore.getElementType(grandParentNode) == STATEMENT ? Indent.getNormalIndent() : Indent.getNoneIndent();
    }

    return null;
  }

}

