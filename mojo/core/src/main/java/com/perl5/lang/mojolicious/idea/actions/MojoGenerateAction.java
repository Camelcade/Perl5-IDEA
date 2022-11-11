/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.concurrency.Semaphore;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.mojolicious.MojoIcons;
import com.perl5.lang.mojolicious.MojoUtil;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public abstract class MojoGenerateAction extends MojoScriptAction {
  private static final Logger LOG = Logger.getInstance(MojoGenerateAction.class);
  private static final String GENERATE_COMMAND = "generate";
  private Semaphore myCallbackTestSemaphore = null;

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
    var project = getEventProject(e);
    var perlSdk = PerlProjectManager.getSdk(e);
    VirtualFile mojoScript = MojoUtil.getMojoScript(e);
    if (mojoScript == null) {
      LOG.warn("No mojo script; project: " + project + " sdk: " + perlSdk);
      return;
    }
    List<String> generationParameters = computeGenerationParameters(e);
    if (generationParameters == null) {
      return;
    }

    var localScriptPath = mojoScript.getPath();
    if (perlSdk == null) {
      LOG.warn("No perl sdk for " + project);
      return;
    }
    PerlHostData<?, ?> hostData = PerlHostData.notNullFrom(perlSdk);
    var remoteScriptPath = hostData.getRemotePath(localScriptPath);
    if (remoteScriptPath == null) {
      LOG.warn("No remote path for " + localScriptPath + " in " + perlSdk);
      return;
    }

    List<String> fullArguments = ContainerUtil.newArrayList(remoteScriptPath, GENERATE_COMMAND);
    fullArguments.addAll(generationParameters);

    VirtualFile targetDirectory = getTargetDirectory(e);
    var targetDirectoryPath = targetDirectory.getPath();
    var callbackTestSemaphore = myCallbackTestSemaphore;

    PerlRunUtil.runInConsole(
      new PerlCommandLine(fullArguments)
        .withProject(project)
        .withConsoleIcon(MojoIcons.MOJO_LOGO)
        .withWorkDirectory(targetDirectoryPath)
        .withProcessListener(new ProcessAdapter() {
          @Override
          public void processTerminated(@NotNull ProcessEvent event) {
            targetDirectory.refresh(true, false);
            try {
              hostData.fixPermissionsRecursively(targetDirectoryPath, project);
            }
            catch (ExecutionException ex) {
              LOG.warn("Error fixing permissions for " + targetDirectoryPath + ": " + ex.getMessage());
            }
            finally {
              if (callbackTestSemaphore != null) {
                callbackTestSemaphore.up();
              }
            }
          }
        })
    );
  }

  /**
   * Should return generation parameters or null if generation should not be performed
   */
  protected @Nullable List<String> computeGenerationParameters(@NotNull AnActionEvent e) {
    String entityName = Messages.showInputDialog(
      e.getProject(), getPromptMessage(), getPromptTitle(), getPromptIcon(), getDefaultName(), getNameValidator());

    return StringUtil.isEmpty(entityName) ? null : Arrays.asList(getGenerateCommand(), entityName);
  }

  protected abstract @NotNull String getDefaultName();

  protected abstract @Nullable InputValidator getNameValidator();

  /**
   * @return icon for name prompt dialog
   */
  protected abstract @NotNull Icon getPromptIcon();

  /**
   * @return title for name prompt dialog
   */
  protected abstract @NotNull String getPromptTitle();

  /**
   * @return message for name prompt dialog
   */
  protected abstract @NotNull String getPromptMessage();

  /**
   * @return a command that should be used to generate entity
   */
  protected abstract @NotNull String getGenerateCommand();

  protected VirtualFile getTargetDirectory(@NotNull AnActionEvent event) {
    Project project = event.getProject();
    VirtualFile virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE);
    return virtualFile != null && project != null &&
           virtualFile.isDirectory() &&
           ProjectFileIndex.getInstance(project).isInContent(virtualFile) ? virtualFile : null;
  }

  @TestOnly
  public Semaphore runWithTestSemaphore(@NotNull Runnable runnable) {
    LOG.assertTrue(myCallbackTestSemaphore == null);
    var semaphore = new Semaphore(1);
    myCallbackTestSemaphore = semaphore;
    try {
      runnable.run();
    }
    finally {
      myCallbackTestSemaphore = null;
    }
    return semaphore;
  }
}
