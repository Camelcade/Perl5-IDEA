/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.AbstractPerlData;
import com.perl5.lang.perl.idea.sdk.PerlSdkAdditionalData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Data necessary for version manager. E.g. system, perbrew, plenv, asdf, berrybrew
 */
public abstract class PerlVersionManagerData<Data extends PerlVersionManagerData<Data, Handler>, Handler extends PerlVersionManagerHandler<Data, Handler>>
  extends AbstractPerlData<Data, Handler> {

  public PerlVersionManagerData(@NotNull Handler handler) {
    super(handler);
  }

  /**
   * Adds bin dirs for script searching. E.g. perlbrew may have patchperl and cpanm installed into perlbrew's home
   *
   * @apiNote returns paths on the target host, not local
   */
  public @NotNull List<File> getBinDirsPath() {
    return Collections.emptyList();
  }

  /**
   * Patched commandline according to the rules of the version manager and current version manager data
   *
   * @param originalCommandLine non-patched commandline
   * @return patched or original commandline
   */
  public abstract @NotNull PerlCommandLine patchCommandLine(@NotNull PerlCommandLine originalCommandLine);

  /**
   * @return short lowercased name, for interpreters list
   */
  public final @NotNull String getShortName() {
    String secondaryShortName = getSecondaryShortName();
    return secondaryShortName == null ? getPrimaryShortName() : getPrimaryShortName() + getSecondaryShortName();
  }

  /**
   * @return primary part of short name, e.g. 'perlbrew'
   */
  public final @NotNull String getPrimaryShortName() {
    return getHandler().getShortName();
  }

  /**
   * @return handler to install a Perl for this version manager or null if installation is not supported
   */
  public abstract @Nullable InstallPerlHandler getInstallPerlHandler();

  /**
   * @return optional secondary short name, e.g. '[perlVersion@library]'
   */
  public abstract @Nullable String getSecondaryShortName();

  @Override
  public String toString() {
    return getShortName();
  }

  public abstract void installCpanminus(@Nullable Project project);

  public static @NotNull PerlVersionManagerData<?, ?> notNullFrom(@NotNull Sdk sdk) {
    return Objects.requireNonNull(from(sdk), () -> "No version manager data in " + sdk);
  }

  @Contract("null->null")
  public static @Nullable PerlVersionManagerData<?, ?> from(@Nullable Sdk sdk) {
    return ObjectUtils.doIfNotNull(PerlSdkAdditionalData.from(sdk), PerlSdkAdditionalData::getVersionManagerData);
  }

  /**
   * @return default, system version manager. Is necessary to avoid patching of external executions
   */
  public static @NotNull PerlVersionManagerData<?, ?> getDefault() {
    return PerlVersionManagerHandler.getDefaultHandler().createData();
  }

  /**
   * @return name of the script for terminal customization (selecting perl) if it is supported by version manager. Otherwise - return null;
   */
  public @Nullable String getTerminalCustomizerScriptName() {
    return null;
  }

  /**
   * @return path to the version managerr if applicable
   */
  public abstract @Nullable String getVersionManagerPath();

  /**
   * @return distribution id if applicable
   */
  public abstract @Nullable String getDistributionId();

  /**
   * @return true iff it is a system version manager data.
   */
  @TestOnly
  public abstract boolean isSystem();
}
