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

package com.perl5.lang.perl.idea.project

import com.intellij.notification.BrowseNotificationAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.util.FileContentUtil
import com.perl5.PerlBundle
import com.perl5.lang.perl.idea.configuration.settings.PerlApplicationSettings
import com.perl5.lang.perl.util.PerlPluginUtil

private val log = Logger.getInstance(Perl5ProjectStartupActivity::class.java)

class Perl5ProjectStartupActivity : ProjectActivity {
  override suspend fun execute(project: Project) {
    if (project.isDefault()) {
      return
    }
    val settings = PerlApplicationSettings.getInstance()
    if (settings.shouldShowAnnounce()) {

      settings.setAnnounceShown()
      val notification = Notification(
        "perl5.plugin.update.notification.group",
        PerlBundle.message("plugin.update.baloon.title", PerlPluginUtil.getPluginVersion()),
        PerlBundle.message("plugin.update.baloon.text"),
        NotificationType.INFORMATION
      ).setImportant(true)
        .addAction(
          BrowseNotificationAction(PerlBundle.message("plugin.update.baloon.changes"), "https://plugins.jetbrains.com/plugin/7796")
        )
        .addAction(
          BrowseNotificationAction(PerlBundle.message("plugin.update.baloon.tracker"), "https://github.com/hurricup/Perl5-IDEA/issues")
        )

      Notifications.Bus.notify(notification)
    }
    if (!ApplicationManager.getApplication().isUnitTestMode()) {
      scheduleNamesUpdateWithReparse(project)
    }
  }

  private fun scheduleNamesUpdateWithReparse(project: Project) {
    ApplicationManager.getApplication().executeOnPooledThread { initNamesWithReparse(project) }
  }


  private fun initNamesWithReparse(project: Project) {
    if (project.isDisposed()) {
      return
    }
    PerlNamesCache.getInstance(project).forceCacheUpdate()
    ApplicationManager.getApplication().invokeLater(FileContentUtil::reparseOpenedFiles)
  }
}