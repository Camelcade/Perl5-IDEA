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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.RootsChangeRescanningInfo;
import com.intellij.openapi.roots.ex.ProjectRootManagerEx;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.EmptyRunnable;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiManager;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.perl.idea.PerlPathMacros;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


@State(
  name = "TemplateToolkitSettings",
  storages = @Storage(PerlPathMacros.PERL5_PROJECT_SHARED_SETTINGS_FILE)

)

public class TemplateToolkitSettings implements PersistentStateComponent<TemplateToolkitSettings> {
  public static final String DEFAULT_START_TAG = "[%";
  public static final String DEFAULT_END_TAG = "%]";
  public static final String DEFAULT_OUTLINE_TAG = "%%";

  public final List<String> substitutedExtensions = new ArrayList<>();
  public String START_TAG = DEFAULT_START_TAG;
  public String END_TAG = DEFAULT_END_TAG;
  public String OUTLINE_TAG = DEFAULT_OUTLINE_TAG;
  public boolean ENABLE_ANYCASE = false;

  @Transient
  private transient AtomicNotNullLazyValue<List<FileNameMatcher>> myLazyMatchers;
  @Transient
  private transient AtomicNotNullLazyValue<Collection<PsiFileSystemItem>> myLazyPsiDirsRoots;
  @Transient
  private final transient @Nullable Project myProject;

  public TemplateToolkitSettings() {
    myProject = null;
  }

  public TemplateToolkitSettings(@NotNull Project project) {
    myProject = project;
    createLazyObjects();
  }

  public @NotNull Project getProject() {
    return Objects.requireNonNull(myProject);
  }

  public void settingsUpdated() {
    createLazyObjects();
    ApplicationManager.getApplication().invokeLater(() -> WriteAction.run(
      () -> ProjectRootManagerEx.getInstanceEx(getProject()).makeRootsChange(
        EmptyRunnable.getInstance(), RootsChangeRescanningInfo.TOTAL_RESCAN)));
  }

  private void createLazyObjects() {
    myLazyMatchers = AtomicNotNullLazyValue.createValue(() -> {
      List<FileNameMatcher> result = new ArrayList<>();
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

      PsiManager psiManager = PsiManager.getInstance(getProject());
      for (VirtualFile virtualFile : getTemplateRoots()) {
        PsiDirectory directory = psiManager.findDirectory(virtualFile);
        if (directory != null) {
          result.add(directory);
        }
      }
      return result;
    });
  }

  @Override
  public @Nullable TemplateToolkitSettings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull TemplateToolkitSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  public @NotNull List<VirtualFile> getTemplateRoots() {
    return PerlProjectManager.getInstance(getProject()).getModulesRootsOfType(TemplateToolkitSourceRootType.INSTANCE);
  }

  public @NotNull Collection<PsiFileSystemItem> getTemplatePsiRoots() {
    return myLazyPsiDirsRoots.getValue();
  }

  /**
   * Checks if virtualFile is under configured root
   *
   * @return true or false
   */
  public boolean isVirtualFileUnderRoot(@NotNull VirtualFile file) {
    for (VirtualFile root : getTemplateRoots()) {
      if (VfsUtilCore.isAncestor(root, file, true)) {
        return true;
      }
    }
    return false;
  }

  public boolean isVirtualFileNameMatches(@NotNull VirtualFile file) {
    for (FileNameMatcher matcher : myLazyMatchers.getValue()) {
      if (matcher.acceptsCharSequence(file.getName())) {
        return true;
      }
    }
    return false;
  }

  public static @NotNull TemplateToolkitSettings getInstance(@NotNull Project project) {
    return project.getService(TemplateToolkitSettings.class);
  }
}
