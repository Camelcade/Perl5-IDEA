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

package com.perl5.lang.perl.idea.sdk.host.wsl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.startup.StartupActivity;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.project.PerlProjectManagerListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is intended to initialize WSL mount point from the safe context on the project opening or sdk change
 */
public class PerlWslMountPointInitializer implements StartupActivity.Background, PerlProjectManagerListener {
  @Override
  public void runActivity(@NotNull Project project) {
    var perlSdk = PerlProjectManager.getSdk(project);
    if (perlSdk == null) {
      return;
    }
    initMntRoot(perlSdk);
  }

  private static void initMntRoot(@Nullable Sdk perlSdk) {
    var wslHostData = PerlWslData.from(perlSdk);
    if (wslHostData != null) {
      wslHostData.getDistribution().getMntRoot();
    }
  }

  @Override
  public void beforeSdkSet(@NotNull Project project, @Nullable Sdk sdk) {
    initMntRoot(sdk);
  }
}
