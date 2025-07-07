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

package com.perl5.lang.perl.psi.stubs.subsdefinitions

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager
import com.perl5.lang.perl.psi.PerlSubDefinitionElement
import com.perl5.lang.perl.psi.impl.PsiPerlSubDefinitionImpl
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations
import com.perl5.lang.perl.psi.utils.PerlSubArgument


open class PerlSubDefinitionStubSerializingFactory(val elementType: IElementType) :
  StubSerializingElementFactory<PerlSubDefinitionStub, PerlSubDefinitionElement> {
  override fun createPsi(stub: PerlSubDefinitionStub): PerlSubDefinitionElement = PsiPerlSubDefinitionImpl(stub, elementType)

  override fun createStub(
    psi: PerlSubDefinitionElement,
    parentStub: StubElement<out PsiElement>?
  ): PerlSubDefinitionStub {
    return createStubElement(
      parentStub,
      psi.namespaceName,
      psi.subName,
      psi.subArgumentsList,
      psi.returnValueFromCode,
      psi.getAnnotations()
    )
  }

  override fun getExternalId(): String = "perl.$elementType"

  override fun serialize(
    stub: PerlSubDefinitionStub,
    dataStream: StubOutputStream
  ) {
    dataStream.writeName(stub.namespaceName)
    dataStream.writeName(stub.subName)

    PerlSubArgument.serializeList(dataStream, stub.subArgumentsList)


    val subAnnotations = stub.annotations
    if (subAnnotations == null) {
      dataStream.writeBoolean(false)
    }
    else {
      dataStream.writeBoolean(true)
      subAnnotations.serialize(dataStream)
    }
    stub.returnValueFromCode.serialize(dataStream)
  }

  override fun deserialize(
    dataStream: StubInputStream,
    parentStub: StubElement<*>?
  ): PerlSubDefinitionStub {
    val packageName = dataStream.readName()!!.getString()
    val functionName = dataStream.readName()!!.getString()
    val arguments = PerlSubArgument.deserializeList(dataStream)
    val annotations: PerlSubAnnotations? = if (dataStream.readBoolean()) PerlSubAnnotations.deserialize(dataStream) else null
    return createStubElement(parentStub, packageName, functionName!!, arguments, PerlValuesManager.readValue(dataStream), annotations)
  }

  override fun indexStub(
    stub: PerlSubDefinitionStub,
    sink: IndexSink
  ) {
    val canonicalName = stub.canonicalName
    if (canonicalName != null) {
      sink.occurrence(getDirectKey(), canonicalName)
    }
    val packageName = stub.namespaceName
    if (packageName != null) {
      sink.occurrence(getReverseKey(), packageName)
    }
    val callableName = stub.callableName
    if (StringUtil.isNotEmpty(callableName)) {
      sink.occurrence(getCallableNameKey(), callableName)
    }
  }

  override fun shouldCreateStub(node: ASTNode): Boolean {
    val element = node.psi
    return element is PerlSubDefinitionElement &&
      element.isValid &&
      StringUtil.isNotEmpty(element.getCanonicalName())
  }

  protected open fun createStubElement(
    parentStub: StubElement<out PsiElement>?,
    packageName: String?,
    functionName: String,
    arguments: List<PerlSubArgument>,
    returnValueFromCode: PerlValue,
    annotations: PerlSubAnnotations?
  ): PerlSubDefinitionStub =
    PerlSubDefinitionStub(parentStub, packageName, functionName, arguments, annotations, returnValueFromCode, elementType)

  protected open fun getDirectKey(): StubIndexKey<String, out PsiElement> = PerlSubDefinitionsIndex.KEY

  protected open fun getReverseKey(): StubIndexKey<String, out PsiElement> = PerlSubDefinitionReverseIndex.KEY

  protected open fun getCallableNameKey(): StubIndexKey<String, out PsiElement> = PerlCallableNamesIndex.KEY
}