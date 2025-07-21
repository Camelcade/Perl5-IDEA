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

package com.perl5.lang.perl.idea.sdk.versionManager.asdf;

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

import static com.perl5.PerlIcons.ASDF_ICON;

@VisibleForTesting
public class AsdfAdapter extends PerlVersionManagerAdapter {
  static final String ASDF_VERSION = "ASDF_PERL_VERSION";
  private static final String ASDF_PERL_DEVEL = "ASDF_PERL_DEVEL";
  static final String ASDF_EXEC = "exec";
  private static final String ASDF_INSTALL = "install";
  private static final String ASDF_PERL = "perl";
  private static final String ASDF_LIST = "list";
  private static final String ASDF_LIST_ALL = "list all";

  public AsdfAdapter(@NotNull String versionManagerPath,
                     @NotNull PerlHostData<?, ?> hostData) {
    super(versionManagerPath, hostData);
  }

  @Override
  protected @Nullable List<String> execWith(@NotNull String distributionId, @NotNull String... commands) {
    return getOutput(new PerlCommandLine(getVersionManagerPath())
                       .withParameters(ASDF_EXEC)
                       .withParameters(commands)
                       .withEnvironment(ASDF_VERSION, distributionId));
  }

  @Override
  public void installPerl(@Nullable Project project,
                          @NotNull String distributionId,
                          @NotNull List<String> params,
                          @NotNull ProcessListener processListener) {
    runInstallInConsole(
      new PerlCommandLine(getVersionManagerPath(), ASDF_INSTALL, ASDF_PERL, distributionId)
        .withParameters(params)
        .withHostData(getHostData())
        .withProject(project)
        .withProcessListener(processListener),
      distributionId
    );
  }

  @Override
  protected @Nullable Icon getIcon() {
    return ASDF_ICON;
  }

  @Override
  protected @Nullable List<String> getInstallableDistributionsList() {
    return parseInstallableDistributionsList(getOutput(
      it -> it.withParameters(ASDF_LIST_ALL, ASDF_PERL).withEnvironment(ASDF_PERL_DEVEL, "true")));
  }

  @Override
  protected @Nullable List<String> getInstalledDistributionsList() {
    return parseInstalledDistributionsList(getOutput(ASDF_LIST, ASDF_PERL));
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

  @Override
  protected @NlsContexts.DialogTitle @NotNull String getErrorNotificationTitle() {
    return PerlBundle.message("perl.vm.asdf.notification.title");
  }

  @Contract("null->null")
  public static @Nullable AsdfAdapter create(@Nullable Project project) {
    return create(PerlProjectManager.getSdk(project));
  }

  @Contract("null->null")
  public static @Nullable AsdfAdapter create(@Nullable Sdk perlSdk) {
    AsdfData plenvData = AsdfData.from(perlSdk);
    return plenvData == null ? null : new AsdfAdapter(plenvData.getVersionManagerPath(), PerlHostData.notNullFrom(perlSdk));
  }
}
