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

package com.perl5.lang.pod.lexer

import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet
import com.perl5.lang.perl.lexer.PerlTokenSetsEx
import com.perl5.lang.pod.parser.PodElementTypesGenerated

object PodTokenSetsEx {
  @JvmField
  val WHITE_SPACES: TokenSet = PerlTokenSetsEx.WHITE_SPACES

  @JvmField
  val ALL_WHITE_SPACES: TokenSet = TokenSet.create(TokenType.WHITE_SPACE, PodElementTypesGenerated.POD_NEWLINE)

  @JvmField
  val COMMENTS: TokenSet = TokenSet.create(PodElementTypes.POD_OUTER)

}