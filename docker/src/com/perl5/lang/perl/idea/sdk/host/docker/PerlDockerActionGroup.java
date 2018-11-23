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

package com.perl5.lang.perl.idea.sdk.host.docker;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlDockerActionGroup extends ActionGroup {

  @NotNull
  @Override
  public AnAction[] getChildren(@Nullable AnActionEvent e) {
    Project project = getEventProject(e);
    if (project == null) {
      return EMPTY_ARRAY;
    }

    PerlDockerHandler dockerHandler = PerlDockerHandler.getInstance();
    if (!dockerHandler.isApplicable()) {
      return EMPTY_ARRAY;
    }

    return PerlVersionManagerHandler.stream()
      .filter(it -> it.isApplicable(dockerHandler.getOsHandler()))
      .map(versionManagerHandler -> new DumbAwareAction(versionManagerHandler.getMenuItemTitle()) {
        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
          new Task.Modal(project, PerlBundle.message("perl.create.interpreter.progress"), false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
              versionManagerHandler.createSdkInteractively(
                myProject, dockerHandler, null, project);
            }
          }.queue();
        }

        @Override
        public void update(@NotNull AnActionEvent e) {
          e.getPresentation().setEnabledAndVisible(true);
        }
      }).toArray(AnAction[]::new);
  }
}
