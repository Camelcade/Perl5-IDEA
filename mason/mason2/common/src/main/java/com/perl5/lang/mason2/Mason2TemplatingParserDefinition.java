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

package com.perl5.lang.mason2;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.DummyHolder;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.perl5.lang.mason2.lexer.Mason2TemplatingLexer;
import com.perl5.lang.mason2.lexer.Mason2TemplatingLexerAdapter;
import com.perl5.lang.mason2.psi.impl.MasonTemplatingFileImpl;
import com.perl5.lang.perl.lexer.PerlTemplatingLexer;
import com.perl5.lang.perl.parser.Mason2TemplatingParserImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.perl5.lang.mason2.elementType.Mason2ElementTypes.*;


public class Mason2TemplatingParserDefinition extends Mason2ParserDefinition {

  @Override
  public @NotNull IFileElementType getFileNodeType() {
    return COMPONENT_FILE;
  }

  @Override
  public @NotNull Lexer createLexer(Project project) {
    return new Mason2TemplatingLexerAdapter(project, false);
  }

  @Override
  public @NotNull PsiParser createParser(Project project) {
    return new Mason2TemplatingParserImpl();
  }

  @Override
  public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
    return new MasonTemplatingFileImpl(viewProvider);
  }

  @Override
  public int getLexerStateFor(@NotNull ASTNode contextNode, @NotNull IElementType elementType) {
    return PerlTemplatingLexer.packState(super.getLexerStateFor(contextNode, elementType),
                                         getTemplatingState(contextNode, elementType));
  }

  private int getTemplatingState(@NotNull ASTNode contextNode, @NotNull IElementType elementType) {
    if (elementType == MASON_TEMPLATE_BLOCK_HTML) {
      return Mason2TemplatingLexer.YYINITIAL;
    }
    PsiElement parent = contextNode.getPsi().getParent();
    ASTNode run = parent instanceof DummyHolder ? Objects.requireNonNull(parent.getContext()).getNode() : contextNode;

    while (run != null) {
      if (StringUtil.containsLineBreak(run.getChars())) {
        return Mason2TemplatingLexer.PERL;
      }
      else if (run.getElementType() == MASON_LINE_OPENER) {
        return Mason2TemplatingLexer.PERL_LINE;
      }
      run = TreeUtil.prevLeaf(run);
    }

    return Mason2TemplatingLexer.YYINITIAL;
  }
}
