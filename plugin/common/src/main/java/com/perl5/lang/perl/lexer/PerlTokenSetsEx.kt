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

package com.perl5.lang.perl.lexer

import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet
import com.perl5.lang.perl.lexer.PerlTokenSets.HEREDOC_BODIES_TOKENSET
import com.perl5.lang.perl.parser.PerlElementTypesGenerated.*


object PerlTokenSetsEx {
  @JvmStatic
  val WHITE_SPACES: TokenSet = TokenSet.create(TokenType.WHITE_SPACE)

  @JvmStatic
  val REAL_COMMENTS: TokenSet = TokenSet.create(
    COMMENT_LINE, COMMENT_BLOCK, COMMENT_ANNOTATION
  )

  @JvmStatic
  val COMMENTS: TokenSet = TokenSet.orSet(
    HEREDOC_BODIES_TOKENSET,
    REAL_COMMENTS,
    TokenSet.create(
      HEREDOC_END, HEREDOC_END_INDENTABLE
    )
  )

  @JvmStatic
  val WHITE_SPACE_AND_REAL_COMMENTS: TokenSet = TokenSet.orSet(WHITE_SPACES, REAL_COMMENTS)

  @JvmStatic
  val WHITE_SPACE_AND_COMMENTS: TokenSet = TokenSet.orSet(WHITE_SPACES, COMMENTS)

  @JvmStatic
  val MEANINGLESS_TOKENS: TokenSet = TokenSet.orSet(
    WHITE_SPACE_AND_COMMENTS,
    TokenSet.create(POD)
  )

  // fixme inline this
  @JvmStatic
  val LITERALS: TokenSet = PerlTokenSets.STRING_CONTENT_TOKENSET

  @JvmStatic
  val IDENTIFIERS: TokenSet = TokenSet.orSet(
    PerlTokenSets.VARIABLE_NAMES,
    TokenSet.create(SUB_NAME, QUALIFYING_PACKAGE, PACKAGE, IDENTIFIER)
  )
}