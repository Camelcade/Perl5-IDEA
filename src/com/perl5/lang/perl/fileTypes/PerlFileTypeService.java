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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.AtomicClearableLazyValue;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.FakeVirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class PerlFileTypeService {
  private final AtomicClearableLazyValue<List<RootDescriptor>> myDescriptorsProvider =
    new AtomicClearableLazyValue<List<RootDescriptor>>() {
      @NotNull
      @Override
      protected List<RootDescriptor> compute() {
        ApplicationManager.getApplication().assertReadAccessAllowed();

        List<RootDescriptor> result = new ArrayList<>();
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
          if (!project.isDisposed()) {
            for (PerlFileTypeProvider fileTypeProvider : PerlFileTypeProvider.EP_NAME.getExtensions()) {
              fileTypeProvider.addDescriptors(project, result::add);
            }
          }
        }

        return result;
      }
    };

  public static void clear() {
    getInstance().myDescriptorsProvider.drop();
  }

  @Nullable
  public static FileType getFileType(@Nullable VirtualFile virtualFile) {
    for (RootDescriptor descriptor : getInstance().myDescriptorsProvider.getValue()) {
      if (!descriptor.isValid()) {
        clear();
        return getFileType(virtualFile);
      }
      FileType result = descriptor.compute(virtualFile);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private static PerlFileTypeService getInstance() {
    return ServiceManager.getService(PerlFileTypeService.class);
  }

  /**
   * Describes a single root with custom extensions. Descriptor consists of two parts:
   * root - a directory which contains files with custom extensions
   * function - a computation, that should return a file type or null
   */
  public static class RootDescriptor {
    @NotNull
    private final VirtualFile myRoot;
    @NotNull
    private final Function<VirtualFile, FileType> myFunction;

    private RootDescriptor(@NotNull VirtualFile root,
                           @NotNull Function<VirtualFile, FileType> typeFunction) {
      if (!root.isValid()) {
        throw new IllegalArgumentException("Attempt to create a descriptor for invalid file");
      }
      if (!root.isDirectory()) {
        throw new IllegalArgumentException("Only directories are accepted");
      }
      myRoot = root;
      myFunction = typeFunction;
    }

    @Nullable
    private FileType compute(@Nullable VirtualFile virtualFile) {
      VirtualFile fileForAncestryCheck = virtualFile instanceof FakeVirtualFile ? virtualFile.getParent() : virtualFile;
      return virtualFile == null || fileForAncestryCheck == null ||
             !fileForAncestryCheck.isInLocalFileSystem() ||
             !VfsUtil.isAncestor(myRoot, fileForAncestryCheck, false) ? null : myFunction.apply(virtualFile);
    }

    public boolean isValid() {
      return myRoot.isValid() && myRoot.isDirectory();
    }

    public static RootDescriptor create(@NotNull VirtualFile root,
                                        @NotNull Function<VirtualFile, FileType> typeFunction) {
      return new RootDescriptor(root, typeFunction);
    }
  }
}
