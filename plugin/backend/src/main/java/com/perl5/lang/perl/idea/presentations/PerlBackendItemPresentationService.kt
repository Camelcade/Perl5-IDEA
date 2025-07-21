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

package com.perl5.lang.perl.idea.presentations

import com.intellij.openapi.roots.ex.ProjectRootManagerEx
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage
import com.perl5.lang.perl.util.PerlPackageUtil


class PerlBackendItemPresentationService : PerlItemPresentationService {
  override fun getLocation(psiElement: PsiElement): String? {
    val containingFile: PsiFile = psiElement.containingFile ?: return null

    val locationString = containingFile.name
    val virtualFile = containingFile.virtualFile ?: return locationString

    if (virtualFile.fileType === PerlFileTypePackage.INSTANCE) {
      val innerMostClassRoot = PerlPackageUtil.getClosestIncRoot(containingFile.project, virtualFile)

      if (innerMostClassRoot != null) {
        val relativePath = VfsUtilCore.getRelativePath(virtualFile, innerMostClassRoot)
        return PerlPackageUtil.getPackageNameByPath(relativePath)
      }
    }

    // trying to get project's root directory
    val innerMostSourceRoot =
      ProjectRootManagerEx.getInstanceEx(containingFile.project).fileIndex.getContentRootForFile(virtualFile)
    return if (innerMostSourceRoot != null) {
      VfsUtilCore.getRelativePath(virtualFile, innerMostSourceRoot)!!
    }
    else {
      virtualFile.path
    }
  }
}