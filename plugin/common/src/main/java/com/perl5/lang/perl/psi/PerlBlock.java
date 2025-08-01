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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.FILE;

/**
 * This is just a block, part of other constructions: loops, declarations or blocks compounds
 */
public interface PerlBlock extends PerlLexicalScope {
  TokenSet LOOPS_CONTAINERS = TokenSet.create(
    BLOCK_COMPOUND,
    FILE,
    NAMESPACE_CONTENT,
    WHILE_COMPOUND,
    UNTIL_COMPOUND,
    FOR_COMPOUND,
    FOREACH_COMPOUND,
    NAMESPACE_DEFINITION,
    TRY_EXPR,
    CATCH_EXPR
  );
  TokenSet BLOCKS_WITH_RETURN_VALUE = TokenSet.create(
    SUB_EXPR,
    DO_BLOCK_EXPR,
    EVAL_EXPR,
    SUB_DEFINITION,
    METHOD_DEFINITION,
    FUNC_DEFINITION
  );

  /**
   * @return container of this block, omitting lazy-parsable part if any.
   */
  @Contract(pure = true)
  default @NotNull PsiElement getContainer() {
    return getParent();
  }

  /**
   * @return compound container of this block. Similar to {@link #getContainer()}, but passes continue blocks and eval anon subs.
   */
  @Contract(pure = true)
  default @NotNull PsiElement getCompoundContainer() {
    PsiElement container = getContainer();
    if (container instanceof PsiPerlContinueBlock) {
      return container.getParent();
    }
    if (!(container instanceof PerlSubExpr)) {
      return container;
    }
    PsiElement containerParent = container.getParent();
    return containerParent instanceof PsiPerlEvalExpr ? containerParent : container;
  }

  /**
   * @return closest parent PerlBlock element if any
   */
  @Contract("null -> null")
  static @Nullable PerlBlock getClosestTo(@Nullable PsiElement element) {
    return PsiTreeUtil.getParentOfType(element, PerlBlock.class);
  }

  /**
   * @return container of closest parent block for the {@code position}
   */
  @Contract("null -> null")
  static @Nullable PsiElement getClosestBlockCompoundContainer(@Nullable PsiElement position) {
    return ObjectUtils.doIfNotNull(getClosestTo(position), PerlBlock::getCompoundContainer);
  }
}
