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

package com.perl5.lang.embedded.psi

import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.perl5.lang.embedded.EmbeddedPerlLanguage
import com.perl5.lang.embedded.psi.EmbeddedPerlElementTypes.*
import com.perl5.lang.perl.lexer.PerlTokenSetsEx
import com.perl5.lang.perl.psi.stubs.PerlFileElementType


object EmbeddedPerlTokenSets {
  @JvmField
  val FILE: IFileElementType = PerlFileElementType("Embedded Perl5", EmbeddedPerlLanguage.INSTANCE)

  @JvmField
  val COMMENTS: TokenSet = TokenSet.orSet(
    PerlTokenSetsEx.COMMENTS,
    TokenSet.create(
      EMBED_TEMPLATE_BLOCK_HTML,
      EMBED_MARKER_OPEN,
      EMBED_MARKER_CLOSE
    )
  )
}