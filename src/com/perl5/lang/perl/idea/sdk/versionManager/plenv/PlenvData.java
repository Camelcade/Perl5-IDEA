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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Paths;

import static com.perl5.lang.perl.adapters.CpanminusAdapter.PACKAGE_NAME;
import static com.perl5.lang.perl.idea.sdk.versionManager.plenv.PlenvAdapter.*;

class PlenvData extends PerlRealVersionManagerData<PlenvData, PlenvHandler> {
  PlenvData(@NotNull PlenvHandler handler) {
    super(handler);
  }

  PlenvData(@NotNull String versionManagerPath,
            @NotNull String distributionId,
            @NotNull PlenvHandler handler) {
    super(versionManagerPath, distributionId, handler);
  }

  @NotNull
  @Override
  public PerlCommandLine patchCommandLine(@NotNull PerlCommandLine originalCommandLine) {
    originalCommandLine.setExePath(Paths.get(originalCommandLine.getExePath()).getFileName().toString());
    return originalCommandLine.prependLineWith(getVersionManagerPath(), PLENV_EXEC).withEnvironment(PLENV_VERSION, getDistributionId());
  }

  @Override
  public void installCpanminus(@Nullable Project project) {
    PlenvAdapter plenvAdapter = PlenvAdapter.create(project);
    if (plenvAdapter == null) {
      return;
    }
    plenvAdapter.runInstallInConsole(project, PACKAGE_NAME, PLENV_INSTALL_CPANM, getDistributionId());
  }

  @NotNull
  @Override
  protected PlenvData self() {
    return this;
  }

  @Nullable
  public static PlenvData from(@Nullable Sdk sdk) {
    return ObjectUtils.tryCast(PerlVersionManagerData.from(sdk), PlenvData.class);
  }
}
