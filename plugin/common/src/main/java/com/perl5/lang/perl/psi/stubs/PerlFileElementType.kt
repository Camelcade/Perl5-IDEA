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

package com.perl5.lang.perl.psi.stubs

import com.intellij.lang.*
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IFileElementType
import com.perl5.lang.perl.parser.builder.PerlPsiBuilderFactory

class PerlFileElementType(debugName: String, language: Language?) : IFileElementType(debugName, language) {
  override fun doParseContents(chameleon: ASTNode, psi: PsiElement): ASTNode? =
    getParser(psi).parse(this, getBuilder(psi, chameleon)).firstChildNode

  private fun getParser(psi: PsiElement): PsiParser =
    LanguageParserDefinitions.INSTANCE.forLanguage(getLanguageForParser(psi)).createParser(psi.project)

  private fun getBuilder(psi: PsiElement, chameleon: ASTNode): PsiBuilder = PerlPsiBuilderFactory.createBuilder(
    psi.project, chameleon, null, getLanguageForParser(psi), chameleon.chars
  )
}