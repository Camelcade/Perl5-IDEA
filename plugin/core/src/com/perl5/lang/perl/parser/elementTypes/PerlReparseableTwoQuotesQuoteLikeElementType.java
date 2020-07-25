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

/**
 * Two quoted quote-like operators element type: strings, matching regexp, string list, regex compile
 */
public abstract class PerlReparseableTwoQuotesQuoteLikeElementType extends PerlReparseableQuoteLikeElementType {
  public PerlReparseableTwoQuotesQuoteLikeElementType(@NotNull String debugName,
                                                      @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, clazz);
  }

  protected abstract boolean isOperatorToken(@Nullable IElementType tokenType);

  protected abstract boolean isOperatorMandatory();

  protected abstract boolean isOpenQuoteToken(@Nullable IElementType tokenType);

  protected abstract boolean isContentToken(@Nullable IElementType tokenType);

  protected abstract boolean isCloseQuoteToken(@Nullable IElementType tokenType);

  @Override
  public boolean isParsable(@Nullable ASTNode parent,
                            @NotNull CharSequence buffer,
                            @NotNull Language fileLanguage,
                            @NotNull Project project) {
    FlexAdapter flexAdapter = createLexer(project);
    flexAdapter.start(buffer);
    if (isOperatorToken(flexAdapter.getTokenType())) {
      flexAdapter.advance();
      skipSpaces(flexAdapter);
    }
    else if (isOperatorMandatory()) {
      return false;
    }

    if (!isOpenQuoteToken(flexAdapter.getTokenType())) {
      return false;
    }
    flexAdapter.advance();
    if (isContentToken(flexAdapter.getTokenType())) {
      flexAdapter.advance();
    }
    if (!isCloseQuoteToken(flexAdapter.getTokenType())) {
      return false;
    }
    flexAdapter.advance();
    if (!checkAfterCloseQuote(flexAdapter)) {
      return false;
    }
    return flexAdapter.getTokenType() == null && flexAdapter.getState() == PerlLexer.AFTER_VALUE;
  }

  protected boolean checkAfterCloseQuote(@NotNull FlexAdapter flexAdapter) {
    return true;
  }
}
