/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.projectRoots.impl.PerlModuleExtension;
import com.intellij.openapi.roots.ui.configuration.ModuleSourceRootEditHandler;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.modules.PerlSourceRootType;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public abstract class PerlMarkSourceRootAction extends PerlSourceRootAction {
  private final @NotNull PerlSourceRootType myType;

  public PerlMarkSourceRootAction(@NotNull PerlSourceRootType type) {
    myType = type;
    ModuleSourceRootEditHandler<?> editHandler = type.getEditHandler();
    Presentation presentation = getTemplatePresentation();
    presentation.setText(editHandler.getMarkRootButtonText());
    presentation.setDescription(ProjectBundle.message("module.toggle.sources.action.description",
                                                      editHandler.getFullRootTypeName().toLowerCase(Locale.getDefault())));
    presentation.setIcon(editHandler.getRootIcon());
  }

  public @NotNull PerlSourceRootType getType() {
    return myType;
  }


  @Override
  protected boolean isEnabled(@NotNull List<VirtualFile> files, @NotNull Module module) {
    if (files.isEmpty() || !PerlProjectManager.isPerlEnabled(module)) {
      return false;
    }
    return !myType.equals(PerlModuleExtension.getInstance(module).getRootType(files.getFirst()));
  }

  @Override
  protected final void modifyRoots(@NotNull AnActionEvent e, @NotNull Module module, VirtualFile @NotNull [] files) {
    markRoot(module, files);
  }

  public final void markRoot(@NotNull Project project, @Nullable VirtualFile virtualFile) {
    if (virtualFile == null || !virtualFile.isValid()) {
      return;
    }
    Module module = ModuleUtilCore.findModuleForFile(virtualFile, project);
    if (module != null) {
      markRoot(module, virtualFile);
    }
  }

  public final void markRoot(@NotNull Module module, @NotNull VirtualFile... files) {
    markRoot(module, Arrays.asList(files));
  }
  public final void markRoot(@NotNull Module module, @NotNull List<? extends VirtualFile> files) {
    PerlModuleExtension.modify(module, it -> {
      for (VirtualFile virtualFile : files) {
        it.setRoot(virtualFile, myType);
      }
    });
  }
}
