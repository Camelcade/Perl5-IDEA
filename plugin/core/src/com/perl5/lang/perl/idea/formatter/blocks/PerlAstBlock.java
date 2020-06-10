/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

import com.intellij.formatting.ASTBlock;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public interface PerlAstBlock extends ASTBlock {
  void setIndent(@Nullable Indent indent);

  default ASTBlock getRealBlock() {
    return this;
  }

  default @NotNull IElementType getElementType() {
    return Objects.requireNonNull(PsiUtilCore.getElementType(getNode()));
  }

  default @Nullable IElementType getChildElementType(int blockIndex) {
    if (blockIndex < 0) {
      return null;
    }
    List<Block> subBlocks = getSubBlocks();
    if (subBlocks.size() <= blockIndex) {
      return null;
    }
    return ASTBlock.getElementType(subBlocks.get(blockIndex));
  }

  default @Nullable Block getLastSubBlock() {
    return ContainerUtil.getLastItem(getSubBlocks());
  }
}
