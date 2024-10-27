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

import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageOptionsProvider
import com.perl5.lang.perl.extensions.packageprocessor.PerlPragmaProcessorBase
import com.perl5.lang.perl.internals.warnings.PerlWarningTree
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement
import kotlinx.collections.immutable.toImmutableMap

class WarningsProcessor : PerlPragmaProcessorBase(), PerlPackageOptionsProvider {
  private val OPTIONS: Map<String, String> by lazy {
    (mapOf("FATAL" to "FATALITY!") +
      PerlWarningTree.LEAF_OPTIONS.entries.associate { entry ->
        entry.key to entry.value.getMinVersion().getStrictDottedVersion()
      }
      ).toImmutableMap()
  }
  private val OPTIONS_BUNDLES: Map<String, String> by lazy {
    PerlWarningTree.NODE_OPTIONS.entries.associate { entry ->
      val joinedOptions = entry.value.collectChildLeafs()
        .joinToString(" ") { leaf -> leaf.getStringIdentifier() + "(" + leaf.getMinVersion().getStrictDottedVersion() + ")" }

      entry.key to "${entry.value.getMinVersion().getStrictDottedVersion()}, $joinedOptions"
    }.toImmutableMap()

  }

  override fun getOptions(): Map<String, String> = OPTIONS

  override fun getOptionsBundles(): Map<String, String> = OPTIONS_BUNDLES

  override fun isWarningsEnabled(useStatement: PerlUseStatementElement): Boolean = true
}
