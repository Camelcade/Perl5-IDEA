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
import com.intellij.lexer.DelegateLexer;
import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlElementTypesGenerated;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.lexer.adapters.PerlSubLexerAdapter;
import org.jetbrains.annotations.NotNull;


public class PerlLazyQStringElementType extends PerlLazyBlockElementType {
  private static final Logger LOG = Logger.getInstance(PerlLazyQStringElementType.class);

  public PerlLazyQStringElementType(String name) {
    super(name);
  }

  public PerlLazyQStringElementType(@NotNull String debugName, @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, clazz);
  }

  @Override
  protected @NotNull Lexer getLexer(@NotNull Project project, @NotNull ASTNode chameleon) {
    PerlSubLexerAdapter subLexerAdapter = PerlSubLexerAdapter.forStringSQ(project);
    ASTNode prevNode = chameleon.getTreePrev();
    if (PsiUtilCore.getElementType(prevNode) == PerlElementTypesGenerated.QUOTE_SINGLE_OPEN) {
      CharSequence nodeChars = prevNode.getChars();
      if (nodeChars.length() != 1) {
        LOG.error("Got " + nodeChars);
      }
      char openQuoteChar = nodeChars.charAt(0);

      FlexLexer perlLexer = subLexerAdapter.getFlex();
      LOG.assertTrue(perlLexer instanceof PerlLexer, "Got " + perlLexer);
      return new DelegateLexer(subLexerAdapter) {
        @Override
        public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
          super.start(buffer, startOffset, endOffset, initialState);
          ((PerlLexer)perlLexer).setSingleOpenQuoteChar(openQuoteChar);
        }
      };
    }
    return subLexerAdapter;
  }
}
