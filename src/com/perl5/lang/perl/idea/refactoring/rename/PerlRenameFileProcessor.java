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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.refactoring.RenameRefactoring;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.rename.RenamePsiFileProcessor;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 05.06.2015.
 */
public class PerlRenameFileProcessor extends RenamePsiFileProcessor {
  @Override
  public boolean canProcessElement(@NotNull PsiElement element) {
    return element instanceof PerlFileImpl && ((PerlFileImpl)element).getVirtualFile().getFileType() == PerlFileTypePackage.INSTANCE;
  }

  @Nullable
  @Override
  public Runnable getPostRenameCallback(final PsiElement element, String newName, RefactoringElementListener elementListener) {
    if (newName.endsWith(".pm")) {
      final Project project = element.getProject();
      final String currentPackageName = ((PerlFileImpl)element).getFilePackageName();

      if (currentPackageName != null) {
        String[] nameChunks = currentPackageName.split(PerlPackageUtil.PACKAGE_SEPARATOR);
        nameChunks[nameChunks.length - 1] = newName.replaceFirst("\\.pm$", "");
        final String newPackageName = StringUtils.join(nameChunks, PerlPackageUtil.PACKAGE_SEPARATOR);

        final String newFileName = ((PerlFileImpl)element).getVirtualFile().getParent().getPath() + '/' + newName;

        return () ->
        {
          VirtualFile newFile = LocalFileSystem.getInstance().findFileByPath(newFileName);

          if (newFile != null) {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(newFile);

            if (psiFile != null) {
              final RenameRefactoring[] refactoring = {null};

              for (PerlNamespaceDefinitionElement namespaceDefinition : PsiTreeUtil
                .findChildrenOfType(psiFile, PerlNamespaceDefinitionElement.class)) {
                if (currentPackageName.equals(namespaceDefinition.getPackageName())) {
                  if (refactoring[0] == null) {
                    refactoring[0] = RefactoringFactory.getInstance(psiFile.getProject()).createRename(namespaceDefinition, newPackageName);
                  }
                  else {
                    refactoring[0].addElement(namespaceDefinition, newPackageName);
                  }
                }
              }

              if (refactoring[0] != null) {
                ApplicationManager.getApplication().invokeLater(refactoring[0]::run);
              }
            }
          }
        };
      }
    }
    return super.getPostRenameCallback(element, newName, elementListener);
  }
}
