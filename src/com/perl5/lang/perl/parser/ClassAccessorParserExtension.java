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

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.parser.Class.Accessor.ClassAccessorElementTypes;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.intellij.lang.parser.GeneratedParserUtilBase.consumeToken;
import static com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter.PERL_SUB_DEFINITION;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.SUB_NAME;

/**
 * Created by hurricup on 21.01.2016.
 */
public class ClassAccessorParserExtension extends PerlParserExtension implements ClassAccessorElementTypes {
  protected static final THashMap<String, IElementType> TOKENS_MAP = new THashMap<>();
  private static final GeneratedParserUtilBase.Parser FBP_PARSER =
    (builder_, level_) -> consumeToken(builder_, RESERVED_FOLLOW_BEST_PRACTICE);

  protected static TokenSet TOKENS_SET;

  static {
    TOKENS_MAP.put("follow_best_practice", RESERVED_FOLLOW_BEST_PRACTICE);
    TOKENS_MAP.put("mk_accessors", RESERVED_MK_ACCESSORS);
    TOKENS_MAP.put("mk_ro_accessors", RESERVED_MK_RO_ACCESSORS);
    TOKENS_MAP.put("mk_wo_accessors", RESERVED_MK_WO_ACCESSORS);

    TOKENS_SET = TokenSet.create(TOKENS_MAP.values().toArray(new IElementType[TOKENS_MAP.values().size()]));
    PerlSyntaxHighlighter.safeMap(PERL_SUB_DEFINITION, TOKENS_SET);
  }

  @NotNull
  @Override
  public Map<String, IElementType> getCustomTokensMap() {
    return TOKENS_MAP;
  }

  @Override
  public boolean parseNestedElement(PerlBuilder b, int l) {
    IElementType elementType = b.getTokenType();
    if (elementType == RESERVED_MK_ACCESSORS) {
      return parseAccessorDeclarations(b, l, CLASS_ACCESSOR_WRAPPER);
    }
    else if (elementType == RESERVED_MK_RO_ACCESSORS) {
      return parseAccessorDeclarations(b, l, CLASS_ACCESSOR_WRAPPER_RO);
    }
    else if (elementType == RESERVED_MK_WO_ACCESSORS) {
      return parseAccessorDeclarations(b, l, CLASS_ACCESSOR_WRAPPER_WO);
    }
    else if (elementType == RESERVED_FOLLOW_BEST_PRACTICE) {
      return parseAccessorDeclarations(b, l, CLASS_ACCESSOR_FBP);
    }

    return super.parseNestedElement(b, l);
  }

  protected boolean parseAccessorDeclarations(@NotNull PerlBuilder builder, int level, @NotNull IElementType wrapperTokenType) {
    PsiBuilder.Marker wrapperMarker = builder.mark();
    if (PerlParserImpl.nested_call_inner(
      builder, level,
      (b, l) -> {
        PsiBuilder.Marker m = b.mark();
        b.advanceLexer();
        m.collapse(SUB_NAME);
        return true;
      },
      PerlParserImpl.optional_expression_parser_
    )) {
      wrapperMarker.done(wrapperTokenType);
      return true;
    }
    else {
      wrapperMarker.rollbackTo();
      return false;
    }
  }

  public static TokenSet getTokenSet() {
    return TOKENS_SET;
  }
}
