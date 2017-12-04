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

import com.intellij.coverage.CoverageRunner;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.coverage.CoverageEnabledConfiguration;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.Nullable;

public class PerlCoverageEnabledConfiguration extends CoverageEnabledConfiguration {
  private static final Logger LOG = Logger.getInstance(PerlCoverageEnabledConfiguration.class);

  public PerlCoverageEnabledConfiguration(RunConfigurationBase configuration) {
    super(configuration);
    setCoverageRunner(CoverageRunner.getInstance(PerlCoverageRunner.class));
  }

  @Nullable
  @Override
  public String getCoverageFilePath() {
    String coverageBasePath = super.getCoverageFilePath();
    if (coverageBasePath == null) {
      LOG.warn("Empty coverage database path");
      return ".";
    }
    return FileUtil.toSystemIndependentName(coverageBasePath);
  }
}
