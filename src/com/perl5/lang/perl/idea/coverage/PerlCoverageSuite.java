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
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  public CoverageEngine getCoverageEngine() {
    return PerlCoverageEngine.getInstance();
  }
}
