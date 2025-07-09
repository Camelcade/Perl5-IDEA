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

package com.perl5.lang.perl.psi.stubs.calls

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.psi.PerlSubCallHandler
import com.perl5.lang.perl.psi.impl.PerlSubCallElement
import com.perl5.lang.perl.psi.impl.PsiPerlSubCallImpl
import com.perl5.lang.perl.psi.stubs.PerlPolynamedStubSerializingFactory


class PerlSubCallStubSerializingFactory(elementType: IElementType) :
  PerlPolynamedStubSerializingFactory<PerlSubCallElementStub, PerlSubCallElement>(elementType) {
  override fun createStub(
    psi: PerlSubCallElement,
    parentStub: StubElement<*>?,
    lightElementsStubs: MutableList<StubElement<*>>
  ): PerlSubCallElementStub =
    PerlSubCallElementStub(parentStub, elementType, lightElementsStubs, psi.getSubName()!!, psi.getCallData())

  override fun deserialize(
    dataStream: StubInputStream,
    parentStub: StubElement<*>?,
    lightElementsStubs: MutableList<StubElement<*>>
  ): PerlSubCallElementStub {
    val subName: String = dataStream.readNameString()!!
    val provider: PerlSubCallHandler<*> = getProvider(subName)
    return PerlSubCallElementStub(parentStub, elementType, lightElementsStubs, subName, provider.deserialize(dataStream))
  }

  override fun serializeStub(stub: PerlSubCallElementStub, dataStream: StubOutputStream) {
    dataStream.writeName(stub.subName)
    getProvider(stub.subName).serialize(stub.callData, dataStream)
  }

  private fun getProvider(subName: String): PerlSubCallHandler<*> =
    PerlSubCallHandler.getHandler(subName) ?: throw IllegalArgumentException("Unable to find call handler for $subName")

  override fun createPsi(stub: PerlSubCallElementStub): PerlSubCallElement = PsiPerlSubCallImpl(stub, elementType)
}