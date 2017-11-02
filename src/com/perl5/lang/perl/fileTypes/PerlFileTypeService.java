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

package com.perl5.lang.perl.fileTypes;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.FakeVirtualFile;
import com.intellij.util.indexing.LightDirectoryIndex;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;


public class PerlFileTypeService implements Disposable {
  private final LightDirectoryIndex<Function<VirtualFile, FileType>> myDirectoryIndex = new LightDirectoryIndex<>(
    this,
    virtualFile -> null,
    directoryIndex -> ReadAction.run(() -> {
      for (Project project : ProjectManager.getInstance().getOpenProjects()) {
        if (!project.isDisposed()) {
          for (PerlFileTypeProvider fileTypeProvider : PerlFileTypeProvider.EP_NAME.getExtensions()) {
            fileTypeProvider.addRoots(project, (root, function) -> {
              if (!root.isValid()) {
                throw new IllegalArgumentException("Attempt to create a descriptor for invalid file");
              }
              if (!root.isDirectory()) {
                throw new IllegalArgumentException("Only directories are accepted");
              }
              directoryIndex.putInfo(root, function);
            });
          }
        }
      }
    })
  );

  @Override
  public void dispose() {

  }

  private static PerlFileTypeService getInstance() {
    return ServiceManager.getService(PerlFileTypeService.class);
  }

  @Nullable
  public static FileType getFileType(@Nullable VirtualFile virtualFile) {
    VirtualFile fileForAncestryCheck = virtualFile instanceof FakeVirtualFile ? virtualFile.getParent() : virtualFile;
    return getInstance().myDirectoryIndex.getInfoForFile(fileForAncestryCheck).apply(virtualFile);
  }
}
