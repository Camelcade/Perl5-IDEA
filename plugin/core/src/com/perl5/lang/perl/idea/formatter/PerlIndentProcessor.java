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

package com.perl5.lang.perl.idea.formatter;

import com.intellij.formatting.ASTBlock;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.formatter.blocks.PerlAstBlock;
import com.perl5.lang.perl.idea.formatter.blocks.PerlSyntheticBlock;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.perl5.lang.perl.idea.formatter.PerlFormattingTokenSets.*;
import static com.perl5.lang.perl.lexer.PerlTokenSets.HEREDOC_BODIES_TOKENSET;
import static com.perl5.lang.perl.lexer.PerlTokenSets.VARIABLE_DECLARATIONS;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.NO_STATEMENT;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.USE_STATEMENT;


public class PerlIndentProcessor implements PerlElementTypes {
  public static final PerlIndentProcessor INSTANCE = new PerlIndentProcessor();

  public TokenSet getAbsoluteUnindentableTokens() {
    return PerlFormattingTokenSets.ABSOLUTE_UNINDENTABLE_TOKENS;
  }

  public TokenSet getBlockLikeContainers() {
    return PerlFormattingTokenSets.BLOCK_LIKE_CONTAINERS;
  }

  public TokenSet getUnindentableContainers() {
    return PerlFormattingTokenSets.UNINDENTABLE_CONTAINERS;
  }

  public TokenSet getUnindentableTokens() {
    return PerlFormattingTokenSets.UNINDENTABLE_TOKENS;
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

    if (PerlFormattingTokenSets.FOR_ELEMENTS_TOKENSET.contains(nodeType)) {
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

    if ((nodeType == BLOCK || nodeType == SUB_EXPR) && PerlFormattingTokenSets.MULTI_PARAM_BLOCK_CONTAINERS.contains(parentNodeType)) {
      return Indent.getNoneIndent();
    }

    if (parent == null || grandParent == null && nodeType != HEREDOC_END_INDENTABLE && !HEREDOC_BODIES_TOKENSET.contains(nodeType)) {
      return Indent.getNoneIndent();
    }

    if (getUnindentableTokens().contains(nodeType) ||
        (nodeType instanceof PerlPolyNamedElementType &&
         !(node.getPsi() instanceof PerlSubCallElement) && nodeType != USE_STATEMENT && nodeType != NO_STATEMENT)) {
      return Indent.getNoneIndent();
    }

    if (parentNodeType == STRING_LIST && ( nodeType == QUOTE_SINGLE_OPEN || nodeType == QUOTE_SINGLE_CLOSE )) {
      return Indent.getNoneIndent();
    }

    if (nodeType == STRING_BARE && (parentNodeType == STRING_LIST || parentNodeType == LP_STRING_QW)) {
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

    if (PerlFormattingTokenSets.COMMA_LIKE_SEQUENCES.contains(parentNodeType)) {
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

    if (SUB_OR_MODIFIER_DEFINITIONS_TOKENSET.contains(elementType) && block.getChildElementType(newChildIndex - 1) == LEFT_PAREN) {
      return Indent.getNormalIndent();
    }

    if (getUnindentableContainers().contains(elementType)) {
      return Indent.getNoneIndent();
    }

    if (getBlockLikeContainers().contains(elementType)) {
      return Indent.getNormalIndent();
    }

    if (block instanceof PerlSyntheticBlock && block.getSubBlocks().size() == newChildIndex) {
      ASTNode parentNode = Objects.requireNonNull(block.getNode());
      ASTNode grandParentNode = parentNode.getTreeParent();
      return PsiUtilCore.getElementType(grandParentNode) == STATEMENT ? Indent.getNormalIndent() : Indent.getNoneIndent();
    }

    List<Block> subBlocks = block.getSubBlocks();
    if (subBlocks.size() > newChildIndex) {
      Block nextBlock = subBlocks.get(newChildIndex);
      if (nextBlock instanceof ASTBlock && PsiUtilCore.getElementType(((ASTBlock)nextBlock).getNode()) == BLOCK) {
        return Indent.getNormalIndent();
      }
    }

    return null;
  }

}

