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

package com.perl5.lang.mojolicious.idea.formatter;

import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.perl.idea.formatter.PerlFormattingTokenSets;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;

import static com.perl5.lang.mojolicious.MojoliciousElementTypes.*;


public class MojoliciousIndentProcessor extends PerlIndentProcessor {
  public static final MojoliciousIndentProcessor INSTANCE = new MojoliciousIndentProcessor();

  public static final TokenSet ABSOLUTE_UNINDENTABLE_TOKENS = TokenSet.orSet(
    PerlFormattingTokenSets.ABSOLUTE_UNINDENTABLE_TOKENS,
    TokenSet.create(
      MOJO_LINE_OPENER,
      MOJO_LINE_EXPR_OPENER,
      MOJO_LINE_EXPR_ESCAPED_OPENER,
      MOJO_TEMPLATE_BLOCK_HTML
    ));

  public static final TokenSet UNINDENTABLE_CONTAINERS = TokenSet.orSet(
    PerlFormattingTokenSets.UNINDENTABLE_CONTAINERS,
    TokenSet.create(
      MojoliciousElementTypes.FILE
    ));


  @Override
  public TokenSet getAbsoluteUnindentableTokens() {
    return ABSOLUTE_UNINDENTABLE_TOKENS;
  }

  @Override
  public TokenSet getUnindentableContainers() {
    return UNINDENTABLE_CONTAINERS;
  }
}
