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
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ex.ActionManagerEx;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.update.MergingUpdateQueue;
import com.intellij.util.ui.update.Update;
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
import java.util.List;
import java.util.Objects;

class PerlWslData extends PerlHostData<PerlWslData, PerlWslHandler> {
  private static final MergingUpdateQueue NOTIFICATIONS_QUEUE = new MergingUpdateQueue(
    "notifications.queue", 3000, true, null);

  private static final Logger LOG = Logger.getInstance(PerlWslData.class);
  private static final int TIMEOUT = 10000;

  @Attribute("distribution-id")
  private String myDistributionId;

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

  @Nullable
  @Override
  public String getSecondaryShortName() {
    return "[" + myDistributionId.toLowerCase() + "]";
  }

  @Nullable
  public PerlWslFileSystem getFileSystem() {
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      LOG.error(PerlBundle.message("perl.host.handler.distribution.unavailable", myDistributionId));
      return null;
    }
    return PerlWslFileSystem.getOrCreate(distribution);
  }

  @Nullable
  WSLDistributionWithRoot getDistribution() {
    return ObjectUtils.doIfNotNull(WSLUtil.getDistributionById(getDistributionId()), WSLDistributionWithRoot::new);
  }

  @Override
  protected void doSyncHelpers() {
  }

  @NotNull
  @Override
  public String getHelpersRootPath() {
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      throw new RuntimeException("No distribution for " + myDistributionId);
    }
    return Objects.requireNonNull(distribution.getWslPath(PerlPluginUtil.getPluginHelpersRoot()));
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
  public String doGetLocalPath(@NotNull String remotePathName) {
    String windowsPath = WSLUtil.getWindowsPath(remotePathName);
    return windowsPath != null ? windowsPath : FileUtil.toSystemDependentName(new File(getLocalCacheRoot(), remotePathName).getPath());
  }

  @Nullable
  @Override
  public String doGetRemotePath(@NotNull String localPathName) {
    File cachePath = new File(getLocalCacheRoot());
    File localPath = new File(localPathName);
    if (FileUtil.isAncestor(cachePath, localPath, false)) {
      return FileUtil.toSystemIndependentName("/" + FileUtil.getRelativePath(cachePath, localPath));
    }
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      LOG.error(PerlBundle.message("perl.host.handler.distribution.unavailable", myDistributionId));
      return null;
    }
    return distribution.getWslPath(localPathName);
  }

  @NotNull
  @Override
  protected String doSyncPath(@NotNull String remotePath) {
    String localPath = getLocalPath(remotePath);
    if (localPath == null) {
      throw new RuntimeException("Unable to compute local path for " + remotePath);
    }
    WSLDistributionWithRoot distribution = getDistribution();
    if (distribution == null) {
      LOG.error("No distribution available for " + myDistributionId);
      return localPath;
    }
    remotePath = FileUtil.toSystemIndependentName(remotePath);

    LOG.info("Syncing " + myDistributionId + ": " + remotePath + " => " + localPath);
    try {
      ProcessOutput output = distribution.copyFromWsl(
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
      int exitCode = output.getExitCode();
      if (exitCode == 0) {
        return localPath;
      }

      LOG.warn("Error while copying: " + remotePath + "; Exit code: " + exitCode + "; Stderr: " + output.getStderr());
      if (exitCode == 127) {
        NOTIFICATIONS_QUEUE.queue(Update.create(this, () -> {
          AnAction action = ActionManagerEx.getInstanceEx().getAction("perl5.sync.interpreter");
          Notification notification = new Notification(
            PerlBundle.message("perl.host.handler.wsl.notification.group"),
            PerlBundle.message("perl.host.handler.wsl.missing.rsync.title"),
            PerlBundle.message("perl.host.handler.wsl.missing.rsync.message"),
            NotificationType.ERROR
          );
          Notifications.Bus.notify(notification.addAction(
            new DumbAwareAction(action.getTemplatePresentation().getText()) {
              @Override
              public void actionPerformed(@NotNull AnActionEvent e) {
                notification.expire();
                action.update(e);
                if (e.getPresentation().isEnabled()) {
                  action.actionPerformed(e);
                }
              }
            }));
        }));
      }
    }
    catch (ExecutionException e) {
      LOG.error(e);
    }
    return localPath;
  }

  @Nullable
  @Override
  public File findFileByName(@NotNull String fileName) {
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

  @NotNull
  @Override
  protected Process createProcess(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    return patchCommandLine(commandLine).createProcess();
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
  protected PerlWslData self() {
    return this;
  }
}
