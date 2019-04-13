/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 15.12.2015.
 */
public class PerlSwitchParserExtensionImpl extends PerlParserExtension implements PerlSwitchParserExtension, PerlElementTypes {
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

  @Override
  public boolean parseStatement(PerlBuilder b, int l) {
    IElementType tokenType = b.getTokenType();

    if (tokenType == RESERVED_SWITCH) {
      PerlBuilder.Marker m = b.mark();
      if (parseSwitchStatement(b, l)) {
        m.done(SWITCH_COMPOUND);
        return true;
      }
      m.rollbackTo();
    }
    else if (tokenType == RESERVED_CASE) {
      return parseCaseSequence(b, l);
    }

    return false;
  }

  @Nullable
  @Override
  public TokenSet getRegexPrefixTokenSet() {
    return BARE_REGEX_PREFIX_TOKEN_SET;
  }

  public static TokenSet getTokenSet() {
    return TOKENS_SET;
  }

  public static boolean parseSwitchStatement(PerlBuilder b, int l) {
    if (PerlParserUtil.consumeToken(b, RESERVED_SWITCH)) {
      boolean r = parseSwitchCondition(b, l);
      r = r && PerlParserImpl.normal_block(b, l);
      return r;
    }
    return false;
  }

  public static boolean parseSwitchCondition(PerlBuilder b, int l) {
    PerlBuilder.Marker m = b.mark();
    boolean r = PerlParserUtil.consumeToken(b, LEFT_PAREN);
    r = r && PerlParserImpl.parse_scalar_expr(b, l);
    r = r && PerlParserUtil.consumeToken(b, RIGHT_PAREN);

    if (r) {
      m.done(SWITCH_CONDITION);
    }
    else {
      m.rollbackTo();
    }

    return r;
  }

  public static boolean parseCaseSequence(PerlBuilder b, int l) {
    int casesNumber = 0;
    while (b.getTokenType() == RESERVED_CASE) {
      PerlBuilder.Marker m = b.mark();
      if (parseCaseStatement(b, l)) {
        m.done(CASE_COMPOUND);
        casesNumber++;
      }
      else {
        m.rollbackTo();
        break;
      }
    }

    if (casesNumber > 0) {
      PerlBuilder.Marker m = b.mark();
      if (PerlParserImpl.if_compound_else(b, l)) {
        m.done(CASE_DEFAULT);
      }
      else {
        m.rollbackTo();
      }
    }
    return casesNumber > 0;
  }

  public static boolean parseCaseStatement(PerlBuilder b, int l) {
    if (PerlParserUtil.consumeToken(b, RESERVED_CASE)) {
      boolean r = parseCaseCondition(b, l);
      r = r && PerlParserImpl.normal_block(b, l);
      return r;
    }
    return false;
  }

  public static boolean parseCaseCondition(PerlBuilder b, int l) {
    PerlBuilder.Marker m = b.mark();

    boolean r = parseCaseConditionParenthesised(b, l);
    r = r || PerlParserImpl.normal_block(b, l);
    r = r || parseCaseConditionSimple(b, l);

    if (r) {
      m.done(CASE_CONDITION);
    }
    else {
      m.rollbackTo();
    }

    return r;
  }

  public static boolean parseCaseConditionParenthesised(PerlBuilder b, int l) {
    boolean r = PerlParserUtil.consumeToken(b, LEFT_PAREN);
    r = r && PerlParserImpl.parse_scalar_expr(b, l);
    r = r && PerlParserUtil.consumeToken(b, RIGHT_PAREN);
    return r;
  }

  public static boolean parseCaseConditionSimple(PerlBuilder b, int l) {
    return PerlParserImpl.string(b, l) ||
           PerlParserImpl.number_constant(b, l) ||
           PerlParserImpl.anon_array(b, l) ||
           PerlParserImpl.match_regex(b, l) ||
           PerlParserImpl.compile_regex(b, l)
      ;
  }
}
