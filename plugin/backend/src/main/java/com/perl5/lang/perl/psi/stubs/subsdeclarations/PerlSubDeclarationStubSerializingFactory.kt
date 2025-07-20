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

package com.perl5.lang.perl.psi.stubs.subsdeclarations

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.psi.PerlSubDeclarationElement
import com.perl5.lang.perl.psi.impl.PsiPerlSubDeclarationImpl
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil
import com.perl5.lang.perl.psi.stubs.PerlStubSerializingFactory
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlCallableNamesIndex
import com.perl5.lang.perl.psi.stubs.subsdefinitions.readSubAnnotations
import com.perl5.lang.perl.psi.stubs.subsdefinitions.writeSubAnnotations
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations


class PerlSubDeclarationStubSerializingFactory(elementType: IElementType) :
  PerlStubSerializingFactory<PerlSubDeclarationStub, PerlSubDeclarationElement>(elementType) {
  override fun createPsi(stub: PerlSubDeclarationStub): PerlSubDeclarationElement =
    PsiPerlSubDeclarationImpl(stub, elementType)

  override fun createStub(psi: PerlSubDeclarationElement, parentStub: StubElement<out PsiElement>?): PerlSubDeclarationStub =
    PerlSubDeclarationStub(
      parentStub,
      psi.getNamespaceName(),
      psi.getSubName(),
      psi.getAnnotations(),
      elementType
    )

  override fun serialize(
    stub: PerlSubDeclarationStub,
    dataStream: StubOutputStream
  ) {
    dataStream.writeName(stub.namespaceName)
    dataStream.writeName(stub.subName)
    val subAnnotations = stub.annotations
    if (subAnnotations == null) {
      dataStream.writeBoolean(false)
    }
    else {
      dataStream.writeBoolean(true)
      dataStream.writeSubAnnotations(subAnnotations)
    }
  }

  override fun deserialize(
    dataStream: StubInputStream,
    parentStub: StubElement<*>?
  ): PerlSubDeclarationStub {
    val packageName = PerlStubSerializationUtil.readString(dataStream)
    val subName = PerlStubSerializationUtil.readString(dataStream)
    val annotations: PerlSubAnnotations? =
      if (dataStream.readBoolean()) dataStream.readSubAnnotations() else null
    return PerlSubDeclarationStub(parentStub, packageName, subName, annotations, elementType)
  }

  override fun shouldCreateStub(node: ASTNode): Boolean {
    val psi = node.psi
    return psi is PerlSubDeclarationElement &&
      StringUtil.isNotEmpty(psi.getNamespaceName()) &&
      StringUtil.isNotEmpty(psi.getSubName())
  }

  override fun indexStub(
    stub: PerlSubDeclarationStub,
    sink: IndexSink
  ) {
    val canonicalName = stub.canonicalName
    if (canonicalName != null) {
      sink.occurrence(PerlSubDeclarationIndex.KEY, canonicalName)
    }
    val packageName = stub.namespaceName
    if (packageName != null) {
      sink.occurrence(PerlSubDeclarationReverseIndex.KEY, packageName)
    }
    val callableName = stub.callableName
    if (StringUtil.isNotEmpty(callableName)) {
      sink.occurrence(PerlCallableNamesIndex.KEY, callableName)
    }
  }
}