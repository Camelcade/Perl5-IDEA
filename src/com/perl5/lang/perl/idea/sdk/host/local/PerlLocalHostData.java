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

package com.perl5.lang.perl.idea.sdk.host.local;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.execution.process.*;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

class PerlLocalHostData extends PerlHostData<PerlLocalHostData, PerlLocalHostHandler> {
  public PerlLocalHostData(@NotNull PerlLocalHostHandler handler) {
    super(handler);
  }

  @NotNull
  @Override
  public PerlOsHandler getOsHandler() {
    return getHandler().getOsHandler();
  }

  @NotNull
  @Override
  protected BaseProcessHandler doCreateProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return new KillableProcessHandler(commandLine);
  }

  @NotNull
  @Override
  protected ProcessHandler doCreateConsoleProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return new ColoredProcessHandler(commandLine);
  }

  @Nullable
  @Override
  public String getSecondaryShortName() {
    return null;
  }

  @Override
  protected void doSyncHelpers() {
  }

  @NotNull
  @Override
  public String getHelpersRootPath() {
    return PerlPluginUtil.getPluginHelpersRoot();
  }

  @Nullable
  @Override
  public VirtualFileSystem getFileSystem() {
    return LocalFileSystem.getInstance();
  }

  @Nullable
  @Override
  public File findFileByName(@NotNull String fileName) {
    return ContainerUtil.getFirstItem(PathEnvironmentVariableUtil.findAllExeFilesInPath(fileName));
  }

  @NotNull
  @Override
  public String doGetLocalPath(@NotNull String remotePath) {
    return remotePath;
  }

  @Nullable
  @Override
  public String getLocalCacheRoot() {
    return null;
  }

  @NotNull
  @Override
  public String doGetRemotePath(@NotNull String localPath) {
    return localPath;
  }

  @NotNull
  @Override
  protected String doSyncPath(@NotNull String remotePath) {
    return remotePath;
  }

  @NotNull
  @Override
  protected PerlLocalHostData self() {
    return this;
  }
}
