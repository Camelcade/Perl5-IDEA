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

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.psi.tree.IElementType
import com.intellij.util.IncorrectOperationException
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue
import com.perl5.lang.perl.psi.PerlSubDefinitionElement
import com.perl5.lang.perl.psi.light.PerlLightSubDefinitionElement
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations
import com.perl5.lang.perl.psi.utils.PerlSubArgument


class PerlLightSubDefinitionStubSerializingFactory(elementType: IElementType) : PerlSubDefinitionStubSerializingFactory(elementType) {

  override fun createStub(psi: PerlSubDefinitionElement, parentStub: StubElement<out PsiElement>?): PerlSubDefinitionStub {
    if (psi is PerlLightSubDefinitionElement<*> && psi.isImplicit) {
      return createStubElement(
        parentStub,
        psi.namespaceName,
        psi.subName,
        emptyList(),
        psi.returnValueFromCode,
        null
      )
    }
    return super.createStub(psi, parentStub)
  }

  override fun createStubElement(
    parentStub: StubElement<out PsiElement>?,
    packageName: String?,
    functionName: String,
    arguments: List<PerlSubArgument>,
    returnValueFromCode: PerlValue,
    annotations: PerlSubAnnotations?
  ): PerlSubDefinitionStub =
    PerlLightSubDefinitionStub(parentStub, packageName, functionName, arguments, annotations, returnValueFromCode, elementType)

  override fun getDirectKey(): StubIndexKey<String, out PsiElement> = PerlLightSubDefinitionsIndex.KEY

  override fun getReverseKey(): StubIndexKey<String, out PsiElement> = PerlLightSubDefinitionsReverseIndex.KEY

  override fun getCallableNameKey(): StubIndexKey<String, out PsiElement> = PerlLightCallableNamesIndex.KEY

  override fun createPsi(stub: PerlSubDefinitionStub): PerlSubDefinitionElement =
    throw IncorrectOperationException("Light elements should be created by wrappers, not element types")

}