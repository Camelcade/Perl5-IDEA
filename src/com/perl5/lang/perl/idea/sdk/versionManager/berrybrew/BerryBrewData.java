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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.adapters.CpanminusAdapter;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.versionManager.InstallPerlHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.idea.sdk.versionManager.berrybrew.BerryBrewAdapter.BERRYBREW_EXEC;
import static com.perl5.lang.perl.idea.sdk.versionManager.berrybrew.BerryBrewAdapter.BERRYBREW_WITH;

class BerryBrewData extends PerlRealVersionManagerData<BerryBrewData, BerryBrewHandler> {
  BerryBrewData(@NotNull BerryBrewHandler handler) {
    super(handler);
  }

  public BerryBrewData(@NotNull String versionManagerPath,
                       @NotNull String distributionId,
                       @NotNull BerryBrewHandler handler) {
    super(versionManagerPath, distributionId, handler);
  }

  @NotNull
  @Override
  public PerlCommandLine patchCommandLine(@NotNull PerlCommandLine originalCommandLine) {
    return originalCommandLine.prependLineWith(getVersionManagerPath(), BERRYBREW_EXEC, BERRYBREW_WITH, getDistributionId());
  }

  @Override
  public void installCpanminus(@Nullable Project project) {
    CpanminusAdapter.install(project);
  }

  @NotNull
  @Override
  protected BerryBrewData self() {
    return this;
  }

  @Nullable
  @Override
  public InstallPerlHandler getInstallPerlHandler() {
    return new BerryBrewInstallPerlHandler(getVersionManagerPath(), getHandler());
  }

  @Nullable
  public static BerryBrewData from(@Nullable Sdk sdk) {
    return ObjectUtils.tryCast(PerlVersionManagerData.from(sdk), BerryBrewData.class);
  }
}
