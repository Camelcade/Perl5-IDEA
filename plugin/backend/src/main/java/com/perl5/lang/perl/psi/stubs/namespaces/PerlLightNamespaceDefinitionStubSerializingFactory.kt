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

package com.perl5.lang.perl.psi.stubs.namespaces

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.psi.tree.IElementType
import com.intellij.util.IncorrectOperationException
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement
import com.perl5.lang.perl.psi.stubs.namespaces.PerlLightNamespaceDescendantsIndex.LIGHT_NAMESPACE_DESCENDANTS_KEY
import com.perl5.lang.perl.psi.stubs.namespaces.PerlLightNamespaceIndex.LIGHT_NAMESPACE_KEY


class PerlLightNamespaceDefinitionStubSerializingFactory(elementType: IElementType) :
  PerlNamespaceDefinitionStubSerializingFactory(elementType) {

  override fun createPsi(stub: PerlNamespaceDefinitionStub): PerlNamespaceDefinitionElement =
    throw IncorrectOperationException("Light elements should be created by wrappers, not element types")

  override fun getNamespacesIndexKey(): StubIndexKey<String, out PsiElement> = LIGHT_NAMESPACE_KEY

  override fun getDescendantsIndexKey(): StubIndexKey<String, out PsiElement> = LIGHT_NAMESPACE_DESCENDANTS_KEY

  override fun createStubElement(parentStub: StubElement<*>?, data: PerlNamespaceDefinitionData): PerlNamespaceDefinitionStub =
    PerlLightNamespaceDefinitionStub(parentStub, elementType, data)
}