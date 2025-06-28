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
package com.perl5.lang.perl.extensions.mojo

import com.perl5.lang.perl.extensions.packageprocessor.*
import com.perl5.lang.perl.extensions.packageprocessor.impl.BaseStrictWarningsProvidingProcessor
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement

private const val MOJO_BASE: String = "Mojo::Base"
private const val IO_HANDLE: String = "IO::Handle"

class MojoBaseProcessor : BaseStrictWarningsProvidingProcessor(), PerlUtfProvider, PerlFeaturesProvider, PerlPackageOptionsProvider,
                          PerlPackageParentsProvider, PerlPackageLoader {

  protected val OPTIONS: Map<String, String> by lazy {
    mapOf(
      "-strict" to "strict,warnings,utf8,v5.10,IO::Handle",
      "-base" to "strict,warnings,utf8,v5.10,IO::Handle,acts as parent"
    )
  }

  override fun getLoadedPackageNames(useStatement: PerlUseStatementElement): MutableList<String> {
    val loadedPackages: MutableList<String> = mutableListOf(IO_HANDLE)
    val allOptions = useStatement.importParameters ?: return loadedPackages

    allOptions -= getOptions().keys

    if (allOptions.isNotEmpty() && MOJO_BASE != allOptions.first()) {
      loadedPackages += allOptions.first()
    }

    return loadedPackages
  }

  override fun getOptions(): Map<String, String> = OPTIONS

  override fun getOptionsBundles(): Map<String, String> = emptyMap()

  override fun changeParentsList(useStatement: PerlUseStatementElement, currentList: MutableList<in String>) {
    val allOptions = useStatement.importParameters ?: return

    if ("-base" in allOptions) {
      currentList += MOJO_BASE
    }
    else {
      allOptions -= getOptions().keys
      if (!allOptions.isEmpty()) {
        currentList += allOptions.first()
      }
    }
  }

  override fun hasPackageFilesOptions(): Boolean = true
}
