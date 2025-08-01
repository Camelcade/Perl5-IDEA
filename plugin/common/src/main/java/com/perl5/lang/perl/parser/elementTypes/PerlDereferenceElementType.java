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
import com.intellij.lang.Language;
import com.intellij.lang.PsiBuilderUtil;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.idea.editor.PerlBraceMatcher;
import com.perl5.lang.perl.lexer.PerlTemplatingLexer;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

import static com.perl5.lang.perl.lexer.PerlLexer.AFTER_VARIABLE;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;

public abstract class PerlDereferenceElementType extends PerlReparseableElementType {
  public PerlDereferenceElementType(@NotNull String debugName) {
    super(debugName);
  }

  @Override
  protected boolean isReparseableOld(@NotNull ASTNode parent,
                                     @NotNull CharSequence buffer,
                                     @NotNull Language fileLanguage,
                                     @NotNull Project project) {
    Lexer lexer = createLexer(parent, this);
    boolean result = hasProperTokensStructure(buffer, lexer);
    if (LOG.isDebugEnabled()) {
      LOG.debug(this + " reparseable: ", result && isLexerStateOk(lexer.getState()),
                "; balanced: ", result,
                "; lexer state: ", lexer.getState());
    }
    return result && isLexerStateOk(lexer.getState());
  }

  private boolean isLexerStateOk(int lexerState) {
    return PerlTemplatingLexer.getPerlLexerState(lexerState) == AFTER_VARIABLE;
  }

  /**
   * Improved copy of {@link PsiBuilderUtil#hasProperBraceBalance(CharSequence, com.intellij.lexer.Lexer, IElementType, IElementType)}
   * Checks that all perl braces within range are balanced and properly nested
   */
  private boolean hasProperTokensStructure(@NotNull CharSequence text,
                                           @NotNull Lexer lexer) {
    lexer.start(text);

    if (lexer.getTokenType() != getSigilTokenType()) {
      return false;
    }

    lexer.advance();
    skipSpaces(lexer);
    if (lexer.getTokenType() != getOpenBraceTokenType()) {
      return false;
    }

    lexer.advance();
    skipSpaces(lexer);
    IElementType tokenType = lexer.getTokenType();
    if (tokenType == getVariableNameTokenType() || tokenType == getCloseBraceTokenType()) {
      // turned to ${varname}
      return false;
    }

    Stack<IElementType> bracesStack = new Stack<>();
    bracesStack.push(getOpenBraceTokenType());

    return checkBracesBalance(lexer, bracesStack);
  }

  protected abstract @NotNull IElementType getSigilTokenType();

  protected abstract @NotNull IElementType getOpenBraceTokenType();

  protected final @NotNull IElementType getCloseBraceTokenType() {
    return PerlBraceMatcher.PERL_BRACES_MAP.get(getOpenBraceTokenType());
  }

  protected abstract @NotNull IElementType getVariableNameTokenType();

  public static class Scalar extends PerlDereferenceElementType {
    public Scalar(@NotNull String debugName) {
      super(debugName);
    }

    @Override
    protected @NotNull IElementType getSigilTokenType() {
      return SIGIL_SCALAR;
    }

    @Override
    protected @NotNull IElementType getOpenBraceTokenType() {
      return LEFT_BRACE_SCALAR;
    }

    @Override
    protected @NotNull IElementType getVariableNameTokenType() {
      return SCALAR_NAME;
    }
  }

  public static class ScalarIndex extends Scalar {
    public ScalarIndex(@NotNull String debugName) {
      super(debugName);
    }

    @Override
    protected @NotNull IElementType getSigilTokenType() {
      return SIGIL_SCALAR_INDEX;
    }
  }


  public static class Array extends PerlDereferenceElementType {
    public Array(@NotNull String debugName) {
      super(debugName);
    }

    @Override
    protected @NotNull IElementType getSigilTokenType() {
      return SIGIL_ARRAY;
    }

    @Override
    protected @NotNull IElementType getOpenBraceTokenType() {
      return LEFT_BRACE_ARRAY;
    }

    @Override
    protected @NotNull IElementType getVariableNameTokenType() {
      return ARRAY_NAME;
    }
  }

  public static class Hash extends PerlDereferenceElementType {
    public Hash(@NotNull String debugName) {
      super(debugName);
    }

    @Override
    protected @NotNull IElementType getSigilTokenType() {
      return SIGIL_HASH;
    }

    @Override
    protected @NotNull IElementType getOpenBraceTokenType() {
      return LEFT_BRACE_HASH;
    }

    @Override
    protected @NotNull IElementType getVariableNameTokenType() {
      return HASH_NAME;
    }
  }

  public static class Code extends PerlDereferenceElementType {
    public Code(@NotNull String debugName) {
      super(debugName);
    }

    @Override
    protected @NotNull IElementType getSigilTokenType() {
      return SIGIL_CODE;
    }

    @Override
    protected @NotNull IElementType getOpenBraceTokenType() {
      return LEFT_BRACE_CODE;
    }

    @Override
    protected @NotNull IElementType getVariableNameTokenType() {
      return SUB_NAME;
    }
  }

  public static class Glob extends PerlDereferenceElementType {
    public Glob(@NotNull String debugName) {
      super(debugName);
    }

    @Override
    protected @NotNull IElementType getSigilTokenType() {
      return SIGIL_GLOB;
    }

    @Override
    protected @NotNull IElementType getOpenBraceTokenType() {
      return LEFT_BRACE_GLOB;
    }

    @Override
    protected @NotNull IElementType getVariableNameTokenType() {
      return GLOB_NAME;
    }
  }
}
