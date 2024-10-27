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
package com.perl5.lang.perl.extensions.packageprocessor.impl

import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorBase
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement
import kotlinx.collections.immutable.toImmutableList

class ListMoreUtilsProcessor : PerlPackageProcessorBase() {
  private val EXPORT_OK_DESCRIPTORS: Map<String, PerlExportDescriptor> by lazy {
    ListMoreUtilsExports.EXPORT_OK.associate { it to PerlExportDescriptor.create("List::MoreUtils::PP", it) }
  }

  override fun getImports(useStatement: PerlUseStatementElement): List<PerlExportDescriptor> =
    useStatement.importParameters
      ?.distinct()
      ?.map { parameter -> EXPORT_OK_DESCRIPTORS.get(parameter) }
      ?.filterNotNull()?.toImmutableList()
      ?: emptyList()

  override fun addExports(
    useStatement: PerlUseStatementElement,
    export: MutableSet<in String>,
    exportOk: MutableSet<in String>
  ) {
    super.addExports(useStatement, export, exportOk)
    exportOk.addAll(ListMoreUtilsExports.EXPORT_OK)
  }
}
