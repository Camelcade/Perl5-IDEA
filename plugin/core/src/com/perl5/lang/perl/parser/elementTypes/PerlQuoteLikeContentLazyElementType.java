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
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import com.perl5.lang.perl.psi.PerlQuoted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlQuoteLikeContentLazyElementType extends PerlLazyBlockElementType {
  private final int myInitialState;

  public PerlQuoteLikeContentLazyElementType(@NotNull String debugName, int initialState) {
    super(debugName);
    myInitialState = initialState;
  }

  @Override
  public boolean isParsable(@Nullable ASTNode parent,
                            @NotNull CharSequence buffer,
                            @NotNull Language fileLanguage,
                            @NotNull Project project) {
    return false;
  }

  protected char getOpenQuoteCharacter(@Nullable ASTNode parent) {
    if (parent == null) {
      return 0;
    }
    PsiElement psiElement = parent.getPsi();
    return psiElement instanceof PerlQuoted ? ((PerlQuoted)psiElement).getOpenQuote() : 0;
  }

  /**
   * @implNote we need to check tree (basically, tokens, because container may be a DUMMY_HOLDER), but we need to lex
   * this properly for stubs.
   */
  @Override
  protected @NotNull PerlLexingContext getLexingContext(@NotNull Project project, @NotNull ASTNode chameleon) {
    PerlLexingContext baseContext = super.getLexingContext(project, chameleon).withEnforcedInitialState(myInitialState);

    ASTNode quoteNode = getRealNode(chameleon).getTreePrev();
    if (quoteNode == null) {
      LOG.error("Missing quote node for " + chameleon);
      return baseContext;
    }

    CharSequence quoteChars = quoteNode.getChars();
    if (quoteChars.length() != 1) {
      LOG.error("Non-single character quote: " + quoteChars);
      return baseContext;
    }

    return baseContext.withOpenChar(quoteChars.charAt(0));
  }
}
