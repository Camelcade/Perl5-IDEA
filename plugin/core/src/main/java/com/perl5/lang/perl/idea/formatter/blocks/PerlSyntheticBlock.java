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
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.formatter.PurePerlFormattingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Synthetic block for wrapping group of other blocks
 */
public class PerlSyntheticBlock implements PerlAstBlock {
  private final @NotNull List<Block> mySubBlocks;
  private final @Nullable Wrap myWrap;
  private final @Nullable Alignment myAlignment;
  private final @NotNull PurePerlFormattingContext myContext;
  private final @NotNull AtomicNotNullLazyValue<TextRange> myRangeProvider = AtomicNotNullLazyValue.createValue(
    () -> TextRange.create(getFirstRealBlock().getTextRange().getStartOffset(), getLastRealBlock().getTextRange().getEndOffset())
  );
  private @Nullable Indent myIndent;
  private final @NotNull PerlAstBlock myRealBlock;

  public PerlSyntheticBlock(@NotNull PerlAstBlock realBlock,
                            @NotNull List<Block> subBlocks,
                            @Nullable Wrap wrap,
                            @Nullable Alignment alignment,
                            @NotNull PurePerlFormattingContext context) {
    if (subBlocks.isEmpty()) {
      throw new IllegalArgumentException("Subblocks should not be empty");
    }
    mySubBlocks = ContainerUtil.immutableList(new ArrayList<>(subBlocks));
    myRealBlock = realBlock;
    myWrap = wrap;
    myAlignment = alignment;
    myContext = context;
    myIndent = getFirstRealBlock().getIndent();
    mySubBlocks.stream()
      .filter(block -> block instanceof PerlAstBlock && block.getIndent() != Indent.getAbsoluteNoneIndent())
      .forEach(block -> ((PerlAstBlock)block)
        .setIndent(Indent.getContinuationWithoutFirstIndent()));
  }

  @Override
  public @NotNull ASTBlock getRealBlock() {
    return myRealBlock.getRealBlock();
  }

  @Override
  public @NotNull ASTNode getNode() {
    return Objects.requireNonNull(getRealBlock().getNode());
  }

  @Override
  public @NotNull TextRange getTextRange() {
    return myRangeProvider.getValue();
  }

  public @NotNull Block getFirstRealBlock() {
    Block candidate = mySubBlocks.getFirst();
    while (candidate instanceof PerlSyntheticBlock perlSyntheticBlock) {
      candidate = perlSyntheticBlock.getFirstRealBlock();
    }
    return candidate;
  }

  public @NotNull Block getLastRealBlock() {
    Block candidate = mySubBlocks.getLast();
    while (candidate instanceof PerlSyntheticBlock block) {
      candidate = block.getLastRealBlock();
    }
    return candidate;
  }

  @Override
  public @NotNull List<Block> getSubBlocks() {
    return mySubBlocks;
  }

  @Override
  public @Nullable Wrap getWrap() {
    return myWrap;
  }

  @Override
  public @Nullable Indent getIndent() {
    return myIndent;
  }

  @Override
  public void setIndent(@Nullable Indent indent) {
    myIndent = indent;
  }

  @Override
  public @Nullable Alignment getAlignment() {
    return myAlignment;
  }

  @Override
  public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    return myContext.getSpacing(this, child1, child2);
  }

  @Override
  public @NotNull ChildAttributes getChildAttributes(int newChildIndex) {
    return myContext.getChildAttributes(this, newChildIndex);
  }

  @Override
  public boolean isIncomplete() {
    return false;
  }

  @Override
  public boolean isLeaf() {
    return false;
  }

  @Override
  public String toString() {
    return "Synthetic block";
  }
}
