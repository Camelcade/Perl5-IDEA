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
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import com.perl5.lang.perl.parser.moose.MooseElementTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.intellij.lang.parser.GeneratedParserUtilBase.consumeToken;


public class MooseParserExtension extends PerlParserExtension implements MooseElementTypes, PerlElementTypes {
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

  protected static final AtomicNotNullLazyValue<Map<String, IElementType>> TOKENS_MAP = AtomicNotNullLazyValue.createValue(
    () -> {
      var result = new HashMap<String, IElementType>();
      // in regular case, these tokens should be created in extension class
      result.put(KEYWORD_INNER, RESERVED_INNER);
      result.put(KEYWORD_WITH, RESERVED_WITH);
      result.put(KEYWORD_EXTENDS, RESERVED_EXTENDS);
      result.put(KEYWORD_META, RESERVED_META);
      result.put(KEYWORD_OVERRIDE, RESERVED_OVERRIDE);
      result.put(KEYWORD_AROUND, RESERVED_AROUND);
      result.put(KEYWORD_SUPER, RESERVED_SUPER);
      result.put(KEYWORD_AUGMENT, RESERVED_AUGMENT);
      result.put(KEYWORD_AFTER, RESERVED_AFTER);
      result.put(KEYWORD_BEFORE, RESERVED_BEFORE);
      result.put(KEYWORD_HAS, RESERVED_HAS);
      return result;
    }
  );
  protected static final AtomicNotNullLazyValue<Map<IElementType, IElementType>> RESERVED_TO_STATEMENT_MAP =
    AtomicNotNullLazyValue.createValue(() -> {
      var result = new HashMap<IElementType, IElementType>();

      result.put(RESERVED_WITH, MOOSE_STATEMENT_WITH);
      result.put(RESERVED_EXTENDS, MOOSE_STATEMENT_EXTENDS);
      result.put(RESERVED_META, MOOSE_STATEMENT_META);
      result.put(RESERVED_AROUND, MOOSE_STATEMENT_AROUND);
      result.put(RESERVED_AUGMENT, MOOSE_STATEMENT_AUGMENT);
      result.put(RESERVED_AFTER, MOOSE_STATEMENT_AFTER);
      result.put(RESERVED_BEFORE, MOOSE_STATEMENT_BEFORE);

      return result;
    });
  @SuppressWarnings("unchecked")
  protected static final AtomicNotNullLazyValue<List<Pair<IElementType, TokenSet>>> EXTENSION_SET = AtomicNotNullLazyValue.createValue(
    () -> {
      var result = new ArrayList<Pair<IElementType, TokenSet>>();
      result.add(Pair.create(EXPR, TokenSet.create(RESERVED_TO_STATEMENT_MAP.get().values().toArray(IElementType.EMPTY_ARRAY))));
      return result;
    }
  );
  static final GeneratedParserUtilBase.Parser SUPER_PARSER = (builder_, level_) -> consumeToken(builder_, RESERVED_SUPER);
  static final GeneratedParserUtilBase.Parser INNER_PARSER = (builder_, level_) -> consumeToken(builder_, RESERVED_INNER);
  protected static final NotNullLazyValue<TokenSet> PARSER_TOKEN_SET = NotNullLazyValue.createValue(
    () -> TokenSet.create(RESERVED_TO_STATEMENT_MAP.get().keySet().toArray(IElementType.EMPTY_ARRAY))
  );
  public static final NotNullLazyValue<TokenSet> MOOSE_RESERVED_TOKENSET = NotNullLazyValue.createValue(
    () -> TokenSet.create(TOKENS_MAP.get().values().toArray(IElementType.EMPTY_ARRAY)));

  private static final GeneratedParserUtilBase.Parser FALLBACK_METHOD_PARSER = (builder, level) -> {
    IElementType tokenType = builder.getTokenType();
    if (tokenType != RESERVED_HAS && !PARSER_TOKEN_SET.get().contains(tokenType)) {
      return false;
    }
    PsiBuilder.Marker m = builder.mark();
    builder.advanceLexer();
    m.collapse(SUB_NAME);
    return true;
  };

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
    return EXTENSION_SET.get();
  }

  private static boolean parseOverride(PerlBuilder b, int l) {
    return parseAnnotatedSimpleStatement(b, l, RESERVED_OVERRIDE, MOOSE_STATEMENT_OVERRIDE);
  }

  private static boolean parseHas(PerlBuilder b, int l) {
    if (b.getTokenType() != RESERVED_HAS) {
      return false;
    }
    PsiBuilder.Marker m = b.mark();
    PsiBuilder.Marker sm = b.mark();
    GeneratedParserUtilBase.consumeToken(b, RESERVED_HAS);
    sm.collapse(SUB_NAME);
    sm.precede().done(METHOD);

    if (PerlParserGenerated.any_call_arguments(b, l + 1)) {
      m.done(SUB_CALL);
      return true;
    }
    m.rollbackTo();
    return PerlParserUtil.parseCustomMethod(b, l, FALLBACK_METHOD_PARSER);
  }

  private static boolean parseAnnotatedSimpleStatement(PerlBuilder b, int l, IElementType keywordToken, IElementType statementToken) {
    PsiBuilder.Marker m = b.mark();

    if (GeneratedParserUtilBase.consumeToken(b, keywordToken)) {
      if (PerlParserGenerated.expr(b, l, -1)) {
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
    if (!PARSER_TOKEN_SET.get().contains(tokenType)) {
      return false;
    }

    PsiBuilder.Marker m = b.mark();
    b.advanceLexer();
    if (PerlParserGenerated.expr(b, l, -1)) {
      PerlParserUtil.parseStatementModifier(b, l);
      m.done(RESERVED_TO_STATEMENT_MAP.get().get(tokenType));
      return true;
    }

    m.rollbackTo();
    return PerlParserUtil.parseCustomMethod(b, l, FALLBACK_METHOD_PARSER);
  }
}
