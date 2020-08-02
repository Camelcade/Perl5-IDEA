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
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lexer.DelegateLexer;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.psi.PerlLexerAwareFileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This token is for template representation in the base language for multi-psi files
 *
 * @see MultiplePsiFilesPerDocumentFileViewProvider
 */
public abstract class PerlReparseableTemplateTokenType extends PerlReparseableTokenType {
  private static final Logger LOG = Logger.getInstance(PerlReparseableTemplateTokenType.class);

  public PerlReparseableTemplateTokenType(@NotNull String debugName,
                                          Class<? extends ASTNode> clazz) {
    super(debugName, clazz);
  }

  public PerlReparseableTemplateTokenType(@NotNull String debugName,
                                          @NotNull Class<? extends ASTNode> clazz, @Nullable Language language) {
    super(debugName, language, clazz);
  }

  @Override
  protected @Nullable Lexer createLexer(@NotNull ASTNode nodeToLex) {
    PsiElement psiElement = nodeToLex.getPsi();
    PsiFile containingFile = psiElement.getContainingFile();
    FileViewProvider fileViewProvider = containingFile.getViewProvider();
    Language baseLanguage = fileViewProvider.getBaseLanguage();
    Lexer lexer = LanguageParserDefinitions.INSTANCE.forLanguage(baseLanguage).createLexer(psiElement.getProject());
    int alternativeInitialState = fileViewProvider instanceof PerlLexerAwareFileViewProvider ?
                                  ((PerlLexerAwareFileViewProvider)fileViewProvider).getLexerStateFor(nodeToLex) : 0;
    if (alternativeInitialState != 0) {
      return new DelegateLexer(lexer) {
        @Override
        public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
          super.start(buffer, startOffset, endOffset, alternativeInitialState);
        }
      };
    }
    return lexer;
  }
}
