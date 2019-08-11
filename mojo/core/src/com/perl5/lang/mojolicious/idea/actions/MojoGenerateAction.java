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

package com.perl5.lang.mojolicious.idea.actions;

import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.mojolicious.MojoIcons;
import com.perl5.lang.mojolicious.MojoUtil;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class MojoGenerateAction extends MojoScriptAction {
  private static final String GENERATE_COMMAND = "generate";

  public MojoGenerateAction(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text) {
    super(text);
  }

  public MojoGenerateAction(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text, @Nullable Icon icon) {
    super(text, icon);
  }

  @Override
  protected boolean isEnabled(@NotNull AnActionEvent event) {
    if (!super.isEnabled(event)) {
      return false;
    }
    return getTargetDirectory(event) != null;
  }

  @Override
  public final void actionPerformed(@NotNull AnActionEvent e) {
    VirtualFile mojoScript = MojoUtil.getMojoScript(e);
    if (mojoScript == null) {
      return;
    }

    String entityName = Messages.showInputDialog(
      e.getProject(), getPromptMessage(), getPromptTitle(), getPromptIcon(), getDefaultName(), getNameValidator());
    if (StringUtil.isEmpty(entityName)) {
      return;
    }

    PerlRunUtil.runInConsole(createCommandLine(getEventProject(e), getTargetDirectory(e), entityName, mojoScript, null));
  }

  @NotNull
  public PerlCommandLine createCommandLine(@Nullable Project project,
                                           @NotNull VirtualFile targetDirectory,
                                           @NotNull String entityName,
                                           @NotNull VirtualFile mojoScript,
                                           @Nullable Runnable postRefreshRunnable) {
    return new PerlCommandLine(mojoScript.getPath(), GENERATE_COMMAND, getGenerateCommand(), entityName)
      .withProject(project)
      .withConsoleIcon(MojoIcons.MOJO_LOGO)
      .withWorkDirectory(targetDirectory.getPath())
      .withProcessListener(new ProcessAdapter() {
        @Override
        public void processTerminated(@NotNull ProcessEvent event) {
          targetDirectory.refresh(true, false, postRefreshRunnable);
        }
      });
  }

  @NotNull
  protected abstract String getDefaultName();

  @Nullable
  protected abstract InputValidator getNameValidator();

  /**
   * @return icon for name prompt dialog
   */
  @NotNull
  protected abstract Icon getPromptIcon();

  /**
   * @return title for name prompt dialog
   */
  @NotNull
  protected abstract String getPromptTitle();

  /**
   * @return message for name prompt dialog
   */
  @NotNull
  protected abstract String getPromptMessage();

  /**
   * @return a command that should be used to generate entity
   */
  @NotNull
  protected abstract String getGenerateCommand();

  protected VirtualFile getTargetDirectory(@NotNull AnActionEvent event) {
    Project project = event.getProject();
    VirtualFile virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
    return virtualFile != null && project != null &&
           virtualFile.isDirectory() &&
           ProjectFileIndex.getInstance(project).isInContent(virtualFile) ? virtualFile : null;
  }
}
