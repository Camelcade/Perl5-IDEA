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

package com.perl5.lang.mason2.elementType

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.tree.IElementType
import com.perl5.lang.htmlmason.MasonCoreUtil
import com.perl5.lang.mason2.Mason2UtilCore
import com.perl5.lang.mason2.psi.MasonNamespaceDefinition
import com.perl5.lang.mason2.psi.impl.MasonNamespaceDefinitionImpl
import com.perl5.lang.mason2.psi.stubs.MasonNamespaceDefitnitionsStubIndex
import com.perl5.lang.mason2.psi.stubs.MasonParentNamespacesStubIndex
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionData
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStubSerializingFactory
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceIndex.NAMESPACE_KEY
import com.perl5.lang.perl.util.PerlFileUtil


class MasonNamespaceStubSerializingFactory(elementType: IElementType) : PerlNamespaceDefinitionStubSerializingFactory(elementType) {
  override fun createPsi(stub: PerlNamespaceDefinitionStub): PerlNamespaceDefinitionElement =
    MasonNamespaceDefinitionImpl(stub, elementType)

  override fun createStub(psi: PerlNamespaceDefinitionElement, parentStub: StubElement<out PsiElement>?): PerlNamespaceDefinitionStub =
    PerlNamespaceDefinitionStub(
      parentStub, elementType, PerlNamespaceDefinitionData(
        StringUtil.notNullize((psi as MasonNamespaceDefinition).getAbsoluteComponentPath()), psi
      )
    )

  override fun indexStub(stub: PerlNamespaceDefinitionStub, sink: IndexSink) {
    val name = stub.namespaceName
    sink.occurrence(MasonNamespaceDefitnitionsStubIndex.KEY, name)

    // fixme this is kinda hack to make MRO work. But, it should be smarter
    sink.occurrence(NAMESPACE_KEY, Mason2UtilCore.getClassnameFromPath(name))

    for (parent in stub.parentNamespacesNames) {
      if (parent != null && !parent.isEmpty()) {
        sink.occurrence(MasonParentNamespacesStubIndex.KEY, parent)
      }
    }
  }

  override fun shouldCreateStub(node: ASTNode): Boolean {
    val psi = node.psi
    return psi is MasonNamespaceDefinition &&
      psi.isValid &&
      StringUtil.isNotEmpty(psi.getAbsoluteComponentPath())
  }
}

/**
 * Returns file path relative to project root
 *
 * @return path, relative to root, null if it's LightVirtualFile without original
 */
fun MasonNamespaceDefinition.getAbsoluteComponentPath(): String? {
  val containingFile = MasonCoreUtil.getContainingVirtualFile(containingFile)
  if (containingFile != null) {
    return PerlFileUtil.getPathRelativeToContentRoot(containingFile, project)
  }

  return null
}
