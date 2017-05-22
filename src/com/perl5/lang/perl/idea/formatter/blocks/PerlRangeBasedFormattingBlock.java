package com.perl5.lang.perl.idea.formatter.blocks;

import com.intellij.formatting.*;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 21.05.2017.
 */
public class PerlRangeBasedFormattingBlock implements Block {
  @NotNull
  private final TextRange myTextRange;
  @Nullable
  private final Indent myIndent;
  @Nullable
  private final Wrap myWrap;
  @Nullable
  private final Alignment myAlignment;


  public PerlRangeBasedFormattingBlock(@NotNull TextRange textRange) {
    this(textRange, null, null, null);
  }

  public PerlRangeBasedFormattingBlock(@NotNull TextRange textRange,
                                       @Nullable Indent indent,
                                       @Nullable Wrap wrap,
                                       @Nullable Alignment alignment) {
    myTextRange = textRange;
    myIndent = indent;
    myWrap = wrap;
    myAlignment = alignment;
  }

  @NotNull
  @Override
  public TextRange getTextRange() {
    return myTextRange;
  }

  @NotNull
  @Override
  public List<Block> getSubBlocks() {
    return Collections.emptyList();
  }

  @Nullable
  @Override
  public Wrap getWrap() {
    return myWrap;
  }

  @Nullable
  @Override
  public Indent getIndent() {
    return myIndent;
  }

  @Nullable
  @Override
  public Alignment getAlignment() {
    return myAlignment;
  }

  @Nullable
  @Override
  public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    return null;
  }

  @NotNull
  @Override
  public ChildAttributes getChildAttributes(int newChildIndex) {
    return ChildAttributes.DELEGATE_TO_PREV_CHILD;
  }

  @Override
  public boolean isIncomplete() {
    return true;
  }

  @Override
  public boolean isLeaf() {
    return true;
  }
}
