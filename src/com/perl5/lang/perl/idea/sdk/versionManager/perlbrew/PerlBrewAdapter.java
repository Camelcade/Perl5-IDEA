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
import java.util.List;
import java.util.stream.Collectors;

import static com.perl5.PerlIcons.PERLBREW_ICON;

/**
 * Api to the perlbrew cli
 */
class PerlBrewAdapter extends PerlVersionManagerAdapter {
  static final String PERLBREW_ROOT = "PERLBREW_ROOT";
  static final String PERLBREW_HOME = "PERLBREW_HOME";
  static final String PERLBREW_PATH = "PERLBREW_PATH";
  static final String PERLBREW_MANPATH = "PERLBREW_MANPATH";
  static final String PERLBREW_EXEC = "exec";
  static final String PERLBREW_INSTALL = "install";
  static final String PERLBREW_LIST = "list";
  static final String PERLBREW_INFO = "info";
  static final String PERLBREW_AVAILABLE = "available";
  static final String PERLBREW_WITH = "--with";
  static final String PERLBREW_QUIET = "-q";
  static final String PERLBREW_VERBOSE = "--verbose";
  static final String PERLBREW_INSTALL_PATCHPERL = "install-patchperl";
  static final String PERLBREW_INSTALL_CPANM = "install-cpanm";

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
   * @return result of {@code perlbrew info} command
   */
  @Nullable
  List<String> getInfo() {
    return getOutput(
      new PerlCommandLine("bash", "-cl", new PerlCommandLine(getVersionManagerPath(), PERLBREW_INFO).getCommandLineString()));
  }

  /**
   * @return list of {@code perlbrew list} items trimmed or null if error happened
   */
  @Nullable
  protected List<String> getDistributionsList() {
    List<String> output = getOutput(PERLBREW_LIST);
    if (output == null) {
      return null;
    }
    return ContainerUtil.map(output, it -> it.replaceAll("\\(.+?\\)", "").trim());
  }

  public void installPerl(@NotNull Project project,
                          @NotNull String distributionId,
                          @NotNull List<String> params,
                          @Nullable ProcessListener processListener) {
    runInstallInConsole(
      new PerlCommandLine(getVersionManagerPath(), PERLBREW_INSTALL, PERLBREW_VERBOSE, distributionId)
        .withParameters(params)
        .withProject(project)
        .withProcessListener(processListener),
      distributionId
    );
  }

  @Nullable
  @Override
  protected Icon getIcon() {
    return PERLBREW_ICON;
  }

  @Nullable
  @Override
  protected List<String> getAvailableDistributionsList() {
    List<String> rawAvailable = getOutput(PERLBREW_AVAILABLE);
    if (rawAvailable == null) {
      return null;
    }
    return rawAvailable.stream().map(String::trim).filter(StringUtil::isNotEmpty).collect(Collectors.toList());
  }

  @NotNull
  @Override
  protected String getErrorNotificationTitle() {
    return PerlBundle.message("perl.vm.perlbrew.notification.title");
  }

  @Contract("null->null")
  @Nullable
  public static PerlBrewAdapter create(@Nullable Project project) {
    return create(PerlProjectManager.getSdk(project));
  }

  @Contract("null->null")
  @Nullable
  public static PerlBrewAdapter create(@Nullable Sdk perlSdk) {
    PerlBrewData perlBrewData = PerlBrewData.from(perlSdk);
    return perlBrewData == null ? null : new PerlBrewAdapter(perlBrewData.getVersionManagerPath(), PerlHostData.notNullFrom(perlSdk));
  }
}
