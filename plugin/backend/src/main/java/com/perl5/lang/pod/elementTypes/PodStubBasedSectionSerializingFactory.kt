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

package com.perl5.lang.pod.elementTypes

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil
import com.perl5.lang.pod.parser.psi.mixin.PodStubBasedSection
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub


abstract class PodStubBasedSectionSerializingFactory<Psi : PodStubBasedSection>(val elementType: IElementType) :
  StubSerializingElementFactory<PodSectionStub, Psi> {

  override fun getExternalId(): String = "PodSection $elementType"

  override fun serialize(stub: PodSectionStub, dataStream: StubOutputStream) = dataStream.writeName(stub.content)

  override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): PodSectionStub =
    PodSectionStub(parentStub, elementType, PerlStubSerializationUtil.readString(dataStream)!!)

  final override fun shouldCreateStub(node: ASTNode): Boolean {
    val psi = node.psi
    @Suppress("UNCHECKED_CAST")
    return psi != null && shouldCreateStub(psi as Psi)
  }

  override fun indexStub(stub: PodSectionStub, sink: IndexSink) = Unit

  open protected fun shouldCreateStub(psi: Psi): Boolean = psi.isIndexed()
}