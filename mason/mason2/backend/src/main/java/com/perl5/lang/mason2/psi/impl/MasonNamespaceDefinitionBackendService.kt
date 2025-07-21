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

package com.perl5.lang.mason2.psi.impl

import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VfsUtilCore
import com.perl5.lang.htmlmason.MasonCoreUtil
import com.perl5.lang.mason2.Mason2Util
import com.perl5.lang.mason2.Mason2UtilCore
import com.perl5.lang.mason2.elementType.getAbsoluteComponentPath
import com.perl5.lang.mason2.psi.MasonNamespaceDefinition
import com.perl5.lang.perl.util.PerlPackageUtilCore

class MasonNamespaceDefinitionBackendService : MasonNamespaceDefinitionService {
  override fun getNamespaceName(namespaceDefinition: MasonNamespaceDefinition): String? {
    val absoluteComponentPath: String? = namespaceDefinition.getAbsoluteComponentPath()
    if (absoluteComponentPath != null) {
      return Mason2UtilCore.getClassnameFromPath(absoluteComponentPath)
    }
    return null
  }

  override fun computeNamespaceName(namespaceDefinition: MasonNamespaceDefinition): String = Mason2Util.getVirtualFileClassName(
    namespaceDefinition.project,
    MasonCoreUtil.getContainingVirtualFile(namespaceDefinition.containingFile)
  ) ?: PerlPackageUtilCore.MAIN_NAMESPACE_NAME

  override fun getPresentableName(namespaceDefinition: MasonNamespaceDefinition): String? {
    val componentRoot = (namespaceDefinition.containingFile as MasonFileImpl).getComponentRoot()
    val containingFile = MasonCoreUtil.getContainingVirtualFile(namespaceDefinition.containingFile)

    if (componentRoot != null && containingFile != null) {
      val componentPath = VfsUtilCore.getRelativePath(containingFile, componentRoot)

      if (componentPath != null) {
        return VfsUtil.VFS_SEPARATOR_CHAR.toString() + componentPath
      }
    }

    return namespaceDefinition.name
  }
}