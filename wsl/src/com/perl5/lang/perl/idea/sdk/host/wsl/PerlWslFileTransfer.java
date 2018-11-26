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
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.execution.wsl.WSLDistributionWithRoot;
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
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.update.MergingUpdateQueue;
import com.intellij.util.ui.update.Update;
import com.perl5.lang.perl.idea.sdk.host.PerlHostFileTransfer;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

class PerlWslFileTransfer extends PerlHostFileTransfer<PerlWslData> {
  private static final Logger LOG = Logger.getInstance(PerlWslFileTransfer.class);

  private static final MergingUpdateQueue NOTIFICATIONS_QUEUE = new MergingUpdateQueue(
    "notifications.queue", 3000, true, null);

  public PerlWslFileTransfer(@NotNull PerlWslData hostData) {
    super(hostData);
  }

  @Override
  protected void doSyncPath(@NotNull String remotePath, String localPath) throws IOException {
    WSLDistributionWithRoot distribution = myHostData.getDistribution();
    if (distribution == null) {
      throw new IOException("No distribution available for " + myHostData.getDistributionId());
    }
    remotePath = FileUtil.toSystemIndependentName(remotePath);

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
        return;
      }

      LOG.warn("Error while copying: " + remotePath + "; Exit code: " + exitCode + "; Stderr: " + output.getStderr());
      if (exitCode == 127) {
        NOTIFICATIONS_QUEUE.queue(Update.create(this, () -> {
          AnAction action = ActionManagerEx.getInstanceEx().getAction("perl5.sync.interpreter");
          Notification notification = new Notification(
            PerlWslBundle.message("perl.host.handler.wsl.notification.group"),
            PerlWslBundle.message("perl.host.handler.wsl.missing.rsync.title"),
            PerlWslBundle.message("perl.host.handler.wsl.missing.rsync.message"),
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
      throw new IOException(e);
    }
  }

  @Override
  protected void doSyncHelpers() {
  }

  @Override
  public void close() throws IOException {

  }
}
