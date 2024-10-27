/*
 * Copyright 2015-2024 Alexandr Evstigneev
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
package com.perl5.lang.perl.cpanminus.cpanfile

import com.intellij.psi.PsiFile
import com.perl5.lang.perl.extensions.imports.PerlImportsProvider
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement
import kotlinx.collections.immutable.toImmutableList

private const val NAMESPACE = "Module::CPANfile::Environment"

class PerlCpanfileImportProvider : PerlImportsProvider {

  private val myDescriptors: List<PerlExportDescriptor> by lazy {
    listOf(
      "on",
      "requires",
      "recommends",
      "suggests",
      "conflicts",
      "feature",
      "osname",
      "mirror",
      "configure_requires",
      "build_requires",
      "test_requires",
      "author_requires"
    ).map { name -> PerlExportDescriptor.create(NAMESPACE, name) }
      .toImmutableList()
  }

  override fun getExportDescriptors(namespaceElement: PerlNamespaceDefinitionElement): List<PerlExportDescriptor> = myDescriptors

  override fun isApplicable(namespaceDefinitionElement: PerlNamespaceDefinitionElement?): Boolean =
    (namespaceDefinitionElement as? PsiFile)?.getOriginalFile()?.getFileType() === PerlFileTypeCpanfile.INSTANCE
}
