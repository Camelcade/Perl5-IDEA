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
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.tree.IElementType
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonArgsBlock
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonArgsBlockImpl
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonArgsBlockStub
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonStubsSerializingFactory
import com.perl5.lang.htmlmason.parser.stubs.impl.HTMLMasonArgsBlockStubImpl
import com.perl5.lang.perl.psi.stubs.subsdefinitions.deserializeArguments
import com.perl5.lang.perl.psi.stubs.subsdefinitions.serializeArguments


class HTMLMasonArgsBlockStubSerializingFactory(elementType: IElementType) :
  HTMLMasonStubsSerializingFactory<HTMLMasonArgsBlockStub, HTMLMasonArgsBlock>(elementType) {
  override fun createPsi(stub: HTMLMasonArgsBlockStub): HTMLMasonArgsBlock = HTMLMasonArgsBlockImpl(stub, elementType)

  override fun createStub(psi: HTMLMasonArgsBlock, parentStub: StubElement<out PsiElement>?): HTMLMasonArgsBlockStub =
    HTMLMasonArgsBlockStubImpl(parentStub, elementType, psi.getArgumentsList())

  override fun serialize(stub: HTMLMasonArgsBlockStub, dataStream: StubOutputStream): Unit =
    dataStream.serializeArguments(stub.getArgumentsList())

  override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): HTMLMasonArgsBlockStub =
    HTMLMasonArgsBlockStubImpl(parentStub, elementType, dataStream.deserializeArguments())

  override fun indexStub(stub: HTMLMasonArgsBlockStub, sink: IndexSink): Unit = Unit
}