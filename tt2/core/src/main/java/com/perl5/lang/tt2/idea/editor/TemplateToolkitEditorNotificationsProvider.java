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

package com.perl5.lang.tt2.idea.editor;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotificationProvider;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurable;
import com.perl5.lang.tt2.TemplateToolkitBundle;
import com.perl5.lang.tt2.filetypes.TemplateToolkitFileType;
import com.perl5.lang.tt2.idea.settings.TemplateToolkitSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.function.Function;


public class TemplateToolkitEditorNotificationsProvider implements EditorNotificationProvider, DumbAware {
  private final Project myProject;

  public TemplateToolkitEditorNotificationsProvider(Project project) {
    myProject = project;
  }

  @Override
  public @Nullable Function<? super @NotNull FileEditor, ? extends @Nullable JComponent> collectNotificationData(@NotNull Project project,
                                                                                                                 @NotNull VirtualFile file) {
    if (file.getFileType() != TemplateToolkitFileType.INSTANCE) {
      return null;
    }
    TemplateToolkitSettings settings = TemplateToolkitSettings.getInstance(myProject);
    if (settings.isVirtualFileUnderRoot(file)) {
      return null;
    }
    return fileEditor -> createNotificationPanel();
  }

  private @NotNull EditorNotificationPanel createNotificationPanel() {
    EditorNotificationPanel panel = new EditorNotificationPanel();
    panel.setText(TemplateToolkitBundle.message("tt2.error.file.not.in.root"));
    panel.createActionLabel("Configure", () -> Perl5SettingsConfigurable.open(myProject));
    return panel;
  }
}
