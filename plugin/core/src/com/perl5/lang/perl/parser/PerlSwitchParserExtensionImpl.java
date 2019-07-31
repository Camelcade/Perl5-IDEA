/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser;

import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.RESERVED_CASE;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.RESERVED_SWITCH;


public class PerlSwitchParserExtensionImpl extends PerlParserExtension {
  protected static final TokenSet BARE_REGEX_PREFIX_TOKEN_SET = TokenSet.create(
    RESERVED_CASE
  );
  protected static TokenSet TOKENS_SET = TokenSet.create(
    RESERVED_CASE, RESERVED_SWITCH
  );

  @Override
  public void addHighlighting() {
    super.addHighlighting();
    PerlSyntaxHighlighter.safeMap(PerlSyntaxHighlighter.PERL_KEYWORD, TOKENS_SET);
  }

  @Nullable
  @Override
  public TokenSet getRegexPrefixTokenSet() {
    return BARE_REGEX_PREFIX_TOKEN_SET;
  }

  public static TokenSet getTokenSet() {
    return TOKENS_SET;
  }
}
