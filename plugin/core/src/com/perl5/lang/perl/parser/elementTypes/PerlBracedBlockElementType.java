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
import com.intellij.lang.PsiBuilderUtil;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlLexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;

public abstract class PerlBracedBlockElementType extends PerlReparseableElementType {
  public PerlBracedBlockElementType(@NotNull String debugName,
                                    @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, clazz);
  }

  @Override
  public final boolean isParsable(@Nullable ASTNode parent,
                                  @NotNull CharSequence buffer,
                                  @NotNull Language fileLanguage,
                                  @NotNull Project project) {
    if (!isNodeReparseable(parent)) {
      return false;
    }
    FlexAdapter lexer = new FlexAdapter(new PerlLexer(null).withProject(project));
    boolean result = hasProperBraceBalance(buffer, lexer, getOpeningBraceType());
    if (LOG.isDebugEnabled()) {
      LOG.debug(this + " reparseable: ", result && isLexerStateOk(lexer.getState()),
                "; balanced: ", result,
                "; lexer state: ", lexer.getState());
    }
    return result && isLexerStateOk(lexer.getState());
  }

  protected boolean isNodeReparseable(@Nullable ASTNode parent) {
    return true;
  }

  protected abstract @NotNull IElementType getOpeningBraceType();

  protected abstract boolean isLexerStateOk(int lexerState);

  /**
   * Improved copy of {@link PsiBuilderUtil#hasProperBraceBalance(CharSequence, com.intellij.lexer.Lexer, IElementType, IElementType)}
   * Checks that all perl braces within range are balanced and properly nested
   */
  private static boolean hasProperBraceBalance(@NotNull CharSequence text,
                                               @NotNull Lexer lexer,
                                               @NotNull IElementType openBrace) {
    lexer.start(text);

    if (lexer.getTokenType() != openBrace) {
      return false;
    }

    Stack<IElementType> bracesStack = new Stack<>();
    bracesStack.push(openBrace);
    lexer.advance();

    return checkBracesBalance(lexer, bracesStack);
  }
}
