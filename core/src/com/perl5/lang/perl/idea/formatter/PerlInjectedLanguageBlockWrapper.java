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

import com.intellij.formatting.*;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.AtomicNullableLazyValue;
import com.intellij.openapi.util.TextRange;
import com.intellij.util.ReflectionUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class PerlInjectedLanguageBlockWrapper implements Block {
  @Nullable
  private final static Method IS_ABSOLUTE = ReflectionUtil.getDeclaredMethod(IndentImpl.class, "isAbsolute");
  protected final PerlInjectedLanguageBlocksBuilder myBuilder;
  private final Block myOriginal;
  private final AtomicNullableLazyValue<TextRange> myRangeProvider;
  private final AtomicNotNullLazyValue<List<Block>> myChildBlocksProvider;

  public PerlInjectedLanguageBlockWrapper(@NotNull Block original, @NotNull PerlInjectedLanguageBlocksBuilder builder) {
    myOriginal = original;
    myBuilder = builder;
    myRangeProvider = AtomicNullableLazyValue.createValue(() -> myBuilder.getRangeInHostDocument(myOriginal.getTextRange()));
    myChildBlocksProvider = AtomicNotNullLazyValue.createValue(() -> myOriginal.getSubBlocks().stream()
      .map(block -> new PerlInjectedLanguageBlockWrapper(block, myBuilder))
      .filter(wrapper -> wrapper.getTextRangeInner() != null)
      .collect(Collectors.toList()));
  }

  @NotNull
  @Override
  public TextRange getTextRange() {
    TextRange textRange = getTextRangeInner();
    assert textRange != null;
    return textRange;
  }

  @Nullable
  private TextRange getTextRangeInner() {
    return myRangeProvider.getValue();
  }

  @NotNull
  @Override
  public List<Block> getSubBlocks() {
    return myChildBlocksProvider.getValue();
  }

  @Nullable
  @Override
  public Wrap getWrap() {
    return myOriginal.getWrap();
  }

  @Nullable
  @Override
  public Indent getIndent() {
    Indent indent = myOriginal.getIndent();
    return isAbsoluteNoneIndent(indent) ? Indent.getNoneIndent() : indent;
  }

  private boolean isAbsoluteNoneIndent(@Nullable Indent indent) {
    if (IS_ABSOLUTE == null || !(indent instanceof IndentImpl) || indent.getType() != Indent.Type.NONE) {
      return false;
    }

    try {
      return (Boolean)IS_ABSOLUTE.invoke(indent);
    }
    catch (Exception e) {
      return false;
    }
  }

  @Nullable
  @Override
  public Alignment getAlignment() {
    Indent indent = myOriginal.getIndent();
    return isAbsoluteNoneIndent(indent) ? myBuilder.getAbsoluteIndentAlignment() : myOriginal.getAlignment();
  }

  @Nullable
  @Override
  public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    assert child1 instanceof PerlInjectedLanguageBlockWrapper;
    assert child2 instanceof PerlInjectedLanguageBlockWrapper;
    return myOriginal.getSpacing(child1, child2);
  }

  @NotNull
  @Override
  public ChildAttributes getChildAttributes(int newChildIndex) {
    return myOriginal.getChildAttributes(newChildIndex);
  }

  @Override
  public boolean isIncomplete() {
    return myOriginal.isIncomplete();
  }

  @Override
  public boolean isLeaf() {
    return myOriginal.isLeaf();
  }

  public static class RootBlockWrapper extends PerlInjectedLanguageBlockWrapper {
    public RootBlockWrapper(@NotNull Block original, @NotNull PerlInjectedLanguageBlocksBuilder builder) {
      super(original, builder);
    }

    @Nullable
    @Override
    public Alignment getAlignment() {
      return myBuilder.getAbsoluteIndentAlignment();
    }
  }
}
