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

package com.perl5.lang.perl.idea.refactoring.move;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.move.moveFilesOrDirectories.MoveFilesOrDirectoriesProcessor;
import com.intellij.refactoring.rename.RenamePsiFileProcessor;
import com.perl5.lang.perl.util.PerlUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Directory renaming being done using custom way
 */
public class PerlRenameDirectoryProcessor extends RenamePsiFileProcessor {
  @Override
  public boolean canProcessElement(@NotNull PsiElement element) {
    return element instanceof PsiDirectory && canProcessDir((PsiDirectory)element);
  }

  protected boolean canProcessDir(PsiDirectory dir) {
    return PerlUtil.getFileClassRoot(dir.getProject(), dir.getVirtualFile()) != null;
  }

  @Override
  public void prepareRenaming(@NotNull final PsiElement element,
                              @NotNull final String newName,
                              @NotNull Map<PsiElement, String> allRenames) {
    allRenames.clear();
    ApplicationManager.getApplication().runWriteAction(() -> renamePsiElement(element, newName));
  }

  protected void renamePsiElement(PsiElement element, String newName) {
    assert element instanceof PsiDirectory;

    PsiDirectory currentRoot = ((PsiDirectory)element).getParentDirectory();

    if (currentRoot != null) {
      recursiveDirectoryMove(element.getProject(), (PsiDirectory)element, getOrCreateSubDir(currentRoot, newName));
    }
    //		element.delete();
  }

  public static void recursiveDirectoryMove(Project project, final PsiDirectory sourceRoot, PsiDirectory dstRoot) {
    for (PsiDirectory subDir : sourceRoot.getSubdirectories()) {
      recursiveDirectoryMove(project, subDir, getOrCreateSubDir(dstRoot, subDir.getName()));
    }

    PsiFile[] files = sourceRoot.getFiles();
    if (files.length > 0) {
      new MoveFilesOrDirectoriesProcessor(project, files, dstRoot, false, false, () ->
      {
        PsiDirectory currentDir = sourceRoot;

        while (currentDir != null && currentDir.getFiles().length == 0 && currentDir.getSubdirectories().length == 0) {
          PsiDirectory parentDir = currentDir.getParentDirectory();
          currentDir.delete();
          currentDir = parentDir;
        }
      }, null).run();
    }
    else {
      sourceRoot.delete();
    }
  }

  public static PsiDirectory getOrCreateSubDir(PsiDirectory root, String dirName) {
    PsiDirectory result = root.findSubdirectory(dirName);

    if (result == null) {
      result = root.createSubdirectory(dirName);
    }

    return result;
  }
}
