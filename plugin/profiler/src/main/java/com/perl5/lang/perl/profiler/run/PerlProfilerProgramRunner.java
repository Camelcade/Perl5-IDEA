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

package com.perl5.lang.perl.profiler.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.profiler.DefaultProfilerExecutorGroup;
import com.intellij.profiler.ProfilerToolWindowManager;
import com.perl5.lang.perl.idea.run.GenericPerlProgramRunner;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import com.perl5.lang.perl.profiler.configuration.PerlProfilerConfigurationState;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.AsyncPromise;

import java.io.IOException;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class PerlProfilerProgramRunner extends GenericPerlProgramRunner {
  private static final Logger LOG = Logger.getInstance(PerlProfilerProgramRunner.class);

  @Override
  public @NotNull String getRunnerId() {
    return "Perl Profiler";
  }

  @Override
  public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
    if (!(profile instanceof GenericPerlRunConfiguration)) {
      return false;
    }
    var executorSettings = getExecutorSettings(executorId);
    return executorSettings != null && executorSettings.canRun(profile);
  }

  private static @Nullable DefaultProfilerExecutorGroup.ProfilerExecutorSettings getExecutorSettings(@NotNull String executorId) {
    var executorGroup = DefaultProfilerExecutorGroup.Companion.getInstance();
    if (executorGroup == null) {
      return null;
    }
    return executorGroup.getRegisteredSettings(executorId);
  }

  @Override
  protected @Nullable PerlRunProfileState createState(@NotNull ExecutionEnvironment executionEnvironment)
    throws ExecutionException {

    var executorSettings = getExecutorSettings(executionEnvironment.getExecutor().getId());
    if (executorSettings == null) {
      var message = "Unable to find executor settings";
      LOG.error(message);
      throw new ExecutionException(message);
    }
    var runProfile = executionEnvironment.getRunProfile();
    if (!executorSettings.canRun(runProfile)) {
      var message = "Unable to run " + runProfile + " with " + executorSettings;
      LOG.error(message);
      throw new ExecutionException(message);
    }
    var perlProfilerConfigurationState = executorSettings.getState();
    if (!(perlProfilerConfigurationState instanceof PerlProfilerConfigurationState profilerConfigurationState)) {
      LOG.error("PerlProfilerConfigurationState expected, got: " + perlProfilerConfigurationState);
      throw new ExecutionException("Wrong profiler configuration state: " + perlProfilerConfigurationState);
    }

    return new PerlProfilerRunProfileState(executionEnvironment, profilerConfigurationState);
  }

  @Override
  protected Set<String> getRequiredModules(@NotNull ExecutionEnvironment environment) {
    var modules = super.getRequiredModules(environment);
    modules.add(PerlPackageUtil.PROFILER_MODULE);
    return modules;
  }

  @Override
  protected void doExecute(@NotNull RunProfileState state,
                           @NotNull ExecutionEnvironment environment,
                           @NotNull AsyncPromise<? super RunContentDescriptor> result) throws ExecutionException {
    if (!(state instanceof PerlProfilerRunProfileState profilerRunProfileState)) {
      LOG.error("PerlProfilerRunProfileState expected, got " + state);
      throw new ExecutionException("Incorrect run configuration state, see logs for details");
    }
    var profileResultsPath = profilerRunProfileState.getProfilingResultsPath();
    LOG.info("Profiling results saved in: " + profileResultsPath);
    if (FileUtil.isAncestor(PathManager.getSystemPath(), profileResultsPath.toString(), true)) {
      try {
        // fxime we probably should fix permissions here
        FileUtil.delete(profileResultsPath);
      }
      catch (IOException e) {
        throw new ExecutionException("Error removing old profiling data at " + profileResultsPath, e);
      }
    }
    else {
      LOG.error("Wrong profiler results directory: " + profileResultsPath);
    }
    //noinspection ResultOfMethodCallIgnored
    profileResultsPath.toFile().mkdirs();

    result.then(descriptor -> {
      if (descriptor instanceof RunContentDescriptor runContentDescriptor) {
        var profilerProcess = new PerlProfilerProcess(environment, runContentDescriptor, profilerRunProfileState);
        ProfilerToolWindowManager.getInstance(environment.getProject()).addProfilerProcessTab(profilerProcess, false);
      }
      return descriptor;
    });
    createAndSetContentDescriptor(environment, state.execute(environment.getExecutor(), this), result);
  }
}
