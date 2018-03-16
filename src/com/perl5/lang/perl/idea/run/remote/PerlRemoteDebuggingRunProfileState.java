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

package com.perl5.lang.perl.idea.run.remote;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.project.Project;
import com.intellij.xdebugger.DefaultDebugProcessHandler;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugProfileState;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 09.05.2016.
 */
public class PerlRemoteDebuggingRunProfileState extends PerlDebugProfileState {
  private final PerlRemoteDebuggingConfiguration myDebuggingConfiguration;
  private final Project myProject;
  private final String myLocalProjectRoot;
  private final String myRemoteProjectRoot;

  public PerlRemoteDebuggingRunProfileState(ExecutionEnvironment environment) {
    super(environment);
    myDebuggingConfiguration = (PerlRemoteDebuggingConfiguration)environment.getRunProfile();
    myProject = environment.getProject();
    myLocalProjectRoot = myProject.getBaseDir().getCanonicalPath();
    myRemoteProjectRoot = myDebuggingConfiguration.getRemoteProjectRoot();
    //		System.err.println("Local project root: " + myLocalProjectRoot);
    //		System.err.println("Remote project root: " + myRemoteProjectRoot);
  }

  @NotNull
  @Override
  public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner) {
    return new DefaultExecutionResult(TextConsoleBuilderFactory.getInstance().createBuilder(myProject).getConsole(),
                                      new DefaultDebugProcessHandler());
  }

  @Override
  public String mapPathToRemote(String localPath) {
    if (localPath.startsWith(myLocalProjectRoot)) {
      return localPath.replace(myLocalProjectRoot, myRemoteProjectRoot);
      //			System.err.println("Mapped to remote: " + localPath + " => " + remotePath);
      //			return remotePath;
    }
    return super.mapPathToRemote(localPath);
  }

  @Override
  public String mapPathToLocal(String remotePath) {
    if (remotePath.startsWith(myRemoteProjectRoot)) {
      return remotePath.replace(myRemoteProjectRoot, myLocalProjectRoot);
      //			System.err.println("Mapped to local: " + remotePath + " => " + localPath);
      //			return localPath;
    }
    return super.mapPathToLocal(remotePath);
  }
}
