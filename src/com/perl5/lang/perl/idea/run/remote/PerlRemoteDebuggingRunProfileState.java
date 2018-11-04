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
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.xdebugger.DefaultDebugProcessHandler;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugProfileState;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by hurricup on 09.05.2016.
 */
public class PerlRemoteDebuggingRunProfileState extends PerlDebugProfileState {
  @NotNull
  private final Project myProject;
  @NotNull
  private final Path myLocalProjectPath;
  @NotNull
  private final Path myRemoteProjectPath;

  public PerlRemoteDebuggingRunProfileState(ExecutionEnvironment environment) {
    super(environment);
    PerlRemoteDebuggingConfiguration debuggingConfiguration = (PerlRemoteDebuggingConfiguration)environment.getRunProfile();
    myProject = environment.getProject();
    String projectPath = myProject.getBaseDir().getCanonicalPath();
    myLocalProjectPath = Paths.get(projectPath == null ? "" : projectPath);
    myRemoteProjectPath = Paths.get(debuggingConfiguration.getRemoteProjectRoot());
  }

  @NotNull
  @Override
  public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner) {
    return new DefaultExecutionResult(TextConsoleBuilderFactory.getInstance().createBuilder(myProject).getConsole(),
                                      new DefaultDebugProcessHandler());
  }

  @Override
  public String mapPathToRemote(String localPathName) {
    Path localPath = Paths.get(localPathName);
    if (localPath.startsWith(myLocalProjectPath)) {
      return FileUtil.toSystemIndependentName(
        myRemoteProjectPath.resolve(myLocalProjectPath.relativize(localPath)).toString()
      );
    }
    else {
      return localPathName;
    }
  }

  @NotNull
  @Override
  public String mapPathToLocal(String remotePathName) {
    Path remotePath = Paths.get(remotePathName);
    if (remotePath.startsWith(myRemoteProjectPath)) {
      return FileUtil.toSystemDependentName(
        myLocalProjectPath.resolve(myRemoteProjectPath.relativize(remotePath)).toString()
      );
    }
    else {
      return remotePathName;
    }
  }
}
