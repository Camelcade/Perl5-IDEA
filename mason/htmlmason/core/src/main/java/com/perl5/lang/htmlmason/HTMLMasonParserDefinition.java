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

package com.perl5.lang.htmlmason;

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
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.htmlmason.lexer.HTMLMasonLexer;
import com.perl5.lang.htmlmason.lexer.HTMLMasonLexerAdapter;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.lexer.PerlTemplatingLexer;
import com.perl5.lang.perl.lexer.PerlTokenSetsEx;
import com.perl5.lang.perl.parser.HTMLMasonParserImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes.*;

public class HTMLMasonParserDefinition extends PerlParserDefinition {

  public static final TokenSet COMMENTS = TokenSet.orSet(PerlTokenSetsEx.getCOMMENTS(),
                                                         TokenSet.create(
                                                           HTML_MASON_LINE_OPENER,
                                                           HTML_MASON_PERL_OPENER,
                                                           HTML_MASON_PERL_CLOSER,
                                                           HTML_MASON_TEMPLATE_BLOCK_HTML
                                                         ));

  @Override
  public @NotNull Lexer createLexer(Project project) {
    return new HTMLMasonLexerAdapter(project, false);
  }

  @Override
  public @NotNull IFileElementType getFileNodeType() {
    return FILE;
  }

  @Override
  public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
    return new HTMLMasonFileImpl(viewProvider);
  }

  @Override
  public @NotNull TokenSet getCommentTokens() {
    return COMMENTS;
  }

  @Override
  public @NotNull PsiParser createParser(Project project) {
    return new HTMLMasonParserImpl();
  }

  @Override
  public int getLexerStateFor(@NotNull ASTNode contextNode, @NotNull IElementType elementType) {
    return PerlTemplatingLexer.packState(super.getLexerStateFor(contextNode, elementType),
                                         getTemplatingState(contextNode, elementType));
  }

  private int getTemplatingState(@NotNull ASTNode contextNode, @NotNull IElementType elementType) {
    if (elementType == HTML_MASON_TEMPLATE_BLOCK_HTML) {
      return HTMLMasonLexer.YYINITIAL;
    }
    PsiElement parent = contextNode.getPsi().getParent();
    ASTNode run = parent instanceof DummyHolder ? Objects.requireNonNull(parent.getContext()).getNode() : contextNode;

    while (run != null) {
      if (StringUtil.containsLineBreak(run.getChars())) {
        return HTMLMasonLexer.PERL;
      }
      else if (run.getElementType() == HTML_MASON_LINE_OPENER) {
        return HTMLMasonLexer.PERL_LINE;
      }
      run = TreeUtil.prevLeaf(run);
    }

    return HTMLMasonLexer.YYINITIAL;
  }
}
