/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.findusages;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlTokenSets.*;


public class PerlWordsScanner extends DefaultWordsScanner implements PerlElementTypes {
  private static final TokenSet IGNORE_CODE_TOKENSET = TokenSet.orSet(
    SIGILS, SPECIAL_STRING_TOKENS, KEYWORDS_TOKENSET, OPERATORS_TOKENSET,
    QUOTE_OPEN_ANY, QUOTE_CLOSE_FIRST_ANY,
    TokenSet.andNot(TAGS_TOKEN_SET, TokenSet.create(TAG_PACKAGE)),
    TokenSet.create(
      REGEX_MODIFIER, REGEX_TOKEN,
      NUMBER, NUMBER_BIN, NUMBER_OCT, NUMBER_HEX,
      OPERATOR_FILETEST, BUILTIN_ARGUMENTLESS, BUILTIN_UNARY, BUILTIN_LIST,
      NUMBER_VERSION, VERSION_ELEMENT, SUB_PROTOTYPE_TOKEN
    ));
  private static final @NotNull TokenSet COMMENTS = TokenSet.andNot(
    TokenSet.orSet(PerlParserDefinition.COMMENTS, TokenSet.create(POD)),
    TokenSet.create(HEREDOC_END)
  );

  public PerlWordsScanner() {
    super(new PerlMergingLexerAdapter(PerlLexingContext.create(null).withEnforcedSublexing(true)),
          PerlParserDefinition.IDENTIFIERS,
          COMMENTS,
          PerlParserDefinition.LITERALS,
          IGNORE_CODE_TOKENSET
    );
  }
}
