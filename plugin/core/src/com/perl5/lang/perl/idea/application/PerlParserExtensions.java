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

package com.perl5.lang.perl.idea.application;

import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.parser.PerlParserImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class PerlParserExtensions implements BaseComponent {
  public PerlParserExtensions() {
  }

  @Override
  public final void initComponent() {
    PerlParserDefinition.PARSER_EXTENSIONS.clear();
    PerlLexer.initReservedTokensMap();
    for (PerlParserExtension extension : PerlParserExtension.EP_NAME.getExtensions()) {
      // register extension
      PerlParserDefinition.PARSER_EXTENSIONS.add(extension);

      // add tokens to lex
      PerlLexer.CUSTOM_TOKEN_TYPES.putAll(extension.getCustomTokensMap());
      PerlLexer.CUSTOM_TOKEN_TYPES_AFTER_DEREFERENCE.putAll(extension.getCustomTokensAfterDereferenceMap());

      // add regex prefix tokenset
      if (extension.getRegexPrefixTokenSet() != null) {
        PerlLexer.BARE_REGEX_PREFIX_TOKENSET = TokenSet.orSet(PerlLexer.BARE_REGEX_PREFIX_TOKENSET, extension.getRegexPrefixTokenSet());
      }

      // add extensions tokens
      List<Pair<IElementType, TokenSet>> extensionSets = extension.getExtensionSets();
      if (extensionSets != null) {
        for (Pair<IElementType, TokenSet> extensionSet : extensionSets) {
          for (int i = 0; i < PerlParserImpl.EXTENDS_SETS_.length; i++) {
            if (PerlParserImpl.EXTENDS_SETS_[i].contains(extensionSet.first)) {
              PerlParserImpl.EXTENDS_SETS_[i] = TokenSet.orSet(PerlParserImpl.EXTENDS_SETS_[i], extensionSet.getSecond());
              break;
            }
          }
        }
      }
    }
    PerlLexer.initReservedTokensSet();
  }

  @Override
  public void disposeComponent() {
    // TODO: insert component disposal logic here
  }

  @Override
  @NotNull
  public String getComponentName() {
    return "PerlParserExtensions";
  }
}
