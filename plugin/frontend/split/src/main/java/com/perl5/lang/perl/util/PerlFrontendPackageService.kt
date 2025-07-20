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

package com.perl5.lang.perl.util

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.perl5.lang.perl.psi.PerlFile
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement
import com.perl5.lang.perl.util.PerlCorePackages.CORE_PACKAGES_DEPRECATED
import com.perl5.lang.pod.parser.psi.PodFile

class PerlFrontendPackageService : PerlPackageService {
  override fun getFilePackageName(perlFile: PerlFile): String? = null
  override fun getFilePackageName(podFile: PodFile): String? = null
  override fun addExports(
    useStatement: PerlUseStatementElement,
    export: MutableSet<in String>,
    exportOk: MutableSet<in String>
  ): Unit = Unit

  override fun isDeprecated(
    project: Project,
    searchScope: GlobalSearchScope,
    packageCanonicalName: String
  ): Boolean = CORE_PACKAGES_DEPRECATED.contains(packageCanonicalName)
}