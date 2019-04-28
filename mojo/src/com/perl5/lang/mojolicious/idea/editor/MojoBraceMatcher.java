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

package com.perl5.lang.mojolicious.idea.editor;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 22.12.2015.
 */
public class MojoBraceMatcher implements PairedBraceMatcher, MojoliciousElementTypes {
  private static final BracePair[] PAIRS = new BracePair[]{
    new BracePair(MOJO_BLOCK_OPENER, MOJO_BLOCK_CLOSER, false),
    new BracePair(MOJO_BLOCK_OPENER, MOJO_BLOCK_NOSPACE_CLOSER, false),
    new BracePair(MOJO_BLOCK_OPENER, MOJO_BLOCK_CLOSER_SEMI, false),

    new BracePair(MOJO_BLOCK_EXPR_OPENER, MOJO_BLOCK_EXPR_CLOSER, false),
    new BracePair(MOJO_BLOCK_EXPR_ESCAPED_OPENER, MOJO_BLOCK_EXPR_CLOSER, false),

    new BracePair(MOJO_BLOCK_EXPR_OPENER, MOJO_BLOCK_EXPR_NOSPACE_CLOSER, false),
    new BracePair(MOJO_BLOCK_EXPR_ESCAPED_OPENER, MOJO_BLOCK_EXPR_NOSPACE_CLOSER, false),

    new BracePair(MOJO_BEGIN, MOJO_END, false),
  };

  @NotNull
  @Override
  public BracePair[] getPairs() {
    return PAIRS;
  }

  @Override
  public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, IElementType contextType) {
    return true;
  }

  @Override
  public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
    return openingBraceOffset;
  }
}
