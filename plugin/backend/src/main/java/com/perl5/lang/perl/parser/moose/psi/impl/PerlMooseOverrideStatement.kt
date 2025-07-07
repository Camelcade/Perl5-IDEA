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
package com.perl5.lang.perl.parser.moose.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.tree.IElementType
import com.intellij.util.IncorrectOperationException
import com.perl5.lang.perl.extensions.parser.PerlReferencesProvider
import com.perl5.lang.perl.parser.moose.psi.PerlMoosePsiUtil
import com.perl5.lang.perl.psi.*
import com.perl5.lang.perl.psi.mixins.PerlSubDefinitionBase
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub

open class PerlMooseOverrideStatement : PerlSubDefinitionBase, PerlReferencesProvider {
  constructor(node: ASTNode) : super(node)

  constructor(stub: PerlSubDefinitionStub, nodeType: IElementType) : super(stub, nodeType)

  override fun getReferences(element: PsiElement?): Array<PsiReference?> = PerlMoosePsiUtil.getModifiersNameReference(this.expr, element)

  /**
   * fixme probably a bug. This need to be refactored anyway
   */
  override fun getSubDefinitionBody(): PsiPerlBlock? = null

  override fun getSignatureContent(): PsiPerlSignatureContent? = null

  val expr: PsiPerlExpr?
    get() = findChildByClass(PsiPerlExpr::class.java)

  override fun getNameIdentifier(): PsiElement? {
    var expr: PsiElement? = this.expr

    if (expr is PsiPerlParenthesisedExpr) {
      expr = expr.getFirstChild()
      if (expr != null) {
        expr = expr.getNextSibling()
      }
    }

    if (expr is PsiPerlCommaSequenceExpr) {
      val nameContainer = expr.getFirstChild()
      if (nameContainer is PerlString) {
        return nameContainer
      }
    }

    return null
  }

  override val subNameHeavy: String?
    get() {
      val nameContainer = getNameIdentifier()

      if (nameContainer != null) {
        return ElementManipulators.getValueText(nameContainer)
      }

      return null
    }

  override fun isMethod(): Boolean = true

  @Throws(IncorrectOperationException::class)
  override fun setName(name: String): PsiElement? {
    if (name.isEmpty()) {
      throw IncorrectOperationException("You can't set an empty method name")
    }

    val nameIdentifier = getNameIdentifier()
    if (nameIdentifier != null) {
      ElementManipulators.handleContentChange<PsiElement?>(nameIdentifier, name)
    }

    return this
  }
}

