/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.profiler.configuration;

import com.intellij.execution.configurations.RunProfile;
import com.intellij.openapi.project.Project;
import com.intellij.profiler.api.configurations.ProfilerStarter;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import org.jetbrains.annotations.NotNull;

class PerlProfilerStarter implements ProfilerStarter {
  private final @NotNull PerlProfilerConfigurationState myProfilerConfigurationState;

  PerlProfilerStarter(@NotNull PerlProfilerConfigurationState profilerConfigurationState) {
    myProfilerConfigurationState = profilerConfigurationState;
  }

  public @NotNull PerlProfilerConfigurationState getProfilerConfigurationState() {
    return myProfilerConfigurationState;
  }

  @Override
  public boolean canRun(@NotNull RunProfile profile) {
    return profile instanceof GenericPerlRunConfiguration;
  }

  @Override
  public boolean isApplicable(@NotNull Project project) {
    return PerlProjectManager.getSdk(project) != null;
  }
}
