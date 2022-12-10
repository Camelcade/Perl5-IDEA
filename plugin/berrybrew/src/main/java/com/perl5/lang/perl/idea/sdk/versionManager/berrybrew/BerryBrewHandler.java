/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

import com.intellij.openapi.util.NlsSafe;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.InstallPerlHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

class BerryBrewHandler extends PerlRealVersionManagerHandler<BerryBrewData, BerryBrewHandler> {
  private static final @NlsSafe String BERRYBREW = "berrybrew";
  private static final @NlsSafe String BERRYBREW_EXE = BERRYBREW + ".exe";


  @SuppressWarnings("NonDefaultConstructor")
  public BerryBrewHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @Override
  protected @NotNull BerryBrewData createData(@NotNull PerlVersionManagerAdapter vmAdapter, @NotNull String distributionId) {
    return new BerryBrewData(vmAdapter.getVersionManagerPath(), distributionId, this);
  }

  @Override
  protected @NotNull String getExecutableName() {
    return BERRYBREW_EXE;
  }

  @Override
  public @NotNull PerlVersionManagerAdapter createAdapter(@NotNull String pathToVersionManager, @NotNull PerlHostData<?, ?> hostData) {
    return new BerryBrewAdapter(pathToVersionManager, hostData);
  }

  @Override
  public @NotNull String getPresentableName() {
    return BERRYBREW;
  }

  @Override
  public boolean isApplicable(@Nullable PerlOsHandler osHandler) {
    return osHandler == null || osHandler.isMsWindows();
  }

  @Override
  public @NotNull BerryBrewData createData() {
    return new BerryBrewData(this);
  }

  @Override
  public @NotNull Icon getIcon() {
    return PerlIcons.STRAWBERRY_ICON;
  }

  @Override
  public @Nullable InstallPerlHandler createInstallHandler(@NotNull String pathToVersionManager) {
    return new BerryBrewInstallPerlHandler(pathToVersionManager, this);
  }
}
