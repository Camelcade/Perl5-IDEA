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
import com.intellij.psi.PsiFile
import com.intellij.psi.stubs.PsiFileStubImpl
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.tree.IElementType
import com.intellij.util.IncorrectOperationException
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue
import com.perl5.lang.perl.psi.stubs.PerlLightElementStub
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations
import com.perl5.lang.perl.psi.utils.PerlSubArgument

class PerlLightSubDefinitionStub(
  private val myParent: StubElement<*>?,
  packageName: String?,
  subName: String,
  arguments: List<PerlSubArgument>,
  annotations: PerlSubAnnotations?,
  returnValueFromCode: PerlValue,
  elementType: IElementType
) : PerlSubDefinitionStub(PsiFileStubImpl<PsiFile?>(null), packageName, subName, arguments, annotations, returnValueFromCode, elementType),
    PerlLightElementStub {
  private var myIsImplicit = false

  override fun getParentStub(): StubElement<*>? = myParent

  override fun <E : PsiElement?> getParentStubOfType(parentClass: Class<E?>): Nothing = throw IncorrectOperationException("NYI")

  override fun toString(): String = elementType.toString() + ":" + super.toString()

  override fun isImplicit(): Boolean = myIsImplicit

  override fun setImplicit(isImplicit: Boolean) {
    myIsImplicit = isImplicit
  }
}
