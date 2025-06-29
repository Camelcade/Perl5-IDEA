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
import com.perl5.lang.pod.parser.psi.PodLinkDescriptor
import com.perl5.lang.pod.parser.psi.mixin.PodFormatterL
import com.perl5.lang.pod.parser.psi.references.PodLinkToFileReference
import com.perl5.lang.pod.parser.psi.references.PodLinkToSectionReference
import com.perl5.lang.pod.psi.PsiLinkName
import com.perl5.lang.pod.psi.PsiLinkSection


class PodFormatterLReferenceProvider : PsiReferenceProvider() {
  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> =
    (element as? PodFormatterL)?.let {
      val references: MutableList<PsiReference> = ArrayList()
      val descriptor: PodLinkDescriptor? = element.linkDescriptor

      if (descriptor != null && !descriptor.isUrl) {
        val linkStartOffset: Int = element.textRange.startOffset
        val linkNameElement: PsiLinkName? = element.linkNameElement
        if (linkNameElement != null) {
          references.add(PodLinkToFileReference(element, linkNameElement.textRange.shiftLeft(linkStartOffset)))
        }

        val linkSectionElement: PsiLinkSection? = element.linkSectionElement
        if (linkSectionElement != null) {
          references.add(PodLinkToSectionReference(element, linkSectionElement.textRange.shiftLeft(linkStartOffset)))
        }
      }
      return references.toTypedArray()
    } ?: PsiReference.EMPTY_ARRAY
}