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

package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.WhitespacesBinders;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.mason2.elementType.Mason2ElementTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.NAMESPACE_CONTENT;


public class Mason2ParserImpl extends PerlParserImpl implements MasonParser {
  protected static final TokenSet BAD_CHARACTER_FORBIDDEN_TOKENS = TokenSet.orSet(
    PerlParserTokenSets.BAD_CHARACTER_FORBIDDEN_TOKENS, TokenSet.create(
      Mason2ElementTypes.MASON_CLASS_CLOSER,
      Mason2ElementTypes.MASON_INIT_CLOSER,
      Mason2ElementTypes.MASON_PERL_CLOSER,

      Mason2ElementTypes.MASON_AFTER_CLOSER,
      Mason2ElementTypes.MASON_BEFORE_CLOSER,
      Mason2ElementTypes.MASON_AUGMENT_CLOSER,
      Mason2ElementTypes.MASON_AROUND_CLOSER,

      Mason2ElementTypes.MASON_METHOD_CLOSER,
      Mason2ElementTypes.MASON_OVERRIDE_CLOSER,
      Mason2ElementTypes.MASON_FILTER_CLOSER,

      Mason2ElementTypes.MASON_SELF_POINTER,
      Mason2ElementTypes.MASON_FILTERED_BLOCK_OPENER,
      Mason2ElementTypes.MASON_FILTERED_BLOCK_CLOSER
    ));


  protected static final TokenSet SIMPLE_MASON_NAMED_BLOCKS;

  protected static final Map<IElementType, IElementType> RESERVED_OPENER_TO_CLOSER_MAP = new HashMap<>();
  protected static final Map<IElementType, IElementType> RESERVED_TO_STATEMENT_MAP = new HashMap<>();

  static {
    RESERVED_TO_STATEMENT_MAP.put(Mason2ElementTypes.MASON_AROUND_OPENER, Mason2ElementTypes.MASON_AROUND_MODIFIER);
    RESERVED_TO_STATEMENT_MAP.put(Mason2ElementTypes.MASON_AUGMENT_OPENER, Mason2ElementTypes.MASON_AUGMENT_MODIFIER);
    RESERVED_TO_STATEMENT_MAP.put(Mason2ElementTypes.MASON_AFTER_OPENER, Mason2ElementTypes.MASON_AFTER_MODIFIER);
    RESERVED_TO_STATEMENT_MAP.put(Mason2ElementTypes.MASON_BEFORE_OPENER, Mason2ElementTypes.MASON_BEFORE_MODIFIER);

    RESERVED_OPENER_TO_CLOSER_MAP.put(Mason2ElementTypes.MASON_AROUND_OPENER, Mason2ElementTypes.MASON_AROUND_CLOSER);
    RESERVED_OPENER_TO_CLOSER_MAP.put(Mason2ElementTypes.MASON_AUGMENT_OPENER, Mason2ElementTypes.MASON_AUGMENT_CLOSER);
    RESERVED_OPENER_TO_CLOSER_MAP.put(Mason2ElementTypes.MASON_AFTER_OPENER, Mason2ElementTypes.MASON_AFTER_CLOSER);
    RESERVED_OPENER_TO_CLOSER_MAP.put(Mason2ElementTypes.MASON_BEFORE_OPENER, Mason2ElementTypes.MASON_BEFORE_CLOSER);

    SIMPLE_MASON_NAMED_BLOCKS = TokenSet.create(
      RESERVED_TO_STATEMENT_MAP.keySet().toArray(IElementType.EMPTY_ARRAY)
    );
  }

  @Override
  public boolean parseFileContents(PsiBuilder b, int l) {
    PsiBuilder.Marker m = b.mark();
    if (super.parseFileContents(b, l)) {
      m.done(NAMESPACE_CONTENT);
      m.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);

      PsiBuilder.Marker definitionMarker = m.precede();
      definitionMarker.done(Mason2ElementTypes.MASON_NAMESPACE_DEFINITION);
      definitionMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);
      return true;
    }
    m.rollbackTo();
    return false;
  }

  @Override
  public @NotNull TokenSet getBadCharacterForbiddenTokens() {
    return BAD_CHARACTER_FORBIDDEN_TOKENS;
  }
}
