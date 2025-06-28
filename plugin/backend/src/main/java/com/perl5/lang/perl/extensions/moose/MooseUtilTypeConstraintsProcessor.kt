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
package com.perl5.lang.perl.extensions.moose

import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor
import com.perl5.lang.perl.extensions.packageprocessor.impl.BaseStrictWarningsProvidingProcessor
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement
import com.perl5.lang.perl.util.PerlPackageUtil
import kotlinx.collections.immutable.toImmutableList

class MooseUtilTypeConstraintsProcessor : BaseStrictWarningsProvidingProcessor() {
  private val EXPORTS: List<PerlExportDescriptor> by lazy {
    listOf(
      "as", "class_type", "coerce", "duck_type", "enum", "find_type_constraint", "from", "inline_as", "match_on_type", "maybe_type",
      "message", "register_type_constraint", "role_type", "subtype", "type", "union", "via", "where"
    ).map { name -> PerlExportDescriptor.create(PerlPackageUtil.PACKAGE_MOOSE_UTIL_TYPE_CONSTRAINTS, name) }
      .toImmutableList()
  }

  override fun getImports(useStatement: PerlUseStatementElement): List<PerlExportDescriptor> = EXPORTS
}
