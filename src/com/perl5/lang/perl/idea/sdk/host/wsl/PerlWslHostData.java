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

package com.perl5.lang.perl.idea.sdk.host.wsl;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.*;
import com.intellij.execution.wsl.WSLDistributionWithRoot;
import com.intellij.execution.wsl.WSLUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xmlb.annotations.Attribute;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.util.PerlPluginUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class PerlWslHostData extends PerlHostData<PerlWslHostData, PerlWslHostHandler> {
  private static final Logger LOG = Logger.getInstance(PerlWslHostData.class);
  private static final int TIMEOUT = 10000;

  @Attribute("distribution-id")
  private String myDistributionId;

  public PerlWslHostData(@NotNull PerlWslHostHandler handler) {
    super(handler);
  }

  @NotNull
  String getDistributionId() {
    return Objects.requireNonNull(myDistributionId);
  }

  void setDistributionId(String distributionId) {
    myDistributionId = distributionId;
  }

  @NotNull
  @Override
  public String getShortName() {
    return super.getShortName() + "[" + myDistributionId + "]";
  }

  @Nullable
  public WslFileSystem getFileSystem() {
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      LOG.error(PerlBundle.message("perl.host.handler.distribution.unavailable", myDistributionId));
      return null;
    }
    return WslFileSystem.getOrCreate(distribution);
  }

  @Nullable
  WSLDistributionWithRoot getDistribution() {
    return ObjectUtils.doIfNotNull(WSLUtil.getDistributionById(getDistributionId()), WSLDistributionWithRoot::new);
  }

  @NotNull
  @Override
  public PerlOsHandler getOsHandler() {
    return getHandler().getOsHandler();
  }

  @NotNull
  @Override
  public String getLocalCacheRoot() {
    String cachesPath = PerlPluginUtil.getRemotesCachePath();
    File cacheRoot = new File(cachesPath, "wsl_" + getDistributionId());
    FileUtil.createDirectory(cacheRoot);
    return cacheRoot.getAbsolutePath();
  }

  @Nullable
  @Override
  public String getLocalPath(@Nullable String remotePathName) {
    if (remotePathName == null) {
      return null;
    }
    String windowsPath = WSLUtil.getWindowsPath(remotePathName);
    return windowsPath != null ? windowsPath : FileUtil.join(getLocalCacheRoot(), remotePathName);
  }

  @Nullable
  @Override
  public String getRemotePath(@Nullable String localPathName) {
    if (localPathName == null) {
      return null;
    }
    Path cachePath = Paths.get(getLocalCacheRoot());
    Path localPath = Paths.get(localPathName);
    if (localPath.startsWith(cachePath)) {
      return FileUtil.toSystemIndependentName("/" + cachePath.relativize(localPath));
    }
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      LOG.error(PerlBundle.message("perl.host.handler.distribution.unavailable", myDistributionId));
      return null;
    }
    return distribution.getWslPath(localPathName);
  }

  @Override
  public void syncPath(@Nullable String remotePath) {
    if (remotePath == null) {
      return;
    }
    if (ApplicationManager.getApplication().isDispatchThread()) {
      throw new RuntimeException("Should not be invoked from EDT");
    }
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      LOG.error("No distribution available for " + myDistributionId);
      return;
    }
    remotePath = FileUtil.toSystemIndependentName(remotePath);

    String localPath = getLocalPath(remotePath);
    LOG.info("Syncing " + myDistributionId + ": " + remotePath + " => " + localPath);
    PerlRunUtil.setProgressText("Syncing: " + remotePath);
    try {
      distribution.copyFromWsl(
        remotePath, localPath, ContainerUtil.newArrayList("-v", "--exclude", "'*.so'", "--delete"),
        it -> it.addProcessListener(
          new ProcessAdapter() {
            @Override
            public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
              String text = event.getText();
              if (StringUtil.isNotEmpty(text)) {
                PerlRunUtil.setProgressText2(text);
              }
            }
          }));
    }
    catch (ExecutionException e) {
      LOG.error(e);
    }
  }

  @Nullable
  @Override
  public Path findFileByName(@NotNull String fileName) {
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      return null;
    }
    try {
      // fixme these commands should be handled by osHandler?
      ProcessOutput output = distribution.executeOnWsl(TIMEOUT, "bash", "-cl", "which " + fileName);
      List<String> lines = output.getStdoutLines();
      return lines.isEmpty() ? null : Paths.get(lines.get(0));
    }
    catch (ExecutionException e) {
      LOG.warn("Error looking for " + fileName, e);
      return null;
    }
  }

  @NotNull
  @Override
  protected ProcessHandler doCreateConsoleProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return new ColoredProcessHandler(patchCommandLine(commandLine));
  }

  @NotNull
  @Override
  protected CapturingProcessHandler doCreateProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return new CapturingProcessHandler(patchCommandLine(commandLine));
  }

  private PerlCommandLine patchCommandLine(@NotNull PerlCommandLine perlCommandLine) throws ExecutionException {
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      throw new ExecutionException(PerlBundle.message("perl.host.handler.distribution.unavailable", getDistributionId()));
    }
    String workingDir = ObjectUtils.doIfNotNull(perlCommandLine.getWorkDirectory(), File::toString);
    perlCommandLine.withWorkDirectory((String)null);
    return distribution.patchCommandLine(
      perlCommandLine,
      perlCommandLine.getEffectiveProject(),
      getRemotePath(workingDir),
      false);
  }

  @NotNull
  @Override
  protected PerlWslHostData self() {
    return this;
  }
}
