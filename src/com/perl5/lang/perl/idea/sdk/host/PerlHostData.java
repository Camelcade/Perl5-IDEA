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

package com.perl5.lang.perl.idea.sdk.host;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.AbstractPerlData;
import com.perl5.lang.perl.idea.sdk.PerlSdkAdditionalData;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Contains data necessary to access a perl host. E.g. local, wsl, ssh, docker
 */
public abstract class PerlHostData<Data extends PerlHostData<Data, Handler>, Handler extends PerlHostHandler<Data, Handler>>
  extends AbstractPerlData<Data, Handler> {
  private static final Logger LOG = Logger.getInstance(PerlHostData.class);

  public PerlHostData(@NotNull Handler handler) {
    super(handler);
  }

  /**
   * @return an OS handler for this host
   */
  @NotNull
  public abstract PerlOsHandler getOsHandler();

  /**
   * @return short lowercased name, for interpreters list
   */
  @NotNull
  public final String getShortName() {
    String secondaryShortName = getSecondaryShortName();
    return secondaryShortName == null ? getPrimaryShortName() : getPrimaryShortName() + getSecondaryShortName();
  }

  /**
   * @return primary part of short name, e.g. 'docker'
   */
  @NotNull
  public final String getPrimaryShortName() {
    return getHandler().getShortName();
  }

  /**
   * @return optional secondary short name, e.g. '[docker_image_name]'
   */
  @Nullable
  public abstract String getSecondaryShortName();

  /**
   * @return a filesystem for this host if available
   */
  @Nullable
  public abstract VirtualFileSystem getFileSystem();

  /**
   * Attempts to find a file at host
   */
  @Nullable
  public abstract Path findFileByName(@NotNull String fileName);

  /**
   * Creates a process and process handler to be run in console.
   */
  @NotNull
  protected abstract ProcessHandler doCreateConsoleProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException;

  /**
   * Creates a process and process handler to be run in background.
   */
  @NotNull
  protected abstract CapturingProcessHandler doCreateProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException;

  /**
   * @return a path on local machine to the file identified by {@code remotePath}
   */
  @Contract("null->null; !null->!null")
  @Nullable
  public abstract String getLocalPath(@Nullable String remotePath);

  @Contract("null->null; !null->!null")
  @Nullable
  public final Path getLocalPath(@Nullable Path remotePath) {
    return remotePath == null ? null : Paths.get(getLocalPath(remotePath.toString()));
  }

  /**
   * @return a path on remote machine to the file identified by {@code localPath}
   */
  @Contract("null->null; !null->!null")
  @Nullable
  public abstract String getRemotePath(@Nullable String localPath);

  @Contract("null->null; !null->!null")
  @Nullable
  public final Path getRemotePath(@Nullable Path localPath) {
    return localPath == null ? null : Paths.get(getRemotePath(localPath.toString()));
  }

  /**
   * @return a path to a local cache of the remote files or null if we have a direct access to the FS   *
   */
  @Nullable
  public abstract String getLocalCacheRoot();

  /**
   * synchronizes {@code remotePath} with local cache
   *
   * @implSpec should check that we are not on edt
   */
  public abstract void syncPath(@Nullable String remotePath);

  public final void syncPath(@Nullable Path remotePath) {
    if (remotePath == null) {
      return;
    }
    syncPath(remotePath.toString());
  }

  @Override
  public String toString() {
    return getShortName();
  }

  @NotNull
  public static ProcessOutput execAndGetOutput(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return createProcessHandler(commandLine).runProcess();
  }

  @NotNull
  public static CapturingProcessHandler createProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    PerlVersionManagerData versionManagerData = commandLine.getEffectiveVersionManagerData();
    if (versionManagerData != null) {
      commandLine = versionManagerData.patchCommandLine(commandLine);
    }

    PerlHostData perlHostData = commandLine.getEffectiveHostData();
    if (perlHostData == null) {
      throw new ExecutionException("No host data in " + commandLine);
    }
    CapturingProcessHandler processHandler = perlHostData.doCreateProcessHandler(commandLine);
    commandLine.getProcessListeners().forEach(processHandler::addProcessListener);
    PerlRunUtil.addMissingPackageListener(processHandler, commandLine);
    return processHandler;
  }

  /**
   * @return stream of existing sdks from this host
   */
  @NotNull
  public final Stream<Sdk> getHostSdkStream() {
    return PerlSdkTable.getInstance().getInterpreters().stream().filter(it -> this.equals(PerlHostData.from(it)));
  }

  @Contract("null->null")
  @Nullable
  public static PerlHostData from(@Nullable Sdk sdk) {
    return ObjectUtils.doIfNotNull(PerlSdkAdditionalData.from(sdk), PerlSdkAdditionalData::getHostData);
  }

  @NotNull
  public static PerlHostData notNullFrom(@NotNull Sdk sdk) {
    return Objects.requireNonNull(from(sdk), () -> "No host data in " + sdk);
  }

  @NotNull
  public static ProcessHandler createConsoleProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    PerlHostData hostData = commandLine.getEffectiveHostData();
    if (hostData == null) {
      throw new ExecutionException("No host data in the command line " + commandLine);
    }
    PerlVersionManagerData versionManagerData = commandLine.getEffectiveVersionManagerData();
    if (versionManagerData != null) {
      commandLine = versionManagerData.patchCommandLine(commandLine);
    }
    final Map<String, String> environment = commandLine.getEnvironment();
    ProcessHandler handler = hostData.doCreateConsoleProcessHandler(commandLine.withPty(true));
    commandLine.getProcessListeners().forEach(handler::addProcessListener);
    handler.addProcessListener(new ProcessAdapter() {
      @Override
      public void startNotified(@NotNull ProcessEvent event) {
        String perl5Opt = environment.get(PerlRunUtil.PERL5OPT);
        if (StringUtil.isNotEmpty(perl5Opt)) {
          handler.notifyTextAvailable(" - " + PerlRunUtil.PERL5OPT + "=" + perl5Opt + '\n', ProcessOutputTypes.SYSTEM);
        }
      }
    });

    PerlRunUtil.addMissingPackageListener(handler, commandLine);
    ProcessTerminatedListener.attach(handler, commandLine.getEffectiveProject());

    return handler;
  }
}

