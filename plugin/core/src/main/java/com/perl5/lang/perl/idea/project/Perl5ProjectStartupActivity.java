/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.project;

import com.intellij.ide.startup.ServiceNotReadyException;
import com.intellij.notification.BrowseNotificationAction;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.util.FileContentUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.PerlApplicationSettings;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.jetbrains.annotations.NotNull;


public class Perl5ProjectStartupActivity implements StartupActivity {
  private static final Logger LOG = Logger.getInstance(Perl5ProjectStartupActivity.class);

  @Override
  public void runActivity(@NotNull Project project) {
    if (project.isDefault()) {
      return;
    }
    PerlApplicationSettings settings = PerlApplicationSettings.getInstance();
    if (settings.shouldShowAnnounce()) {
      StartupManager.getInstance(project).runAfterOpened(() -> {
        settings.setAnnounceShown();
        Notification notification = new Notification(
          "perl5.plugin.update.notification.group",
          PerlBundle.message("plugin.update.baloon.title", PerlPluginUtil.getPluginVersion()),
          PerlBundle.message("plugin.update.baloon.text"),
          NotificationType.INFORMATION
        ).setImportant(true)
          .addAction(
            new BrowseNotificationAction(PerlBundle.message("plugin.update.baloon.changes"), "https://plugins.jetbrains.com/plugin/7796"))
          .addAction(new BrowseNotificationAction(PerlBundle.message("plugin.update.baloon.tracker"),
                                                  "https://github.com/hurricup/Perl5-IDEA/issues"))
          .addAction(new BrowseNotificationAction(PerlBundle.message("plugin.update.baloon.support"),
                                                  "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=HJCUADZKY5G7E"))
          .addAction(new BrowseNotificationAction(PerlBundle.message("plugin.update.baloon.support.ym"),
                                                  "https://money.yandex.ru/to/41001227135087"));

        Notifications.Bus.notify(notification);
      });
    }
    StartupManager.getInstance(project).runAfterOpened(() -> scheduleNamesUpdateWithReparse(project));
  }

  private static void scheduleNamesUpdateWithReparse(@NotNull Project project) {
    ApplicationManager.getApplication().executeOnPooledThread(() -> initNamesWithReparse(project));
  }


  private static void initNamesWithReparse(@NotNull Project project) {
    if (project.isDisposed()) {
      return;
    }
    try {
      PerlNamesCache.getInstance(project).forceCacheUpdate();
      ApplicationManager.getApplication().invokeLater(FileContentUtil::reparseOpenedFiles);
    }
    catch (ServiceNotReadyException e) {
      LOG.warn(e);
      DumbService.getInstance(project).smartInvokeLater(() -> scheduleNamesUpdateWithReparse(project));
    }
  }
}
