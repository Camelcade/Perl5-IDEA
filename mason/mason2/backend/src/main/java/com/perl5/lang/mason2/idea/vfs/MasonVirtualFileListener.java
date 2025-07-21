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

package com.perl5.lang.mason2.idea.vfs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.*;
import com.intellij.util.indexing.FileBasedIndex;
import com.perl5.lang.htmlmason.idea.configuration.AbstractMasonSettings;
import com.perl5.lang.mason2.filetypes.MasonPurePerlComponentFileType;
import com.perl5.lang.mason2.idea.configuration.MasonSettings;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public final class MasonVirtualFileListener implements VirtualFileListener {
  public static final Key<Boolean> FORCE_REINDEX = new Key<>("Force re-indexing");

  private final @NotNull Project myProject;

  public MasonVirtualFileListener(@NotNull Project project) {
    myProject = project;
  }

  /**
   * Fired when a virtual file is renamed from within IDEA, or its writable status is changed.
   * For files renamed externally, {@link #fileCreated} and {@link #fileDeleted} events will be fired.
   *
   * @param event the event object containing information about the change.
   */
  @Override
  public void propertyChanged(@NotNull VirtualFilePropertyEvent event) {
    if (!event.getPropertyName().equals(VirtualFile.PROP_NAME)) {
      return;
    }

    processFileChange(event.getFile());
  }

  private void processFileChange(@NotNull VirtualFile changedFile) {
    List<VirtualFile> componentsRoots = getComponentsRoots();
    if (componentsRoots.isEmpty()) {
      return;
    }


    Set<VirtualFile> rootsSet = new HashSet<>(componentsRoots);
    if (changedFile.isDirectory()) {
      if (changedFile.getUserData(FORCE_REINDEX) != null ||
          VfsUtilCore.isUnder(changedFile, rootsSet) ||        // moved to component root
          containsAtLeastOneFile(changedFile, componentsRoots)
      ) {
        FileBasedIndex.getInstance().requestReindex(changedFile);
      }
    }
    else if (changedFile.getFileType() instanceof MasonPurePerlComponentFileType)    // Mason file has been moved
    {
      if (changedFile.getUserData(FORCE_REINDEX) != null ||
          VfsUtilCore.isUnder(changedFile, rootsSet)
      ) {
        FileBasedIndex.getInstance().requestReindex(changedFile);
      }
    }
  }

  private @NotNull MasonSettings getSettings() {
    return MasonSettings.getInstance(getProject());
  }

  /**
   * Fired when a virtual file is moved from within IDEA.
   *
   * @param event the event object containing information about the change.
   */
  @Override
  public void fileMoved(@NotNull VirtualFileMoveEvent event) {
    processFileChange(event.getFile());
  }

  /**
   * Fired before the change of a name or writable status of a file is processed.
   *
   * @param event the event object containing information about the change.
   */
  @Override
  public void beforePropertyChange(@NotNull VirtualFilePropertyEvent event) {
    if (!event.getPropertyName().equals(VirtualFile.PROP_NAME)) {
      return;
    }

    List<VirtualFile> componentsRoots = getComponentsRoots();
    if (componentsRoots.isEmpty()) {
      return;
    }

    VirtualFile renamedFile = event.getFile();

    if (renamedFile.isDirectory()) {
      if (containsAtLeastOneFile(renamedFile, componentsRoots)) // contains component root
      {
        renamedFile.putUserData(FORCE_REINDEX, true);
      }
    }
  }

  private @NotNull List<VirtualFile> getComponentsRoots() {
    AbstractMasonSettings settings = getSettings();
    return PerlProjectManager.getInstance(settings.getProject()).getModulesRootsOfType(settings.getSourceRootType());
  }

  /**
   * Fired before the movement of a file is processed.
   *
   * @param event the event object containing information about the change.
   */
  @Override
  public void beforeFileMovement(@NotNull VirtualFileMoveEvent event) {
    List<VirtualFile> componentsRoots = getComponentsRoots();
    if (componentsRoots.isEmpty()) {
      return;
    }

    VirtualFile movedFile = event.getFile();

    Set<VirtualFile> rootsSet = new HashSet<>(componentsRoots);
    if (movedFile.isDirectory()) {
      if (VfsUtilCore.isUnder(movedFile, rootsSet) ||    // moved from component root
          containsAtLeastOneFile(movedFile, componentsRoots) // contains component root
      ) {
        movedFile.putUserData(FORCE_REINDEX, true);
      }
    }
    else if (movedFile.getFileType() instanceof MasonPurePerlComponentFileType)    // Mason file has been moved
    {
      if (VfsUtilCore.isUnder(movedFile, rootsSet)) {
        movedFile.putUserData(FORCE_REINDEX, true);
      }
    }
  }

  private @NotNull Project getProject() {
    return myProject;
  }

  private static boolean containsAtLeastOneFile(VirtualFile root, List<? extends VirtualFile> files) {
    for (VirtualFile file : files) {
      if (VfsUtilCore.isAncestor(root, file, false)) {
        return true;
      }
    }
    return false;
  }
}
