/*
 * Copyright 2015-2021 Alexandr Evstigneev
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
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugProcessStarter;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.perl5.lang.perl.debugger.PerlDebugProcess;
import com.perl5.lang.perl.idea.run.GenericPerlProgramRunner;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


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
  protected @Nullable PerlRunProfileState createState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment)
    throws ExecutionException {
    return new PerlDebugProfileState(executionEnvironment);
  }

  @Override
  public void execute(@NotNull ExecutionEnvironment env) throws ExecutionException {
    FileDocumentManager.getInstance().saveAllDocuments();
    ExecutionManager.getInstance(env.getProject()).startRunProfile(env, state -> {
      if (!(state instanceof PerlDebugProfileStateBase)) {
        LOG.error("PerlDebugProfileStateBase expected, got " + state + " for " + env);
        throw new ExecutionException("Incorrect run state");
      }
      XDebugSession xDebugSession = XDebuggerManager.getInstance(env.getProject()).startSession(env, new XDebugProcessStarter() {
        @Override
        public @NotNull XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException {
          return new PerlDebugProcess(session, (PerlDebugProfileStateBase)state,
                                      ((PerlDebugProfileStateBase)state).execute(env.getExecutor()));
        }
      });
      return xDebugSession.getRunContentDescriptor();
    });
  }
}
