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

package com.perl5.lang.htmlmason;

import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.FakeVirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.indexing.IndexingDataKeys;
import com.perl5.lang.htmlmason.idea.configuration.AbstractMasonSettings;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class MasonCoreUtil {
  private MasonCoreUtil() {
  }

  public static @Nullable VirtualFile getComponentRoot(@NotNull AbstractMasonSettings masonSettings, @Nullable VirtualFile file) {
    if (file != null) {
      if (file instanceof FakeVirtualFile) {
        file = file.getParent();
      }

      for (VirtualFile componentRoot : PerlProjectManager.getInstance(masonSettings.getProject())
        .getModulesRootsOfType(masonSettings.getSourceRootType())) {
        if (VfsUtilCore.isAncestor(componentRoot, file, false)) {
          return componentRoot;
        }
      }
    }
    return null;
  }

  /**
   * Returns a real containing virtual file, not the Light one
   *
   * @return virtual file or null
   */
  public static @Nullable VirtualFile getContainingVirtualFile(PsiFile psiFile) {
    VirtualFile originalFile = psiFile.getViewProvider().getVirtualFile();

    if (originalFile instanceof LightVirtualFile lightVirtualFile) {
      if (psiFile.getUserData(IndexingDataKeys.VIRTUAL_FILE) != null) {
        originalFile = psiFile.getUserData(IndexingDataKeys.VIRTUAL_FILE);
      }
      else if (lightVirtualFile.getOriginalFile() != null) {
        originalFile = lightVirtualFile.getOriginalFile();
      }
    }
    return originalFile instanceof LightVirtualFile || originalFile == null || !originalFile.exists() ? null : originalFile;
  }
}
