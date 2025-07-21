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

package com.perl5.lang.perl.psi.stubs.imports

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement
import com.perl5.lang.perl.psi.stubs.PerlPolynamedStubSerializingFactory
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil.readString
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil.readStringsList


open class PerlUseStatementStubSerializingFactory(elementType: IElementType) :
  PerlPolynamedStubSerializingFactory<PerlUseStatementStub, PerlUseStatementElement>(elementType) {
  override fun createStub(
    psi: PerlUseStatementElement,
    parentStub: StubElement<*>?,
    lightElementsStubs: MutableList<StubElement<*>>
  ): PerlUseStatementStub =
    PerlUseStatementStub(parentStub, psi.namespaceName, psi.packageName!!, psi.importParameters, lightElementsStubs)

  override fun deserialize(
    dataStream: StubInputStream,
    parentStub: StubElement<*>?,
    lightElementsStubs: MutableList<StubElement<*>>
  ): PerlUseStatementStub = PerlUseStatementStub(
    parentStub,
    readString(dataStream)!!,
    readString(dataStream)!!,
    readStringsList(dataStream),
    lightElementsStubs
  )

  override fun serializeStub(stub: PerlUseStatementStub, dataStream: StubOutputStream) {
    dataStream.writeName(stub.namespaceName)
    dataStream.writeName(stub.packageName)
    PerlStubSerializationUtil.writeStringsList(dataStream, stub.importParameters)
  }

  override fun doIndexStub(stub: PerlUseStatementStub, sink: IndexSink): Unit =
    sink.occurrence(PerlUseStatementsIndex.KEY, stub.namespaceName)

  override fun shouldCreateStub(node: ASTNode): Boolean {
    val element = node.psi
    return element is PerlUseStatementElement &&
      element.isValid() &&
      StringUtil.isNotEmpty(element.packageName)
  }

  override fun createPsi(stub: PerlUseStatementStub): PerlUseStatementElement = PerlUseStatementElement(stub, elementType)
}