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

package com.perl5.lang.pod;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.pod.lexer.PodDebuggingLexerAdapter;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.parser.PodParser;
import com.perl5.lang.pod.parser.psi.impl.PodFileImpl;
import org.jetbrains.annotations.NotNull;

public class PodParserDefinition implements ParserDefinition, PodElementTypes {

  public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
  public static final TokenSet ALL_WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE, POD_NEWLINE);
  public static final TokenSet COMMENTS = TokenSet.create(POD_OUTER);

  @Override
  public @NotNull Lexer createLexer(Project project) {
    return new PodDebuggingLexerAdapter(project);
  }

  @Override
  public @NotNull TokenSet getWhitespaceTokens() {
    return WHITE_SPACES;
  }

  @Override
  public @NotNull TokenSet getCommentTokens() {
    return COMMENTS;
  }

  @Override
  public @NotNull TokenSet getStringLiteralElements() {
    return TokenSet.EMPTY;
  }

  @Override
  public @NotNull PsiParser createParser(final Project project) {
    return new PodParser();
  }

  @Override
  public @NotNull IFileElementType getFileNodeType() {
    return FILE;
  }

  @Override
  public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
    return new PodFileImpl(viewProvider);
  }

  @Override
  public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
    return SpaceRequirements.MAY;
  }

  @Override
  public @NotNull PsiElement createElement(ASTNode node) {
    return ((PsiElementProvider)node.getElementType()).getPsiElement(node);
  }
}