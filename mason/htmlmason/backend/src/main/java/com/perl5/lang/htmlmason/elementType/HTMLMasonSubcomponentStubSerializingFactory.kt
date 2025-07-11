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

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.tree.IElementType
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonSubcomponentDefitnition
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonSubcomponentDefitnitionImpl
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonStubsSerializingFactory
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonSubcomponentDefinitionStub
import com.perl5.lang.htmlmason.parser.stubs.impl.HTMLMasonSubcomponentDefinitionStubImpl
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil


class HTMLMasonSubcomponentStubSerializingFactory(elementType: IElementType) :
  HTMLMasonStubsSerializingFactory<HTMLMasonSubcomponentDefinitionStub, HTMLMasonSubcomponentDefitnition>(elementType) {
  override fun createPsi(stub: HTMLMasonSubcomponentDefinitionStub): HTMLMasonSubcomponentDefitnition =
    HTMLMasonSubcomponentDefitnitionImpl(stub, elementType)

  override fun createStub(
    psi: HTMLMasonSubcomponentDefitnition,
    parentStub: StubElement<out PsiElement>?
  ): HTMLMasonSubcomponentDefinitionStub = HTMLMasonSubcomponentDefinitionStubImpl(parentStub, elementType, psi.name)

  override fun serialize(stub: HTMLMasonSubcomponentDefinitionStub, dataStream: StubOutputStream) = dataStream.writeName(stub.getName())

  override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): HTMLMasonSubcomponentDefinitionStub =
    HTMLMasonSubcomponentDefinitionStubImpl(parentStub, elementType, PerlStubSerializationUtil.readString(dataStream))

  override fun indexStub(
    stub: HTMLMasonSubcomponentDefinitionStub,
    sink: IndexSink
  ): Unit = Unit

  override fun shouldCreateStub(node: ASTNode): Boolean {
    val psi = node.psi
    return psi is HTMLMasonSubcomponentDefitnition &&
      StringUtil.isNotEmpty(psi.name)
  }
}