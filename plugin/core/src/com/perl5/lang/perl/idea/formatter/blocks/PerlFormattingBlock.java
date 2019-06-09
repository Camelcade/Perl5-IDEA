/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.intellij.openapi.util.AtomicNullableLazyValue;
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


public class PerlFormattingBlock extends AbstractBlock implements PerlElementTypes, PerlAstBlock {
  /**
   * Composite elements that should be treated as leaf elements, no children
   */
  public final static TokenSet LEAF_ELEMENTS =
    TokenSet.create(
      POD,
      PerlParserUtil.DUMMY_BLOCK
    );

  @NotNull
  protected final PerlFormattingContext myContext;
  private Indent myIndent;
  private Boolean myIsFirst;
  private Boolean myIsLast;
  private Boolean myIsIncomple;
  private final AtomicNotNullLazyValue<List<Block>> mySubBlocksProvider = AtomicNotNullLazyValue.createValue(
    () -> ContainerUtil.immutableList(buildSubBlocks())
  );
  private final AtomicNullableLazyValue<Alignment> myAlignmentProvider = AtomicNullableLazyValue.createValue(
    () -> getContext().getAlignment(myNode)
  );

  public PerlFormattingBlock(@NotNull ASTNode node, @NotNull PerlFormattingContext context) {
    super(context.registerNode(node), context.getWrap(node), null);
    myContext = context;
    buildChildren();
    myIndent = context.getIndentProcessor().getNodeIndent(node);
  }

  @Override
  public void setIndent(@Nullable Indent indent) {
    myIndent = indent;
  }

  @NotNull
  protected final PerlFormattingContext getContext() {
    return myContext;
  }

  @Nullable
  @Override
  public final Alignment getAlignment() {
    return myAlignmentProvider.getValue();
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
  protected final Indent getChildIndent() {
    throw new IllegalArgumentException("Formatting context must be used for this");
  }

  @NotNull
  @Override
  public final ChildAttributes getChildAttributes(int newChildIndex) {
    return myContext.getChildAttributes(this, newChildIndex);
  }

  public boolean isLast() {
    if (myIsLast == null) {
      myIsLast = FormatterUtil.getNextNonWhitespaceSibling(myNode) == null;
    }
    return myIsLast;
  }

  public boolean isFirst() {
    if (myIsFirst == null) {
      myIsFirst = FormatterUtil.getPreviousNonWhitespaceSibling(myNode) == null;
    }
    return myIsFirst;
  }

  @Override
  public final boolean isIncomplete() {
    if (myIsIncomple == null) {
      myIsIncomple = myContext.isIncomplete(this);
      if (myIsIncomple == null) {
        myIsIncomple = super.isIncomplete();
      }
    }
    return myIsIncomple;
  }

  protected static boolean shouldCreateBlockFor(ASTNode node) {
    IElementType elementType = PsiUtilCore.getElementType(node);
    return elementType != TokenType.WHITE_SPACE && !node.getText().isEmpty() &&
           !(HEREDOC_BODIES_TOKENSET.contains(elementType) && node.getTextLength() == 1);
  }
}
