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

package com.perl5.lang.perl.idea.sdk.versionManager.perlbrew;

import com.intellij.openapi.util.NlsSafe;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.InstallPerlHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PerlBrewHandler extends PerlRealVersionManagerHandler<PerlBrewData, PerlBrewHandler> {
  private static final Pattern KEY_VAL_PATTERN = Pattern.compile("\\s+([\\w_]+):\\s*(\\S+)");
  private static final @NlsSafe String PERLBREW = "perlbrew";
  private static final @NlsSafe String PERLBREW_DEFAULT_PATH = "~/perl5/perlbrew/bin/" + PERLBREW;

  @SuppressWarnings("NonDefaultConstructor")
  public PerlBrewHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @Override
  protected @NotNull String getExecutableName() {
    return PERLBREW;
  }

  @Override
  protected @NotNull Collection<String> getPossibleVersionManagerPaths() {
    return Collections.singletonList(PERLBREW_DEFAULT_PATH);
  }

  @Override
  public @NotNull PerlVersionManagerAdapter createAdapter(@NotNull String pathToVersionManager, @NotNull PerlHostData<?, ?> hostData) {
    return new PerlBrewAdapter(pathToVersionManager, hostData);
  }

  @Override
  public @NotNull String getPresentableName() {
    return PERLBREW;
  }

  @Override
  public @NotNull PerlBrewData createData() {
    return new PerlBrewData(this);
  }

  @Override
  protected @NotNull PerlBrewData createData(@NotNull PerlVersionManagerAdapter vmAdapter, @NotNull String distributionId) {
    return new PerlBrewData(vmAdapter.getVersionManagerPath(), distributionId, this, computeInfoData((PerlBrewAdapter)vmAdapter));
  }

  /**
   * Builds a map of key-val from {@code perlbrew info}
   */
  static @NotNull Map<String, String> computeInfoData(@NotNull PerlBrewAdapter adapter) {
    List<String> infoOutput = adapter.getInfo();
    if (infoOutput == null || infoOutput.isEmpty()) {
      return Collections.emptyMap();
    }
    Map<String, String> result = new HashMap<>();
    infoOutput.forEach(it -> {
      Matcher matcher = KEY_VAL_PATTERN.matcher(it);
      if (matcher.matches()) {
        result.put(matcher.group(1), matcher.group(2));
      }
    });
    return result;
  }

  @Override
  public @NotNull Icon getIcon() {
    return PerlIcons.PERLBREW_ICON;
  }

  @Override
  public @Nullable InstallPerlHandler createInstallHandler(@NotNull String pathToVersionManager) {
    return new PerlBrewInstallPerlHandler(pathToVersionManager, this);
  }

  static @NotNull PerlBrewHandler getInstance() {
    for (PerlVersionManagerHandler<?, ?> handler : PerlVersionManagerHandler.all()) {
      if (handler instanceof PerlBrewHandler perlBrewHandler) {
        return perlBrewHandler;
      }
    }
    throw new IllegalStateException("PerlBrewHandler is not registered");
  }

  @Override
  public @NotNull String getControlOutputForPerlVersion(@NotNull String perlVersion) {
    return "perl5/perlbrew/perls/perl-" + perlVersion + "/lib/" + perlVersion;
  }
}
