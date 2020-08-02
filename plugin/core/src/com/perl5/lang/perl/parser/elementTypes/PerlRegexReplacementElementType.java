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
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;
import static com.perl5.lang.perl.lexer.PerlLexer.AFTER_VALUE;

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
    if (parent == null) {
      return false;
    }
    Lexer lexer = createLexer(parent);
    lexer.start(buffer);

    IElementType firstType = lexer.getTokenType();
    if (firstType == RESERVED_S || firstType == RESERVED_TR || firstType == RESERVED_Y) {
      lexer.advance();
    }
    skipSpaces(lexer);
    if (lexer.getTokenType() != REGEX_QUOTE_OPEN) {
      return false;
    }
    lexer.advance();

    IElementType closeQuoteType;
    while (true) {
      closeQuoteType = lexer.getTokenType();
      if (closeQuoteType == null) {
        return false;
      }
      else if (closeQuoteType == REGEX_QUOTE_CLOSE || closeQuoteType == REGEX_QUOTE || closeQuoteType == REGEX_QUOTE_E) {
        break;
      }
      lexer.advance();
    }

    if (closeQuoteType == REGEX_QUOTE_CLOSE) {
      lexer.advance();
      skipSpaces(lexer);
      IElementType secondOpenQuoteType = lexer.getTokenType();
      if (secondOpenQuoteType != REGEX_QUOTE_OPEN && secondOpenQuoteType != REGEX_QUOTE_OPEN_E) {
        return false;
      }
    }
    lexer.advance();

    while (true) {
      closeQuoteType = lexer.getTokenType();
      if (closeQuoteType == null) {
        return false;
      }
      else if (closeQuoteType == REGEX_QUOTE_CLOSE) {
        break;
      }
      lexer.advance();
    }

    lexer.advance();
    skipSpaces(lexer);
    while (lexer.getTokenType() == REGEX_MODIFIER) {
      lexer.advance();
    }
    return lexer.getTokenType() == null && lexer.getState() == AFTER_VALUE;
  }
}
