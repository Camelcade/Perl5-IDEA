/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.coverage;

import com.intellij.coverage.BaseCoverageSuite;
import com.intellij.coverage.CoverageEngine;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

public class PerlCoverageSuite extends BaseCoverageSuite {
  public PerlCoverageSuite() {
  }

  public PerlCoverageSuite(@NotNull PerlCoverageEnabledConfiguration config) {
    super(config.createSuiteName(),
          config.getConfiguration().getProject(),
          config.getCoverageRunner(),
          config.createFileProvider(),
          new Date().getTime());
  }


  @Override
  public @NotNull GenericPerlRunConfiguration getConfiguration() {
    return Objects.requireNonNull((GenericPerlRunConfiguration)super.getConfiguration());
  }

  @Override
  public void setConfiguration(RunConfigurationBase configuration) {
    if (!(configuration instanceof GenericPerlRunConfiguration)) {
      throw new RuntimeException("Got " + configuration);
    }
    super.setConfiguration(configuration);
  }

  @Override
  public @NotNull CoverageEngine getCoverageEngine() {
    return PerlCoverageEngine.getInstance();
  }
}
