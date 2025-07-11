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

package com.perl5.lang.htmlmason.elementType

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IElementType
import com.intellij.util.io.StringRef
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonFlagsStatement
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonFlagsStatement.UNDEF_RESULT
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFlagsStatementImpl
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonFlagsStatementStub
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonFlagsStubIndex
import com.perl5.lang.htmlmason.parser.stubs.impl.HTMLMasonFlagsStatementStubImpl


class HTMLMasonFlagsStatementStubSerializingFactory(val elementType: IElementType) :
  StubSerializingElementFactory<HTMLMasonFlagsStatementStub, HTMLMasonFlagsStatement> {
  override fun createPsi(stub: HTMLMasonFlagsStatementStub): HTMLMasonFlagsStatement = HTMLMasonFlagsStatementImpl(stub, elementType)

  override fun createStub(psi: HTMLMasonFlagsStatement, parentStub: StubElement<out PsiElement>?): HTMLMasonFlagsStatementStub =
    HTMLMasonFlagsStatementStubImpl(parentStub, elementType, psi.getParentComponentPath())

  override fun getExternalId(): String = "HTML::Mason::$elementType"

  override fun serialize(stub: HTMLMasonFlagsStatementStub, dataStream: StubOutputStream) {
    val parentComponentPath = stub.getParentComponentPath()

    if (UNDEF_RESULT == parentComponentPath) {
      dataStream.writeBoolean(false)
      return
    }
    dataStream.writeBoolean(true)
    dataStream.writeName(parentComponentPath)
  }

  override fun deserialize(
    dataStream: StubInputStream,
    parentStub: StubElement<*>?
  ): HTMLMasonFlagsStatementStub {
    if (!dataStream.readBoolean()) {
      return HTMLMasonFlagsStatementStubImpl(parentStub, elementType, UNDEF_RESULT)
    }
    val nameRef: StringRef? = dataStream.readName()
    return HTMLMasonFlagsStatementStubImpl(parentStub, elementType, nameRef?.toString())
  }

  override fun indexStub(stub: HTMLMasonFlagsStatementStub, sink: IndexSink) {
    val parentComponentPath = stub.getParentComponentPath()

    if (parentComponentPath != null && UNDEF_RESULT != parentComponentPath) {
      sink.occurrence(HTMLMasonFlagsStubIndex.KEY, parentComponentPath)
    }
  }
}