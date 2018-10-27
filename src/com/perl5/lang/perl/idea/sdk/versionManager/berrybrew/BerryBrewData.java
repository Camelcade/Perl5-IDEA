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

import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerData;
import org.jetbrains.annotations.NotNull;

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
  protected BerryBrewData self() {
    return this;
  }
}
