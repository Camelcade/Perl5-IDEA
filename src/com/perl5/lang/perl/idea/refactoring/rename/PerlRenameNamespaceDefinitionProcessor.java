/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.refactoring.rename;

import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 29.05.2015.
 */
public class PerlRenameNamespaceDefinitionProcessor extends PerlRenamePolyReferencedElementProcessor {
  @Override
  public boolean canProcessElement(@NotNull PsiElement element) {
    return element instanceof PerlNamespaceDefinitionElement;
  }

  protected boolean isFileToBeRenamed(PerlNamespaceDefinitionElement namespaceDefinition) {
    String currentPackageName = namespaceDefinition.getPackageName();
    assert currentPackageName != null;

    VirtualFile virtualFile = namespaceDefinition.getContainingFile().getVirtualFile();

    if (virtualFile.getFileType() == PerlFileTypePackage.INSTANCE) {
      VirtualFile classRoot = PerlUtil.getFileClassRoot(namespaceDefinition.getProject(), virtualFile);

      if (classRoot != null &&
          currentPackageName.equals(PerlPackageUtil.getPackageNameByPath(VfsUtil.getRelativePath(virtualFile, classRoot)))) {
        return true;
      }
    }
    return false;
  }

  @Nullable
  @Override
  public Runnable getPostRenameCallback(PsiElement element, final String newName, RefactoringElementListener elementListener) {
    if (element instanceof PerlNamespaceDefinitionElement && isFileToBeRenamed((PerlNamespaceDefinitionElement)element)) {
      final PsiFile file = element.getContainingFile();

      return new Runnable() {
        @Override
        public void run() {
          for (PsiReference reference : ReferencesSearch.search(file, file.getUseScope()).findAll()) {
            PerlPsiUtil.renameFileReferencee(reference.getElement(), newName);
          }

          // rename file
          String newPackageName = PerlPackageUtil.getCanonicalPackageName(newName);
          List<String> newPackageChunks = Arrays.asList(newPackageName.split(PerlPackageUtil.PACKAGE_SEPARATOR));
          String newFileName = newPackageChunks.get(newPackageChunks.size() - 1) + ".pm";
          file.setName(newFileName);

          // move file
          VirtualFile containingDir = file.getVirtualFile().getParent();
          VirtualFile newContainingDir = PerlUtil.getFileClassRoot(file.getProject(), containingDir);

          for (int i = 0; i < newPackageChunks.size() - 1; i++) {
            String subDirName = newPackageChunks.get(i);

            assert subDirName != null && !subDirName.isEmpty();
            assert newContainingDir != null;

            VirtualFile subDir = newContainingDir.findChild(subDirName);

            try {
              newContainingDir = subDir != null ? subDir : newContainingDir.createChildDirectory(null, subDirName);
            }
            catch (IOException e) {
              throw new IncorrectOperationException("Could not create subdirectory: " + newContainingDir.getPath() + "/" + subDirName);
            }
          }

          if (newContainingDir != null && !newContainingDir.equals(containingDir)) {
            try {
              file.getVirtualFile().move(this, newContainingDir);
            }
            catch (IOException e) {
              throw new IncorrectOperationException("Could not move package file to the: " + newContainingDir.getPath());
            }
          }
        }
      };
    }

    return super.getPostRenameCallback(element, newName, elementListener);
  }
}
