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

package com.perl5.lang.perl.idea.refactoring.move;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.refactoring.RenameRefactoring;
import com.intellij.refactoring.move.moveFilesOrDirectories.MoveFileHandler;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 05.06.2015.
 */
public class PerlMoveFileHandler extends MoveFileHandler {
  private static final Key<String> ORIGINAL_PACKAGE_NAME = Key.create("PERL_ORIGINAL_PACKAGE_NAME");

  @Override
  public boolean canProcessElement(PsiFile element) {
    return element instanceof PerlFileImpl && element.getVirtualFile().getFileType() == PerlFileTypePackage.INSTANCE;
  }

  @Override
  public void prepareMovedFile(PsiFile file, PsiDirectory moveDestination, Map<PsiElement, PsiElement> oldToNewMap) {
    file.putUserData(ORIGINAL_PACKAGE_NAME, ((PerlFileImpl)file).getFilePackageName());

    String newFilePath = moveDestination.getVirtualFile().getPath() + '/' + file.getName();
    VirtualFile newClassRoot = PerlUtil.getFileClassRoot(moveDestination.getProject(), newFilePath);

    if (newClassRoot != null) {
      String newRelativePath = newFilePath.substring(newClassRoot.getPath().length());
      String newPackageName = PerlPackageUtil.getPackageNameByPath(newRelativePath);

      if (newPackageName != null) {
        for (PsiReference reference : ReferencesSearch.search(file, file.getUseScope()).findAll()) {
          PerlPsiUtil.renameFileReferencee(reference.getElement(), newPackageName);
        }
      }
    }
  }

  @Override
  public void updateMovedFile(PsiFile file) throws IncorrectOperationException {
    String originalPackageName = file.getUserData(ORIGINAL_PACKAGE_NAME);
    Project project = file.getProject();
    VirtualFile virtualFile = file.getVirtualFile();
    VirtualFile newInnermostRoot = PerlUtil.getFileClassRoot(project, virtualFile);

    if (newInnermostRoot != null && originalPackageName != null) {
      String newRelativePath = VfsUtil.getRelativePath(virtualFile, newInnermostRoot);
      String newPackageName = PerlPackageUtil.getPackageNameByPath(newRelativePath);

      final RenameRefactoring[] refactoring = {null};

      for (PerlNamespaceDefinitionElement namespaceDefinition : PsiTreeUtil
        .findChildrenOfType(file, PerlNamespaceDefinitionElement.class)) {
        if (originalPackageName.equals(namespaceDefinition.getPackageName())) {
          if (refactoring[0] == null) {
            refactoring[0] = RefactoringFactory.getInstance(file.getProject()).createRename(namespaceDefinition, newPackageName);
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

  @Nullable
  @Override
  public List<UsageInfo> findUsages(PsiFile psiFile, PsiDirectory newParent, boolean searchInComments, boolean searchInNonJavaFiles) {
    return null;
  }

  @Override
  public void retargetUsages(List<UsageInfo> usageInfos, Map<PsiElement, PsiElement> oldToNewMap) {
  }
}
