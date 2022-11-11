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

package com.perl5.lang.perl.idea.sdk.versionManager.perlbrew;

import com.google.common.annotations.VisibleForTesting;
import com.intellij.execution.process.ProcessListener;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.perl5.PerlIcons.PERLBREW_ICON;

/**
 * Api to the perlbrew cli
 */
@VisibleForTesting
public class PerlBrewAdapter extends PerlVersionManagerAdapter {
  static final String PERLBREW_ROOT = "PERLBREW_ROOT";
  static final String PERLBREW_EXEC = "exec";
  static final String PERLBREW_INSTALL = "install";
  static final String PERLBREW_LIST = "list";
  static final String PERLBREW_INFO = "info";
  static final String PERLBREW_AVAILABLE = "available";
  static final String PERLBREW_AVAILABLE_ALL = "--all";
  static final String PERLBREW_WITH = "--with";
  static final String PERLBREW_QUIET = "-q";
  static final String PERLBREW_VERBOSE = "--verbose";
  static final String PERLBREW_INSTALL_PATCHPERL = "install-patchperl";
  static final String PERLBREW_INSTALL_CPANM = "install-cpanm";

  public PerlBrewAdapter(@NotNull String versionManagerPath, @NotNull PerlHostData<?, ?> hostData) {
    super(versionManagerPath, hostData);
  }

  /**
   * @see PerlBrewData#patchCommandLine(com.perl5.lang.perl.idea.execution.PerlCommandLine)
   */
  @Override
  protected @Nullable List<String> execWith(@NotNull String distributionId, @NotNull String... commands) {
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
   * @return result of {@code perlbrew info} command
   */
  @Nullable
  List<String> getInfo() {
    String verionsManagerPath = getVersionManagerPath();
    return getOutput(new PerlCommandLine(
      "bash", "-c", "eval $(" + verionsManagerPath + " init|grep source);" + verionsManagerPath + " " + PERLBREW_INFO));
  }

  /**
   * @return list of {@code perlbrew list} items trimmed or null if error happened
   */
  @Override
  protected @Nullable List<String> getInstalledDistributionsList() {
    return parseInstalledDistributionsList(getOutput(PERLBREW_LIST));
  }

  @Override
  protected @Nullable List<String> getInstallableDistributionsList() {
    List<String> filteredDistributions = parseInstallableDistributionsList(getOutput(PERLBREW_AVAILABLE, PERLBREW_AVAILABLE_ALL));
    if (filteredDistributions == null) {
      return null;
    }

    List<String> installedDistributionsList = getInstalledDistributionsList();
    if (installedDistributionsList == null) {
      return filteredDistributions;
    }

    Set<String> installedNames = new HashSet<>(installedDistributionsList);

    return ContainerUtil.map(filteredDistributions, it ->
      installedNames.contains(it) ? PerlBrewInstallPerlHandler.INSTALLED_PREFIX + it : it);
  }

  @Override
  public void installPerl(@Nullable Project project,
                          @NotNull String distributionId,
                          @NotNull List<String> params,
                          @NotNull ProcessListener processListener) {
    runInstallInConsole(
      new PerlCommandLine(getVersionManagerPath(), PERLBREW_INSTALL, PERLBREW_VERBOSE, distributionId)
        .withParameters(params)
        .withHostData(getHostData())
        .withProject(project)
        .withProcessListener(processListener),
      distributionId
    );
  }

  @Override
  protected @Nullable Icon getIcon() {
    return PERLBREW_ICON;
  }

  /**
   * @return list of installed perls parsed from the output of {@code perlbrew list}
   */
  @VisibleForTesting
  @Contract("null->null; !null->!null")
  public static List<String> parseInstalledDistributionsList(@Nullable List<String> output) {
    return output == null ? null : ContainerUtil.map(output, it -> it.replaceAll("\\(.+?\\)", "").replaceAll("^\\s*\\**\\s*", "").trim());
  }

  /**
   * @return list of distributions available for installation parsed from {@code perlbrew available} command output lines
   */
  @VisibleForTesting
  @Contract("null->null;!null->!null")
  public static List<String> parseInstallableDistributionsList(@Nullable List<String> output) {
    if (output == null) {
      return null;
    }
    return output.stream()
      .map(s -> s.trim().replace(".tar.bz2", ""))
      .filter(StringUtil::isNotEmpty)
      .filter(it -> !StringUtil.startsWith(it, "perl5"))
      .collect(Collectors.toList());
  }

  @Override
  protected @NotNull String getErrorNotificationTitle() {
    return PerlBundle.message("perl.vm.perlbrew.notification.title");
  }

  @Contract("null->null")
  public static @Nullable PerlBrewAdapter create(@Nullable Project project) {
    return create(PerlProjectManager.getSdk(project));
  }

  @Contract("null->null")
  public static @Nullable PerlBrewAdapter create(@Nullable Sdk perlSdk) {
    PerlBrewData perlBrewData = PerlBrewData.from(perlSdk);
    return perlBrewData == null ? null : new PerlBrewAdapter(perlBrewData.getVersionManagerPath(), PerlHostData.notNullFrom(perlSdk));
  }
}
