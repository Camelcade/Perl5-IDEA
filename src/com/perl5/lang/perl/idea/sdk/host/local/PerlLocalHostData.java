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
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ColoredProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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
  protected CapturingProcessHandler doCreateProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return new CapturingProcessHandler(commandLine);
  }

  @NotNull
  @Override
  protected ProcessHandler doCreateConsoleProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return new ColoredProcessHandler(commandLine.withPty(true));
  }

  @Override
  public boolean isFileExists(@Nullable Path path) {
    return path != null && path.toFile().exists();
  }

  @Override
  public boolean isDirectory(@Nullable Path path) {
    return path != null && path.toFile().isDirectory();
  }

  @Nullable
  @Override
  public Path suggestHomePath() {
    String perlPath = PerlRunUtil.getPathFromPerl(this);

    if (perlPath != null) {
      return Paths.get(perlPath);
    }

    return getOsHandler().getDefaultHomePath();
  }

  @Nullable
  @Override
  public Path findFile(@NotNull String fileName) {
    return PathEnvironmentVariableUtil.findAllExeFilesInPath(fileName).stream()
      .map(File::toPath)
      .findFirst()
      .orElse(null);
  }

  @Override
  protected PerlLocalHostData self() {
    return this;
  }

  @Override
  public String toString() {
    return getShortName();
  }
}
