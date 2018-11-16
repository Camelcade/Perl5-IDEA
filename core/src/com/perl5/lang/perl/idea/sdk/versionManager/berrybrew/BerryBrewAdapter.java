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

class BerryBrewAdapter extends PerlVersionManagerAdapter {
  private static final String BERRYREW_AVAILABLE = "available";
  private static final String BERRYBREW_INSTALL = "install";
  static final String BERRYBREW_EXEC = "exec";
  static final String BERRYBREW_WITH = "--with";

  BerryBrewAdapter(@NotNull String versionManagerPath, @NotNull PerlHostData hostData) {
    super(versionManagerPath, hostData);
  }

  @Nullable
  @Override
  protected List<String> execWith(@NotNull String distributionId, @NotNull String... commands) {
    List<String> commandsList = ContainerUtil.newArrayList(BERRYBREW_EXEC, BERRYBREW_WITH, distributionId);
    commandsList.addAll(Arrays.asList(commands));
    return getOutput(commandsList);
  }

  public void installPerl(@NotNull Project project,
                          @NotNull String distributionId,
                          @NotNull List<String> params,
                          @Nullable ProcessListener processListener) {
    runInstallInConsole(
      new PerlCommandLine(getVersionManagerPath(), BERRYBREW_INSTALL, distributionId)
        .withParameters(params)
        .withProject(project)
        .withProcessListener(processListener),
      distributionId
    );
  }

  @Nullable
  @Override
  protected Icon getIcon() {
    return PerlIcons.STRAWBERRY_ICON;
  }

  @Nullable
  @Override
  protected List<String> getDistributionsList() {
    List<String> output = getOutput("list");
    if (output == null) {
      return null;
    }
    return ContainerUtil.map(output, it -> it.replaceAll("\\[.+?]", "").replaceAll("\\*", "").trim());
  }

  @Nullable
  @Override
  protected List<String> getAvailableDistributionsList() {
    List<String> output = getOutput(BERRYREW_AVAILABLE);
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

  @NotNull
  @Override
  protected String getErrorNotificationTitle() {
    return PerlBundle.message("perl.vm.berrybrew.notification.title");
  }
}
