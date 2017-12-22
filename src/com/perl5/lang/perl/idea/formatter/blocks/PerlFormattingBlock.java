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

package com.perl5.lang.perl.idea.formatter.blocks;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.formatter.PerlFormattingContext;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.PerlParserUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlTokenSets.HEREDOC_BODIES_TOKENSET;

/**
 * Created by hurricup on 03.09.2015.
 */
public class PerlFormattingBlock extends AbstractBlock implements PerlElementTypes, PerlAstBlock {
  /**
   * Composite elements that should be treated as leaf elements, no children
   */
  public final static TokenSet LEAF_ELEMENTS =
    TokenSet.create(
      POD,
      PerlParserUtil.DUMMY_BLOCK
    );

  protected final PerlFormattingContext myContext;
  private Indent myIndent;
  private final boolean myIsFirst;
  private final boolean myIsLast;
  private final IElementType myElementType;
  private final AtomicNotNullLazyValue<Boolean> myIsIncomple;
  private final AtomicNotNullLazyValue<List<Block>> mySubBlocksProvider = AtomicNotNullLazyValue.createValue(
    () -> ContainerUtil.immutableList(buildSubBlocks())
  );

  public PerlFormattingBlock(@NotNull ASTNode node, @NotNull PerlFormattingContext context) {
    super(node, context.getWrap(node), context.getAlignment(node));
    myContext = context;
    myIndent = context.getIndentProcessor().getNodeIndent(node);
    myIsFirst = FormatterUtil.getPreviousNonWhitespaceSibling(node) == null;
    myIsLast = FormatterUtil.getNextNonWhitespaceSibling(node) == null;
    myElementType = node.getElementType();
    myIsIncomple = AtomicNotNullLazyValue.createValue(() -> {
      if (myElementType == COMMA_SEQUENCE_EXPR) {
        IElementType lastNodeType = PsiUtilCore.getElementType(myNode.getLastChildNode());
        if (lastNodeType == COMMA || lastNodeType == FAT_COMMA) {
          return true;
        }
      }

      List<Block> blocks = getSubBlocks();
      if (!blocks.isEmpty()) {
        Block lastBlock = blocks.get(blocks.size() - 1);
        if (lastBlock.isIncomplete()) {
          return true;
        }
      }

      return PerlFormattingBlock.super.isIncomplete();
    });
  }

  @Override
  public void setIndent(@Nullable Indent indent) {
    myIndent = indent;
  }

  @NotNull
  @Override
  protected final List<Block> buildChildren() {
    if (isLeaf()) {
      return Collections.emptyList();
    }
    return mySubBlocksProvider.getValue();
  }

  @NotNull
  protected List<Block> buildSubBlocks() {
    final List<Block> blocks = new ArrayList<>();

    for (ASTNode child = myNode.getFirstChildNode(); child != null; child = child.getTreeNext()) {
      if (shouldCreateBlockFor(child)) {
        blocks.add(createBlock(child));
      }
    }
    return processSubBlocks(blocks);
  }

  @NotNull
  private List<Block> processSubBlocks(@NotNull List<Block> rawBlocks) {
    if (getElementType() != COMMA_SEQUENCE_EXPR) {
      return rawBlocks;
    }

    List<Block> result = new ArrayList<>();
    List<Block> blocksToGroup = new ArrayList<>();
    boolean[] hasFatComma = new boolean[]{false};
    Runnable blocksDispatcher = () -> {
      if (blocksToGroup.isEmpty()) {
        return;
      }
      if (hasFatComma[0]) {
        result.add(new PerlSyntheticBlock(this, blocksToGroup, null, null, myContext));
        hasFatComma[0] = false;
      }
      else {
        result.addAll(blocksToGroup);
      }
      blocksToGroup.clear();
    };

    for (Block block : rawBlocks) {
      IElementType blockType = block instanceof ASTBlock ? PsiUtilCore.getElementType(((ASTBlock)block).getNode()) : null;
      if ((blockType == null || blockType == COMMA)) {
        blocksToGroup.add(block);
        blocksDispatcher.run();
      }
      else if (blocksToGroup.isEmpty() && (blockType == COMMENT_LINE || blockType == COMMENT_ANNOTATION)) {
        result.add(block);
      }
      else {
        blocksToGroup.add(block);
        hasFatComma[0] |= blockType == FAT_COMMA;
      }
    }
    blocksDispatcher.run();

    return result;
  }

  protected PerlFormattingBlock createBlock(@NotNull ASTNode node) {
    if (HEREDOC_BODIES_TOKENSET.contains(PsiUtilCore.getElementType(node))) {
      return new PerlHeredocFormattingBlock(node, myContext);
    }
    return new PerlFormattingBlock(node, myContext);
  }

  @Nullable
  @Override
  public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    return myContext.getSpacing(this, child1, child2);
  }

  @Override
  public boolean isLeaf() {
    return myNode.getFirstChildNode() == null || LEAF_ELEMENTS.contains(myNode.getElementType());
  }

  @Override
  public Indent getIndent() {
    return myIndent;
  }

  @Nullable
  @Override
  protected Indent getChildIndent() {
    IElementType elementType = getElementType();

    if (myContext.getIndentProcessor().getUnindentableContainers().contains(elementType)) {
      return Indent.getNoneIndent();
    }

    if (myContext.getIndentProcessor().getBlockLikeContainers().contains(elementType)) {
      return Indent.getNormalIndent();
    }

    return super.getChildIndent();
  }

  @NotNull
  @Override
  public ChildAttributes getChildAttributes(int newChildIndex) {
    return new ChildAttributes(getChildIndent(), getChildAlignment());
  }

  @Nullable
  private Alignment getChildAlignment() {

    IElementType elementType = getElementType();
    if (PerlFormattingContext.COMMA_LIKE_SEQUENCES.contains(elementType)) {
      return null;
    }

    // this is default algorythm from AbstractBlock#getFirstChildAlignment()
    List<Block> subBlocks = getSubBlocks();
    for (final Block subBlock : subBlocks) {
      Alignment alignment = subBlock.getAlignment();
      if (alignment != null) {
        return alignment;
      }
    }
    return null;
  }

  public boolean isLast() {
    return myIsLast;
  }

  public boolean isFirst() {
    return myIsFirst;
  }

  public IElementType getElementType() {
    return myElementType;
  }

  @Override
  public boolean isIncomplete() {
    return myIsIncomple.getValue();
  }

  protected static boolean shouldCreateBlockFor(ASTNode node) {
    IElementType elementType = PsiUtilCore.getElementType(node);
    return elementType != TokenType.WHITE_SPACE && !node.getText().isEmpty() &&
           !(HEREDOC_BODIES_TOKENSET.contains(elementType) && node.getTextLength() == 1);
  }
}
