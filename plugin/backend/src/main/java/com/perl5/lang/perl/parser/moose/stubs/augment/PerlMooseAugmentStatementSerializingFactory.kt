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

package com.perl5.lang.perl.parser.moose.stubs.augment

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.parser.moose.psi.PerlMooseAugmentStatement
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseAugmentStatementImpl
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil


open class PerlMooseAugmentStatementSerializingFactory(val elementType: IElementType) :
  StubSerializingElementFactory<PerlMooseAugmentStatementStub, PerlMooseAugmentStatement> {
  override fun createPsi(stub: PerlMooseAugmentStatementStub): PerlMooseAugmentStatement = PerlMooseAugmentStatementImpl(stub, elementType)

  override fun createStub(psi: PerlMooseAugmentStatement, parentStub: StubElement<out PsiElement>?): PerlMooseAugmentStatementStub =
    PerlMooseAugmentStatementStubImpl(parentStub, elementType, psi.getSubName())

  override fun getExternalId(): String = "perl.$elementType"

  override fun serialize(stub: PerlMooseAugmentStatementStub, dataStream: StubOutputStream): Unit = dataStream.writeName(stub.getSubName())

  override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): PerlMooseAugmentStatementStub =
    PerlMooseAugmentStatementStubImpl(parentStub, elementType, PerlStubSerializationUtil.readString(dataStream))

  override fun indexStub(stub: PerlMooseAugmentStatementStub, sink: IndexSink): Unit = Unit

  override fun shouldCreateStub(node: ASTNode): Boolean {
    val element = node.psi
    return element is PerlMooseAugmentStatement &&
      element.isValid &&
      StringUtil.isNotEmpty(element.getSubName())
  }
}