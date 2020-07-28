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

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlLexer;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.LEFT_BRACE;

public class PerlAnonHashElementType extends PerlBracedBlockElementType {
  public PerlAnonHashElementType(@NotNull String debugName,
                                 @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, clazz);
  }

  @Override
  protected @NotNull IElementType getOpeningBraceType() {
    return LEFT_BRACE;
  }

  @Override
  protected boolean isLexerStateOk(int lexerState) {
    return lexerState == PerlLexer.AFTER_RIGHT_BRACE;
  }
}
