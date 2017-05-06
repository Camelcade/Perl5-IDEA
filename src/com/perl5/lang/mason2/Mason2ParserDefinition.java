/*
 * Copyright 2015 Alexandr Evstigneev
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

import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.mason2.elementType.Mason2ElementTypes;
import com.perl5.lang.mason2.elementType.MasonFileElementType;
import com.perl5.lang.mason2.psi.impl.MasonFileImpl;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter;
import com.perl5.lang.perl.parser.Mason2ParserImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 20.12.2015.
 */
public class Mason2ParserDefinition extends PerlParserDefinition implements Mason2ElementTypes {
  public static final IFileElementType FILE = new MasonFileElementType("Mason PP component", Mason2Language.INSTANCE);

  public static final TokenSet COMMENTS = TokenSet.orSet(PerlParserDefinition.COMMENTS,
                                                         TokenSet.create(
                                                           MASON_LINE_OPENER,
                                                           MASON_TEMPLATE_BLOCK_HTML
                                                         ));

  @NotNull
  @Override
  public Lexer createLexer(Project project) {
    return new PerlMergingLexerAdapter(project);
  }

  @Override
  public IFileElementType getFileNodeType() {
    return FILE;
  }

  @Override
  public PsiFile createFile(FileViewProvider viewProvider) {
    return new MasonFileImpl(viewProvider);
  }

  @NotNull
  public TokenSet getCommentTokens() {
    return COMMENTS;
  }

  @NotNull
  @Override
  public PsiParser createParser(Project project) {
    return new Mason2ParserImpl();
  }
}
