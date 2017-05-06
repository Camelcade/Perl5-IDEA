/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.mason2.idea.formatter;

import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.mason2.Mason2TemplatingParserDefinition;
import com.perl5.lang.mason2.elementType.Mason2ElementTypes;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;

/**
 * Created by hurricup on 09.01.2016.
 */
public class MasonIndentProcessor extends PerlIndentProcessor implements Mason2ElementTypes {
  public static final MasonIndentProcessor INSTANCE = new MasonIndentProcessor();

  public static final TokenSet ABSOLUTE_UNINDENTABLE_TOKENS = TokenSet.orSet(
    PerlIndentProcessor.ABSOLUTE_UNINDENTABLE_TOKENS,
    TokenSet.create(
      MASON_LINE_OPENER,
      MASON_TEMPLATE_BLOCK_HTML
    ));

  public static final TokenSet UNINDENTABLE_CONTAINERS = TokenSet.orSet(
    PerlIndentProcessor.UNINDENTABLE_CONTAINERS,
    TokenSet.create(
      Mason2TemplatingParserDefinition.FILE,

      MASON_AUGMENT_MODIFIER,
      MASON_AFTER_MODIFIER,
      MASON_BEFORE_MODIFIER,
      MASON_AROUND_MODIFIER,

      MASON_FILTER_DEFINITION,
      MASON_METHOD_DEFINITION,
      MASON_OVERRIDE_DEFINITION
    ));

  public static final TokenSet UNINDENTABLE_TOKENS = TokenSet.orSet(
    PerlIndentProcessor.UNINDENTABLE_TOKENS,
    TokenSet.create(
      MASON_METHOD_OPENER,
      MASON_METHOD_CLOSER,

      MASON_OVERRIDE_OPENER,
      MASON_OVERRIDE_CLOSER,

      MASON_AFTER_OPENER,
      MASON_AFTER_CLOSER,

      MASON_BEFORE_OPENER,
      MASON_BEFORE_CLOSER,

      MASON_AROUND_OPENER,
      MASON_AROUND_CLOSER,

      MASON_AUGMENT_OPENER,
      MASON_AUGMENT_CLOSER,

      MASON_FLAGS_OPENER,
      MASON_FLAGS_CLOSER,

      MASON_INIT_OPENER,
      MASON_INIT_CLOSER,

      MASON_CLASS_OPENER,
      MASON_CLASS_CLOSER,

      MASON_PERL_OPENER,
      MASON_PERL_CLOSER
    ));


  @Override
  public TokenSet getAbsoluteUnindentableTokens() {
    return ABSOLUTE_UNINDENTABLE_TOKENS;
  }

  @Override
  public TokenSet getUnindentableContainers() {
    return UNINDENTABLE_CONTAINERS;
  }

  @Override
  public TokenSet getUnindentableTokens() {
    return UNINDENTABLE_TOKENS;
  }
}
