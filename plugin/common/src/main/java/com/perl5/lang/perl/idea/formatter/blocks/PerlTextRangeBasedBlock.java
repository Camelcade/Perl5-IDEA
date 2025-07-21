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
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

class PerlTextRangeBasedBlock implements PerlAstBlock {
  private final @NotNull TextRange myTextRange;

  PerlTextRangeBasedBlock(@NotNull TextRange textRange) {
    myTextRange = textRange;
  }

  @Override
  public void setIndent(@Nullable Indent indent) {
  }

  @Override
  public @Nullable ASTNode getNode() {
    return null;
  }

  @Override
  public @NotNull TextRange getTextRange() {
    return myTextRange;
  }

  @Override
  public @NotNull List<Block> getSubBlocks() {
    return Collections.emptyList();
  }

  @Override
  public @Nullable Wrap getWrap() {
    return null;
  }

  @Override
  public @Nullable Indent getIndent() {
    return null;
  }

  @Override
  public @Nullable Alignment getAlignment() {
    return null;
  }

  @Override
  public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    return null;
  }

  @Override
  public @NotNull ChildAttributes getChildAttributes(int newChildIndex) {
    return ChildAttributes.DELEGATE_TO_PREV_CHILD;
  }

  @Override
  public boolean isIncomplete() {
    return false;
  }

  @Override
  public boolean isLeaf() {
    return true;
  }
}
