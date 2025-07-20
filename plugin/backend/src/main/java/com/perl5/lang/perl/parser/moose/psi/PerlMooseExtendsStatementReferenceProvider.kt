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

package com.perl5.lang.perl.parser.moose.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.perl5.lang.perl.psi.PerlString
import com.perl5.lang.perl.psi.PsiPerlStatement
import com.perl5.lang.perl.psi.references.PerlNamespaceReference


class PerlMooseExtendsStatementReferenceProvider : PsiReferenceProvider() {
  override fun getReferencesByElement(
    element: PsiElement,
    context: ProcessingContext
  ): Array<out PsiReference> {
    val provider = PsiTreeUtil.getParentOfType(
      element, PerlMooseExtendsStatement::class.java, true, PsiPerlStatement::class.java
    ) ?: return PsiReference.EMPTY_ARRAY

    val string = element.parent ?: return PsiReference.EMPTY_ARRAY
    val meOrCommaSequence = string.parent
    if (meOrCommaSequence === provider || meOrCommaSequence != null && meOrCommaSequence.parent === provider) {
      if (!PerlString.looksLikePackage(element.text)) {
        return arrayOf<PsiReference>(PerlNamespaceReference(element))
      }
    }
    return PsiReference.EMPTY_ARRAY
  }
}