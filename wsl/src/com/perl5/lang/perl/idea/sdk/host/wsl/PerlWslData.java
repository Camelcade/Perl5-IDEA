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

package com.perl5.lang.perl.idea.sdk.host.wsl;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.execution.wsl.WSLDistributionWithRoot;
import com.intellij.execution.wsl.WSLUtil;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostFileTransfer;
import com.perl5.lang.perl.idea.sdk.host.PerlHostVirtualFileSystem;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

class PerlWslData extends PerlHostData<PerlWslData, PerlWslHandler> {
  private static final Logger LOG = Logger.getInstance(PerlWslData.class);
  private static final int TIMEOUT = 10000;

  @Attribute("distribution-id")
  private String myDistributionId;

  @Transient private @Nullable PerlWslFileSystem myFileSystem;

  @Transient
  private final PerlWslFileTransfer myFileTransfer = new PerlWslFileTransfer(this);

  public PerlWslData(@NotNull PerlWslHandler handler) {
    super(handler);
  }

  @NotNull
  String getDistributionId() {
    return Objects.requireNonNull(myDistributionId);
  }

  void setDistributionId(String distributionId) {
    myDistributionId = distributionId;
  }

  @Override
  public @Nullable String getSecondaryShortName() {
    return "[" + myDistributionId.toLowerCase() + "]";
  }

  @Override
  public synchronized @Nullable PerlHostVirtualFileSystem getFileSystem(@NotNull Disposable disposable) {
    if (myFileSystem == null) {
      WSLDistributionWithRoot distribution = getDistribution();
      if (distribution == null) {
        LOG.error(PerlWslBundle.message("perl.host.handler.distribution.unavailable", myDistributionId));
        return null;
      }
      myFileSystem = PerlWslFileSystem.create(distribution);
      Disposer.register(disposable, () -> {
        PerlWslFileSystem fileSystem = myFileSystem;
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
          if (fileSystem != null) {
            fileSystem.clean();
          }
        });
        myFileSystem = null;
      });
    }
    PerlHostVirtualFileSystem hostFileSystem = PerlHostVirtualFileSystem.getInstance();
    hostFileSystem.setDelegate(myFileSystem);
    return hostFileSystem;
  }

  @Nullable
  WSLDistributionWithRoot getDistribution() {
    return ObjectUtils.doIfNotNull(WSLUtil.getDistributionById(getDistributionId()), WSLDistributionWithRoot::new);
  }

  @NotNull
  WSLDistributionWithRoot getNotNullDistribution() throws IOException {
    WSLDistributionWithRoot distribution =
      ObjectUtils.doIfNotNull(WSLUtil.getDistributionById(getDistributionId()), WSLDistributionWithRoot::new);
    if (distribution != null) {
      return distribution;
    }
    throw new IOException("No distribution for " + myDistributionId);
  }

  @Override
  public @NotNull String getHelpersRootPath() {
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      throw new RuntimeException("No distribution for " + myDistributionId);
    }
    return Objects.requireNonNull(distribution.getWslPath(PerlPluginUtil.getPluginHelpersRoot()));
  }

  @Override
  public @NotNull PerlOsHandler getOsHandler() {
    return getHandler().getOsHandler();
  }

  @Override
  public @NotNull String getLocalCacheRoot() {
    String cachesPath = PerlPluginUtil.getRemotesCachePath();
    File cacheRoot = new File(cachesPath, "wsl_" + getDistributionId());
    FileUtil.createDirectory(cacheRoot);
    return cacheRoot.getAbsolutePath();
  }

  /**
   * @return true iff {@code remotePathName} is directly available in windows file system.
   * @implNote it should be returned by {@link #doGetLocalPath(String)} and we don't need to download it
   */
  boolean isFileDirectlyAvailable(@NotNull String remotePathName) {
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      return false;
    }
    return distribution.getWindowsPath(remotePathName) != null;
  }

  @Override
  public @Nullable String doGetLocalPath(@NotNull String remotePathName) {
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      LOG.warn("Distribution unavailable: " + myDistributionId);
      return null;
    }
    String windowsPath = distribution.getWindowsPath(remotePathName);
    return windowsPath != null ? windowsPath : FileUtil.toSystemDependentName(new File(getLocalCacheRoot(), remotePathName).getPath());
  }

  @Override
  public @Nullable String doGetRemotePath(@NotNull String localPathName) {
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      LOG.error(PerlWslBundle.message("perl.host.handler.distribution.unavailable", myDistributionId));
      return null;
    }
    return distribution.getWslPath(localPathName);
  }


  @Override
  public @Nullable File findFileByName(@NotNull String fileName) {
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      return null;
    }
    try {
      // fixme these commands should be handled by osHandler?
      ProcessOutput output = distribution.executeOnWsl(TIMEOUT, "bash", "-cl", "which " + fileName);
      List<String> lines = output.getStdoutLines();
      return lines.isEmpty() ? null : new File(lines.get(0));
    }
    catch (ExecutionException e) {
      LOG.warn("Error looking for " + fileName, e);
      return null;
    }
  }

  @Override
  protected @NotNull Process createProcess(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return patchCommandLine(commandLine).createProcess();
  }

  private PerlCommandLine patchCommandLine(@NotNull PerlCommandLine perlCommandLine) throws ExecutionException {
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      throw new ExecutionException(PerlWslBundle.message("perl.host.handler.distribution.unavailable", getDistributionId()));
    }
    String workingDir = ObjectUtils.doIfNotNull(perlCommandLine.getWorkDirectory(), File::toString);
    perlCommandLine.withWorkDirectory((String)null);
    return distribution.patchCommandLine(
      perlCommandLine,
      perlCommandLine.getEffectiveProject(),
      getRemotePath(workingDir),
      false);
  }

  @Override
  public @NotNull PerlHostFileTransfer<PerlWslData> getFileTransfer() {
    return myFileTransfer;
  }

  @Override
  protected @NotNull PerlWslData self() {
    return this;
  }
}
