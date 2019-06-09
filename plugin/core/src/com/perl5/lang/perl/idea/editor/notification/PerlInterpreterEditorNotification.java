/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurable;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PerlInterpreterEditorNotification extends EditorNotifications.Provider<EditorNotificationPanel> implements DumbAware {
  private static final Key<EditorNotificationPanel> KEY = Key.create("perl.interpreter.not.choosen");
  private final Project myProject;

  public PerlInterpreterEditorNotification(Project myProject) {
    this.myProject = myProject;
  }

  @NotNull
  @Override
  public Key<EditorNotificationPanel> getKey() {
    return KEY;
  }

  @Nullable
  @Override
  public EditorNotificationPanel createNotificationPanel(@NotNull VirtualFile virtualFile, @NotNull FileEditor fileEditor) {
    if (virtualFile.getFileType() instanceof PerlFileType && !(virtualFile instanceof LightVirtualFile)) {
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
    return null;
  }
}
