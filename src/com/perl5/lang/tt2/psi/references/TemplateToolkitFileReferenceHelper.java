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

package com.perl5.lang.tt2.psi.references;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.ManagingFS;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceHelper;
import com.perl5.lang.tt2.filetypes.TemplateToolkitFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by hurricup on 15.06.2016.
 */
public class TemplateToolkitFileReferenceHelper extends FileReferenceHelper {

  @Nullable
  @Override
  public PsiFileSystemItem findRoot(Project project, @NotNull VirtualFile file) {
    VirtualFile root = file;
    VirtualFile parent;
    while ((parent = root.getParent()) != null) {
      root = parent;
    }
    return getPsiFileSystemItem(project, root);
  }

  @NotNull
  @Override
  public Collection<PsiFileSystemItem> getContexts(Project project, @NotNull VirtualFile file) {
    PsiFileSystemItem item = getPsiFileSystemItem(project, file);
    return item == null ? Collections.emptyList() : Collections.singleton(item);
  }

  @NotNull
  @Override
  public Collection<PsiFileSystemItem> getRoots(@NotNull Module module) {
    Collection<PsiFileSystemItem> result = new ArrayList<>();
    PsiManager psiManager = PsiManager.getInstance(module.getProject());
    for (VirtualFile root : ManagingFS.getInstance().getLocalRoots()) {
      PsiDirectory directory = psiManager.findDirectory(root);
      if (directory != null) {
        result.add(directory);
      }
    }
    return result;
  }

  @Override
  public boolean isMine(Project project, @NotNull VirtualFile file) {
    return file.getFileType() == TemplateToolkitFileType.INSTANCE;
  }
}
