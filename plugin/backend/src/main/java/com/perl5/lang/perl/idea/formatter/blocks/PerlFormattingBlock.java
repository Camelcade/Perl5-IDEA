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

package com.perl5.lang.perl.idea.formatter.blocks;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.formatter.PurePerlFormattingContext;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.PerlParserUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlTokenSets.HEREDOC_BODIES_TOKENSET;
import static com.perl5.lang.perl.lexer.PerlTokenSets.TRANSPARENT_ELEMENT_TYPES;


public class PerlFormattingBlock extends AbstractBlock implements PerlElementTypes, PerlAstBlock {
  /**
   * Composite elements that should be treated as leaf elements, no children
   */
  public static final TokenSet LEAF_ELEMENTS =
    TokenSet.create(
      POD,
      PerlParserUtil.DUMMY_BLOCK
    );

  protected final @NotNull PurePerlFormattingContext myContext;
  private Indent myIndent;
  private Boolean myIsIncomplete;
  private final AtomicNotNullLazyValue<List<Block>> mySubBlocksProvider = AtomicNotNullLazyValue.createValue(
    () -> List.copyOf(buildSubBlocks())
  );

  public PerlFormattingBlock(@NotNull ASTNode node, @NotNull PurePerlFormattingContext context) {
    super(node, context.getWrap(node), context.getAlignment(node));
    myContext = context;
    myIndent = context.getNodeIndent(node);
  }

  @Override
  public void setIndent(@Nullable Indent indent) {
    myIndent = indent;
  }

  protected final @NotNull PurePerlFormattingContext getContext() {
    return myContext;
  }

  @Override
  protected final @NotNull List<Block> buildChildren() {
    if (isLeaf()) {
      return Collections.emptyList();
    }
    return mySubBlocksProvider.getValue();
  }

  protected @NotNull List<Block> buildSubBlocks() {
    ASTNode run = myNode.getFirstChildNode();
    if (run == null) {
      return Collections.emptyList();
    }

    if (getElementType() == PERL_REGEX) {
      return buildRegexpSubBlocks();
    }
    List<Block> blocks = new ArrayList<>();
    buildSubBlocksForNode(blocks, myNode);
    return processSubBlocks(blocks.isEmpty() ? Collections.emptyList() : blocks);
  }

  private void buildSubBlocksForNode(@NotNull List<? super Block> blocks, @NotNull ASTNode node) {
    TextRange formattedRange = myContext.getTextRange();

    ASTNode run = node.getFirstChildNode();
    int startOffset = run.getStartOffset();

    ASTNode lastCoverableNode = null;
    while (!run.getTextRange().intersects(formattedRange)) {
      if (shouldCreateSubBlockFor(run)) {
        lastCoverableNode = run;
      }
      run = run.getTreeNext();
    }

    if (lastCoverableNode != null) {
      while (TRANSPARENT_ELEMENT_TYPES.contains(PsiUtilCore.getElementType(lastCoverableNode)) &&
             lastCoverableNode.getLastChildNode() != null) {
        lastCoverableNode = lastCoverableNode.getLastChildNode();
      }
      blocks.add(new PerlTextRangeBasedBlock(TextRange.create(startOffset, lastCoverableNode.getTextRange().getEndOffset())));
    }

    for (; run != null && run.getTextRange().intersects(formattedRange); run = run.getTreeNext()) {
      if (TRANSPARENT_ELEMENT_TYPES.contains(PsiUtilCore.getElementType(run))) {
        buildSubBlocksForNode(blocks, run);
      }
      else if (shouldCreateSubBlockFor(run)) {
        blocks.add(createBlock(run));
      }
    }

    if (run != null) {
      blocks.add(new PerlTextRangeBasedBlock(TextRange.create(run.getStartOffset(), node.getTextRange().getEndOffset())));
    }
  }

  protected @NotNull List<Block> buildRegexpSubBlocks() {
    ASTNode run = myNode.getFirstChildNode();
    if (run == null) {
      return Collections.emptyList();
    }

    List<Block> result = new ArrayList<>();
    int startOffset = -1;
    while (run != null) {
      IElementType elementType = PsiUtilCore.getElementType(run);
      if (elementType == TokenType.WHITE_SPACE || elementType == REGEX_TOKEN || elementType == COMMENT_LINE) {
        if (startOffset < 0) {
          startOffset = run.getStartOffset();
        }
      }
      else {
        if (startOffset >= 0) {
          result.add(new PerlTextRangeBasedBlock(TextRange.create(startOffset, run.getStartOffset())));
          startOffset = -1;
        }
        if (shouldCreateSubBlockFor(run)) {
          result.add(createBlock(run));
        }
      }
      run = run.getTreeNext();
    }

    if (startOffset >= 0) {
      result.add(new PerlTextRangeBasedBlock(TextRange.create(startOffset, myNode.getStartOffset() + myNode.getTextLength())));
    }

    return result;
  }

  private @NotNull List<Block> processSubBlocks(@NotNull List<Block> rawBlocks) {
    IElementType elementType = getElementType();
    if (elementType == SIGNATURE_ELEMENT && rawBlocks.size() == 1) {
      while (rawBlocks.size() == 1) {
        rawBlocks = rawBlocks.getFirst().getSubBlocks();
      }
      return rawBlocks;
    }
    else if (elementType == COMMA_SEQUENCE_EXPR) {
      return processCommaSequenceBlocks(rawBlocks);
    }
    return rawBlocks;
  }

  private @NotNull List<Block> processCommaSequenceBlocks(@NotNull List<? extends Block> rawBlocks) {
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
      IElementType blockType = ASTBlock.getElementType(block);
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

  @Override
  public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
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

  @Override
  protected final @Nullable Indent getChildIndent() {
    throw new IllegalArgumentException("Formatting context must be used for this");
  }

  @Override
  public final @NotNull ChildAttributes getChildAttributes(int newChildIndex) {
    return myContext.getChildAttributes(this, newChildIndex);
  }

  @Override
  public final boolean isIncomplete() {
    if (myIsIncomplete == null) {
      myIsIncomplete = myContext.isIncomplete(this);
      if (myIsIncomplete == null) {
        myIsIncomplete = super.isIncomplete();
      }
    }
    return myIsIncomplete;
  }

  protected boolean shouldCreateSubBlockFor(ASTNode node) {
    IElementType elementType = PsiUtilCore.getElementType(node);
    return elementType != TokenType.WHITE_SPACE && !node.getText().isEmpty() &&
           !(HEREDOC_BODIES_TOKENSET.contains(elementType) && node.getTextLength() == 1);
  }
}
