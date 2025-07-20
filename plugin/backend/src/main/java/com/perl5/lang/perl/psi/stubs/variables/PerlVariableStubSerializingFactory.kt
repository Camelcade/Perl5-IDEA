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
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValueSerializer.serialize
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement
import com.perl5.lang.perl.psi.impl.PsiPerlVariableDeclarationElementImpl
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil
import com.perl5.lang.perl.psi.stubs.PerlStubSerializingFactory
import com.perl5.lang.perl.psi.utils.PerlVariableAnnotations
import com.perl5.lang.perl.psi.utils.PerlVariableType
import com.perl5.lang.perl.util.PerlPackageUtilCore
import com.perl5.lang.perl.util.PerlValuesUtil
import com.perl5.lang.perl.util.PerlVariableUtil
import java.io.IOException


class PerlVariableStubSerializingFactory(elementType: IElementType) :
  PerlStubSerializingFactory<PerlVariableDeclarationStub, PerlVariableDeclarationElement>(elementType) {
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

  override fun serialize(stub: PerlVariableDeclarationStub, dataStream: StubOutputStream) {
    dataStream.writeName(stub.namespaceName)
    dataStream.writeName(stub.variableName)
    serialize(stub.declaredValue, dataStream)
    dataStream.writeByte(stub.actualType.ordinal)

    val annotations = stub.variableAnnotations
    if (annotations.isEmpty) {
      dataStream.writeBoolean(false)
    }
    else {
      dataStream.writeBoolean(true)
      dataStream.writeAnnotations(annotations)
    }
  }

  override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): PerlVariableDeclarationStub =
    PerlVariableDeclarationStub(
      parentStub,
      elementType,
      PerlStubSerializationUtil.readNotNullString(dataStream),
      PerlStubSerializationUtil.readNotNullString(dataStream),
      PerlValuesUtil.readValue(dataStream),
      PerlVariableType.entries[dataStream.readByte().toInt()],
      readAnnotations(dataStream)
    )

  @Throws(IOException::class)
  private fun readAnnotations(dataStream: StubInputStream): PerlVariableAnnotations =
    if (dataStream.readBoolean()) dataStream.readVariableAnnotations() else PerlVariableAnnotations.empty()

  override fun indexStub(stub: PerlVariableDeclarationStub, sink: IndexSink) {
    val variableName = PerlPackageUtilCore.join(stub.namespaceName, stub.variableName)
    val indexKeys = PerlVariableUtil.getIndexKey(stub)
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

fun StubOutputStream.writeAnnotations(annotations: PerlVariableAnnotations) {
  writeByte(annotations.flags.toInt())
  serialize(annotations.annotatedValue, this)
}

fun StubInputStream.readVariableAnnotations(): PerlVariableAnnotations =
  PerlVariableAnnotations(readByte(), PerlValuesUtil.readValue(this))
