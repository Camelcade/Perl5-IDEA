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

package com.perl5.lang.perl.extensions.packageprocessor.impl

import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageOptionsProvider
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorBase
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement
import kotlinx.collections.immutable.toImmutableMap

class TestMoreProcessor : PerlPackageProcessorBase(), PerlPackageOptionsProvider {
  private val myOptions: Map<String, String> by lazy {
    mapOf(
      "no_plan" to "",
      "skip_all" to "Skip all tests",
      "tests" to "Number of tests expected"
    ).toImmutableMap()
  }

  override fun getImportParameters(useStatement: PerlUseStatementElement): List<String>? =
    null // this will cause descriptors to ignore parameters above

  override fun getOptions(): Map<String, String> = myOptions

  override fun getOptionsBundles(): Map<String, String> = emptyMap()
}