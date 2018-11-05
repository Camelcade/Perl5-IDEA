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

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.util.PerlPluginUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.stream.Collectors;

public class PerlHostCacheCleaner implements StartupActivity, DumbAware {
  private static final Logger LOG = Logger.getInstance(PerlHostCacheCleaner.class);

  @Override
  public void runActivity(@NotNull Project project) {
    if (ApplicationManager.getApplication().isDispatchThread()) {
      LOG.error("This supposed to be invoked from pooled thread");
      return;
    }
    VirtualFile cacheRoot = VfsUtil.findFileByIoFile(new File(PerlPluginUtil.getRemotesCachePath()), true);
    VirtualFile[] cacheDirs = cacheRoot == null ? null : cacheRoot.getChildren();
    if (cacheDirs == null || cacheDirs.length == 0) {
      return;
    }
    HashSet<VirtualFile> existingCaches = ContainerUtil.newHashSet(cacheDirs);
    PerlSdkTable.getInstance().getInterpreters().forEach(it -> {
      String sdkCacheRoot = PerlHostData.notNullFrom(it).getLocalCacheRoot();
      if (StringUtil.isEmpty(sdkCacheRoot)) {
        return;
      }
      existingCaches.remove(VfsUtil.findFileByIoFile(new File(sdkCacheRoot), true));
    });
    if (existingCaches.isEmpty()) {
      return;
    }
    Notification notification = new Notification(
      PerlBundle.message("perl.cache.cleaner.notification.group"),
      PerlBundle.message("perl.cache.cleaner.notification.title"),
      PerlBundle.message(
        "perl.cache.cleaner.notification.message",
        existingCaches.stream().map(it -> "<li>" + it.getName() + "</li>").sorted().collect(Collectors.joining(""))),
      NotificationType.WARNING
    );

    notification.addAction(new DumbAwareAction(PerlBundle.message("perl.cache.cleaner.action.clean")) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent event) {
        new Task.Backgroundable(project, PerlBundle.message("perl.cache.cleaner.cleaning"), true) {
          @Override
          public void run(@NotNull ProgressIndicator indicator) {
            notification.expire();
            existingCaches.forEach(it -> {
              indicator.checkCanceled();
              try {
                PerlRunUtil.setProgressText(PerlBundle.message(
                  "perl.cache.cleaner.deleting",
                  StringUtil.shortenPathWithEllipsis(FileUtil.toSystemDependentName(it.getPath()), 50)));
                WriteAction.runAndWait(() -> it.delete(this));
              }
              catch (IOException e) {
                LOG.error(e);
              }
            });
          }
        }.queue();
      }
    });

    Notifications.Bus.notify(notification, project);
  }
}
