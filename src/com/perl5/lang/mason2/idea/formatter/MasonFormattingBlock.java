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

package com.perl5.lang.mason2.idea.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.htmlmason.idea.formatter.AbstractMasonFormattingBlock;
import com.perl5.lang.mason2.elementType.Mason2ElementTypes;
import com.perl5.lang.perl.idea.formatter.PerlFormattingContext;
import com.perl5.lang.perl.idea.formatter.blocks.PerlFormattingBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 09.01.2016.
 */
public class MasonFormattingBlock extends AbstractMasonFormattingBlock implements Mason2ElementTypes {
  public MasonFormattingBlock(@NotNull ASTNode node,
                              @Nullable Wrap wrap,
                              @Nullable Alignment alignment,
                              @NotNull PerlFormattingContext context
  ) {
    super(node, wrap, alignment, context);
  }

  @Override
  protected IElementType getLineOpenerToken() {
    return MASON_LINE_OPENER;
  }

  @Override
  protected PerlFormattingBlock createBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment) {
    return new MasonFormattingBlock(node, wrap, alignment, myContext);
  }
}
