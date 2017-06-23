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
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

import static com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter.PERL_SUB_DEFINITION;

/**
 * Created by hurricup on 23.04.2016.
 */
public class MojoParserExtension extends PerlParserExtensionBase implements MojoliciousElementTypes {
  protected static final THashMap<String, IElementType> TOKENS_MAP = new THashMap<>();
  protected static TokenSet TOKENS_SET;

  static {
    TOKENS_MAP.put(KEYWORD_MOJO_HELPER_METHOD, MOJO_HELPER_METHOD);

    TOKENS_SET = TokenSet.create(TOKENS_MAP.values().toArray(new IElementType[TOKENS_MAP.values().size()]));
    PerlSyntaxHighlighter.safeMap(PERL_SUB_DEFINITION, TOKENS_SET);
  }

  @NotNull
  @Override
  public Map<String, IElementType> getCustomTokensMap() {
    return Collections.emptyMap();
  }

  @Override
  public boolean parseNestedElement(PerlBuilder builder, int level) {
    if (builder.getTokenType() == MOJO_HELPER_METHOD) {
      return parseCustomNestedCall(builder, level, MOJO_HELPER_WRAPPER);
    }
    return super.parseNestedElement(builder, level);
  }
}

