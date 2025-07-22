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

package com.perl5.lang.tt2.idea.settings;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiManager;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service(Service.Level.PROJECT)
public final class TemplateToolkitSettingsBackendHelper implements Disposable, TemplateToolkitSettings.SettingsListener {
  private AtomicNotNullLazyValue<List<FileNameMatcher>> myLazyMatchers;
  private AtomicNotNullLazyValue<Collection<PsiFileSystemItem>> myLazyPsiDirsRoots;
  private final @NotNull Project myProject;

  public TemplateToolkitSettingsBackendHelper(@NotNull Project project) {
    myProject = project;
    createLazyObjects();
    myProject.getMessageBus().connect(this).subscribe(TemplateToolkitSettings.TT2_SETTINGS_TOPIC, this);
  }

  @Override
  public void dispose() {
  }

  @Override
  public void settingsUpdated() {
    createLazyObjects();
  }

  private void createLazyObjects() {
    myLazyMatchers = AtomicNotNullLazyValue.createValue(() -> {
      List<FileNameMatcher> result = new ArrayList<>();
      var substitutedExtensions = TemplateToolkitSettings.getInstance(myProject).getSubstitutedExtensions();
      FileTypeManager fileTypeManager = FileTypeManager.getInstance();
      for (FileType fileType : fileTypeManager.getRegisteredFileTypes()) {
        if (fileType instanceof LanguageFileType) {
          for (FileNameMatcher matcher : fileTypeManager.getAssociations(fileType)) {
            if (substitutedExtensions.contains(matcher.getPresentableString())) {
              result.add(matcher);
            }
          }
        }
      }
      return result;
    });

    myLazyPsiDirsRoots = AtomicNotNullLazyValue.createValue(() -> {
      Collection<PsiFileSystemItem> result = new ArrayDeque<>();

      PsiManager psiManager = PsiManager.getInstance(myProject);
      for (VirtualFile virtualFile : PerlProjectManager.getInstance(myProject)
        .getModulesRootsOfType(TemplateToolkitSourceRootType.INSTANCE)) {
        PsiDirectory directory = psiManager.findDirectory(virtualFile);
        if (directory != null) {
          result.add(directory);
        }
      }
      return result;
    });
  }

  public @NotNull Collection<PsiFileSystemItem> getTemplatePsiRoots() {
    return myLazyPsiDirsRoots.getValue();
  }

  public @NotNull List<VirtualFile> getTemplateRoots() {
    return PerlProjectManager.getInstance(myProject).getModulesRootsOfType(TemplateToolkitSourceRootType.INSTANCE);
  }

  public boolean isVirtualFileNameMatches(@NotNull VirtualFile file) {
    for (FileNameMatcher matcher : myLazyMatchers.getValue()) {
      if (matcher.acceptsCharSequence(file.getName())) {
        return true;
      }
    }
    return false;
  }


  /**
   * Checks if virtualFile is under configured root
   *
   * @return true or false
   */
  public boolean isVirtualFileUnderRoot(@NotNull VirtualFile file) {
    for (VirtualFile root : PerlProjectManager.getInstance(myProject).getModulesRootsOfType(TemplateToolkitSourceRootType.INSTANCE)) {
      if (VfsUtilCore.isAncestor(root, file, true)) {
        return true;
      }
    }
    return false;
  }

  public static @NotNull TemplateToolkitSettingsBackendHelper getInstance(@NotNull Project project) {
    return project.getService(TemplateToolkitSettingsBackendHelper.class);
  }
}
