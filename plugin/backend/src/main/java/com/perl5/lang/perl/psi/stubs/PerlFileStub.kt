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
package com.perl5.lang.perl.psi.stubs

import com.intellij.psi.stubs.PsiFileStubImpl
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.psi.PerlFile
import com.perl5.lang.perl.psi.PerlNamespaceDefinition
import com.perl5.lang.perl.psi.mro.PerlMroType
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionData
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations

class PerlFileStub : PsiFileStubImpl<PerlFile>, PerlNamespaceDefinition {
  val data: PerlNamespaceDefinitionData
  private val myElementType: PerlFileElementType

  constructor(file: PerlFile, elementType: PerlFileElementType) : super(file) {
    myElementType = elementType
    this.data = PerlNamespaceDefinitionData(file)
  }

  constructor(data: PerlNamespaceDefinitionData, elementType: PerlFileElementType) : super(null) {
    this.data = data
    myElementType = elementType
  }

  val isEmpty: Boolean
    /**
     * @return true iff this stub is empty and contains no useful data
     * @see PerlNamespaceDefinitionData.isEmpty
     */
    get() = data.isEmpty

  override fun getFileElementType(): IElementType = myElementType

  override fun getNamespaceName(): String = data.namespaceName

  override fun getMroType(): PerlMroType = data.mroType

  override fun getParentNamespacesNames(): MutableList<String?> = data.parentNamespacesNames

  override fun getAnnotations(): PerlNamespaceAnnotations? = data.annotations

  override fun getEXPORT(): MutableList<String> = data.export

  override fun getEXPORT_OK(): MutableList<String> = data.exporT_OK

  override fun getEXPORT_TAGS(): MutableMap<String, MutableList<String>> = data.exporT_TAGS

  override fun toString(): String = super.toString() + "\n" +
    this.data + "\n" +
    "\tType: " + myElementType
}
