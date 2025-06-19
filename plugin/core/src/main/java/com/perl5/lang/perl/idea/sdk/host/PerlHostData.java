/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.util.ObjectUtils;
import com.intellij.util.io.BaseDataReader;
import com.intellij.util.io.BaseOutputReader;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.AbstractPerlData;
import com.perl5.lang.perl.idea.sdk.PerlConfig;
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
  public abstract @NotNull PerlOsHandler getOsHandler();

  /**
   * @return short lowercased name, for interpreters list
   */
  public final @NotNull String getShortName() {
    String secondaryShortName = getSecondaryShortName();
    return secondaryShortName == null ? getPrimaryShortName() : getPrimaryShortName() + getSecondaryShortName();
  }

  /**
   * @return primary part of short name, e.g. 'docker'
   */
  public final @NotNull String getPrimaryShortName() {
    return getHandler().getShortName();
  }

  /**
   * @return optional secondary short name, e.g. '[docker_image_name]'
   */
  public abstract @Nullable String getSecondaryShortName();

  /**
   * @param disposable if filesystem is temporary, it may be bound by this disposable
   * @return a filesystem for this host if available
   */
  public abstract @Nullable VirtualFileSystem getFileSystem(@NotNull Disposable disposable);

  /**
   * Attempts to find a file at host
   */
  public abstract @Nullable File findFileByName(@NotNull String fileName);

  /**
   * Creates a process and process handler to be run in console.
   */
  protected @NotNull ProcessHandler doCreateConsoleProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return new KillableProcessHandler(
      createConsoleProcess(commandLine), commandLine.getCommandLineString(), commandLine.getCharset()) {
      @Override
      protected @NotNull BaseOutputReader.Options readerOptions() {
        return new BaseOutputReader.Options() {
          @Override
          public BaseDataReader.SleepingPolicy policy() {
            return BaseDataReader.SleepingPolicy.BLOCKING;
          }

          @Override
          public boolean splitToLines() {
            return false;
          }
        };
      }
    };
  }

  /**
   * @return process created from {@code commandLine} to execute in console
   */
  protected @NotNull Process createConsoleProcess(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return createProcess(commandLine);
  }

  /**
   * Creates a process and process handler to be run in background.
   */
  protected @NotNull BaseProcessHandler<?> doCreateProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return new KillableProcessHandler(createProcess(commandLine), commandLine.getCommandLineString(), commandLine.getCharset());
  }

  /***
   * @return process created from {@code commandLine} to execute directly
   */
  protected abstract @NotNull Process createProcess(@NotNull PerlCommandLine commandLine) throws ExecutionException;

  /**
   * @return a path on local machine to the file identified by {@code remotePath}
   */
  protected abstract @Nullable String doGetLocalPath(@NotNull String remotePath);

  @Contract("null->null")
  public final @Nullable String getLocalPath(@Nullable String remotePathname) {
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
  public abstract @NotNull PerlHostFileTransfer<Data> getFileTransfer();

  @Contract("null->null")
  public final @Nullable File getLocalPath(@Nullable File remotePath) {
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
  protected abstract @Nullable String doGetRemotePath(@NotNull String localPath);

  @Contract("null->null;!null->!null")
  public final @Nullable String getRemotePath(@Nullable String localPathName) {
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
  public final @Nullable File getRemotePath(@Nullable File localPath) {
    return localPath == null ? null : new File(getRemotePath(localPath.getPath()));
  }

  /**
   * @return a path to a local cache of the remote files or null if we have a direct access to the FS   *
   */
  public abstract @Nullable String getLocalCacheRoot();

  /**
   * On unix systems updates permissions/ownership on {@code localPath} exposed to the remote (e.g. mount in docker) to be consistent
   * with current users.
   */
  public abstract void fixPermissionsRecursively(@NotNull String localPath,
                                                 @Nullable Project project) throws ExecutionException;

  /**
   * @return path to the helpers root on the target host
   * @see PerlPluginUtil#getPluginHelpersRoot()
   */
  public abstract @NotNull String getHelpersRootPath();

  /**
   * @return passed {@code remotePath} with {@code ~} expanded to the real home path.
   */
  public abstract @NotNull String expandUserHome(@NotNull String remotePath);

  @Override
  public String toString() {
    return getShortName();
  }

  public static @NotNull ProcessOutput execAndGetOutput(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return getOutput(createProcessHandler(commandLine));
  }

  public static ProcessOutput getOutput(@NotNull ProcessHandler processHandler) {
    CapturingProcessAdapter adapter = new CapturingProcessAdapter();
    processHandler.addProcessListener(adapter);
    processHandler.startNotify();
    processHandler.waitFor();
    return adapter.getOutput();
  }

  public static @NotNull BaseProcessHandler<?> createProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    PerlConfig.init(commandLine.getEffectiveSdk());

    PerlVersionManagerData<?, ?> versionManagerData = commandLine.getEffectiveVersionManagerData();
    if (versionManagerData != null) {
      commandLine = versionManagerData.patchCommandLine(commandLine);
    }

    PerlHostData<?, ?> perlHostData = commandLine.getEffectiveHostData();
    if (perlHostData == null) {
      throw new ExecutionException(PerlBundle.message("dialog.message.no.host.data.in", commandLine));
    }
    BaseProcessHandler<?> processHandler = perlHostData.doCreateProcessHandler(commandLine);
    commandLine.getProcessListeners().forEach(processHandler::addProcessListener);
    PerlRunUtil.addMissingPackageListener(processHandler, commandLine);
    return processHandler;
  }

  /**
   * @return stream of existing sdks from this host
   */
  public final @NotNull Stream<Sdk> getHostSdkStream() {
    return PerlSdkTable.getInstance().getInterpreters().stream().filter(it -> this.equals(PerlHostData.from(it)));
  }

  @Contract("null->null")
  public static @Nullable PerlHostData<?, ?> from(@Nullable Sdk sdk) {
    return ObjectUtils.doIfNotNull(PerlSdkAdditionalData.from(sdk), PerlSdkAdditionalData::getHostData);
  }

  public static @NotNull PerlHostData<?, ?> notNullFrom(@NotNull Sdk sdk) {
    return PerlSdkAdditionalData.notNullFrom(sdk).getHostData();
  }

  public static @NotNull ProcessHandler createConsoleProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    PerlConfig.init(commandLine.getEffectiveSdk());
    PerlHostData<?, ?> hostData = commandLine.getEffectiveHostData();
    if (hostData == null) {
      throw new ExecutionException(PerlBundle.message("dialog.message.no.host.data.in.command.line", commandLine));
    }
    PerlVersionManagerData<?, ?> versionManagerData = commandLine.getEffectiveVersionManagerData();
    if (versionManagerData != null) {
      commandLine = versionManagerData.patchCommandLine(commandLine);
    }
    final Map<String, String> environment = commandLine.getEnvironment();
    ProcessHandler handler = hostData.doCreateConsoleProcessHandler(commandLine);
    commandLine.getProcessListeners().forEach(handler::addProcessListener);
    handler.addProcessListener(new ProcessListener() {
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

