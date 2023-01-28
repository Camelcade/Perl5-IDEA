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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lexer.Lexer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

public class PerlMatchRegexpElementType extends PerlTwoQuotesQuoteLikeElementType {
  public PerlMatchRegexpElementType(@NotNull String debugName,
                                    @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, clazz);
  }

  @Override
  protected boolean isOperatorToken(@Nullable IElementType tokenType) {
    return tokenType == RESERVED_M;
  }

  @Override
  protected boolean isOperatorMandatory() {
    return false;
  }

  @Override
  protected boolean isOpenQuoteToken(@Nullable IElementType tokenType) {
    return tokenType == REGEX_QUOTE_OPEN;
  }

  @Override
  protected boolean isCloseQuoteToken(@Nullable IElementType tokenType) {
    return tokenType == REGEX_QUOTE_CLOSE;
  }

  @Override
  protected boolean checkAfterCloseQuote(@NotNull Lexer flexAdapter) {
    while (flexAdapter.getTokenType() == REGEX_MODIFIER) {
      flexAdapter.advance();
    }
    return true;
  }
}
