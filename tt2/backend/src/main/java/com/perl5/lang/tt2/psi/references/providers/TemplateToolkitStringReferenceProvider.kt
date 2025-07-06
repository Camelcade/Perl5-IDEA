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

package com.perl5.lang.tt2.psi.references.providers

import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileSystemItem
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiUtilCore
import com.intellij.util.ProcessingContext
import com.perl5.lang.tt2.parser.TemplateToolkitElementTypesGenerated.*
import com.perl5.lang.tt2.psi.mixins.TemplateToolkitStringMixin
import com.perl5.lang.tt2.psi.mixins.TemplateToolkitStringMixin.BLOCK_NAME_TARGETED_CONTAINERS
import com.perl5.lang.tt2.psi.references.TemplateToolkitBlockReference


private val FILES_TARGETED_CONTAINERS: TokenSet = TokenSet.create(INSERT_DIRECTIVE, INCLUDE_DIRECTIVE, PROCESS_DIRECTIVE, WRAPPER_DIRECTIVE)

class TemplateToolkitStringReferenceProvider : PsiReferenceProvider() {
  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> =
    (element as? TemplateToolkitStringMixin)?.let {
      val references: MutableList<PsiReference> = ArrayList()
      val parentElementType: IElementType? = PsiUtilCore.getElementType(element.parent)

      if (parentElementType in FILES_TARGETED_CONTAINERS) {
        references.addAll(object : FileReferenceSet(element) {
          override fun computeDefaultContexts(): MutableCollection<PsiFileSystemItem?> {
            val path = pathString
            val containingFile = this.containingFile
            if (StringUtil.startsWith(path, ".") && containingFile != null && containingFile.parent != null) {
              return mutableListOf(containingFile.parent)
            }
            return super.computeDefaultContexts()
          }
        }.allReferences)
      }

      if (parentElementType in BLOCK_NAME_TARGETED_CONTAINERS) {
        references.add(TemplateToolkitBlockReference(element))
      }
      return references.toTypedArray()

    } ?: PsiReference.EMPTY_ARRAY
}