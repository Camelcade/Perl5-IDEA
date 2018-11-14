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
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.AbstractPerlData;
import com.perl5.lang.perl.idea.sdk.PerlSdkAdditionalData;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import com.perl5.lang.perl.util.PerlPluginUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
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
   * @param disposable if filesystem is temporary, it may be bound by this disposable
   */
  @Nullable
  public abstract VirtualFileSystem getFileSystem(@NotNull Disposable disposable);

  /**
   * Attempts to find a file at host
   */
  @Nullable
  public abstract File findFileByName(@NotNull String fileName);

  /**
   * Creates a process and process handler to be run in console.
   */
  @NotNull
  protected ProcessHandler doCreateConsoleProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return new KillableColoredProcessHandler(createConsoleProcess(commandLine), commandLine.getCommandLineString(),
                                             commandLine.getCharset());
  }

  /**
   * @return process created from {@code commandLine} to execute in console
   */
  @NotNull
  protected Process createConsoleProcess(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return createProcess(commandLine);
  }

  /**
   * Creates a process and process handler to be run in background.
   */
  @NotNull
  protected BaseProcessHandler doCreateProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return new KillableProcessHandler(createProcess(commandLine), commandLine.getCommandLineString(), commandLine.getCharset());
  }

  /***
   * @return process created from {@code commandLine} to execute directly
   */
  @NotNull
  protected abstract Process createProcess(@NotNull PerlCommandLine commandLine) throws ExecutionException;

  /**
   * @return a path on local machine to the file identified by {@code remotePath}
   */
  @Nullable
  protected abstract String doGetLocalPath(@NotNull String remotePath);

  @Contract("null->null")
  @Nullable
  public final String getLocalPath(@Nullable String remotePathname) {
    if (remotePathname == null) {
      return null;
    }

    File remotePath = new File(remotePathname);
    File remoteHelpersPath = new File(getHelpersRootPath());
    if (FileUtil.isAncestor(remoteHelpersPath, remotePath, false)) {
      return new File(
        PerlPluginUtil.getPluginHelpersRoot(), Objects.requireNonNull(FileUtil.getRelativePath(remoteHelpersPath, remotePath))).getPath();
    }

    String localPath = doGetLocalPath(remotePathname);
    if (localPath == null) {
      LOG.warn("Unable to map remote to local " + remotePathname + " for " + this);
    }
    return localPath;
  }

  /**
   * @return file transfer object, responsible for syncing files and helpers
   */
  @NotNull
  public abstract PerlHostFileTransfer<Data> getFileTransfer();

  @Contract("null->null")
  @Nullable
  public final File getLocalPath(@Nullable File remotePath) {
    if (remotePath == null) {
      return null;
    }
    String localPath = getLocalPath(remotePath.getPath());
    return localPath == null ? null : new File(localPath);
  }

  /**
   * @return a path on remote machine to the file identified by {@code localPath}
   * @apiNote this method invoked after checking that file is not under helpers root and not cached copy of remote files
   */
  @Nullable
  protected abstract String doGetRemotePath(@NotNull String localPath);

  @Contract("null->null")
  @Nullable
  public final String getRemotePath(@Nullable String localPathName) {
    if (localPathName == null) {
      return null;
    }

    // checking helpers
    File localPathFile = new File(localPathName);
    File localHelpersPath = new File(PerlPluginUtil.getPluginHelpersRoot());
    if (FileUtil.isAncestor(localHelpersPath, localPathFile, false)) {
      return FileUtil.toSystemIndependentName(
        new File(getHelpersRootPath(), Objects.requireNonNull(FileUtil.getRelativePath(localHelpersPath, localPathFile))).getPath());
    }

    // checking local cache if any
    String localCacheRoot = getLocalCacheRoot();
    if (localCacheRoot != null) {
      File localCacheFile = new File(localCacheRoot);
      if (FileUtil.isAncestor(localCacheFile, localPathFile, false)) {
        return FileUtil.toSystemIndependentName("/" + FileUtil.getRelativePath(localCacheFile, localPathFile));
      }
    }

    // mapping with host data
    String remotePath = doGetRemotePath(localPathName);
    if (remotePath == null) {
      LOG.warn("Unable to map local to remote " + localPathName + " for " + this);
    }
    return remotePath;
  }

  @Contract("null->null")
  @Nullable
  public final File getRemotePath(@Nullable File localPath) {
    if (localPath == null) {
      return null;
    }
    String remotePath = getRemotePath(localPath.getPath());
    return remotePath == null ? null : new File(remotePath);
  }

  /**
   * @return a path to a local cache of the remote files or null if we have a direct access to the FS   *
   */
  @Nullable
  public abstract String getLocalCacheRoot();

  /**
   * @return path to the helpers root on the target host
   * @see PerlPluginUtil#getPluginHelpersRoot()
   */
  @NotNull
  public abstract String getHelpersRootPath();

  @Override
  public String toString() {
    return getShortName();
  }

  @NotNull
  public static ProcessOutput execAndGetOutput(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return getOutput(createProcessHandler(commandLine));
  }

  public static ProcessOutput getOutput(@NotNull ProcessHandler processHandler) {
    CapturingProcessAdapter adapter = new CapturingProcessAdapter();
    processHandler.addProcessListener(adapter);
    processHandler.startNotify();
    processHandler.waitFor();
    return adapter.getOutput();
  }

  @NotNull
  public static BaseProcessHandler createProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    PerlVersionManagerData versionManagerData = commandLine.getEffectiveVersionManagerData();
    if (versionManagerData != null) {
      commandLine = versionManagerData.patchCommandLine(commandLine);
    }

    PerlHostData perlHostData = commandLine.getEffectiveHostData();
    if (perlHostData == null) {
      throw new ExecutionException("No host data in " + commandLine);
    }
    BaseProcessHandler processHandler = perlHostData.doCreateProcessHandler(commandLine);
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

