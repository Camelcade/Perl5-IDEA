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

import com.intellij.execution.process.ProcessOutput;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * Api to the perlbrew cli
 */
class PerlBrewAdapter extends PerlVersionManagerAdapter {
  static final String PERLBREW_EXEC = "exec";
  static final String PERLBREW_WITH = "--with";
  static final String PERLBREW_QUIET = "-q";

  public PerlBrewAdapter(@NotNull String versionManagerPath, @NotNull PerlHostData hostData) {
    super(versionManagerPath, hostData);
  }

  /**
   * @see PerlBrewData#patchCommandLine(com.perl5.lang.perl.idea.execution.PerlCommandLine)
   */
  @Nullable
  protected List<String> execWith(@NotNull String distributionId, @NotNull String... commands) {
    List<String> commandsList = ContainerUtil.newArrayList(PERLBREW_EXEC, PERLBREW_QUIET, PERLBREW_WITH, distributionId);
    commandsList.addAll(Arrays.asList(commands));
    return getOutput(commandsList);
  }

  /**
   * Creates a library with {@code libararyName} for the perl with {@code perlVersion}. Optionally invokes the {@code successCallback} on success
   */
  @Nullable
  ProcessOutput createLibrary(@NotNull String perlVersion, @NotNull String libraryName) {
    return getProcessOutput(
      new PerlCommandLine(getVersionManagerPath(), "lib", "create", perlVersion + "@" + libraryName).withHostData(getHostData()));
  }

  /**
   * @return list of {@code perlbrew list} items trimmed or null if error happened
   */
  @Nullable
  protected List<String> getDistributionsList() {
    List<String> output = getOutput("list");
    if (output == null) {
      return null;
    }
    return ContainerUtil.map(output, it -> it.replaceAll("\\(.+?\\)", "").trim());
  }

  @NotNull
  @Override
  protected String getErrorNotificationTitle() {
    return PerlBundle.message("perl.vm.perlbrew.notification.title");
  }
}
