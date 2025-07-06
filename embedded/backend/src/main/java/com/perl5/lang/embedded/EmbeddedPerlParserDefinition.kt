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
package com.perl5.lang.embedded

import com.intellij.lang.ASTNode
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.perl5.lang.embedded.lexer.EmbeddedPerlLexer
import com.perl5.lang.embedded.lexer.EmbeddedPerlLexerAdapter
import com.perl5.lang.embedded.psi.EmbeddedPerlElementTypes
import com.perl5.lang.embedded.psi.EmbeddedPerlElementTypes.EMBED_TEMPLATE_BLOCK_HTML
import com.perl5.lang.embedded.psi.EmbeddedPerlTokenSets
import com.perl5.lang.embedded.psi.impl.EmbeddedPerlFileImpl
import com.perl5.lang.perl.PerlParserDefinition
import com.perl5.lang.perl.lexer.PerlTemplatingLexer

class EmbeddedPerlParserDefinition : PerlParserDefinition() {
  override fun getCommentTokens(): TokenSet = EmbeddedPerlTokenSets.COMMENTS

  override fun createLexer(project: Project?): Lexer = EmbeddedPerlLexerAdapter(project, false)

  override fun getFileNodeType(): IFileElementType = EmbeddedPerlElementTypes.FILE

  override fun createFile(viewProvider: FileViewProvider): PsiFile = EmbeddedPerlFileImpl(viewProvider)

  override fun getLexerStateFor(contextNode: ASTNode, elementType: IElementType): Int =
    PerlTemplatingLexer.packState(super.getLexerStateFor(contextNode, elementType), getTemplatingStateFor(elementType))

  private fun getTemplatingStateFor(tokenType: IElementType): Int =
    if (tokenType === EMBED_TEMPLATE_BLOCK_HTML) EmbeddedPerlLexer.YYINITIAL else EmbeddedPerlLexer.PERL
}
