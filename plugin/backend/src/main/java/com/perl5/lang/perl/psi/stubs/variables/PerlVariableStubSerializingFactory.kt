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

package com.perl5.lang.perl.psi.stubs.variables

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement
import com.perl5.lang.perl.psi.impl.PsiPerlVariableDeclarationElementImpl
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil
import com.perl5.lang.perl.psi.utils.PerlVariableAnnotations
import com.perl5.lang.perl.psi.utils.PerlVariableType
import com.perl5.lang.perl.util.PerlPackageUtil
import java.io.IOException


class PerlVariableStubSerializingFactory(val elementType: IElementType) :
  StubSerializingElementFactory<PerlVariableDeclarationStub, PerlVariableDeclarationElement> {
  override fun createPsi(stub: PerlVariableDeclarationStub): PerlVariableDeclarationElement =
    PsiPerlVariableDeclarationElementImpl(stub, elementType)

  override fun createStub(psi: PerlVariableDeclarationElement, parentStub: StubElement<out PsiElement>?): PerlVariableDeclarationStub =
    PerlVariableDeclarationStub(
      parentStub,
      elementType,
      psi.getNamespaceName()!!,
      psi.name!!,
      psi.getDeclaredValue(),
      psi.getActualType(),
      psi.getLocalVariableAnnotations()
    )

  override fun getExternalId(): String = "perl.$elementType"

  override fun serialize(stub: PerlVariableDeclarationStub, dataStream: StubOutputStream) {
    dataStream.writeName(stub.namespaceName)
    dataStream.writeName(stub.variableName)
    stub.declaredValue.serialize(dataStream)
    dataStream.writeByte(stub.actualType.ordinal)

    val annotations = stub.variableAnnotations
    if (annotations.isEmpty) {
      dataStream.writeBoolean(false)
    }
    else {
      dataStream.writeBoolean(true)
      annotations.serialize(dataStream)
    }
  }

  override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): PerlVariableDeclarationStub =
    PerlVariableDeclarationStub(
      parentStub,
      elementType,
      PerlStubSerializationUtil.readNotNullString(dataStream),
      PerlStubSerializationUtil.readNotNullString(dataStream),
      PerlValuesManager.readValue(dataStream),
      PerlVariableType.entries[dataStream.readByte().toInt()],
      readAnnotations(dataStream)
    )

  @Throws(IOException::class)
  private fun readAnnotations(dataStream: StubInputStream): PerlVariableAnnotations =
    if (dataStream.readBoolean()) PerlVariableAnnotations.deserialize(dataStream) else PerlVariableAnnotations.empty()

  override fun indexStub(stub: PerlVariableDeclarationStub, sink: IndexSink) {
    val variableName = PerlPackageUtil.join(stub.namespaceName, stub.variableName)
    val indexKeys = stub.getIndexKey()
    sink.occurrence(indexKeys.getFirst(), variableName)
    sink.occurrence(indexKeys.getSecond(), stub.namespaceName)
  }

  override fun shouldCreateStub(node: ASTNode): Boolean {
    val psi = node.psi
    return psi is PerlVariableDeclarationElement &&
      psi.isValid &&
      psi.isGlobalDeclaration() &&
      StringUtil.isNotEmpty(psi.name) &&
      StringUtil.isNotEmpty(psi.getNamespaceName())
  }
}