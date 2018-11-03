/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.coverage;

import com.intellij.coverage.BaseCoverageSuite;
import com.intellij.coverage.CoverageEngine;
import com.intellij.coverage.CoverageFileProvider;
import com.intellij.coverage.CoverageRunner;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.idea.run.PerlRunConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PerlCoverageSuite extends BaseCoverageSuite {
  public PerlCoverageSuite() {
  }

  public PerlCoverageSuite(String name,
                           @Nullable CoverageFileProvider fileProvider,
                           long lastCoverageTimeStamp,
                           boolean coverageByTestEnabled,
                           boolean tracingEnabled,
                           boolean trackTestFolders,
                           CoverageRunner coverageRunner, Project project) {
    super(name, fileProvider, lastCoverageTimeStamp, coverageByTestEnabled, tracingEnabled, trackTestFolders, coverageRunner, project);
  }

  @NotNull
  @Override
  public PerlRunConfiguration getConfiguration() {
    return Objects.requireNonNull((PerlRunConfiguration)super.getConfiguration());
  }

  @Override
  public void setConfiguration(RunConfigurationBase configuration) {
    if (!(configuration instanceof PerlRunConfiguration)) {
      throw new RuntimeException("Got " + configuration);
    }
    super.setConfiguration(configuration);
  }

  @NotNull
  @Override
  public CoverageEngine getCoverageEngine() {
    return PerlCoverageEngine.getInstance();
  }
}
