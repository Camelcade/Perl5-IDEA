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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lexer.FlexAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlLexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.REGEX_QUOTE_OPEN;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;
import static com.perl5.lang.perl.lexer.PerlLexer.AFTER_VALUE;
import static com.perl5.lang.perl.lexer.PerlTokenSets.*;

public class PerlRegexReplacementElementType extends PerlReparseableElementType {
  public PerlRegexReplacementElementType(@NotNull String debugName,
                                         @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, clazz);
  }

  @Override
  public boolean isParsable(@Nullable ASTNode parent,
                            @NotNull CharSequence buffer,
                            @NotNull Language fileLanguage,
                            @NotNull Project project) {
    FlexAdapter flexLexer = new FlexAdapter(new PerlLexer(null).withProject(project));
    flexLexer.start(buffer);

    IElementType firstType = flexLexer.getTokenType();
    if (firstType == RESERVED_S || firstType == RESERVED_TR || firstType == RESERVED_Y) {
      flexLexer.advance();
    }
    skipSpaces(flexLexer);
    if (flexLexer.getTokenType() != REGEX_QUOTE_OPEN) {
      return false;
    }
    flexLexer.advance();
    IElementType contentTokenType = flexLexer.getTokenType();
    if (LAZY_PARSABLE_REGEX.contains(contentTokenType) || LAZY_TR_STRINGS.contains(contentTokenType)) {
      flexLexer.advance();
    }

    IElementType closeQuoteType = flexLexer.getTokenType();
    if (closeQuoteType == REGEX_QUOTE_CLOSE) {
      flexLexer.advance();
      skipSpaces(flexLexer);
      IElementType secondOpenQuoteType = flexLexer.getTokenType();
      if (secondOpenQuoteType != REGEX_QUOTE_OPEN && secondOpenQuoteType != REGEX_QUOTE_OPEN_E) {
        return false;
      }
    }
    else if (closeQuoteType != REGEX_QUOTE && closeQuoteType != REGEX_QUOTE_E) {
      return false;
    }
    flexLexer.advance();
    IElementType secondBlockContentType = flexLexer.getTokenType();
    if (LAZY_CODE_BLOCKS.contains(secondBlockContentType) ||
        LAZY_REGEX_STRINGS.contains(secondBlockContentType) ||
        LAZY_TR_STRINGS.contains(secondBlockContentType)) {
      flexLexer.advance();
    }

    if (flexLexer.getTokenType() != REGEX_QUOTE_CLOSE) {
      return false;
    }

    flexLexer.advance();
    skipSpaces(flexLexer);
    while (flexLexer.getTokenType() == REGEX_MODIFIER) {
      flexLexer.advance();
    }
    return flexLexer.getTokenType() == null && flexLexer.getState() == AFTER_VALUE;
  }
}
