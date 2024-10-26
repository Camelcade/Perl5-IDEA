/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.debugger.run.run.debugger;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugProcessStarter;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.debugger.PerlDebugProcess;
import com.perl5.lang.perl.idea.run.GenericPerlProgramRunner;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptions;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.AsyncPromise;

import java.util.Set;


public class PerlDebuggerProgramRunner extends GenericPerlProgramRunner {
  private static final Logger LOG = Logger.getInstance(PerlDebuggerProgramRunner.class);
  @Override
  public @NotNull String getRunnerId() {
    return "Perl Debugger";
  }

  @Override
  public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
    return executorId.equals(DefaultDebugExecutor.EXECUTOR_ID) && profile instanceof PerlDebugOptions;
  }

  @Override
  protected @Nullable PerlRunProfileState createState(@NotNull ExecutionEnvironment executionEnvironment) {
    return new PerlDebugProfileState(executionEnvironment);
  }

  @Override
  protected Set<String> getRequiredModules(@NotNull ExecutionEnvironment environment) {
    var modules = super.getRequiredModules(environment);
    modules.add(PerlPackageUtil.DEBUGGER_MODULE);
    return modules;
  }

  @Override
  protected void doExecute(@NotNull RunProfileState state,
                           @NotNull ExecutionEnvironment env,
                           @NotNull AsyncPromise<RunContentDescriptor> result) throws ExecutionException {
    if (!(state instanceof PerlDebugProfileStateBase perlDebugProfileStateBase)) {
      LOG.error("PerlDebugProfileStateBase expected, got " + state + " for " + env);
      throw new ExecutionException(PerlBundle.message("dialog.message.incorrect.run.state"));
    }
    var executionResult = perlDebugProfileStateBase.execute(env.getExecutor(), this);
    ApplicationManager.getApplication().invokeLater(() -> {
      try {
        XDebugSession xDebugSession = XDebuggerManager.getInstance(env.getProject()).startSession(env, new XDebugProcessStarter() {
          @Override
          public @NotNull XDebugProcess start(@NotNull XDebugSession session) {
            return new PerlDebugProcess(session, (PerlDebugProfileStateBase)state, executionResult);
          }
        });
        result.setResult(xDebugSession.getRunContentDescriptor());
      }
      catch (ExecutionException e) {
        LOG.error(e);
        result.setError(e);
      }
    });
  }
}
