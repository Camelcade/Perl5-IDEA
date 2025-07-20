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
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage
import com.perl5.lang.perl.psi.PerlFile
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement
import com.perl5.lang.perl.util.PerlCorePackages.CORE_PACKAGES_DEPRECATED
import com.perl5.lang.pod.parser.psi.PodFile
import com.perl5.lang.pod.parser.psi.util.PodFileUtil


class PerlBackendPackageService : PerlPackageService {
  override fun getFilePackageName(perlFile: PerlFile): String? {
    val containingFile: VirtualFile = perlFile.virtualFile ?: return null
    if (containingFile.fileType !== PerlFileTypePackage.INSTANCE) return null
    val innermostSourceRoot = PerlPackageUtil.getClosestIncRoot(perlFile.project, containingFile) ?: return null
    return PerlPackageUtil.getPackageNameByPath(VfsUtilCore.getRelativePath(containingFile, innermostSourceRoot))
  }

  override fun addExports(
    useStatement: PerlUseStatementElement,
    export: MutableSet<in String>,
    exportOk: MutableSet<in String>
  ) {
    val packageName = useStatement.packageName ?: return

    if (StringUtil.isEmpty(packageName)) {
      return
    }

    // fixme handle tags
    val scope = GlobalSearchScope.allScope(useStatement.getProject())
    for (namespaceDefinition in PerlNamespaceUtil
      .getNamespaceDefinitions(useStatement.getProject(), scope, packageName)) {
      val defaultExports = namespaceDefinition.getEXPORT()
      export.addAll(defaultExports)
      exportOk.addAll(defaultExports)
      exportOk.addAll(namespaceDefinition.getEXPORT_OK())
    }
  }

  override fun getFilePackageName(podFile: PodFile): String? = PodFileUtil.getPackageName(podFile)

  override fun isDeprecated(project: Project, searchScope: GlobalSearchScope, packageCanonicalName: String): Boolean =
    CORE_PACKAGES_DEPRECATED.contains(packageCanonicalName) ||
      !PerlNamespaceUtil.processNamespaces(
        packageCanonicalName,
        project,
        searchScope,
        Processor { it: PerlNamespaceDefinitionElement? -> !it!!.isDeprecated() })
}