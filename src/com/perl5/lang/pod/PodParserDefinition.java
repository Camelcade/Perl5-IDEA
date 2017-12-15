/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.perl5.lang.pod.elementTypes.PodFileElementType;
import com.perl5.lang.pod.lexer.PodDebuggingLexerAdapter;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.parser.PodParser;
import com.perl5.lang.pod.parser.psi.impl.PodFileImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PodParserDefinition implements ParserDefinition, PodElementTypes {

  public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
  public static final TokenSet ALL_WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE, POD_NEWLINE);
  public static final TokenSet COMMENTS = TokenSet.create(POD_OUTER);
  public static final TokenSet IDENTIFIERS = TokenSet.create(POD_IDENTIFIER);

  public static final IFileElementType FILE = new PodFileElementType("Plain old document");

  @NotNull
  @Override
  public Lexer createLexer(Project project) {
    return new PodDebuggingLexerAdapter(project);
  }

  @NotNull
  public TokenSet getWhitespaceTokens() {
    return WHITE_SPACES;
  }

  @NotNull
  public TokenSet getCommentTokens() {
    return COMMENTS;
  }

  @NotNull
  public TokenSet getStringLiteralElements() {
    return TokenSet.EMPTY;
  }

  @NotNull
  public PsiParser createParser(final Project project) {
    return new PodParser();
  }

  @Override
  public IFileElementType getFileNodeType() {
    return FILE;
  }

  public PsiFile createFile(FileViewProvider viewProvider) {
    return new PodFileImpl(viewProvider);
  }

  public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
    return SpaceRequirements.MAY;
  }

  @NotNull
  public PsiElement createElement(ASTNode node) {
    return ((PsiElementProvider)node.getElementType()).getPsiElement(node);
  }
}