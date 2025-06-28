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
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement

class ParentProcessor : BaseProcessor(), PerlPackageOptionsProvider {
  protected val myOptions: Map<String, String> by lazy {
    mapOf("-norequire" to "suppresses attempt to require a package file")
  }

  override fun getOptions(): Map<String, String> = myOptions

  override fun getOptionsBundles(): Map<String, String> = emptyMap()

  override fun changeParentsList(useStatement: PerlUseStatementElement, currentList: MutableList<in String>) {
    super.changeParentsList(useStatement, currentList)
    currentList -= getOptions().keys
  }
}
