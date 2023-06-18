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

package com.perl5.lang.perl.coverage;

import com.intellij.coverage.CoverageDataManager;
import com.intellij.coverage.CoverageExecutor;
import com.intellij.coverage.CoverageRunnerData;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.configurations.ConfigurationInfoProvider;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.DefaultProgramRunnerKt;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.perl5.lang.perl.idea.run.GenericPerlProgramRunner;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class PerlCoverageProgramRunner extends GenericPerlProgramRunner {
  @Override
  public @NotNull String getRunnerId() {
    return "PERL5_COVERAGE_RUNNER";
  }

  @Override
  public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
    return executorId.equals(CoverageExecutor.EXECUTOR_ID) && profile instanceof GenericPerlRunConfiguration;
  }

  @Override
  protected @Nullable PerlRunProfileState createState(@NotNull ExecutionEnvironment executionEnvironment)
    throws ExecutionException {
    return new PerlCoverageProfileState(executionEnvironment);
  }

  @Override
  public @Nullable RunnerSettings createConfigurationData(@NotNull ConfigurationInfoProvider settingsProvider) {
    return new CoverageRunnerData();
  }

  @Override
  protected Set<String> getRequiredModules(@NotNull ExecutionEnvironment environment) {
    var modules = super.getRequiredModules(environment);
    modules.add(PerlPackageUtil.COVERAGE_MODULE);
    modules.add(PerlPackageUtil.JSON_MODULE);
    return modules;
  }

  @Override
  protected void doExecute(@NotNull ExecutionEnvironment environment) throws ExecutionException {
    ExecutionManager.getInstance(environment.getProject()).startRunProfile(environment, state -> {
      GenericPerlRunConfiguration runConfiguration = (GenericPerlRunConfiguration)environment.getRunProfile();
      RunContentDescriptor descriptor = DefaultProgramRunnerKt.executeState(state, environment, this);
      if (descriptor == null) {
        return null;
      }
      ProcessHandler processHandler = descriptor.getProcessHandler();
      if (processHandler != null) {
        CoverageDataManager.getInstance(runConfiguration.getProject())
          .attachToProcess(processHandler, runConfiguration, environment.getRunnerSettings());
      }
      return descriptor;
    });
  }
}
