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

import com.intellij.formatting.*;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.NullableLazyValue;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

import static com.intellij.openapi.util.NullableLazyValue.atomicLazyNullable;

public class PerlInjectedLanguageBlockWrapper implements Block {
  protected final PerlInjectedLanguageBlocksBuilder myBuilder;
  private final Block myOriginal;
  private final NullableLazyValue<TextRange> myRangeProvider;
  private final AtomicNotNullLazyValue<List<Block>> myChildBlocksProvider;

  public PerlInjectedLanguageBlockWrapper(@NotNull Block original, @NotNull PerlInjectedLanguageBlocksBuilder builder) {
    myOriginal = original;
    myBuilder = builder;
    myRangeProvider = atomicLazyNullable(() -> myBuilder.getRangeInHostDocument(myOriginal.getTextRange()));
    myChildBlocksProvider = AtomicNotNullLazyValue.createValue(() -> myOriginal.getSubBlocks().stream()
      .map(block -> new PerlInjectedLanguageBlockWrapper(block, myBuilder))
      .filter(wrapper -> wrapper.getTextRangeInner() != null)
      .collect(Collectors.toList()));
  }

  @Override
  public @NotNull TextRange getTextRange() {
    TextRange textRange = getTextRangeInner();
    assert textRange != null;
    return textRange;
  }

  private @Nullable TextRange getTextRangeInner() {
    return myRangeProvider.getValue();
  }

  @Override
  public @NotNull List<Block> getSubBlocks() {
    return myChildBlocksProvider.getValue();
  }

  @Override
  public @Nullable Wrap getWrap() {
    return myOriginal.getWrap();
  }

  @Override
  public @Nullable Indent getIndent() {
    Indent indent = myOriginal.getIndent();
    return isAbsoluteNoneIndent(indent) ? Indent.getNoneIndent() : indent;
  }

  private boolean isAbsoluteNoneIndent(@Nullable Indent indent) {
    return indent instanceof IndentImpl indentImpl &&
           indent.getType() == Indent.Type.NONE &&
           indentImpl.isAbsolute();
  }

  @Override
  public @Nullable Alignment getAlignment() {
    Indent indent = myOriginal.getIndent();
    return isAbsoluteNoneIndent(indent) ? myBuilder.getAbsoluteIndentAlignment() : myOriginal.getAlignment();
  }

  @Override
  public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    assert child1 instanceof PerlInjectedLanguageBlockWrapper;
    assert child2 instanceof PerlInjectedLanguageBlockWrapper;
    return myOriginal.getSpacing(child1, child2);
  }

  @Override
  public @NotNull ChildAttributes getChildAttributes(int newChildIndex) {
    ChildAttributes originalAttributes = myOriginal.getChildAttributes(newChildIndex);
    if (originalAttributes.getAlignment() != null) {
      return originalAttributes;
    }
    Indent originalIndent = originalAttributes.getChildIndent();
    if (originalIndent != null && originalIndent.getType() == Indent.Type.SPACES) {
      return originalAttributes;
    }
    // The injected formatter answered with a generic indent and no alignment. A generic indent is resolved
    // against the host file indent options and drifts away from the fragment's own layout. If the sibling
    // this line is being inserted before is positioned with an explicit space indent (sql clause elements,
    // for example), reuse it, so the new line lands where reformatting would put that sibling.
    List<Block> children = getSubBlocks();
    if (newChildIndex >= 0 && newChildIndex < children.size()) {
      Indent siblingIndent = children.get(newChildIndex).getIndent();
      if (siblingIndent != null && siblingIndent.getType() == Indent.Type.SPACES) {
        return new ChildAttributes(siblingIndent, null);
      }
    }
    return originalAttributes;
  }

  @Override
  public boolean isIncomplete() {
    return myOriginal.isIncomplete();
  }

  @Override
  public boolean isLeaf() {
    return myOriginal.isLeaf();
  }
}
