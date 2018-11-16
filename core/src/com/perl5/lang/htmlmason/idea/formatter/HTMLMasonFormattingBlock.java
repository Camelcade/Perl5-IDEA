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

package com.perl5.lang.htmlmason.idea.formatter;

import com.intellij.lang.ASTNode;
import com.perl5.lang.htmlmason.HTMLMasonElementPatterns;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.perl.idea.formatter.PerlFormattingContext;
import com.perl5.lang.perl.idea.formatter.blocks.PerlFormattingBlock;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 08.03.2016.
 */
public class HTMLMasonFormattingBlock extends PerlFormattingBlock implements HTMLMasonElementTypes, HTMLMasonElementPatterns {
  public HTMLMasonFormattingBlock(@NotNull ASTNode node, @NotNull PerlFormattingContext context
  ) {
    super(node, context);
  }

  @Override
  protected PerlFormattingBlock createBlock(@NotNull ASTNode node) {
    return new HTMLMasonFormattingBlock(node, myContext);
  }
}
