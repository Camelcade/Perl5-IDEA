/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.project;

import com.intellij.ide.projectView.actions.MarkExcludeRootAction;
import com.intellij.ide.projectView.actions.MarkTestSourceRootAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.actions.PerlMarkLibrarySourceRootAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public final class PerlDirectoryInfoCollector {
  private final Set<VirtualFile> myLibRoots = new LinkedHashSet<>();
  private final Set<VirtualFile> myExternalLibRoots = new LinkedHashSet<>();
  private final Set<VirtualFile> myExcludedRoots = new LinkedHashSet<>();
  private final Set<VirtualFile> myTestRoots = new LinkedHashSet<>();
  private final ProjectFileIndex myProjectFileIndex;

  PerlDirectoryInfoCollector(@NotNull Project project) {
    myProjectFileIndex = ProjectFileIndex.getInstance(project);
  }

  public void addLibRoot(@Nullable VirtualFile virtualFile) {
    if (virtualFile != null) {
      myLibRoots.add(virtualFile);
    }
  }

  public void addExternalLibRoot(@Nullable VirtualFile virtualFile) {
    if (virtualFile != null) {
      myExternalLibRoots.add(virtualFile);
    }
  }

  public void addExcludedRoot(@Nullable VirtualFile virtualFile) {
    if (virtualFile != null && !myProjectFileIndex.isExcluded(virtualFile)) {
      myExcludedRoots.add(virtualFile);
    }
  }

  public void addTestRoot(@Nullable VirtualFile virtualFile) {
    if (virtualFile != null && !myProjectFileIndex.isInTestSourceContent(virtualFile)) {
      myTestRoots.add(virtualFile);
    }
  }

  void commitExcluded(@NotNull Module module) {
    if (module.isDisposed() || myExcludedRoots.isEmpty()) {
      return;
    }
    new MarkExcludeRootAction() {
      public void actionPerformed() {
        modifyRoots(module, myExcludedRoots.toArray(VirtualFile.EMPTY_ARRAY));
        myExcludedRoots.clear();
      }
    }.actionPerformed();
  }

  void commitTestRoots(@NotNull Module module) {
    if (module.isDisposed() || myTestRoots.isEmpty()) {
      return;
    }
    new MarkTestSourceRootAction() {
      public void actionPerformed() {
        modifyRoots(module, myTestRoots.toArray(VirtualFile.EMPTY_ARRAY));
        myTestRoots.clear();
      }
    }.actionPerformed();
  }

  void commitExternalLibRoots(@NotNull Project project){
    if( project.isDisposed() || myExternalLibRoots.isEmpty()){
      return;
    }
    var projectRootManager = PerlProjectManager.getInstance(project);
    projectRootManager.addExternalLibraries(new ArrayList<>(myExternalLibRoots));
    myExternalLibRoots.clear();
  }

  void commitLibRoots(@NotNull Module module){
    if(module.isDisposed() || myLibRoots.isEmpty()){
      return;
    }
    new PerlMarkLibrarySourceRootAction().markRoot(module, new ArrayList<>(myLibRoots));
    myLibRoots.clear();
  }
}
