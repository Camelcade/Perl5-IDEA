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

package com.perl5.lang.perl.idea.sdk.versionManager.perlbrew;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * Api to the perlbrew cli
 */
class PerlBrewAdapter {
  static final String PERLBREW_EXEC = "exec";
  static final String PERLBREW_WITH = "--with";

  private static final Logger LOG = Logger.getInstance(PerlBrewAdapter.class);
  @NotNull
  private final String myPerlBrewPath;
  @NotNull
  private final PerlHostData myHostData;

  PerlBrewAdapter(@NotNull String perlBrewPath, @NotNull PerlHostData hostData) {
    myPerlBrewPath = perlBrewPath;
    myHostData = hostData;
  }

  @Nullable
  List<String> execWith(@NotNull String distributionId, @NotNull String... commands) {
    List<String> commandsList = ContainerUtil.newArrayList(PERLBREW_EXEC, PERLBREW_WITH, distributionId);
    commandsList.addAll(Arrays.asList(commands));
    return getOutput(commandsList);
  }

  /**
   * @return list of {@code perlbrew list} items trimmed or null if error happened
   */
  @Nullable
  List<String> getList() {
    List<String> output = getOutput("list");
    if (output == null) {
      return null;
    }
    return ContainerUtil.map(output, String::trim);
  }

  /**
   * @return output of perlbew command or null if error happened
   */
  @Nullable
  private List<String> getOutput(@NotNull List<String> parameters) {
    GeneralCommandLine commandLine = new GeneralCommandLine(myPerlBrewPath).withParameters(parameters);
    try {
      ProcessOutput processOutput = PerlHostData.execAndGetOutput(myHostData, commandLine);
      if (processOutput.getExitCode() == 0) {
        return processOutput.getStdoutLines(true);
      }
      LOG.warn("Process exited with non-zero code. Command line: " + commandLine +
               "; exit code: " + processOutput.getExitCode() +
               "; stdout: " + processOutput.getStdout() +
               "; stderr: " + processOutput.getStderr());
      notifyUser(PerlBundle.message("perl.vm.perlbrew.notification.message.exitcode"));
    }
    catch (ExecutionException e) {
      LOG.warn("Error running " + commandLine, e);
      notifyUser(PerlBundle.message("perl.vm.perlbrew.notification.message.exception", e.getMessage()));
    }
    return null;
  }

  @Nullable
  private List<String> getOutput(@NotNull String... parameters) {
    return getOutput(Arrays.asList(parameters));
  }

  private void notifyUser(@NotNull String message) {
    Notifications.Bus.notify(new Notification(
      PerlBundle.message("perl.vm.perlbrew.notification.group"),
      PerlBundle.message("perl.vm.perlbrew.notification.title"),
      message,
      NotificationType.ERROR
    ));
  }
}
