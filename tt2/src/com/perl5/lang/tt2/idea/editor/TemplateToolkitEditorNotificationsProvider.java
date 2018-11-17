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

package com.perl5.lang.tt2.idea.editor;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurable;
import com.perl5.lang.tt2.TemplateToolkitBundle;
import com.perl5.lang.tt2.filetypes.TemplateToolkitFileType;
import com.perl5.lang.tt2.idea.settings.TemplateToolkitSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 14.06.2016.
 */
public class TemplateToolkitEditorNotificationsProvider extends EditorNotifications.Provider<EditorNotificationPanel> implements DumbAware {
  private static final Key<EditorNotificationPanel> KEY = Key.create("perl.tt2.roots.not.set.panel");
  private final Project myProject;

  public TemplateToolkitEditorNotificationsProvider(Project project) {
    myProject = project;
  }

  @NotNull
  @Override
  public Key<EditorNotificationPanel> getKey() {
    return KEY;
  }

  @Nullable
  @Override
  public EditorNotificationPanel createNotificationPanel(@NotNull VirtualFile file, @NotNull FileEditor fileEditor) {
    if (file.getFileType() == TemplateToolkitFileType.INSTANCE) {
      TemplateToolkitSettings settings = TemplateToolkitSettings.getInstance(myProject);
      if (!settings.isVirtualFileUnderRoot(file)) {
        EditorNotificationPanel panel = new EditorNotificationPanel();
        panel.setText(TemplateToolkitBundle.message("tt2.error.file.not.in.root"));
        panel.createActionLabel("Configure", () -> Perl5SettingsConfigurable.open(myProject));
        return panel;
      }
    }

    return null;
  }
}
