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

package com.perl5.lang.pod.parser.psi.references.providers

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import com.perl5.lang.pod.parser.psi.PodSectionTitle
import com.perl5.lang.pod.parser.psi.impl.PodIdentifierImpl
import com.perl5.lang.pod.parser.psi.references.PodSubReference


class PodIdentifierReferenceProvider : PsiReferenceProvider() {
  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> =
    (element as? PodIdentifierImpl)?.let {
      if (element.parent is PodSectionTitle && element.prevSibling == null) {
        return@let arrayOf(PodSubReference(element))
      }
      null
    } ?: PsiReference.EMPTY_ARRAY
}