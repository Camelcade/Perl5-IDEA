/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk.versionManager;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessListener;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Base class for CLI adapter of version manager
 */
public abstract class PerlVersionManagerAdapter {
  private static final Logger LOG = Logger.getInstance(PerlVersionManagerAdapter.class);

  private final @NotNull String myVersionManagerPath;
  private final @NotNull PerlHostData<?, ?> myHostData;

  public PerlVersionManagerAdapter(@NotNull String versionManagerPath, @NotNull PerlHostData<?, ?> hostData) {
    myVersionManagerPath = versionManagerPath;
    myHostData = hostData;
  }

  public @NotNull String getVersionManagerPath() {
    return myVersionManagerPath;
  }

  public @NotNull PerlHostData<?, ?> getHostData() {
    return myHostData;
  }

  protected abstract @Nullable Icon getIcon();

  protected void runInstallInConsole(@NotNull PerlCommandLine commandLine, @NotNull String entityName) {
    PerlRunUtil.runInConsole(
      commandLine
        .withHostData(myHostData)
        .withConsoleTitle(PerlBundle.message("perl.vm.installing", entityName))
        .withConsoleIcon(getIcon())
        .withVersionManagerData(PerlVersionManagerData.getDefault())
    );
  }

  /**
   * Used for installing packages using version manager, e.g. {@code install-cpanm}
   */
  public void runInstallInConsole(@NotNull Project project, @NotNull String packageName, @NotNull String command) {
    runInstallInConsole(createInstallCommandLine(project, command), packageName);
  }

  protected @NotNull PerlCommandLine createInstallCommandLine(@NotNull Project project,
                                                              @NotNull String command) {
    return new PerlCommandLine(getVersionManagerPath(), command)
      .withProject(project)
      .withHostData(myHostData)
      .withProcessListener(new ProcessAdapter() {
        @Override
        public void processTerminated(@NotNull ProcessEvent event) {
          PerlRunUtil.refreshSdkDirs(project);
        }
      });
  }

  /**
   * Executes commands in the context of {@code distributionId}
   */
  protected abstract @Nullable List<String> execWith(@NotNull String distributionId, @NotNull String... commands);

  /**
   * Installs perl to the version manager
   *
   * @param project         context project
   * @param distributionId  perl to install
   * @param params          additional params
   * @param processListener process listener
   */
  public abstract void installPerl(@Nullable Project project,
                                   @NotNull String distributionId,
                                   @NotNull List<String> params,
                                   @NotNull ProcessListener processListener);

  /**
   * @return list of installed perl distributions
   * @apiNote returned items format is version-manager specific, for perlbrew it may be a {@code version@library}
   */
  protected abstract @Nullable List<String> getInstalledDistributionsList();

  /**
   * @return list of perls available for installation
   */
  protected abstract @Nullable List<String> getInstallableDistributionsList();

  protected @Nullable List<String> getOutput(@NotNull List<String> parameters) {
    return getOutput(it -> it.withParameters(parameters));
  }

  protected @Nullable List<String> getOutput(@NotNull Function<? super PerlCommandLine, ? extends PerlCommandLine> commandLineProcessor) {
    return getOutput(commandLineProcessor.apply(new PerlCommandLine(getVersionManagerPath())));
  }

  /**
   * @return output of perlbrew command or null if error happened
   */
  protected @Nullable List<String> getOutput(@NotNull PerlCommandLine commandLine) {
    ProcessOutput processOutput = getProcessOutput(commandLine.withHostData(getHostData()));
    if (processOutput == null) {
      return null;
    }
    if (processOutput.getExitCode() == 0) {
      return processOutput.getStdoutLines(true);
    }
    LOG.warn("Process exited with non-zero code. Command line: " + commandLine +
             "; exit code: " + processOutput.getExitCode() +
             "; stdout: " + processOutput.getStdout() +
             "; stderr: " + processOutput.getStderr());
    notifyUser(PerlBundle.message("perl.vm.adapter.notification.message.exitcode"));
    return null;
  }

  protected @Nullable ProcessOutput getProcessOutput(@NotNull PerlCommandLine commandLine) {
    try {
      return PerlHostData.execAndGetOutput(commandLine.withHostData(getHostData()));
    }
    catch (ExecutionException e) {
      LOG.warn("Error running " + commandLine, e);
      notifyUser(PerlBundle.message("perl.vm.adapter.notification.message.exception", e.getMessage()));
    }
    return null;
  }

  protected @Nullable List<String> getOutput(@NotNull String... parameters) {
    return getOutput(Arrays.asList(parameters));
  }

  protected @NotNull String getNotificationGroup() {
    return PerlBundle.message("perl.vm.notification.group");
  }

  protected abstract @NotNull String getErrorNotificationTitle();

  private void notifyUser(@NotNull String message) {
    ApplicationManager.getApplication().invokeLater(() -> {
      if (ModalityState.current() != ModalityState.nonModal()) {
        Messages.showErrorDialog(message, getErrorNotificationTitle());
      }
      else {
        Notifications.Bus.notify(new Notification(
          getNotificationGroup(),
          getErrorNotificationTitle(),
          message,
          NotificationType.ERROR
        ));
      }
    });
  }

  @TestOnly
  public final @Nullable List<String> getInstalledDistributionsListInTests() {
    return getInstalledDistributionsList();
  }

  @TestOnly
  public final @Nullable List<String> getInstallableDistributionsListInTests() {
    return getInstallableDistributionsList();
  }
}
