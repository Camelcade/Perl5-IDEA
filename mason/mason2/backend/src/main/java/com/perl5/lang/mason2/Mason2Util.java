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

package com.perl5.lang.mason2;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.perl5.lang.htmlmason.MasonCoreUtil;
import com.perl5.lang.mason2.idea.configuration.MasonSettings;
import com.perl5.lang.mason2.psi.MasonNamespaceDefinition;
import com.perl5.lang.mason2.psi.stubs.MasonNamespaceDefitnitionsStubIndex;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.util.PerlFileUtil;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public final class Mason2Util {
  private Mason2Util() {
  }

  public static @NotNull String getClassnameFromPath(@NotNull String path) {
    return "/MC0::" +
           path.replaceAll("[^\\p{L}\\d_\\/]", "_").replaceAll("" + VfsUtil.VFS_SEPARATOR_CHAR, PerlPackageUtilCore.NAMESPACE_SEPARATOR);
  }

  public static @Nullable String getVirtualFileClassName(@NotNull Project project, @Nullable VirtualFile componentFile) {
    if (componentFile != null && componentFile.isValid()) {
      VirtualFile componentRoot = getComponentRoot(project, componentFile);
      if (componentRoot != null) {
        //noinspection ConstantConditions
        return getClassnameFromPath(VfsUtilCore.getRelativePath(componentFile, componentRoot));
      }
    }

    return null;
  }

  public static @Nullable VirtualFile getComponentRoot(@NotNull Project project, @Nullable VirtualFile file) {
    return MasonCoreUtil.getComponentRoot(MasonSettings.getInstance(project), file);
  }

  public static List<PerlNamespaceDefinitionElement> getMasonNamespacesByAbsolutePath(@NotNull Project project,
                                                                                      @NotNull String absolutePath) {
    return new ArrayList<>(
      StubIndex.getElements(
        MasonNamespaceDefitnitionsStubIndex.KEY,
        absolutePath,
        project,
        GlobalSearchScope.projectScope(project),
        MasonNamespaceDefinition.class
      )
    );
  }

  public static @NotNull List<PerlNamespaceDefinitionElement> collectComponentNamespacesByPaths(@NotNull Project project,
                                                                                                @NotNull List<String> componentPaths,
                                                                                                @NotNull VirtualFile anchorDir) {
    List<PerlNamespaceDefinitionElement> result = new ArrayList<>();
    MasonSettings masonSettings = MasonSettings.getInstance(project);

    for (String componentPath : componentPaths) {
      VirtualFile componentFile = null;
      if (componentPath
        .startsWith("" + VfsUtil.VFS_SEPARATOR_CHAR)) // abs path relative to mason roots, see the Mason::Interp::_determine_parent_compc
      {
        for (VirtualFile componentRoot : masonSettings.getComponentsRoots()) {
          componentFile = componentRoot.findFileByRelativePath(componentPath.substring(1));
          if (componentFile != null) {
            break;
          }
        }
      }
      else // relative path
      {
        componentFile = anchorDir.findFileByRelativePath(componentPath);
      }

      if (componentFile != null) {
        String absolutePath = PerlFileUtil.getPathRelativeToContentRoot(componentFile, project);
        if (absolutePath != null) {
          result.addAll(Mason2Util.getMasonNamespacesByAbsolutePath(project, absolutePath));
        }
      }
    }

    return result;
  }
}
