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

package com.perl5.lang.tt2;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.parser.elementTypes.PerlPsiElementFactory;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitTokenSets;
import com.perl5.lang.tt2.lexer.TemplateToolkitLexerAdapter;
import com.perl5.lang.tt2.parser.TemplateToolkitParser;
import com.perl5.lang.tt2.psi.impl.TemplateToolkitFileImpl;
import org.jetbrains.annotations.NotNull;


public class TemplateToolkitParserDefinition implements ParserDefinition, TemplateToolkitElementTypes {

  @Override
  public @NotNull Lexer createLexer(Project project) {
    return new TemplateToolkitLexerAdapter(project);
  }

  @Override
  public @NotNull PsiParser createParser(Project project) {
    return new TemplateToolkitParser();
  }

  @Override
  public @NotNull IFileElementType getFileNodeType() {
    return TT2_FILE;
  }

  @Override
  public @NotNull TokenSet getWhitespaceTokens() {
    return TemplateToolkitTokenSets.WHITE_SPACES;
  }

  @Override
  public @NotNull TokenSet getCommentTokens() {
    return TemplateToolkitTokenSets.COMMENTS;
  }

  @Override
  public @NotNull TokenSet getStringLiteralElements() {
    return TemplateToolkitTokenSets.LITERALS;
  }

  @Override
  public @NotNull PsiElement createElement(ASTNode node) {
    return PerlPsiElementFactory.create(node.getElementType(), node);
  }

  @Override
  public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
    return new TemplateToolkitFileImpl(viewProvider);
  }
}
