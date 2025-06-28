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

package com.perl5.lang.perl.idea.run.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.perl5.lang.perl.idea.run.GenericPerlProgramRunner;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.AsyncPromise;

public class PerlRunProgramRunner extends GenericPerlProgramRunner {
  @Override
  public @NotNull String getRunnerId() {
    return "Perl Default Runner";
  }

  @Override
  protected @Nullable PerlRunProfileState createState(@NotNull ExecutionEnvironment executionEnvironment) {
    return new PerlRunProfileState(executionEnvironment);
  }

  @Override
  protected void doExecute(@NotNull RunProfileState state,
                           @NotNull ExecutionEnvironment environment,
                           @NotNull AsyncPromise<? super RunContentDescriptor> result) throws ExecutionException {
    createAndSetContentDescriptor(environment, state.execute(environment.getExecutor(), this), result);
  }

  @Override
  public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
    return executorId.equals(DefaultRunExecutor.EXECUTOR_ID) && profile instanceof GenericPerlRunConfiguration;
  }
}
