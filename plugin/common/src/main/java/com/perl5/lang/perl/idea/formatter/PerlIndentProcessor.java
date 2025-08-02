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
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.perl5.lang.perl.lexer.PerlTokenSets.*;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.NO_STATEMENT;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.USE_STATEMENT;


public class PerlIndentProcessor {
  public static final PerlIndentProcessor INSTANCE = new PerlIndentProcessor();

  public TokenSet getAbsoluteUnindentableTokens() {
    return PerlFormattingTokenSets.ABSOLUTE_UNINDENTABLE_TOKENS;
  }

  public TokenSet getBlockLikeContainers() {
    return PerlFormattingTokenSets.FORMATTING_BLOCK_LIKE_CONTAINERS;
  }

  public TokenSet getUnindentableContainers() {
    return PerlFormattingTokenSets.UNINDENTABLE_CONTAINERS;
  }

  public TokenSet getUnindentableTokens() {
    return PerlFormattingTokenSets.UNINDENTABLE_TOKENS;
  }

  public @NotNull Indent getNodeIndent(@NotNull ASTNode node) {
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

    if (isFirst && PerlFormattingTokenSets.BLOCK_OPENERS.contains(nodeType)
        || isLast && PerlFormattingTokenSets.BLOCK_CLOSERS.contains(nodeType)
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

    if ((BLOCK_LIKE_CONTAINERS.contains(nodeType) || nodeType == SUB_EXPR) &&
        PerlFormattingTokenSets.MULTI_PARAM_BLOCK_CONTAINERS.contains(parentNodeType)) {
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

    if (nodeType == STRING_BARE && parentNodeType == STRING_LIST) {
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

  public @Nullable Indent getChildIndent(@NotNull PerlAstBlock block, int newChildIndex) {
    ASTNode node = block.getNode();
    IElementType elementType = PsiUtilCore.getElementType(node);
    ASTNode parentNode = node == null ? null : node.getTreeParent();

    // hack for signature_element wrapping variable_declaration
    ASTNode grandParentNode = parentNode == null ? null : parentNode.getTreeParent();
    IElementType grandParentElementType = PsiUtilCore.getElementType(grandParentNode);
    if (grandParentElementType == SIGNATURE_ELEMENT && PsiUtilCore.getElementType(parentNode) == VARIABLE_DECLARATION_ELEMENT &&
        node.getTextRange().equals(grandParentNode.getTextRange())) {
      elementType = grandParentElementType;
    }

    if (elementType == ATTRIBUTES) {
      return Indent.getContinuationIndent();
    }
    if (elementType == SIGNATURE_ELEMENT) {
      return Indent.getContinuationWithoutFirstIndent();
    }
    if (PerlFormattingTokenSets.SUB_OR_MODIFIER_DEFINITIONS_TOKENSET.contains(elementType) &&
        block.getChildElementType(newChildIndex - 1) == LEFT_PAREN) {
      return Indent.getNormalIndent();
    }

    if (getUnindentableContainers().contains(elementType)) {
      return Indent.getNoneIndent();
    }

    if (getBlockLikeContainers().contains(elementType)) {
      return Indent.getNormalIndent();
    }

    if (elementType == REPLACEMENT_REGEX) {
      List<Block> subBlocks = block.getSubBlocks();
      IElementType currentBlockElementType = subBlocks.size() > newChildIndex
                                             ? ASTBlock.getElementType(subBlocks.get(newChildIndex)) : null;
      if (BLOCK_LIKE_CONTAINERS.contains(currentBlockElementType) || currentBlockElementType == REGEX_QUOTE_CLOSE) {
        return Indent.getNormalIndent();
      }
    }

    if (parentNode != null && block instanceof PerlSyntheticBlock && block.getSubBlocks().size() == newChildIndex) {
      return PsiUtilCore.getElementType(parentNode) == STATEMENT ? Indent.getNormalIndent() : Indent.getNoneIndent();
    }

    List<Block> subBlocks = block.getSubBlocks();
    if (subBlocks.size() > newChildIndex) {
      Block nextBlock = subBlocks.get(newChildIndex);
      if (BLOCK_LIKE_CONTAINERS.contains(ASTBlock.getElementType(nextBlock))) {
        return Indent.getNormalIndent();
      }
    }

    return null;
  }

}

