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

import com.intellij.coverage.CoverageRunner;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.coverage.CoverageEnabledConfiguration;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import org.jetbrains.annotations.NotNull;

public class PerlCoverageEnabledConfiguration extends CoverageEnabledConfiguration {

  public PerlCoverageEnabledConfiguration(RunConfigurationBase configuration) {
    super(configuration);
    setCoverageRunner(CoverageRunner.getInstance(PerlCoverageRunner.class));
  }

  @Override
  public @NotNull GenericPerlRunConfiguration getConfiguration() {
    return (GenericPerlRunConfiguration)super.getConfiguration();
  }

  @Override
  protected String coverageFileNameSeparator() {
    return "_S_";
  }
}
