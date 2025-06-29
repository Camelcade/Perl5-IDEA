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

package com.perl5.lang.perl.psi.references.providers

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionWithIdentifier
import com.perl5.lang.perl.psi.PsiPerlRequireExpr
import com.perl5.lang.perl.psi.impl.PerlNamespaceElementImpl
import com.perl5.lang.perl.psi.impl.PerlUseStatementElementBase
import com.perl5.lang.perl.psi.references.PerlNamespaceFileReference
import com.perl5.lang.perl.psi.references.PerlNamespaceReference


class PerlNamespaceElementReferenceProvider : PsiReferenceProvider() {
  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> =
    (element as? PerlNamespaceElementImpl)?.let {
      val nameSpaceContainer: PsiElement? = element.parent
      if (nameSpaceContainer is PerlUseStatementElementBase || nameSpaceContainer is PsiPerlRequireExpr) {
        return arrayOf(PerlNamespaceFileReference(element))
      }
      else if (nameSpaceContainer is PerlNamespaceDefinitionWithIdentifier) {
        return PsiReference.EMPTY_ARRAY
      }
      return arrayOf(PerlNamespaceReference(element))
    } ?: PsiReference.EMPTY_ARRAY
}