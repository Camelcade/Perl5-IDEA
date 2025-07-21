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
package com.perl5.lang.embedded.idea.formatter

import com.intellij.formatting.Indent
import com.perl5.lang.embedded.psi.EmbeddedPerlElementTypes
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor
import com.perl5.lang.perl.idea.formatter.blocks.PerlAstBlock

object EmbeddedPerlIndentProcessor : PerlIndentProcessor() {
  override fun getChildIndent(block: PerlAstBlock, newChildIndex: Int): Indent? =
    if (block.elementType === EmbeddedPerlElementTypes.FILE) Indent.getNoneIndent() else super.getChildIndent(block, newChildIndex)
}
