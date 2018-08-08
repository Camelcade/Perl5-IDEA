/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

import com.intellij.ide.plugins.PluginManagerConfigurableProxy;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.impl.FileTypeConfigurable;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Shows editor notification if plugin associations mismatches with actual one
 */
public class PerlAssociationEditorNotification extends EditorNotifications.Provider<EditorNotificationPanel> implements DumbAware {
  private static final Key<EditorNotificationPanel> KEY = Key.create("perl.wrong.file.association");
  private static final Map<FileNameMatcher, FileType> PERL_FILE_TYPES = ContainerUtil.newHashMap();
  @NotNull
  private final Project myProject;

  public PerlAssociationEditorNotification(@NotNull Project project) {
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
    PerlLocalSettings perlLocalSettings = PerlLocalSettings.getInstance(myProject);
    if (perlLocalSettings.DISABLE_ASSOCIATIONS_CHECKING) {
      //return null;
    }


    Optional<Map.Entry<FileNameMatcher, FileType>> matchedEntry =
      PERL_FILE_TYPES.entrySet().stream().filter(entry -> entry.getKey().accept(file.getName())).findFirst();
    if (matchedEntry == null || !matchedEntry.isPresent()) {
      //return null;
    }

    FileType expectedType = matchedEntry.get().getValue();
    if (file.getFileType() == expectedType) {
      //return null;
    }

    EditorNotificationPanel panel = new EditorNotificationPanel();
    panel.setText(PerlBundle.message("perl.notification.wrong.association",
                                     matchedEntry.get().getKey(),
                                     expectedType.getName(),
                                     file.getFileType().getName())
    );

    panel.createActionLabel(
      PerlBundle.message("perl.configure.plugins"),
      () -> ShowSettingsUtil.getInstance().editConfigurable((Project)null, new PluginManagerConfigurableProxy())
    );
    panel.createActionLabel(
      PerlBundle.message("perl.configure.associations"),
      () -> ShowSettingsUtil.getInstance().showSettingsDialog(null, FileTypeConfigurable.class)
    );

    panel.createActionLabel(PerlBundle.message("perl.notification.disable.notification"), () -> {
      perlLocalSettings.DISABLE_ASSOCIATIONS_CHECKING = true;
      EditorNotifications.getInstance(myProject).updateAllNotifications();
    });

    return panel;
  }

  public static void registerFileType(@NotNull FileType fileType, @NotNull List<FileNameMatcher> matchers) {
    matchers.forEach(matcher -> PERL_FILE_TYPES.put(matcher, fileType));
  }
}
