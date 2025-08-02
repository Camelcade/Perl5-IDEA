/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlTemplatingLexer;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlLexer.AFTER_VALUE;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.LEFT_PAREN;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.PRINT_EXPR;

public class PerlParenthesizedCallArgumentsElementType extends PerlBracedBlockElementType {
  public PerlParenthesizedCallArgumentsElementType(@NotNull String debugName) {
    super(debugName);
  }

  @Override
  protected boolean isParentOk(@NotNull ASTNode parent) {
    return PsiUtilCore.getElementType(parent) != PRINT_EXPR && super.isParentOk(parent);
  }

  @Override
  protected @NotNull IElementType getOpeningBraceType() {
    return LEFT_PAREN;
  }

  @Override
  protected boolean isLexerStateOk(int lexerState) {
    return PerlTemplatingLexer.getPerlLexerState(lexerState) == AFTER_VALUE;
  }
}
