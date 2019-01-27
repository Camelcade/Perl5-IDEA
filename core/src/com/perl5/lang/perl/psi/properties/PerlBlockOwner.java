/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.properties;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlTokenSets.LAZY_CODE_BLOCKS;

/**
 * Implement this interface if element contains block
 */
public interface PerlBlockOwner extends PsiElement {
  @Nullable
  default PsiPerlBlock getBlock() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlBlock.class);
  }

  /**
   * @return a block for the {@code blockOwner}, omiting lazy-parsable blocks if any
   */
  @Nullable
  static PsiPerlBlock findBlock(@NotNull PerlBlockOwner blockOwner) {
    PsiPerlBlock block = blockOwner.getBlock();
    if (block != null) {
      return block;
    }

    ASTNode[] children = blockOwner.getNode().getChildren(LAZY_CODE_BLOCKS);
    PsiElement lazyParsableBlock = children.length == 0 ? null : children[0].getPsi();
    if (lazyParsableBlock != null) {
      PsiElement possibleBlock = lazyParsableBlock.getFirstChild();
      return possibleBlock instanceof PsiPerlBlock ? (PsiPerlBlock)possibleBlock : null;
    }
    return null;
  }
}
