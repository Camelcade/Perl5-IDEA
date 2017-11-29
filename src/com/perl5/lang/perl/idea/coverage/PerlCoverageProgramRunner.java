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

import com.intellij.coverage.CoverageExecutor;
import com.intellij.coverage.CoverageHelper;
import com.intellij.coverage.CoverageRunnerData;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.ConfigurationInfoProvider;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.DefaultProgramRunner;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.perl5.lang.perl.idea.run.PerlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlCoverageProgramRunner extends DefaultProgramRunner {
  @NotNull
  @Override
  public String getRunnerId() {
    return "PERL5_COVERAGE_RUNNER";
  }

  @Override
  public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
    return executorId.equals(CoverageExecutor.EXECUTOR_ID) && profile instanceof PerlConfiguration;
  }

  @Nullable
  @Override
  public RunnerSettings createConfigurationData(ConfigurationInfoProvider settingsProvider) {
    return new CoverageRunnerData();
  }

  @Override
  protected void execute(ExecutionEnvironment environment, Callback callback, RunProfileState state)
    throws ExecutionException {
    super.execute(environment, descriptor -> {
      ProcessHandler processHandler = descriptor.getProcessHandler();
      if (processHandler != null) {
        CoverageHelper.attachToProcess((PerlConfiguration)environment.getRunProfile(), processHandler, environment.getRunnerSettings());
      }
      if (callback != null) {
        callback.processStarted(descriptor);
      }
    }, new PerlCoverageProfileState(environment));
  }
}
