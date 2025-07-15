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

import com.intellij.codeInsight.controlflow.Instruction
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.ClearableLazyValue
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlControlFlowBuilder
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimpleDynamicLocation
import com.perl5.lang.perl.lexer.PerlElementTypes
import com.perl5.lang.perl.psi.*
import com.perl5.lang.perl.psi.properties.PerlLexicalScope
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub
import com.perl5.lang.perl.psi.utils.PerlResolveUtil
import com.perl5.lang.perl.psi.utils.PerlSubArgument
import java.util.function.Supplier

abstract class PerlSubDefinitionBase : PerlSubBase<PerlSubDefinitionStub>, PerlSubDefinitionElement, PerlLexicalScope, PerlElementTypes,
                                       PerlControlFlowOwner {
  private val myControlFlow = PerlControlFlowBuilder.createLazy(this)
  private val myReturnValueFromCode = ClearableLazyValue.create<PerlValue?>(
    Supplier { PerlResolveUtil.computeReturnValueFromControlFlow(this) })

  constructor(node: ASTNode) : super(node)

  constructor(stub: PerlSubDefinitionStub, nodeType: IElementType) : super(stub, nodeType)

  override fun isMethod(): Boolean {
    if (super<PerlSubBase>.isMethod()) {
      return true
    }

    val arguments = getSubArgumentsList()
    return !arguments.isEmpty() && arguments.first().isSelf(getProject())
  }

  override fun getSubArgumentsList(): List<PerlSubArgument> {
    val stub = getGreenStub()
    if (stub != null) {
      return ArrayList<PerlSubArgument>(stub.getSubArgumentsList())
    }

    return perlSubArgumentsFromSignature ?: this.perlSubArgumentsFromBody
  }

  override fun getReturnValueFromCode(): PerlValue {
    val returnValue = super<PerlSubDefinitionElement>.getReturnValueFromCode()
    if (!returnValue.isUnknown) {
      return returnValue
    }
    val greenStub = getGreenStub()
    return greenStub?.getReturnValueFromCode() ?: myReturnValueFromCode.getValue()
  }

  override fun getPresentation(): ItemPresentation = PerlItemPresentationSimpleDynamicLocation(this, presentableName)

  /**
   * Returns list of arguments defined in signature
   *
   * @return list of arguments or null if there is no signature
   */
  private val perlSubArgumentsFromSignature: List<PerlSubArgument>?
    get() {
      var arguments: MutableList<PerlSubArgument>? = null
      val signatureContainer: PsiElement? = getSignatureContent()

      if (signatureContainer != null) {
        arguments = ArrayList()

        var signatureElement = signatureContainer.firstChild

        while (signatureElement != null) {
          if (signatureElement is PerlSignatureElement) {
            processSignatureElement(signatureElement.getDeclarationElement(), arguments)
          }
          else {
            processSignatureElement(signatureElement, arguments)
          }
          signatureElement = signatureElement.nextSibling
        }
      }

      return arguments
    }

  protected open fun processSignatureElement(signatureElement: PsiElement?, arguments: MutableList<PerlSubArgument>): Boolean {
    if (signatureElement is PerlVariableDeclarationElement) {
      val variable = signatureElement.getVariable()
      val newArgument = PerlSubArgument.mandatory(variable.getActualType(), variable.getName()!!)
      newArgument.isOptional = signatureElement.nextSibling != null
      arguments.add(newArgument)
      return true
    }
    return false
  }

  override fun accept(visitor: PsiElementVisitor): Unit =
    if (visitor is PerlVisitor) {
      visitor.visitPerlSubDefinitionElement(this)
    }
    else {
      super.accept(visitor)
    }

  override fun getSubDefinitionBody(): PsiPerlBlock? = block

  override fun getControlFlow(): Array<Instruction> = myControlFlow.getValue()

  override fun subtreeChanged() {
    myControlFlow.drop()
    myReturnValueFromCode.drop()
  }
}