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

package com.perl5.lang.perl.psi.stubs.globs

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.psi.PerlGlobVariableElement
import com.perl5.lang.perl.psi.PsiPerlGlobVariable
import com.perl5.lang.perl.psi.impl.PsiPerlGlobVariableImpl
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil
import com.perl5.lang.perl.psi.stubs.PerlStubSerializingFactory
import com.perl5.lang.perl.psi.stubs.globs.PerlGlobNamespaceStubIndex.KEY_GLOB_NAMESPACE
import com.perl5.lang.perl.psi.stubs.globs.PerlGlobStubIndex.KEY_GLOB
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlCallableNamesIndex


class PerlGlobStubSerializingFactory(elementType: IElementType) :
  PerlStubSerializingFactory<PerlGlobStub, PsiPerlGlobVariable>(elementType) {
  override fun createPsi(stub: PerlGlobStub): PsiPerlGlobVariable = PsiPerlGlobVariableImpl(stub, elementType)

  override fun createStub(psi: PsiPerlGlobVariable, parentStub: StubElement<out PsiElement>?): PerlGlobStub =
    PerlGlobStub(parentStub, psi.getNamespaceName()!!, psi.name, psi.isLeftSideOfAssignment())

  override fun serialize(stub: PerlGlobStub, dataStream: StubOutputStream) {
    dataStream.writeName(stub.namespaceName)
    dataStream.writeName(stub.globName)
    dataStream.writeBoolean(stub.isLeftSideOfAssignment)
  }

  override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): PerlGlobStub =
    PerlGlobStub(
      parentStub, PerlStubSerializationUtil.readString(dataStream)!!,
      PerlStubSerializationUtil.readString(dataStream), dataStream.readBoolean()
    )

  override fun indexStub(stub: PerlGlobStub, sink: IndexSink) {
    val canonicalName = stub.getCanonicalName()
    if (StringUtil.isNotEmpty(canonicalName)) {
      sink.occurrence(KEY_GLOB, canonicalName!!)
    }
    val namespaceName = stub.namespaceName
    if (StringUtil.isNotEmpty(namespaceName)) {
      sink.occurrence(KEY_GLOB_NAMESPACE, namespaceName)
    }
    val callableName = stub.callableName
    if (StringUtil.isNotEmpty(callableName)) {
      sink.occurrence(PerlCallableNamesIndex.KEY, callableName)
    }
  }

  override fun shouldCreateStub(node: ASTNode): Boolean =
    (node.psi as? PerlGlobVariableElement)?.let { it.isValid && StringUtil.isNotEmpty(it.getCanonicalName()) } == true
}