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

package com.perl5.lang.mason2.idea.editor.notification;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotificationProvider;
import com.perl5.lang.mason2.Mason2Bundle;
import com.perl5.lang.mason2.filetypes.MasonPurePerlComponentFileType;
import com.perl5.lang.mason2.idea.configuration.MasonSettings;
import com.perl5.lang.mason2.psi.impl.MasonFileImpl;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.function.Function;


public class MasonPathsNotification implements EditorNotificationProvider, DumbAware {
  private final Project myProject;

  public MasonPathsNotification(Project myProject) {
    this.myProject = myProject;
  }

  @Override
  public @Nullable Function<? super @NotNull FileEditor, ? extends @Nullable JComponent> collectNotificationData(@NotNull Project project,
                                                                                                                 @NotNull VirtualFile file) {
    if (!(file.getFileType() instanceof MasonPurePerlComponentFileType)) {
      return null;
    }
    if (MasonSettings.getInstance(myProject).getComponentsRoots().isEmpty()) {
      return fileEditor -> createNotificationPanel(Mason2Bundle.message("label.mason2.components.roots.are.not.configured"));
    }
    else {
      PsiFile psiFile = PsiManager.getInstance(myProject).findFile(file);
      if (psiFile instanceof MasonFileImpl masonFile && masonFile.getComponentRoot() == null) {
        return fileEditor -> createNotificationPanel(Mason2Bundle.message("label.component.not.under.one.configured.roots"));
      }
    }
    return null;
  }

  private @NotNull EditorNotificationPanel createNotificationPanel(@NotNull @NlsContexts.Label String message) {
    EditorNotificationPanel panel = new EditorNotificationPanel();
    panel.setText(message);
    panel.createActionLabel(Mason2Bundle.message("link.label.configure"), () -> Perl5SettingsConfigurable.open(myProject));
    return panel;
  }
}
