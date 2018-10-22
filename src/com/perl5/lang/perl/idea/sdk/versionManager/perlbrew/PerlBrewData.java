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

package com.perl5.lang.perl.idea.sdk.versionManager.perlbrew;

import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerData;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.idea.sdk.versionManager.perlbrew.PerlBrewAdapter.*;

class PerlBrewData extends PerlRealVersionManagerData<PerlBrewData, PerlBrewHandler> {

  PerlBrewData(@NotNull PerlBrewHandler handler) {
    super(handler);
  }

  PerlBrewData(@NotNull String versionManagerPath,
               @NotNull String distributionId,
               @NotNull PerlBrewHandler handler) {
    super(versionManagerPath, distributionId, handler);
  }


  /**
   * @see PerlBrewAdapter#execWith(java.lang.String, java.lang.String...)
   */
  @NotNull
  @Override
  public PerlCommandLine patchCommandLine(@NotNull PerlCommandLine originalCommandLine) {
    return originalCommandLine.prependLineWith(getVersionManagerPath(), PERLBREW_EXEC, PERLBREW_QUIET, PERLBREW_WITH, getDistributionId());
  }

  @Override
  protected final PerlBrewData self() {
    return this;
  }
}
