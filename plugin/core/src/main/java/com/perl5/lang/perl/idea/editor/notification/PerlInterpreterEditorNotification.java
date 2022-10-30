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

package com.perl5.lang.perl.idea.editor.notification;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotificationProvider;
import com.intellij.ui.EditorNotifications;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurable;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.function.Function;


public class PerlInterpreterEditorNotification implements EditorNotificationProvider, DumbAware {
  private final Project myProject;

  public PerlInterpreterEditorNotification(Project myProject) {
    this.myProject = myProject;
  }

  @Override
  public @Nullable Function<? super @NotNull FileEditor, ? extends @Nullable JComponent> collectNotificationData(@NotNull Project project,
                                                                                                                 @NotNull VirtualFile file) {
    return fileEditor -> createNotificationPanel(file);
  }


  private @Nullable EditorNotificationPanel createNotificationPanel(@NotNull VirtualFile virtualFile) {
    if (!(virtualFile.getFileType() instanceof PerlFileType) || virtualFile instanceof LightVirtualFile) {
      return null;
    }
    final PerlLocalSettings perlLocalSettings = PerlLocalSettings.getInstance(myProject);
    if (perlLocalSettings.DISABLE_NO_INTERPRETER_WARNING) {
      return null;
    }

    if (PerlProjectManager.getSdk(myProject, virtualFile) != null) {
      return null;
    }

    EditorNotificationPanel panel = new EditorNotificationPanel();
    panel.setText(PerlBundle.message("perl.notification.sdk.not.configured"));
    panel.createActionLabel(PerlBundle.message("perl.notification.configure"),
                            () -> Perl5SettingsConfigurable.open(myProject));
    panel.createActionLabel(PerlBundle.message("perl.notification.disable.notification"), () -> {
      perlLocalSettings.DISABLE_NO_INTERPRETER_WARNING = true;
      EditorNotifications.getInstance(myProject).updateAllNotifications();
    });

    return panel;
  }
}
