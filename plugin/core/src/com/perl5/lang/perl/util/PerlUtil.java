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

package com.perl5.lang.perl.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.util.processors.PerlInternalIndexKeysProcessor;
import gnu.trove.THashSet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.Set;

/**
 * Misc helper methods
 */
public class PerlUtil implements PerlElementTypes {
  @Contract("null->null")
  @Nullable
  public static VirtualFile getFileClassRoot(@Nullable PsiFile psiFile) {
    return psiFile == null ? null : getFileClassRoot(psiFile.getProject(), PsiUtilCore.getVirtualFile(psiFile));
  }

  /**
   * Searches for innermost source root for a file
   *
   * @param project project to search in
   * @param file    containing file
   * @return innermost root
   */
  @Contract("_,null->null")
  @Nullable
  public static VirtualFile getFileClassRoot(@NotNull Project project, @Nullable VirtualFile file) {
    if (file == null) {
      return null;
    }
    VirtualFile result = null;
    for (VirtualFile classRoot : PerlProjectManager.getInstance(project).getAllLibraryRoots()) {
      if (VfsUtil.isAncestor(classRoot, file, false) && (result == null || VfsUtil.isAncestor(result, classRoot, true))) {
        result = classRoot;
      }
    }

    return result;
  }

  /**
   * Searches for innermost source root for a file by it's absolute path
   *
   * @param project  module to search in
   * @param filePath containing filename
   * @return innermost root
   */
  @Nullable
  public static VirtualFile getFileClassRoot(@NotNull Project project, @NotNull String filePath) {
    File file = new File(filePath);
    VirtualFile result = null;

    for (VirtualFile classRoot : PerlProjectManager.getInstance(project).getAllLibraryRoots()) {
      File sourceRootFile = new File(classRoot.getPath());
      if (VfsUtil.isAncestor(sourceRootFile, file, false) && (result == null || VfsUtil.isAncestor(result, classRoot, true))) {
        result = classRoot;
      }
    }

    return result;
  }

  @Deprecated // make reverse index and use it
  public static Collection<String> getIndexKeysWithoutInternals(@NotNull StubIndexKey<String, ?> key, @NotNull Project project) {
    final Set<String> result = new THashSet<>();

    // safe for getElements
    StubIndex.getInstance().processAllKeys(key, project, new
      PerlInternalIndexKeysProcessor() {
        @Override
        public boolean process(String name) {
          if (super.process(name)) {
            result.add(name);
          }
          return true;
        }
      });

    return result;
  }

  @NotNull
  public static String getParentsChain(@Nullable PsiElement element) {
    if (element == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder();
    while (true) {
      sb.append(element.getClass()).append(": ");
      if (element instanceof PsiFile || element.getParent() == null) {
        break;
      }
      element = element.getParent();
    }
    return sb.toString();
  }
}
