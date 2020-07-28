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
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.LEFT_PAREN;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.PRINT_EXPR;
import static com.perl5.lang.perl.lexer.PerlLexer.AFTER_VALUE;

public class PerlParenthesizedCallArgumentsElementType extends PerlBracedBlockElementType {
  public PerlParenthesizedCallArgumentsElementType(@NotNull String debugName,
                                                   @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, clazz);
  }

  @Override
  protected boolean isNodeReparseable(@Nullable ASTNode parent) {
    return PsiUtilCore.getElementType(parent) != PRINT_EXPR;
  }

  @Override
  protected @NotNull IElementType getOpeningBraceType() {
    return LEFT_PAREN;
  }

  @Override
  protected boolean isLexerStateOk(int lexerState) {
    return lexerState == AFTER_VALUE;
  }
}
