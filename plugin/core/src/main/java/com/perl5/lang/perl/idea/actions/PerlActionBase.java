/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public abstract class PerlActionBase extends AnAction implements PerlAction {
  public PerlActionBase() {
  }

  public PerlActionBase(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text,
                        @Nls(capitalization = Nls.Capitalization.Sentence) @Nullable String description,
                        @Nullable Icon icon) {
    super(text, description, icon);
  }

  public PerlActionBase(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text,
                        @Nullable Icon icon) {
    this(text, null, icon);
  }

  public PerlActionBase(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text) {
    this(text, null, null);
  }

  protected boolean isEnabled(@NotNull AnActionEvent event) {
    return PerlProjectManager.isPerlEnabled(event.getDataContext());
  }

  @Override
  public @NotNull ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.BGT;
  }

  @Override
  public void update(@NotNull AnActionEvent event) {
    final boolean enabled = isEnabled(event);
    var eventPresentation = event.getPresentation();
    eventPresentation.setEnabled(enabled);
    if (alwaysHideDisabled() || ActionPlaces.isPopupPlace(event.getPlace())) {
      eventPresentation.setVisible(enabled);
    }
    else {
      eventPresentation.setVisible(true);
    }
  }

  protected boolean alwaysHideDisabled() {
    return false;
  }

  protected static @Nullable PsiFile getPsiFile(@NotNull AnActionEvent event) {
    final DataContext context = event.getDataContext();
    final Project project = CommonDataKeys.PROJECT.getData(context);
    if (project == null) {
      return null;
    }

    final Editor editor = CommonDataKeys.EDITOR.getData(context);
    if (editor != null) {
      return PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
    }
    else {
      VirtualFile virtualFile = getVirtualFile(event);
      if (virtualFile != null) {
        return PsiManager.getInstance(project).findFile(virtualFile);
      }
    }
    return null;
  }

  protected static @Nullable VirtualFile getVirtualFile(@NotNull AnActionEvent event) {
    return CommonDataKeys.VIRTUAL_FILE.getData(event.getDataContext());
  }
}
