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

package com.perl5.lang.perl.idea.actions;

import com.intellij.ide.projectView.actions.MarkSourceRootAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.impl.DirectoryIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.modules.JpsPerlLibrarySourceRootType;
import com.perl5.lang.perl.idea.modules.PerlModuleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 29.08.2015.
 */
public class PerlMarkLibrarySourceRootAction extends MarkSourceRootAction {
  public PerlMarkLibrarySourceRootAction() {
    super(JpsPerlLibrarySourceRootType.INSTANCE);
  }

  @Override
  protected boolean isEnabled(@NotNull RootsSelection selection, @NotNull Module module) {
    return super.isEnabled(selection, module) && ModuleType.get(module) == PerlModuleType.getInstance();
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    super.actionPerformed(e);

    VirtualFile[] files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
    final Module module = getModule(e, files);
    if (module == null) {
      return;
    }

    final ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      @Override
      public void run() {
        modifiableModel.commit();
        module.getProject().save();
      }
    });
  }

  // following methods copied from parent
  @Nullable
  private static Module getModule(@NotNull AnActionEvent e, @Nullable VirtualFile[] files) {
    if (files == null) {
      return null;
    }
    Module module = e.getData(LangDataKeys.MODULE);
    if (module == null) {
      module = findParentModule(e.getProject(), files);
    }
    return module;
  }

  @Nullable
  private static Module findParentModule(@Nullable Project project, @NotNull VirtualFile[] files) {
    if (project == null) {
      return null;
    }
    Module result = null;
    DirectoryIndex index = DirectoryIndex.getInstance(project);
    for (VirtualFile file : files) {
      Module module = index.getInfoForFile(file).getModule();
      if (module == null) {
        return null;
      }
      if (result == null) {
        result = module;
      }
      else if (!result.equals(module)) {
        return null;
      }
    }
    return result;
  }
}
