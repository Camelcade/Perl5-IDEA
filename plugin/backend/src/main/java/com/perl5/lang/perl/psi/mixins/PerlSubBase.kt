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
import com.intellij.psi.StubBasedPsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.util.IncorrectOperationException
import com.perl5.PerlIcons
import com.perl5.lang.perl.parser.PerlElementTypesGenerated
import com.perl5.lang.perl.psi.PerlDeprecatable
import com.perl5.lang.perl.psi.PerlNamespaceElement
import com.perl5.lang.perl.psi.PerlStubBasedPsiElementBase
import com.perl5.lang.perl.psi.PerlSubElement
import com.perl5.lang.perl.psi.properties.PerlLabelScope
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer
import com.perl5.lang.perl.psi.stubs.PerlSubStub
import com.perl5.lang.perl.psi.utils.PerlAnnotations
import com.perl5.lang.perl.psi.utils.PerlPsiUtil
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations
import com.perl5.lang.perl.util.PerlPackageUtil

abstract class PerlSubBase<Stub : PerlSubStub<*>> : PerlStubBasedPsiElementBase<Stub>, PerlSubElement, StubBasedPsiElement<Stub>,
                                                    PerlNamespaceElementContainer, PerlDeprecatable, PerlLabelScope {
  constructor(node: ASTNode) : super(node)

  constructor(stub: Stub, nodeType: IElementType) : super(stub, nodeType)

  override fun getNamespaceName(): String? {
    val stub = getGreenStub()
    if (stub != null) {
      return stub.namespaceName
    }

    var namespace = getExplicitNamespaceName()
    if (namespace == null) {
      namespace = PerlPackageUtil.getContextNamespaceName(this)
    }

    return namespace
  }


  @Throws(IncorrectOperationException::class)
  override fun setName(name: String): PsiElement? = PerlPsiUtil.renameNamedElement(this, name)

  override fun getName(): String? = getSubName()

  override fun getSubName(): String? {
    val stub = getGreenStub()
    if (stub != null) {
      return stub.subName
    }

    return subNameHeavy
  }

  protected open val subNameHeavy: String?
    get() = nameIdentifier?.text

  override fun getExplicitNamespaceName(): String? = namespaceElement?.getCanonicalName()

  override fun getNameIdentifier(): PsiElement? = findChildByType(PerlElementTypesGenerated.SUB_NAME)

  override fun getNamespaceElement(): PerlNamespaceElement? = findChildByClass(PerlNamespaceElement::class.java)

  override fun getAnnotations(): PerlSubAnnotations? {
    val stub = getGreenStub()
    if (stub != null) {
      return stub.annotations
    }
    return PerlSubAnnotations.createFromAnnotationsList(PerlAnnotations.collectAnnotations(this))
  }

  override fun getIcon(flags: Int) = if (isMethod()) PerlIcons.METHOD_GUTTER_ICON else PerlIcons.SUB_GUTTER_ICON


  override fun getTextOffset(): Int = this.nameIdentifier?.textOffset ?: super.getTextOffset()

  override fun toString(): String = super.toString() + "@" + (if (isValid()) getCanonicalName() else "!INVALID!")
}
