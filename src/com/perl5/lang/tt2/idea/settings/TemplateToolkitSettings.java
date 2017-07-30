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

package com.perl5.lang.tt2.idea.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.impl.FileTypeManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiManager;
import com.intellij.util.FileContentUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.perl.idea.PerlPathMacros;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 05.06.2016.
 */
@State(
  name = "TemplateToolkitSettings",
  storages = {
    @Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
    @Storage(id = "dir", file = PerlPathMacros.PERL5_PROJECT_SHARED_SETTINGS_FILE, scheme = StorageScheme.DIRECTORY_BASED)
  }
)

public class TemplateToolkitSettings implements PersistentStateComponent<TemplateToolkitSettings> {
  public static final String DEFAULT_START_TAG = "[%";
  public static final String DEFAULT_END_TAG = "%]";
  public static final String DEFAULT_OUTLINE_TAG = "%%";

  public List<String> substitutedExtensions = new ArrayList<>();
  public List<String> TEMPLATE_DIRS = new ArrayList<>();
  public String START_TAG = DEFAULT_START_TAG;
  public String END_TAG = DEFAULT_END_TAG;
  public String OUTLINE_TAG = DEFAULT_OUTLINE_TAG;
  public boolean ENABLE_ANYCASE = false;

  @Transient
  private transient AtomicNotNullLazyValue<List<FileNameMatcher>> myLazyMatchers;
  @Transient
  private transient AtomicNotNullLazyValue<List<VirtualFile>> myLazyVirtualFilesRoots;
  @Transient
  private transient AtomicNotNullLazyValue<Collection<PsiFileSystemItem>> myLazyPsiDirsRoots;
  @Transient
  private transient Project myProject;

  public TemplateToolkitSettings() {
    createLazyObjects();
  }

  public void settingsUpdated() {
    createLazyObjects();
    final FileTypeManager fileTypeManager = FileTypeManager.getInstance();
    if (fileTypeManager instanceof FileTypeManagerImpl) {
      ApplicationManager.getApplication().runWriteAction(
        () -> {
          ((FileTypeManagerImpl)fileTypeManager).fireBeforeFileTypesChanged();
          ((FileTypeManagerImpl)fileTypeManager).fireFileTypesChanged();
        }

      );
    }
    FileContentUtil.reparseOpenedFiles();
  }

  protected void setProject(Project project) {
    myProject = project;
  }

  private void createLazyObjects() {
    myLazyMatchers = new AtomicNotNullLazyValue<List<FileNameMatcher>>() {
      @SuppressWarnings("Duplicates")
      @NotNull
      @Override
      protected List<FileNameMatcher> compute() {
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
      }
    };

    myLazyVirtualFilesRoots = new AtomicNotNullLazyValue<List<VirtualFile>>() {
      @NotNull
      @Override
      protected List<VirtualFile> compute() {
        List<VirtualFile> result = new ArrayList<>();

        for (String relativeRoot : TEMPLATE_DIRS) {
          VirtualFile rootFile = VfsUtil.findRelativeFile(relativeRoot, myProject.getBaseDir());
          if (rootFile != null && rootFile.exists()) {
            result.add(rootFile);
          }
        }

        return result;
      }
    };

    myLazyPsiDirsRoots = new AtomicNotNullLazyValue<Collection<PsiFileSystemItem>>() {
      @NotNull
      @Override
      protected Collection<PsiFileSystemItem> compute() {
        Collection<PsiFileSystemItem> result = new ArrayDeque<>();

        PsiManager psiManager = PsiManager.getInstance(myProject);
        for (VirtualFile virtualFile : getTemplateRoots()) {
          PsiDirectory directory = psiManager.findDirectory(virtualFile);
          if (directory != null) {
            result.add(directory);
          }
        }
        return result;
      }
    };
  }

  @Nullable
  @Override
  public TemplateToolkitSettings getState() {
    return this;
  }

  @Override
  public void loadState(TemplateToolkitSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  @NotNull
  public List<FileNameMatcher> getMatchers() {
    return myLazyMatchers.getValue();
  }

  @NotNull
  public List<VirtualFile> getTemplateRoots() {
    return myLazyVirtualFilesRoots.getValue();
  }

  @NotNull
  public Collection<PsiFileSystemItem> getTemplatePsiRoots() {
    return myLazyPsiDirsRoots.getValue();
  }

  /**
   * Checks if virtualFile is under configured root
   *
   * @return true or false
   */
  public boolean isVirtualFileUnderRoot(@NotNull VirtualFile file) {
    for (VirtualFile root : getTemplateRoots()) {
      if (VfsUtil.isAncestor(root, file, true)) {
        return true;
      }
    }
    return false;
  }

  public boolean isVirtualFileNameMatches(@NotNull VirtualFile file) {
    for (FileNameMatcher matcher : getMatchers()) {
      if (matcher.accept(file.getName())) {
        return true;
      }
    }
    return false;
  }

  @NotNull
  public static TemplateToolkitSettings getInstance(@NotNull Project project) {
    TemplateToolkitSettings persisted = ServiceManager.getService(project, TemplateToolkitSettings.class);

    if (persisted == null) {
      persisted = new TemplateToolkitSettings();
    }
    persisted.setProject(project);

    return persisted;
  }
}
