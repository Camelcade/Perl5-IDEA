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
package com.perl5.lang.perl.psi.mixins

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.perl5.lang.perl.psi.PerlMethodDefinition
import com.perl5.lang.perl.psi.PerlVariable
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement
import com.perl5.lang.perl.psi.PsiPerlMethodSignatureInvocant
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub
import com.perl5.lang.perl.psi.utils.PerlSubArgument
import com.perl5.lang.perl.util.PerlScalarUtilCore

abstract class PerlMethodDefinitionMixin : PerlSubDefinitionBase, PerlMethodDefinition {
  protected val myImplicitVariables: List<PerlVariableDeclarationElement> by lazy { buildImplicitVariables() }

  constructor(node: ASTNode) : super(node)

  constructor(stub: PerlSubDefinitionStub, nodeType: IElementType) : super(stub, nodeType)

  protected open fun buildImplicitVariables(): List<PerlVariableDeclarationElement> =
    listOf(PerlImplicitVariableDeclaration.createInvocant(this))

  override fun isMethod(): Boolean = true

  override fun processSignatureElement(signatureElement: PsiElement?, arguments: MutableList<PerlSubArgument>): Boolean {
    if (signatureElement is PsiPerlMethodSignatureInvocant) {
      val variable = PsiTreeUtil.findChildOfType(signatureElement, PerlVariable::class.java)
      if (variable != null) {
        arguments.add(PerlSubArgument.mandatory(variable.getActualType(), variable.getName()!!))
      }
    }
    else if (signatureElement is PerlVariableDeclarationElement) {
      if (arguments.isEmpty()) {
        arguments.add(PerlSubArgument.mandatoryScalar(defaultInvocantName.substring(1)))
      }

      return super.processSignatureElement(signatureElement, arguments)
    }
    return false
  }

  /**
   * Checks if method has an explicit invocant
   *
   * @return check result
   */
  private fun hasExplicitInvocant(): Boolean {
    val signatureContainer: PsiElement? = getSignatureContent()
    return signatureContainer != null && signatureContainer.firstChild is PsiPerlMethodSignatureInvocant
  }

  override fun getImplicitVariables(): List<PerlVariableDeclarationElement> = if (hasExplicitInvocant()) {
    emptyList()
  }
  else {
    myImplicitVariables
  }

  companion object {
    // fixme see #717
    @JvmStatic
    val defaultInvocantName: String
      get() =
        PerlScalarUtilCore.DEFAULT_SELF_SCALAR_NAME
  }
}
