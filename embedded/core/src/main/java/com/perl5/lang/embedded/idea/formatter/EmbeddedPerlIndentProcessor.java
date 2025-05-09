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

package com.perl5.lang.embedded.idea.formatter;

import com.intellij.formatting.Indent;
import com.perl5.lang.embedded.psi.EmbeddedPerlTokenSets;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;
import com.perl5.lang.perl.idea.formatter.blocks.PerlAstBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmbeddedPerlIndentProcessor extends PerlIndentProcessor {
  public static final EmbeddedPerlIndentProcessor INSTANCE = new EmbeddedPerlIndentProcessor();

  @Override
  public @Nullable Indent getChildIndent(@NotNull PerlAstBlock block, int newChildIndex) {
    if (block.getElementType() == EmbeddedPerlTokenSets.FILE) {
      return Indent.getNoneIndent();
    }
    return super.getChildIndent(block, newChildIndex);
  }
}
