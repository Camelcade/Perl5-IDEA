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

package com.perl5.lang.perl.idea.sdk.versionManager.system;

import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.adapters.PackageManagerAdapter;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.versionManager.InstallPerlHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class PerlSystemData extends PerlVersionManagerData<PerlSystemData, PerlSystemHandler> {
  PerlSystemData(@NotNull PerlSystemHandler handler) {
    super(handler);
  }

  @Override
  protected @NotNull PerlSystemData self() {
    return this;
  }

  @Override
  public @NotNull PerlCommandLine patchCommandLine(@NotNull PerlCommandLine originalCommandLine) {
    return originalCommandLine;
  }

  @Override
  public @Nullable String getSecondaryShortName() {
    return null;
  }

  @Override
  public void installCpanminus(@Nullable Project project) {
    PackageManagerAdapter.installCpanminus(project);
  }

  @Override
  public @Nullable InstallPerlHandler getInstallPerlHandler() {
    return null;
  }

  @Override
  public @Nullable String getVersionManagerPath() {
    return null;
  }

  @Override
  public @Nullable String getDistributionId() {
    return null;
  }

  @Override
  public boolean isSystem() {
    return true;
  }
}
