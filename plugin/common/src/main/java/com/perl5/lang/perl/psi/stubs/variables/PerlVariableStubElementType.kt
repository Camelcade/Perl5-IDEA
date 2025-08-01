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
package com.perl5.lang.perl.psi.stubs.variables

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.PerlLanguage
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider
import com.perl5.lang.perl.psi.impl.PsiPerlVariableDeclarationElementImpl

class PerlVariableStubElementType(debugName: String) : IElementType(debugName, PerlLanguage.INSTANCE),
                                                       PsiElementProvider {
  override fun getPsiElement(node: ASTNode): PsiElement = PsiPerlVariableDeclarationElementImpl(node)
}
