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

import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

class BerryBrewAdapter extends PerlVersionManagerAdapter {
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
    throw new RuntimeException("Not implemented");
  }

  @NotNull
  @Override
  protected String getErrorNotificationTitle() {
    return PerlBundle.message("perl.vm.berrybrew.notification.title");
  }
}
