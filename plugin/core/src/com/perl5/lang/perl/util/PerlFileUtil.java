/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlFileUtil {
  private static final Logger LOG = Logger.getInstance(PerlFileUtil.class);
  /**
   * Linuxies path if local system is windows. Changing {@code c:\some\path} to {@code /c/some/path}
   */
  public static @NotNull String linuxisePath(@NotNull String localPath) {
    if (!SystemInfo.isWindows || localPath.isEmpty()) {
      return localPath;
    }
    char driveLetter = localPath.charAt(0);
    return "/" + driveLetter + "/" +
           (localPath.length() > 3 ? FileUtil.toSystemIndependentName(localPath.substring(3)) : "");
  }

  /**
   * Reverse operation for {@link #linuxisePath(String)}
   */
  public static @NotNull String unLinuxisePath(@NotNull String linuxisedPath) {
    if (!SystemInfo.isWindows || linuxisedPath.length() < 2) {
      return linuxisedPath;
    }
    String normalizedPath = FileUtil.toSystemIndependentName(linuxisedPath);
    if (normalizedPath.charAt(0) != '/') {
      throw new RuntimeException("Badly formatted path: " + linuxisedPath);
    }
    char driveLetter = normalizedPath.charAt(1);
    if (!Character.isLetter(driveLetter)) {
      throw new RuntimeException("Unexpected drive letter: " + linuxisedPath);
    }
    return FileUtil.toSystemDependentName(driveLetter + ":" + (linuxisedPath.length() > 2 ? linuxisedPath.substring(2) : ""));
  }

  /**
   * @return a content root for the {@code virtualFile} in the context of {@code project} or null if file is not in content
   */
  public static @Nullable VirtualFile getContentRoot(@NotNull Project project, @Nullable VirtualFile virtualFile) {
    return virtualFile == null ? null : ProjectFileIndex.getInstance(project).getContentRootForFile(virtualFile);
  }

  /**
   * @return a path to the {@code virtualFile} relative to the nearest content root in the context of {@code project}
   */
  public static @Nullable String getPathRelativeToContentRoot(@Nullable VirtualFile virtualFile,
                                                              @NotNull Project project) {
    VirtualFile root = getContentRoot(project, virtualFile);
    return root == null ? null : VfsUtilCore.getRelativePath(virtualFile, root);
  }
}
