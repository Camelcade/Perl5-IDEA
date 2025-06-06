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

package com.perl5.lang.perl.idea.configuration.settings.sdk;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.projectRoots.impl.PerlModuleExtension;
import com.intellij.openapi.roots.ui.configuration.ModuleSourceRootEditHandler;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class PerlToggleSourceRootAction extends ToggleAction {
  private final @NotNull PerlContentEntriesTreeEditor myEditor;
  private final ModuleSourceRootEditHandler<?> myHandler;

  public PerlToggleSourceRootAction(@NotNull PerlContentEntriesTreeEditor editor,
                                    @NotNull ModuleSourceRootEditHandler<?> handler) {
    super(handler.getMarkRootButtonText(),
          ProjectBundle.message("module.toggle.sources.action.description",
                                handler.getFullRootTypeName().toLowerCase(Locale.getDefault())),
          handler.getRootIcon()
    );
    myHandler = handler;
    myEditor = editor;
    getTemplatePresentation().putClientProperty(ActionUtil.SHOW_TEXT_IN_TOOLBAR, true);
  }

  @Override
  public @NotNull ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.BGT;
  }

  @Override
  public boolean isSelected(@NotNull AnActionEvent e) {
    VirtualFile[] files = myEditor.getSelectedFiles();
    if (files.length == 0) {
      return false;
    }
    return myHandler.getRootType().equals(myEditor.getPerlModuleExtensionModifiableModel().getRootType(files[0]));
  }

  @Override
  public void setSelected(@NotNull AnActionEvent e, boolean state) {
    PerlModuleExtension modifiableModel = myEditor.getPerlModuleExtensionModifiableModel();
    for (VirtualFile virtualFile : myEditor.getSelectedFiles()) {
      if (state) {
        modifiableModel.setRoot(virtualFile, myHandler.getRootType());
      }
      else {
        modifiableModel.removeRoot(virtualFile);
      }
    }
    myEditor.repaint();
  }
}
