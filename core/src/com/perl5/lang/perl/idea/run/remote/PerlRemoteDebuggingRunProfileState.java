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

import java.io.File;
import java.util.Objects;

/**
 * Created by hurricup on 09.05.2016.
 */
public class PerlRemoteDebuggingRunProfileState extends PerlDebugProfileState {
  @NotNull
  private final Project myProject;
  @NotNull
  private final File myLocalProjectPath;
  @NotNull
  private final File myRemoteProjectPath;

  public PerlRemoteDebuggingRunProfileState(ExecutionEnvironment environment) {
    super(environment);
    PerlRemoteDebuggingConfiguration debuggingConfiguration = (PerlRemoteDebuggingConfiguration)environment.getRunProfile();
    myProject = environment.getProject();
    String projectPath = myProject.getBaseDir().getCanonicalPath();
    myLocalProjectPath = new File(projectPath == null ? "" : projectPath);
    myRemoteProjectPath = new File(debuggingConfiguration.getRemoteProjectRoot());
  }

  @NotNull
  @Override
  public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner) {
    return new DefaultExecutionResult(TextConsoleBuilderFactory.getInstance().createBuilder(myProject).getConsole(),
                                      new DefaultDebugProcessHandler());
  }

  @Override
  public String mapPathToRemote(String localPathName) {
    File localPath = new File(localPathName);
    if (FileUtil.isAncestor(myLocalProjectPath, localPath, false)) {
      return FileUtil.toSystemIndependentName(
        new File(myRemoteProjectPath, Objects.requireNonNull(FileUtil.getRelativePath(myLocalProjectPath, localPath))).getPath()
      );
    }
    else {
      return localPathName;
    }
  }

  @NotNull
  @Override
  public String mapPathToLocal(String remotePathName) {
    File remotePath = new File(remotePathName);
    if (FileUtil.isAncestor(myRemoteProjectPath, remotePath, false)) {
      return FileUtil.toSystemDependentName(
        new File(myLocalProjectPath, Objects.requireNonNull(FileUtil.getRelativePath(myRemoteProjectPath, remotePath))).getPath()
      );
    }
    else {
      return remotePathName;
    }
  }
}
