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
import com.perl5.lang.perl.lexer.PerlLexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Two quoted quote-like operators element type: strings, matching regexp, string list, regex compile
 */
public abstract class PerlTwoQuotesQuoteLikeElementType extends PerlQuoteLikeElementType {
  public PerlTwoQuotesQuoteLikeElementType(@NotNull String debugName,
                                           @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, clazz);
  }

  protected abstract boolean isOperatorToken(@Nullable IElementType tokenType);

  protected abstract boolean isOperatorMandatory();

  protected abstract boolean isOpenQuoteToken(@Nullable IElementType tokenType);

  protected abstract boolean isContentToken(@Nullable IElementType tokenType);

  protected abstract boolean isCloseQuoteToken(@Nullable IElementType tokenType);

  @Override
  protected boolean isReparseableOld(@NotNull ASTNode parent,
                                     @NotNull CharSequence buffer,
                                     @NotNull Language fileLanguage,
                                     @NotNull Project project) {
    Lexer lexer = createLexer(parent, this);
    lexer.start(buffer);
    if (isOperatorToken(lexer.getTokenType())) {
      lexer.advance();
      skipSpaces(lexer);
    }
    else if (isOperatorMandatory()) {
      return false;
    }

    if (!isOpenQuoteToken(lexer.getTokenType())) {
      return false;
    }
    lexer.advance();
    while (true) {
      IElementType tokenType = lexer.getTokenType();
      if (tokenType == null) {
        return false;
      }
      else if (isCloseQuoteToken(tokenType)) {
        break;
      }
      lexer.advance();
    }
    lexer.advance();
    if (!checkAfterCloseQuote(lexer)) {
      return false;
    }
    return lexer.getTokenType() == null && lexer.getState() == PerlLexer.AFTER_VALUE;
  }

  protected boolean checkAfterCloseQuote(@NotNull Lexer flexAdapter) {
    return true;
  }
}
