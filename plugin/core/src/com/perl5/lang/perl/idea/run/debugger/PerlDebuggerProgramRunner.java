/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run.debugger;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugProcessStarter;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import org.jetbrains.annotations.NotNull;


public class PerlDebuggerProgramRunner implements ProgramRunner<RunnerSettings> {
  @Override
  public @NotNull String getRunnerId() {
    return "Perl Debugger";
  }

  @Override
  public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
    return executorId.equals(DefaultDebugExecutor.EXECUTOR_ID) && profile instanceof PerlDebuggableRunConfiguration;
  }

  @Override
  public void execute(@NotNull ExecutionEnvironment env) throws ExecutionException {
    FileDocumentManager.getInstance().saveAllDocuments();
    ExecutionManager.getInstance(env.getProject()).startRunProfile(env, state -> {
      XDebugSession xDebugSession = XDebuggerManager.getInstance(env.getProject()).startSession(env, new XDebugProcessStarter() {
        @Override
        public @NotNull XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException {
          return ((PerlDebuggableRunConfiguration)env.getRunProfile())
            .createDebugProcess(((PerlDebuggableRunConfiguration)env.getRunProfile()).computeDebugAddress(null), session, null, env);
        }
      });
      return xDebugSession.getRunContentDescriptor();
    });
  }
}
