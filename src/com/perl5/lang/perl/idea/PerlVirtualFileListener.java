/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileMoveEvent;
import com.intellij.openapi.vfs.VirtualFilePropertyEvent;
import com.perl5.lang.perl.idea.refactoring.rename.RenameRefactoringQueue;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 29.05.2015.
 */
public class PerlVirtualFileListener extends VirtualFileAdapter {
  Project myProject;
  ProjectFileIndex myProjectFileIndex;

  public PerlVirtualFileListener(Project project) {
    myProject = project;
    myProjectFileIndex = ProjectRootManager.getInstance(project).getFileIndex();
  }

  @Override
  public void propertyChanged(@NotNull VirtualFilePropertyEvent event) {
    VirtualFile virtualFile = event.getFile();
    if (myProjectFileIndex.isInSource(virtualFile) && event.getNewValue() != null && event.getOldValue() != null) {
      if ("name".equals(event.getPropertyName()) && virtualFile.isDirectory()) {
        // package path change
        String oldPath = virtualFile.getPath().replaceFirst(event.getNewValue().toString() + "$", event.getOldValue().toString());
        RenameRefactoringQueue queue = new RenameRefactoringQueue(myProject);
        PerlPackageUtil.collectNestedPackageDefinitions(queue, virtualFile, oldPath);
        queue.run();
      }
    }
  }


  @Override
  public void fileMoved(@NotNull VirtualFileMoveEvent event) {
    if (!(event.getRequestor() instanceof PerlNamespaceDefinition)) {
      if (myProjectFileIndex.isInSource(event.getNewParent())) {
        VirtualFile movedFile = event.getFile();
        if (movedFile.isDirectory()) {
          String oldPath = event.getOldParent().getPath() + '/' + movedFile.getName();
          // one of the dirs been moved to other one
          RenameRefactoringQueue queue = new RenameRefactoringQueue(myProject);
          PerlPackageUtil.collectNestedPackageDefinitions(queue, movedFile, oldPath);
          queue.run();
        }
      }
    }
  }

  @Override
  public void beforePropertyChange(@NotNull VirtualFilePropertyEvent event) {
    VirtualFile virtualFile = event.getFile();
    if (myProjectFileIndex.isInSource(virtualFile)) {
      if ("name".equals(event.getPropertyName()) && virtualFile.isDirectory()) {
        // package path change, preprocessing
        String newPath = virtualFile.getPath().replaceFirst(event.getOldValue().toString() + "$", event.getNewValue().toString());
        PerlPackageUtil.adjustNestedFiles(myProject, virtualFile, newPath);
      }
    }
  }

  @Override
  public void beforeFileMovement(@NotNull VirtualFileMoveEvent event) {
    VirtualFile virtualFile = event.getFile();
    if (myProjectFileIndex.isInSource(virtualFile)) {
      if (virtualFile.isDirectory()) {
        // package path change, preprocessing
        String newPath = event.getNewParent().getPath() + '/' + virtualFile.getName();
        PerlPackageUtil.adjustNestedFiles(myProject, virtualFile, newPath);
      }
    }
  }
}
