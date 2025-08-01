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

package com.perl5.lang.perl.idea.sdk.versionManager.plenv;

import com.google.common.annotations.VisibleForTesting;
import com.intellij.execution.process.ProcessListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.perl5.PerlIcons.PLENV_ICON;

@VisibleForTesting
public class PlenvAdapter extends PerlVersionManagerAdapter {
  static final String PLENV_VERSION = "PLENV_VERSION";
  static final String PLENV_EXEC = "exec";
  static final String PLENV_INSTALL_CPANM = "install-cpanm";
  private static final String PLENV_INSTALL = "install";
  private static final String PLENV_VERSIONS = "versions";

  public PlenvAdapter(@NotNull String versionManagerPath,
                      @NotNull PerlHostData<?, ?> hostData) {
    super(versionManagerPath, hostData);
  }

  @Override
  protected @Nullable List<String> execWith(@NotNull String distributionId, @NotNull String... commands) {
    return getOutput(new PerlCommandLine(getVersionManagerPath())
                       .withParameters(PLENV_EXEC)
                       .withParameters(commands)
                       .withEnvironment(PLENV_VERSION, distributionId));
  }

  @Override
  public void installPerl(@Nullable Project project,
                          @NotNull String distributionId,
                          @NotNull List<String> params,
                          @NotNull ProcessListener processListener) {
    runInstallInConsole(
      new PerlCommandLine(getVersionManagerPath(), PLENV_INSTALL, distributionId)
        .withParameters(params)
        .withHostData(getHostData())
        .withProject(project)
        .withProcessListener(processListener),
      distributionId
    );
  }

  @Override
  protected @Nullable Icon getIcon() {
    return PLENV_ICON;
  }

  @Override
  protected @Nullable List<String> getInstallableDistributionsList() {
    return parseInstallableDistributionsList(getOutput(PLENV_INSTALL, "-l"));
  }

  @Override
  protected @Nullable List<String> getInstalledDistributionsList() {
    return parseInstalledDistributionsList(getOutput(PLENV_VERSIONS));
  }

  /**
   * @return list of distributions, available for installations, parsed from {@code plenv install -l} output lines
   */
  @VisibleForTesting
  @Contract("null->null; !null->!null")
  public static @Nullable List<String> parseInstallableDistributionsList(@Nullable List<String> output) {
    return output == null ? null : output.stream()
      .map(String::trim)
      .filter(StringUtil::isNotEmpty)
      .filter(it -> !StringUtil.contains(it, "Available"))
      .collect(Collectors.toList());
  }

  /**
   * @return list of installed perl versions, parsed from {@code plenv versions} output lines
   */
  @VisibleForTesting
  @Contract("null->null; !null->!null")
  public static @Nullable List<String> parseInstalledDistributionsList(@Nullable List<String> output) {
    return output == null ? null : output.stream()
      .filter(it -> !StringUtil.contains(it, "system"))
      .map(it -> it.replaceAll("\\(.+?\\)", "").replaceAll("^\\s*\\**\\s*", "").trim())
      .collect(Collectors.toList());
  }

  public void runInstallInConsole(@NotNull Project project,
                                  @NotNull String packageName,
                                  @NotNull String command,
                                  @NotNull String distributionId) {
    runInstallInConsole(createInstallCommandLine(project, command)
                          .withEnvironment(PLENV_VERSION, distributionId), packageName);
  }

  @Override
  protected @NlsContexts.DialogTitle @NotNull String getErrorNotificationTitle() {
    return PerlBundle.message("perl.vm.plenv.notification.title");
  }

  @Contract("null->null")
  public static @Nullable PlenvAdapter create(@Nullable Project project) {
    return create(PerlProjectManager.getSdk(project));
  }

  @Contract("null->null")
  public static @Nullable PlenvAdapter create(@Nullable Sdk perlSdk) {
    PlenvData plenvData = PlenvData.from(perlSdk);
    return plenvData == null ? null : new PlenvAdapter(plenvData.getVersionManagerPath(), PerlHostData.notNullFrom(perlSdk));
  }
}
