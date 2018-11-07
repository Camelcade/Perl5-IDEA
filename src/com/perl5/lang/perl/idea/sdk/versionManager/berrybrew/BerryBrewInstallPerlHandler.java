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

import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.InstallPerlHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlInstallFormOptions;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import org.jetbrains.annotations.NotNull;

class BerryBrewInstallPerlHandler extends InstallPerlHandler {
  private static final String INSTALLED_MARK = "[installed]";

  public BerryBrewInstallPerlHandler(@NotNull String versionManagerPath,
                                     @NotNull PerlRealVersionManagerHandler<?, ?> versionManageHandler) {
    super(versionManagerPath, versionManageHandler);
  }

  @NotNull
  @Override
  protected String doCleanDistributionItem(@NotNull String rawItem) {
    return rawItem.replace(INSTALLED_MARK, "").replaceAll("\\*", "").trim();
  }

  @Override
  protected boolean doIsInstalled(@NotNull String rawItem) {
    return rawItem.indexOf(INSTALLED_MARK) > 0;
  }

  @NotNull
  @Override
  protected PerlVersionManagerAdapter createAdapter(@NotNull String vmPath, @NotNull PerlHostData hostData) {
    return new BerryBrewAdapter(vmPath, hostData);
  }

  @NotNull
  @Override
  protected PerlInstallFormOptions createOptionsForm() {
    return new PerlInstallFormOptions() {
    };
  }
}
