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

package com.perl5.lang.perl.debugger.run.run.debugger.remote;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.xdebugger.DefaultDebugProcessHandler;
import com.perl5.lang.perl.debugger.run.run.debugger.PerlDebugProfileStateBase;
import com.perl5.lang.perl.idea.run.PerlRunConsole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Objects;

/**
 * For attaching to the remote process
 */
public class PerlRemoteDebuggingRunProfileState extends PerlDebugProfileStateBase {
  private final @NotNull File myLocalProjectPath;
  private final @NotNull File myRemoteProjectPath;

  public PerlRemoteDebuggingRunProfileState(ExecutionEnvironment environment) {
    super(environment);
    PerlRemoteDebuggingConfiguration debuggingConfiguration = (PerlRemoteDebuggingConfiguration)environment.getRunProfile();
    Project project = environment.getProject();
    String projectPath = project.getBaseDir().getCanonicalPath();
    myLocalProjectPath = new File(projectPath == null ? "" : projectPath);
    myRemoteProjectPath = new File(debuggingConfiguration.getRemoteProjectRoot());
  }

  @Override
  public @NotNull ExecutionResult execute(@NotNull Executor executor) throws ExecutionException {
    return new DefaultExecutionResult(createConsole(executor), markAsReady(new DefaultDebugProcessHandler()));
  }

  @Override
  public String mapPathToRemote(@NotNull String localPathName) {
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

  @SuppressWarnings("RedundantThrows")
  @Override
  protected @Nullable ConsoleView createConsole(@NotNull Executor executor) throws ExecutionException {
    return new PerlRunConsole(getEnvironment().getProject());
  }

  @Override
  public @NotNull String mapPathToLocal(@NotNull String remotePathName) {
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
