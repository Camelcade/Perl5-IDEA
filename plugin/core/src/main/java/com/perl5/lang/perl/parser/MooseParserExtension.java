/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import com.perl5.lang.perl.parser.moose.MooseElementTypes;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.intellij.lang.parser.GeneratedParserUtilBase.consumeToken;


public class MooseParserExtension extends PerlParserExtension implements MooseElementTypes, PerlElementTypes {
  protected static final Map<String, IElementType> TOKENS_MAP = new HashMap<>();
  protected static final Map<IElementType, IElementType> RESERVED_TO_STATEMENT_MAP = new HashMap<>();
  @SuppressWarnings("unchecked")
  protected static final List<Pair<IElementType, TokenSet>> EXTENSION_SET = new ArrayList<>();
  static final GeneratedParserUtilBase.Parser SUPER_PARSER = (builder_, level_) -> consumeToken(builder_, RESERVED_SUPER);
  static final GeneratedParserUtilBase.Parser INNER_PARSER = (builder_, level_) -> consumeToken(builder_, RESERVED_INNER);
  protected static TokenSet PARSER_TOKEN_SET;
  public static final TokenSet MOOSE_RESERVED_TOKENSET;

  public static final String KEYWORD_AFTER = "after";
  public static final String KEYWORD_BEFORE = "before";
  public static final String KEYWORD_HAS = "has";
  public static final String KEYWORD_AUGMENT = "augment";
  public static final String KEYWORD_SUPER = "super";
  public static final String KEYWORD_AROUND = "around";
  public static final String KEYWORD_OVERRIDE = "override";
  public static final String KEYWORD_META = "meta";
  public static final String KEYWORD_EXTENDS = "extends";
  public static final String KEYWORD_WITH = "with";
  public static final String KEYWORD_INNER = "inner";
  private static final GeneratedParserUtilBase.Parser FALLBACK_METHOD_PARSER = (builder, level) -> {
    IElementType tokenType = builder.getTokenType();
    if (tokenType != RESERVED_HAS && !PARSER_TOKEN_SET.contains(tokenType)) {
      return false;
    }
    PsiBuilder.Marker m = builder.mark();
    builder.advanceLexer();
    m.collapse(SUB_NAME);
    return true;
  };

  static {
    // in regular case, these tokens should be created in extension class
    TOKENS_MAP.put(KEYWORD_INNER, RESERVED_INNER);
    TOKENS_MAP.put(KEYWORD_WITH, RESERVED_WITH);
    TOKENS_MAP.put(KEYWORD_EXTENDS, RESERVED_EXTENDS);
    TOKENS_MAP.put(KEYWORD_META, RESERVED_META);
    TOKENS_MAP.put(KEYWORD_OVERRIDE, RESERVED_OVERRIDE);
    TOKENS_MAP.put(KEYWORD_AROUND, RESERVED_AROUND);
    TOKENS_MAP.put(KEYWORD_SUPER, RESERVED_SUPER);
    TOKENS_MAP.put(KEYWORD_AUGMENT, RESERVED_AUGMENT);
    TOKENS_MAP.put(KEYWORD_AFTER, RESERVED_AFTER);
    TOKENS_MAP.put(KEYWORD_BEFORE, RESERVED_BEFORE);
    TOKENS_MAP.put(KEYWORD_HAS, RESERVED_HAS);

    RESERVED_TO_STATEMENT_MAP.put(RESERVED_WITH, MOOSE_STATEMENT_WITH);
    RESERVED_TO_STATEMENT_MAP.put(RESERVED_EXTENDS, MOOSE_STATEMENT_EXTENDS);
    RESERVED_TO_STATEMENT_MAP.put(RESERVED_META, MOOSE_STATEMENT_META);
    RESERVED_TO_STATEMENT_MAP.put(RESERVED_AROUND, MOOSE_STATEMENT_AROUND);
    RESERVED_TO_STATEMENT_MAP.put(RESERVED_AUGMENT, MOOSE_STATEMENT_AUGMENT);
    RESERVED_TO_STATEMENT_MAP.put(RESERVED_AFTER, MOOSE_STATEMENT_AFTER);
    RESERVED_TO_STATEMENT_MAP.put(RESERVED_BEFORE, MOOSE_STATEMENT_BEFORE);


    List<IElementType> tokensList = new ArrayList<>(RESERVED_TO_STATEMENT_MAP.values());
    EXTENSION_SET.add(Pair.create(EXPR, TokenSet.create(tokensList.toArray(IElementType.EMPTY_ARRAY))));

    PARSER_TOKEN_SET =
      TokenSet.create(RESERVED_TO_STATEMENT_MAP.keySet().toArray(IElementType.EMPTY_ARRAY));

    Collection<IElementType> reservedTokens = TOKENS_MAP.values();
    MOOSE_RESERVED_TOKENSET = TokenSet.create(reservedTokens.toArray(IElementType.EMPTY_ARRAY));
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

  @Override
  public @NotNull List<Pair<IElementType, TokenSet>> getExtensionSets() {
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
    PsiBuilder.Marker sm = b.mark();
    PerlParserUtil.consumeToken(b, RESERVED_HAS);
    sm.collapse(SUB_NAME);
    sm.precede().done(METHOD);

    if (PerlParserImpl.any_call_arguments(b, l + 1)) {
      m.done(SUB_CALL);
      return true;
    }
    m.rollbackTo();
    return PerlParserUtil.parseCustomMethod(b, l, FALLBACK_METHOD_PARSER);
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

    IElementType tokenType = b.getTokenType();
    if (!PARSER_TOKEN_SET.contains(tokenType)) {
      return false;
    }

    PerlBuilder.Marker m = b.mark();
    b.advanceLexer();
    if (PerlParserImpl.expr(b, l, -1)) {
      PerlParserUtil.parseStatementModifier(b, l);
      m.done(RESERVED_TO_STATEMENT_MAP.get(tokenType));
      return true;
    }

    m.rollbackTo();
    return PerlParserUtil.parseCustomMethod(b, l, FALLBACK_METHOD_PARSER);
  }
}
