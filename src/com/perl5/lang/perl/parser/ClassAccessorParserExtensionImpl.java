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

package com.perl5.lang.perl.parser;

import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.lang.parser.GeneratedParserUtilBase.Parser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

import static com.intellij.lang.parser.GeneratedParserUtilBase.consumeToken;
import static com.perl5.lang.perl.parser.PerlParserGenerated.default_parenthesised_call_arguments;
import static com.perl5.lang.perl.parser.PerlParserGenerated.optional_expression_parser_;

/**
 * Created by hurricup on 21.01.2016.
 */
public class ClassAccessorParserExtensionImpl extends PerlParserExtension implements ClassAccessorParserExtension {
  protected static final THashMap<String, IElementType> TOKENS_MAP = new THashMap<String, IElementType>();
  final static GeneratedParserUtilBase.Parser FBP_PARSER = (builder_, level_) -> consumeToken(builder_, RESERVED_FOLLOW_BEST_PRACTICE);
  final static GeneratedParserUtilBase.Parser MK_ACCESSOR_PARSER = (builder_, level_) -> consumeToken(builder_, RESERVED_MK_ACCESSORS);
  final static GeneratedParserUtilBase.Parser MK_RO_ACCESSOR_PARSER =
    (builder_, level_) -> consumeToken(builder_, RESERVED_MK_RO_ACCESSORS);
  final static GeneratedParserUtilBase.Parser MK_WO_ACCESSOR_PARSER =
    (builder_, level_) -> consumeToken(builder_, RESERVED_MK_WO_ACCESSORS);
  final static GeneratedParserUtilBase.Parser DECLARATION_PARSER = (builder_, level_) ->
  {
    // fixme implement
    return default_parenthesised_call_arguments(builder_, level_);
  };

  protected static TokenSet TOKENS_SET;

  static {
    TOKENS_MAP.put("follow_best_practice", RESERVED_FOLLOW_BEST_PRACTICE);
    TOKENS_MAP.put("mk_accessors", RESERVED_MK_ACCESSORS);
    TOKENS_MAP.put("mk_ro_accessors", RESERVED_MK_RO_ACCESSORS);
    TOKENS_MAP.put("mk_wo_accessors", RESERVED_MK_WO_ACCESSORS);

    TOKENS_SET = TokenSet.create(TOKENS_MAP.values().toArray(new IElementType[TOKENS_MAP.values().size()]));
  }

  @NotNull
  @Override
  public Map<String, IElementType> getCustomTokensMap() {
    return Collections.emptyMap();
  }

  @Override
  public boolean parseNestedElement(PerlBuilder b, int l) {
    IElementType elementType = b.getTokenType();
    if (elementType == RESERVED_MK_ACCESSORS) {
      return parseAccessorDeclarations(b, l, MK_ACCESSOR_PARSER);
    }
    else if (elementType == RESERVED_MK_RO_ACCESSORS) {
      return parseAccessorDeclarations(b, l, MK_RO_ACCESSOR_PARSER);
    }
    else if (elementType == RESERVED_MK_WO_ACCESSORS) {
      return parseAccessorDeclarations(b, l, MK_WO_ACCESSOR_PARSER);
    }
    else if (elementType == RESERVED_FOLLOW_BEST_PRACTICE) {
      return PerlParserImpl.nested_call(b, l, FBP_PARSER, optional_expression_parser_);
    }

    return super.parseNestedElement(b, l);
  }

  protected boolean parseAccessorDeclarations(PerlBuilder b, int l, Parser subElementParser) {
    return PerlParserImpl.nested_call(b, l, subElementParser, DECLARATION_PARSER);
  }

  public static TokenSet getTokenSet() {
    return TOKENS_SET;
  }
}
