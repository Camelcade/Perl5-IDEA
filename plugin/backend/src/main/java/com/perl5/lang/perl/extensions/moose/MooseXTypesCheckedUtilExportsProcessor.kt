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
package com.perl5.lang.perl.extensions.moose

import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor
import com.perl5.lang.perl.extensions.packageprocessor.impl.BaseStrictWarningsProvidingProcessor
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement
import com.perl5.lang.perl.util.PerlPackageUtilCore
import kotlinx.collections.immutable.toImmutableList

class MooseXTypesCheckedUtilExportsProcessor : BaseStrictWarningsProvidingProcessor() {
  private val myExports: List<PerlExportDescriptor> by lazy {
    listOf(
      "as",
      "class_type",
      "coerce",
      "duck_type",
      "enum",
      "from",
      "maybe_type",
      "role_type",
      "subtype",
      "type"
    ).map { name -> PerlExportDescriptor.create(PerlPackageUtilCore.PACKAGE_MOOSE_X_TYPES_CHECKEDUTILEXPORTS, name) }
      .toImmutableList()
  }

  override fun getImports(useStatement: PerlUseStatementElement): List<PerlExportDescriptor> = myExports
}
