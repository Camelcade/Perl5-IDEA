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

package com.perl5.lang.perl.idea.sdk.versionManager.berrybrew;

import com.intellij.execution.process.ProcessListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

import static com.perl5.lang.perl.util.PerlUtil.mutableList;

class BerryBrewAdapter extends PerlVersionManagerAdapter {
  private static final String BERRYBREW_AVAILABLE = "available";
  private static final String BERRYBREW_INSTALL = "install";
  static final String BERRYBREW_EXEC = "exec";
  static final String BERRYBREW_WITH = "--with";

  BerryBrewAdapter(@NotNull String versionManagerPath, @NotNull PerlHostData<?, ?> hostData) {
    super(versionManagerPath, hostData);
  }

  @Override
  protected @Nullable List<String> execWith(@NotNull String distributionId, @NotNull String... commands) {
    List<String> commandsList = mutableList(BERRYBREW_EXEC, BERRYBREW_WITH, distributionId);
    commandsList.addAll(Arrays.asList(commands));
    return getOutput(commandsList);
  }

  @Override
  public void installPerl(@Nullable Project project,
                          @NotNull String distributionId,
                          @NotNull List<String> params,
                          @NotNull ProcessListener processListener) {
    runInstallInConsole(
      new PerlCommandLine(getVersionManagerPath(), BERRYBREW_INSTALL, distributionId)
        .withParameters(params)
        .withHostData(getHostData())
        .withProject(project)
        .withProcessListener(processListener),
      distributionId
    );
  }

  @Override
  protected @Nullable Icon getIcon() {
    return PerlIcons.STRAWBERRY_ICON;
  }

  @Override
  protected @Nullable List<String> getInstalledDistributionsList() {
    List<String> output = getOutput("list");
    if (output == null) {
      return null;
    }
    return ContainerUtil.map(output, it -> it.replaceAll("\\[.+?]", "").replaceAll("\\*", "").trim());
  }

  @Override
  protected @Nullable List<String> getInstallableDistributionsList() {
    List<String> output = getOutput(BERRYBREW_AVAILABLE);
    if (output == null) {
      return null;
    }
    return ContainerUtil.filter(output, it -> {
      String trimmed = it.trim();
      if (StringUtil.isEmpty(trimmed)) {
        return false;
      }
      return trimmed.indexOf("Currently using") <= 0 && trimmed.indexOf("are available") <= 0;
    });
  }

  @Override
  protected @NotNull String getErrorNotificationTitle() {
    return PerlBundle.message("perl.vm.berrybrew.notification.title");
  }
}
