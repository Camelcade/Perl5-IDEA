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
package com.perl5.lang.perl.extensions.moo

import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageLoader
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageParentsProvider
import com.perl5.lang.perl.extensions.packageprocessor.impl.BaseStrictWarningsProvidingProcessor
import com.perl5.lang.perl.parser.moose.MooseSyntax.MOOSE_KEYWORD_AFTER
import com.perl5.lang.perl.parser.moose.MooseSyntax.MOOSE_KEYWORD_AROUND
import com.perl5.lang.perl.parser.moose.MooseSyntax.MOOSE_KEYWORD_BEFORE
import com.perl5.lang.perl.parser.moose.MooseSyntax.MOOSE_KEYWORD_EXTENDS
import com.perl5.lang.perl.parser.moose.MooseSyntax.MOOSE_KEYWORD_HAS
import com.perl5.lang.perl.parser.moose.MooseSyntax.MOOSE_KEYWORD_WITH
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement
import com.perl5.lang.perl.util.PerlPackageUtilCore
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.annotations.NonNls

class MooProcessor : BaseStrictWarningsProvidingProcessor(), PerlPackageParentsProvider, PerlPackageLoader {
  private val mooObject: @NonNls String = PerlPackageUtilCore.join(PerlPackageUtilCore.PACKAGE_MOO, "Object")
  private val loadedClasses: PersistentList<String> = persistentListOf(mooObject)
  private val parentClasses: PersistentList<String> = loadedClasses

  private val myExports: List<PerlExportDescriptor> by lazy {
    listOf(
      MOOSE_KEYWORD_AFTER,
      MOOSE_KEYWORD_AROUND,
      MOOSE_KEYWORD_BEFORE,
      MOOSE_KEYWORD_EXTENDS,
      MOOSE_KEYWORD_HAS,
      MOOSE_KEYWORD_WITH
    ).map { keyword -> PerlExportDescriptor.create(PerlPackageUtilCore.PACKAGE_MOO, keyword) }
      .toImmutableList()
  }

  override fun getLoadedPackageNames(useStatement: PerlUseStatementElement?): List<String> = loadedClasses

  override fun changeParentsList(useStatement: PerlUseStatementElement, currentList: MutableList<in String>) {
    currentList.apply {
      clear()
      addAll(parentClasses)
    }
  }

  override fun hasPackageFilesOptions(): Boolean = false

  override fun getImports(useStatement: PerlUseStatementElement): List<PerlExportDescriptor> = myExports
}
