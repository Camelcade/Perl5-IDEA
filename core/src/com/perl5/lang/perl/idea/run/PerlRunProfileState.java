/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author VISTALL
 * @since 16-Sep-15
 */
public class PerlRunProfileState extends CommandLineState {
  public PerlRunProfileState(ExecutionEnvironment environment) {
    super(environment);
  }

  @Override
  @NotNull
  public final ExecutionResult execute(@NotNull final Executor executor, @NotNull final ProgramRunner runner) throws ExecutionException {
    return execute(executor);
  }

  /**
   * Copy paste of {@link CommandLineState#execute(com.intellij.execution.Executor, com.intellij.execution.runners.ProgramRunner)} without
   * a runner argument
   */
  @NotNull
  public ExecutionResult execute(@NotNull final Executor executor) throws ExecutionException {
    final ProcessHandler processHandler = startProcess();
    final ConsoleView console = createConsole(executor);
    if (console != null) {
      console.attachToProcess(processHandler);
    }
    return new DefaultExecutionResult(
      console,
      GenericPerlRunConfiguration.patchProcessHandler(processHandler, this),
      createActions(console, processHandler, executor));
  }

  @NotNull
  @Override
  protected ProcessHandler startProcess() throws ExecutionException {
    return PerlHostData.createConsoleProcessHandler(createCommandLine());
  }

  @NotNull
  protected PerlCommandLine createCommandLine() throws ExecutionException {
    return ((GenericPerlRunConfiguration)getEnvironment().getRunProfile()).createCommandLine(this);
  }

  @Nullable
  @Override
  protected ConsoleView createConsole(@NotNull Executor executor) throws ExecutionException {
    return ((GenericPerlRunConfiguration)getEnvironment().getRunProfile()).createConsole(this);
  }

  @NotNull
  public List<String> getAdditionalPerlParameters(@NotNull GenericPerlRunConfiguration perlRunConfiguration) throws ExecutionException {
    return Collections.emptyList();
  }

  public Map<String, String> getAdditionalEnvironmentVariables() throws ExecutionException {
    return Collections.emptyMap();
  }

  /**
   * @return true iff this state allows parallel running of inner processes
   */
  public boolean isParallelRunAllowed() {
    return true;
  }
}
