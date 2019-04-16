/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.presentations;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.openapi.roots.ex.ProjectRootManagerEx;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.util.PerlUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.Nullable;

public class PerlItemPresentationSimpleDynamicLocation extends PerlItemPresentationSimple {

  public PerlItemPresentationSimpleDynamicLocation(PsiElement element, String presentableText) {
    super(element, presentableText);
  }

  @Nullable
  @Override
  public String getLocationString() {
    PsiFile containingFile = getElement().getContainingFile();
    String locationString = null;

    if (containingFile == null) {
      return locationString;
    }

    locationString = containingFile.getName();
    VirtualFile virtualFile = containingFile.getVirtualFile();

    if (virtualFile == null) {
      return locationString;
    }

    if (virtualFile.getFileType() == PerlFileTypePackage.INSTANCE) {
      VirtualFile innerMostClassRoot = PerlUtil.getFileClassRoot(containingFile.getProject(), virtualFile);

      if (innerMostClassRoot != null) {
        String relativePath = VfsUtil.getRelativePath(virtualFile, innerMostClassRoot);
        return PerlPackageUtil.getPackageNameByPath(relativePath);
      }
    }

    // trying to get project's root directory
    VirtualFile innerMostSourceRoot = ProjectRootManagerEx.getInstanceEx(containingFile.getProject()).getFileIndex().getContentRootForFile(virtualFile);
    if (innerMostSourceRoot != null) {
      return VfsUtil.getRelativePath(virtualFile, innerMostSourceRoot);
    } else {
      return virtualFile.getPath();
    }
  }
}
