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
package com.perl5.lang.mason2.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException
import com.perl5.lang.perl.extensions.PerlImplicitVariablesProvider
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseOverrideStatement
import com.perl5.lang.perl.psi.PerlSubNameElement
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement
import com.perl5.lang.perl.psi.PsiPerlBlock
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub

class MasonOverrideDefinition : PerlMooseOverrideStatement, PerlImplicitVariablesProvider {
  private val myImplicitVariables: List<PerlVariableDeclarationElement> by lazy { buildImplicitVariables() }

  constructor(node: ASTNode) : super(node)

  constructor(stub: PerlSubDefinitionStub, nodeType: IElementType) : super(stub, nodeType)

  protected fun buildImplicitVariables(): MutableList<PerlVariableDeclarationElement> {
    val newImplicitVariables: MutableList<PerlVariableDeclarationElement> = ArrayList()
    if (isValid()) {
      newImplicitVariables.add(PerlImplicitVariableDeclaration.createInvocant(this))
    }
    return newImplicitVariables
  }

  override fun getSubDefinitionBody(): PsiPerlBlock = findNotNullChildByClass(PsiPerlBlock::class.java)

  override fun getNameIdentifier(): PsiElement? = PsiTreeUtil.getChildOfType(this, PerlSubNameElement::class.java)

  override val subNameHeavy: String?
    get() = getNameIdentifier()?.getNode()?.getText()

  @Throws(IncorrectOperationException::class)
  override fun setName(name: String): PsiElement {
    if (name.isEmpty()) {
      throw IncorrectOperationException("You can't set an empty method name")
    }
    val nameIdentifier = getNameIdentifier()
    if (nameIdentifier is LeafPsiElement) {
      nameIdentifier.replaceWithText(name)
    }

    return this
  }

  override fun getImplicitVariables(): List<PerlVariableDeclarationElement> {
    return myImplicitVariables
  }
}
