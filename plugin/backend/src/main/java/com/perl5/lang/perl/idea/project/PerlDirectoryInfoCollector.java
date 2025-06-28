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
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.concurrency.annotations.RequiresEdt;
import com.intellij.util.concurrency.annotations.RequiresWriteLock;
import com.perl5.lang.perl.idea.actions.PerlMarkLibrarySourceRootAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class PerlDirectoryInfoCollector {
  private final Set<VirtualFile> myExternalLibRoots = new LinkedHashSet<>();
  private final Map<Module, ModuleRootInfo> myModulesInfo = new HashMap<>();
  private final ProjectFileIndex myProjectFileIndex;
  private final Project myProject;

  PerlDirectoryInfoCollector(@NotNull Project project) {
    myProject = project;
    myProjectFileIndex = ProjectFileIndex.getInstance(project);
  }

  public void addLibRoot(@NotNull Module module, @Nullable VirtualFile virtualFile) {
    getModuleRootInfo(module).addLibRoot(virtualFile);
  }

  private @NotNull ModuleRootInfo getModuleRootInfo(@NotNull Module module) {
    return myModulesInfo.computeIfAbsent(module, it -> new ModuleRootInfo());
  }

  public void addExternalLibRoot(@Nullable VirtualFile virtualFile) {
    if (virtualFile != null) {
      myExternalLibRoots.add(virtualFile);
    }
  }

  public void addExcludedRoot(@NotNull Module module, @Nullable VirtualFile virtualFile) {
    if (virtualFile != null && !myProjectFileIndex.isExcluded(virtualFile)) {
      getModuleRootInfo(module).addExcludedRoot(virtualFile);
    }
  }

  public void addTestRoot(@NotNull Module module, @Nullable VirtualFile virtualFile) {
    if (virtualFile != null && !myProjectFileIndex.isInTestSourceContent(virtualFile)) {
      getModuleRootInfo(module).addTestRoot(virtualFile);
    }
  }

  private void commitExcluded(@NotNull Module module ) {
    if (module.isDisposed()) {
      return;
    }
    var excludedRoots = getModuleRootInfo(module).getExcludedRoots();
    if( excludedRoots.isEmpty()){
      return;
    }

    new MarkExcludeRootAction() {
      public void actionPerformed() {
        modifyRoots(module, excludedRoots.toArray(VirtualFile.EMPTY_ARRAY));
        excludedRoots.clear();
      }
    }.actionPerformed();
  }

  private void commitTestRoots(@NotNull Module module) {
    if (module.isDisposed() ) {
      return;
    }

    var testRoots = getModuleRootInfo(module).getTestRoots();
    if( testRoots.isEmpty()){
      return;
    }

    new MarkTestSourceRootAction() {
      public void actionPerformed() {
        modifyRoots(module, testRoots.toArray(VirtualFile.EMPTY_ARRAY));
        testRoots.clear();
      }
    }.actionPerformed();
  }

  @RequiresWriteLock
  void commit(){
    if( myProject.isDisposed()){
      return;
    }
    for (Module module : myModulesInfo.keySet()) {
      if( !module.isDisposed()){
        commitLibRoots(module);
        commitTestRoots(module);
        commitExcluded(module);
      }
    }
    commitExternalLibRoots(myProject);
  }

  private void commitExternalLibRoots(@NotNull Project project){
    if( project.isDisposed() || myExternalLibRoots.isEmpty()){
      return;
    }
    var projectRootManager = PerlProjectManager.getInstance(project);
    projectRootManager.addExternalLibraries(new ArrayList<>(myExternalLibRoots));
    myExternalLibRoots.clear();
  }

  private void commitLibRoots(@NotNull Module module) {
    if (module.isDisposed()) {
      return;
    }

    var libRoots = getModuleRootInfo(module).getLibRoots();
    new PerlMarkLibrarySourceRootAction().markRoot(module, new ArrayList<>(libRoots));
    libRoots.clear();
  }
}
