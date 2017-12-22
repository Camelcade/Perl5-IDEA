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
import com.intellij.openapi.util.TextRange;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.formatter.PerlFormattingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Synthetic block for wrapping group of other blocks
 */
public class PerlSyntheticBlock implements PerlAstBlock {
  @NotNull
  private final List<Block> mySubBlocks;
  @Nullable
  private final Wrap myWrap;
  @Nullable
  private final Alignment myAlignment;
  @NotNull
  private final PerlFormattingContext myContext;
  @NotNull
  private final AtomicNotNullLazyValue<TextRange> myRangeProvider = AtomicNotNullLazyValue.createValue(
    () -> TextRange.create(getFirstRealBlock().getTextRange().getStartOffset(), getLastRealBlock().getTextRange().getEndOffset())
  );
  @Nullable
  private Indent myIndent;
  @NotNull
  private PerlAstBlock myRealBlock;

  public PerlSyntheticBlock(@NotNull PerlAstBlock realBlock,
                            @NotNull List<Block> subBlocks,
                            @Nullable Wrap wrap,
                            @Nullable Alignment alignment,
                            @NotNull PerlFormattingContext context) {
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
      .filter(block -> block instanceof PerlAstBlock)
      .forEach(block -> ((PerlAstBlock)block)
        .setIndent(Indent.getContinuationWithoutFirstIndent()));
  }

  @Override
  @NotNull
  public ASTBlock getRealBlock() {
    return myRealBlock.getRealBlock();
  }

  @NotNull
  @Override
  public ASTNode getNode() {
    return getRealBlock().getNode();
  }

  @NotNull
  @Override
  public TextRange getTextRange() {
    return myRangeProvider.getValue();
  }

  @NotNull
  public Block getFirstRealBlock() {
    Block candidate = mySubBlocks.get(0);
    while (candidate instanceof PerlSyntheticBlock) {
      candidate = ((PerlSyntheticBlock)candidate).getFirstRealBlock();
    }
    return candidate;
  }

  @NotNull
  public Block getLastRealBlock() {
    Block candidate = mySubBlocks.get(mySubBlocks.size() - 1);
    while (candidate instanceof PerlSyntheticBlock) {
      candidate = ((PerlSyntheticBlock)candidate).getLastRealBlock();
    }
    return candidate;
  }

  @NotNull
  @Override
  public List<Block> getSubBlocks() {
    return mySubBlocks;
  }

  @Nullable
  @Override
  public Wrap getWrap() {
    return myWrap;
  }

  @NotNull
  @Override
  public Indent getIndent() {
    return myIndent;
  }

  @Override
  public void setIndent(@Nullable Indent indent) {
    myIndent = indent;
  }

  @Nullable
  @Override
  public Alignment getAlignment() {
    return myAlignment;
  }

  @Nullable
  @Override
  public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    return myContext.getSpacing(this, child1, child2);
  }

  @NotNull
  @Override
  public ChildAttributes getChildAttributes(int newChildIndex) {
    return new ChildAttributes(null, null);
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
