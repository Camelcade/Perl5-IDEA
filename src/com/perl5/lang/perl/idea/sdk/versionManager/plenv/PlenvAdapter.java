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

package com.perl5.lang.perl.idea.sdk.versionManager.plenv;

import com.intellij.openapi.util.text.StringUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

class PlenvAdapter extends PerlVersionManagerAdapter {
  static final String PLENV_VERSION = "PLENV_VERSION";
  static final String PLENV_EXEC = "exec";

  public PlenvAdapter(@NotNull String versionManagerPath,
                      @NotNull PerlHostData hostData) {
    super(versionManagerPath, hostData);
  }

  @Nullable
  @Override
  protected List<String> execWith(@NotNull String distributionId, @NotNull String... commands) {
    return getOutput(new PerlCommandLine(getVersionManagerPath())
                       .withParameters(PLENV_EXEC)
                       .withParameters(commands)
                       .withEnvironment(PLENV_VERSION, distributionId));
  }

  @Nullable
  @Override
  protected List<String> getDistributionsList() {
    List<String> rawVersions = getOutput("versions");
    return rawVersions == null ? null : rawVersions.stream()
      .filter(it -> !StringUtil.contains(it, "system"))
      .map(String::trim)
      .collect(Collectors.toList());
  }

  @NotNull
  @Override
  protected String getErrorNotificationTitle() {
    return PerlBundle.message("perl.vm.plenv.notification.title");
  }
}
