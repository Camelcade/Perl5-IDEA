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

package com.perl5.lang.perl.idea.sdk.versionManager.asdf;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.adapters.PackageManagerAdapter;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

import static com.perl5.lang.perl.idea.sdk.versionManager.asdf.AsdfAdapter.ASDF_EXEC;
import static com.perl5.lang.perl.idea.sdk.versionManager.asdf.AsdfAdapter.ASDF_VERSION;

class AsdfData extends PerlRealVersionManagerData<AsdfData, AsdfHandler> {
  AsdfData(@NotNull AsdfHandler handler) {
    super(handler);
  }

  AsdfData(@NotNull String versionManagerPath,
           @NotNull String distributionId,
           @NotNull AsdfHandler handler) {
    super(versionManagerPath, distributionId, handler);
  }

  @Override
  public @NotNull PerlCommandLine patchCommandLine(@NotNull PerlCommandLine originalCommandLine) {
    originalCommandLine.setExePath(new File(originalCommandLine.getExePath()).getName());
    return originalCommandLine.prependLineWith(getVersionManagerPath(), ASDF_EXEC).withEnvironment(ASDF_VERSION, getDistributionId());
  }

  @Override
  public void installCpanminus(@Nullable Project project) {
    PackageManagerAdapter.installCpanminus(project);
  }

  @Override
  protected @NotNull AsdfData self() {
    return this;
  }

  @Contract("null->null")
  public static @Nullable AsdfData from(@Nullable Sdk sdk) {
    return ObjectUtils.tryCast(PerlVersionManagerData.from(sdk), AsdfData.class);
  }

  @Override
  public @Nullable String getTerminalCustomizerScriptName() {
    return "asdf_starter.sh";
  }
}
