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

package com.perl5.lang.perl.psi.stubs.namespaces

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement
import com.perl5.lang.perl.psi.impl.PsiPerlNamespaceDefinitionImpl
import com.perl5.lang.perl.psi.stubs.PerlStubSerializingFactory
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDescendantsIndex.NAMESPACE_DESCENDANTS_KEY
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceIndex.NAMESPACE_KEY


open class PerlNamespaceDefinitionStubSerializingFactory(elementType: IElementType) :
  PerlStubSerializingFactory<PerlNamespaceDefinitionStub, PerlNamespaceDefinitionElement>(elementType) {
  override fun createPsi(stub: PerlNamespaceDefinitionStub): PerlNamespaceDefinitionElement =
    PsiPerlNamespaceDefinitionImpl(stub, elementType)

  override fun createStub(psi: PerlNamespaceDefinitionElement, parentStub: StubElement<out PsiElement>?): PerlNamespaceDefinitionStub =
    createStubElement(parentStub, PerlNamespaceDefinitionData(psi))

  override fun serialize(stub: PerlNamespaceDefinitionStub, dataStream: StubOutputStream): Unit = stub.data.serialize(dataStream)

  override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): PerlNamespaceDefinitionStub =
    createStubElement(parentStub, PerlNamespaceDefinitionData.deserialize(dataStream))

  override fun indexStub(stub: PerlNamespaceDefinitionStub, sink: IndexSink) {
    val name: String = stub.namespaceName
    sink.occurrence(getNamespacesIndexKey(), name)

    for (parent in stub.parentNamespacesNames) {
      if (parent != null && !parent.isEmpty()) {
        sink.occurrence(getDescendantsIndexKey(), parent)
      }
    }
  }

  override fun shouldCreateStub(node: ASTNode): Boolean {
    val psi = node.psi
    return psi is PerlNamespaceDefinitionElement &&
      psi.isValid &&
      StringUtil.isNotEmpty(psi.getNamespaceName())
  }

  protected open fun getNamespacesIndexKey(): StubIndexKey<String, out PsiElement> = NAMESPACE_KEY

  protected open fun getDescendantsIndexKey(): StubIndexKey<String, out PsiElement> = NAMESPACE_DESCENDANTS_KEY

  protected open fun createStubElement(
    parentStub: StubElement<*>?,
    data: PerlNamespaceDefinitionData
  ): PerlNamespaceDefinitionStub = PerlNamespaceDefinitionStub(parentStub, elementType, data)
}
