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

package com.perl5.lang.perl.idea.sdk.perlInstall;

import com.perl5.lang.perl.idea.sdk.versionManager.InstallPerlHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlInstallFormOptions;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler;
import org.jetbrains.annotations.NotNull;

public abstract class PerlInstallHandlerBase extends InstallPerlHandler {
  public PerlInstallHandlerBase(@NotNull String versionManagerPath,
                                @NotNull PerlRealVersionManagerHandler<?, ?> versionManageHandler) {
    super(versionManagerPath, versionManageHandler);
  }

  @Override
  protected @NotNull String doCleanDistributionItem(@NotNull String rawItem) {
    return rawItem;
  }

  @Override
  protected boolean doIsInstalled(@NotNull String rawItem) {
    return false;
  }

  @Override
  protected @NotNull PerlInstallFormOptions createOptionsForm() {
    return new PerlInstallOptionsForm();
  }
}
