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
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.mojolicious.MojoliciousElementTypes.*;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.BLOCK_BRACELESS;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.SUB_EXPR;
import static com.perl5.lang.perl.parser.PerlParserTokenSets.BAD_CHARACTER_FORBIDDEN_TOKENS;


public class MojoliciousParser extends PerlParserImpl {
  public static final TokenSet BAD_CAHARACTER_FORBIDDEN_TOKENS = TokenSet.orSet(
    BAD_CHARACTER_FORBIDDEN_TOKENS,
    TokenSet.create(
      MOJO_BLOCK_EXPR_CLOSER,
      MOJO_BLOCK_EXPR_NOSPACE_CLOSER,
      MOJO_BLOCK_CLOSER_SEMI,
      MOJO_END
    ));
  public static final TokenSet CONSUMABLE_SEMI_TOKENS = TokenSet.orSet(
    PerlParserTokenSets.CONSUMABLE_SEMI_TOKENS, TokenSet.create(
      MOJO_BLOCK_EXPR_CLOSER,
      MOJO_BLOCK_CLOSER_SEMI,
      MOJO_BLOCK_EXPR_NOSPACE_CLOSER
    ));
  public static final TokenSet UNCONSUMABLE_SEMI_TOKENS = TokenSet.orSet(
    PerlParserTokenSets.UNCONSUMABLE_SEMI_TOKENS, TokenSet.create(
      MOJO_END
    ));


  @Override
  public boolean parseTerm(PsiBuilder b, int l) {
    IElementType tokenType = b.getTokenType();

    if (tokenType == MOJO_BEGIN) {
      PsiBuilder.Marker subMarker = b.mark();
      b.advanceLexer();
      PsiBuilder.Marker blockMarker = b.mark();

      PerlParserProxy.block_content(b, l);

      if (b.getTokenType() == MOJO_END) {
        blockMarker.done(BLOCK_BRACELESS);
        blockMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);
        subMarker.done(SUB_EXPR);
        return true;
      }
      else {
        blockMarker.drop();
        subMarker.rollbackTo();
      }
    }

    return super.parseTerm(b, l);
  }

  @Override
  public boolean parseStatementSemi(PsiBuilder b, int l) {
    if (b.getTokenType() == MOJO_END) {
      b.advanceLexer();
      return true;
    }
    return super.parseStatementSemi(b, l);
  }

  @Override
  public @NotNull TokenSet getBadCharacterForbiddenTokens() {
    return BAD_CAHARACTER_FORBIDDEN_TOKENS;
  }

  @Override
  public @NotNull TokenSet getConsumableSemicolonTokens() {
    return CONSUMABLE_SEMI_TOKENS;
  }

  @Override
  public @NotNull TokenSet getUnconsumableSemicolonTokens() {
    return UNCONSUMABLE_SEMI_TOKENS;
  }
}
