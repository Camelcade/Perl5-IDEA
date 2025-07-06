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
package com.perl5.lang.mojolicious

import com.intellij.psi.tree.TokenSet
import com.perl5.lang.mojolicious.MojoliciousElementTypes.Companion.MOJO_BLOCK_CLOSER
import com.perl5.lang.mojolicious.MojoliciousElementTypes.Companion.MOJO_BLOCK_EXPR_ESCAPED_OPENER
import com.perl5.lang.mojolicious.MojoliciousElementTypes.Companion.MOJO_BLOCK_EXPR_OPENER
import com.perl5.lang.mojolicious.MojoliciousElementTypes.Companion.MOJO_BLOCK_NOSPACE_CLOSER
import com.perl5.lang.mojolicious.MojoliciousElementTypes.Companion.MOJO_BLOCK_OPENER
import com.perl5.lang.mojolicious.MojoliciousElementTypes.Companion.MOJO_BLOCK_OPENER_TAG
import com.perl5.lang.mojolicious.MojoliciousElementTypes.Companion.MOJO_LINE_EXPR_ESCAPED_OPENER
import com.perl5.lang.mojolicious.MojoliciousElementTypes.Companion.MOJO_LINE_EXPR_OPENER
import com.perl5.lang.mojolicious.MojoliciousElementTypes.Companion.MOJO_LINE_OPENER
import com.perl5.lang.mojolicious.MojoliciousElementTypes.Companion.MOJO_LINE_OPENER_TAG
import com.perl5.lang.mojolicious.MojoliciousElementTypes.Companion.MOJO_TEMPLATE_BLOCK_HTML
import com.perl5.lang.perl.lexer.PerlTokenSetsEx


object MojoTokenSets {
  @JvmField
  val LINE_OPENERS: TokenSet = TokenSet.create(
    MOJO_LINE_OPENER,
    MOJO_LINE_EXPR_OPENER,
    MOJO_LINE_EXPR_ESCAPED_OPENER
  )

  @JvmField
  val COMMENTS: TokenSet = TokenSet.orSet(
    PerlTokenSetsEx.COMMENTS,
    TokenSet.create(
      MOJO_TEMPLATE_BLOCK_HTML,

      MOJO_BLOCK_OPENER,
      MOJO_BLOCK_CLOSER,
      MOJO_BLOCK_NOSPACE_CLOSER,

      MOJO_LINE_OPENER,
      MOJO_LINE_EXPR_OPENER,
      MOJO_LINE_EXPR_ESCAPED_OPENER,

      MOJO_BLOCK_EXPR_OPENER,
      MOJO_BLOCK_EXPR_ESCAPED_OPENER,

      MOJO_BLOCK_OPENER_TAG,
      MOJO_LINE_OPENER_TAG
    )
  )

}
