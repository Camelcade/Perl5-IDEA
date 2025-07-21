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

package com.perl5.lang.mojolicious.idea.folding;

import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.perl.idea.folding.PerlFoldingBuilder;
import org.jetbrains.annotations.NotNull;


public class MojoliciousFoldingBuilder extends PerlFoldingBuilder implements MojoliciousElementTypes {
  protected static final TokenSet COMMENT_EXCLUDED_TOKENS = TokenSet.orSet(
    PerlFoldingBuilder.COMMENT_EXCLUDED_TOKENS,
    TokenSet.create(
      MojoliciousElementTypes.MOJO_BLOCK_OPENER,
      MojoliciousElementTypes.MOJO_BLOCK_CLOSER,
      MojoliciousElementTypes.MOJO_BLOCK_NOSPACE_CLOSER,

      MojoliciousElementTypes.MOJO_BLOCK_EXPR_OPENER,
      MojoliciousElementTypes.MOJO_BLOCK_EXPR_CLOSER,

      MojoliciousElementTypes.MOJO_BLOCK_EXPR_ESCAPED_OPENER,
      MojoliciousElementTypes.MOJO_BLOCK_EXPR_NOSPACE_CLOSER,

      MojoliciousElementTypes.MOJO_LINE_EXPR_ESCAPED_OPENER,
      MojoliciousElementTypes.MOJO_LINE_EXPR_OPENER,
      MojoliciousElementTypes.MOJO_LINE_OPENER,

      MojoliciousElementTypes.MOJO_BLOCK_OPENER_TAG,
      MojoliciousElementTypes.MOJO_LINE_OPENER_TAG,

      MojoliciousElementTypes.MOJO_TEMPLATE_BLOCK_HTML
    ));

  @Override
  protected @NotNull TokenSet getCommentExcludedTokens() {
    return COMMENT_EXCLUDED_TOKENS;
  }
}
