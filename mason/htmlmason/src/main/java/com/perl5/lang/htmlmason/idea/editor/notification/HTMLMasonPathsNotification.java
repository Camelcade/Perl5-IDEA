/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason.idea.editor.notification;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotificationProvider;
import com.perl5.lang.htmlmason.HtmlMasonBundle;
import com.perl5.lang.htmlmason.filetypes.HTMLMasonFileType;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.function.Function;


public class HTMLMasonPathsNotification implements EditorNotificationProvider, DumbAware {
  private final Project myProject;

  public HTMLMasonPathsNotification(Project myProject) {
    this.myProject = myProject;
  }

  @Override
  public @Nullable Function<? super @NotNull FileEditor, ? extends @Nullable JComponent> collectNotificationData(@NotNull Project project,
                                                                                                                 @NotNull VirtualFile file) {
    return fileEditor -> createNotificationPanel(file);
  }

  private @Nullable EditorNotificationPanel createNotificationPanel(@NotNull VirtualFile file) {
    if (file.getFileType() != HTMLMasonFileType.INSTANCE) {
      return null;
    }
    String message = null;

    if (HTMLMasonSettings.getInstance(myProject).getComponentsRoots().isEmpty()) {
      message = HtmlMasonBundle.message("link.label.html.mason.components.roots.are.not.configured");
    }
    else {
      PsiFile psiFile = PsiManager.getInstance(myProject).findFile(file);
      if (psiFile instanceof HTMLMasonFileImpl && ((HTMLMasonFileImpl)psiFile).getComponentRoot() == null) {
        message = HtmlMasonBundle.message("link.label.component.not.under.one.configured.roots");
      }
    }

    if (message == null) {
      return null;
    }
    EditorNotificationPanel panel = new EditorNotificationPanel();
    panel.setText(message);
    panel.createActionLabel(HtmlMasonBundle.message("link.label.configure"), () -> Perl5SettingsConfigurable.open(myProject));
    return panel;
  }
}