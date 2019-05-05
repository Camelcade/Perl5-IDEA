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

import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import com.perl5.lang.perl.parser.moose.MooseElementTypes;
import gnu.trove.THashMap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.intellij.lang.parser.GeneratedParserUtilBase.consumeToken;


public class MooseParserExtension extends PerlParserExtension implements MooseElementTypes, PerlElementTypes {
  protected static final THashMap<String, IElementType> TOKENS_MAP = new THashMap<>();
  protected static final THashMap<IElementType, IElementType> RESERVED_TO_STATEMENT_MAP = new THashMap<>();
  @SuppressWarnings("unchecked")
  protected static final List<Pair<IElementType, TokenSet>> EXTENSION_SET = new ArrayList<>();
  final static GeneratedParserUtilBase.Parser SUPER_PARSER = (builder_, level_) -> consumeToken(builder_, RESERVED_SUPER);
  final static GeneratedParserUtilBase.Parser INNER_PARSER = (builder_, level_) -> consumeToken(builder_, RESERVED_INNER);
  protected static TokenSet PARSER_TOKEN_SET;
  public static final TokenSet MOOSE_RESERVED_TOKENSET;

  static {
    // in regular case, these tokens should be created in extension class
    TOKENS_MAP.put("inner", RESERVED_INNER);
    TOKENS_MAP.put("with", RESERVED_WITH);
    TOKENS_MAP.put("extends", RESERVED_EXTENDS);
    TOKENS_MAP.put("meta", RESERVED_META);
    TOKENS_MAP.put("override", RESERVED_OVERRIDE);
    TOKENS_MAP.put("around", RESERVED_AROUND);
    TOKENS_MAP.put("super", RESERVED_SUPER);
    TOKENS_MAP.put("augment", RESERVED_AUGMENT);
    TOKENS_MAP.put("after", RESERVED_AFTER);
    TOKENS_MAP.put("before", RESERVED_BEFORE);
    TOKENS_MAP.put("has", RESERVED_HAS);

    RESERVED_TO_STATEMENT_MAP.put(RESERVED_WITH, MOOSE_STATEMENT_WITH);
    RESERVED_TO_STATEMENT_MAP.put(RESERVED_EXTENDS, MOOSE_STATEMENT_EXTENDS);
    RESERVED_TO_STATEMENT_MAP.put(RESERVED_META, MOOSE_STATEMENT_META);
    RESERVED_TO_STATEMENT_MAP.put(RESERVED_AROUND, MOOSE_STATEMENT_AROUND);
    RESERVED_TO_STATEMENT_MAP.put(RESERVED_AUGMENT, MOOSE_STATEMENT_AUGMENT);
    RESERVED_TO_STATEMENT_MAP.put(RESERVED_AFTER, MOOSE_STATEMENT_AFTER);
    RESERVED_TO_STATEMENT_MAP.put(RESERVED_BEFORE, MOOSE_STATEMENT_BEFORE);


    List<IElementType> tokensList = new ArrayList<>();
    tokensList.add(MOOSE_HAS_EXPR);
    tokensList.addAll(RESERVED_TO_STATEMENT_MAP.values());
    EXTENSION_SET.add(Pair.create(EXPR, TokenSet.create(tokensList.toArray(new IElementType[tokensList.size()]))));

    PARSER_TOKEN_SET =
      TokenSet.create(RESERVED_TO_STATEMENT_MAP.keySet().toArray(new IElementType[RESERVED_TO_STATEMENT_MAP.keySet().size()]));

    Collection<IElementType> reservedTokens = TOKENS_MAP.values();
    MOOSE_RESERVED_TOKENSET = TokenSet.create(reservedTokens.toArray(new IElementType[reservedTokens.size()]));
  }

  @Override
  public boolean parseTerm(PerlBuilder b, int l) {
    return parseOverride(b, l) ||
           parseHas(b, l) ||
           parseDefault(b, l) ||
           PerlParserUtil.parseCustomCallExpr(b, l, INNER_PARSER) ||
           PerlParserUtil.parseCustomCallExpr(b, l, SUPER_PARSER) ||
           super.parseTerm(b, l);
  }

  @Nullable
  @Override
  public List<Pair<IElementType, TokenSet>> getExtensionSets() {
    return EXTENSION_SET;
  }

  private static boolean parseOverride(PerlBuilder b, int l) {
    return parseAnnotatedSimpleStatement(b, l, RESERVED_OVERRIDE, MOOSE_STATEMENT_OVERRIDE);
  }

  private static boolean parseHas(PerlBuilder b, int l) {
    if (b.getTokenType() != RESERVED_HAS) {
      return false;
    }
    PerlBuilder.Marker m = b.mark();
    PerlParserUtil.consumeToken(b, RESERVED_HAS);

    PerlBuilder.Marker wrapperMarker = b.mark();
    if (PerlParserImpl.parse_list_expr(b, l + 1)) {
      wrapperMarker.done(MOOSE_ATTRIBUTE_WRAPPER);
      m.done(MOOSE_HAS_EXPR);
      return true;
    }
    wrapperMarker.drop();
    m.error("Incomplete has expression");
    return true;
  }

  private static boolean parseAnnotatedSimpleStatement(PerlBuilder b, int l, IElementType keywordToken, IElementType statementToken) {
    PerlBuilder.Marker m = b.mark();

    if (PerlParserUtil.consumeToken(b, keywordToken)) {
      if (PerlParserImpl.expr(b, l, -1)) {
        PerlParserUtil.parseStatementModifier(b, l);
        m.done(statementToken);
        return true;
      }
    }

    m.rollbackTo();
    return false;
  }

  private static boolean parseDefault(PerlBuilder b, int l) {
    PerlBuilder.Marker m = b.mark();

    IElementType tokenType = b.getTokenType();
    if (PARSER_TOKEN_SET.contains(tokenType)) {
      b.advanceLexer();
      if (PerlParserImpl.expr(b, l, -1)) {
        PerlParserUtil.parseStatementModifier(b, l);
        m.done(RESERVED_TO_STATEMENT_MAP.get(tokenType));
        return true;
      }
    }

    m.rollbackTo();
    return false;
  }
}
