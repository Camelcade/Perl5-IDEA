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
package com.perl5.lang.perl.extensions.role.tiny

import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor
import com.perl5.lang.perl.extensions.packageprocessor.impl.BaseStrictWarningsProvidingProcessor
import com.perl5.lang.perl.parser.moose.MooseSyntax.MOOSE_KEYWORD_AFTER
import com.perl5.lang.perl.parser.moose.MooseSyntax.MOOSE_KEYWORD_AROUND
import com.perl5.lang.perl.parser.moose.MooseSyntax.MOOSE_KEYWORD_BEFORE
import com.perl5.lang.perl.parser.moose.MooseSyntax.MOOSE_KEYWORD_REQUIRES
import com.perl5.lang.perl.parser.moose.MooseSyntax.MOOSE_KEYWORD_WITH
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement
import kotlinx.collections.immutable.toImmutableList

class RoleTinyProcessor : BaseStrictWarningsProvidingProcessor() {
  private val myExports: List<PerlExportDescriptor> by lazy {
    listOf(
      MOOSE_KEYWORD_AFTER,
      MOOSE_KEYWORD_AROUND,
      MOOSE_KEYWORD_BEFORE,
      MOOSE_KEYWORD_REQUIRES,
      MOOSE_KEYWORD_WITH
    ).map { keyword -> PerlExportDescriptor.create("Role::Tiny", keyword) }
      .toImmutableList()
  }

  override fun getImports(useStatement: PerlUseStatementElement): List<PerlExportDescriptor> = myExports

}
