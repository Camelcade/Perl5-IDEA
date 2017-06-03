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

package com.perl5.lang.perl.idea.editor.notification;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.ui.configuration.ProjectSettingsService;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;
import com.intellij.util.PlatformUtils;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import com.perl5.lang.perl.idea.configuration.settings.PerlSettingsConfigurable;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 30.04.2016.
 */
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

      EditorNotificationPanel panel = null;

      if (PlatformUtils.isIntelliJ()) {
        if (VfsUtil.isAncestor(myProject.getBaseDir(), virtualFile, true)) {
          Module fileModule = ModuleUtilCore.findModuleForFile(virtualFile, myProject);
          Sdk fileSdk;
          if (fileModule == null) {
            fileSdk = ProjectRootManager.getInstance(myProject).getProjectSdk();
          }
          else {
            fileSdk = ModuleRootManager.getInstance(fileModule).getSdk();
          }
          if (fileSdk == null || fileSdk.getSdkType() != PerlSdkType.getInstance()) {
            panel = new EditorNotificationPanel();
            panel.setText(PerlBundle.message("perl.notification.sdk.not.configured"));
            panel.createActionLabel(PerlBundle.message("perl.notification.configure"), new Runnable() {
              @Override
              public void run() {
                ProjectSettingsService.getInstance(myProject).openProjectSettings();
              }
            });
          }
        }
      }
      else {
        if (StringUtil.isEmpty(perlLocalSettings.PERL_PATH)) {
          panel = new EditorNotificationPanel();
          panel.setText(PerlBundle.message("perl.notification.interperter.not.configured"));
          panel.createActionLabel(PerlBundle.message("perl.notification.configure"), new Runnable() {
            @Override
            public void run() {
              ShowSettingsUtil.getInstance().editConfigurable(myProject, new PerlSettingsConfigurable(myProject));
            }
          });
        }
      }

      if (panel != null) {
        panel.createActionLabel(PerlBundle.message("perl.notification.disable.notification"), new Runnable() {
          @Override
          public void run() {
            perlLocalSettings.DISABLE_NO_INTERPRETER_WARNING = true;
            EditorNotifications.getInstance(myProject).updateAllNotifications();
          }
        });
      }

      return panel;
    }
    return null;
  }
}
