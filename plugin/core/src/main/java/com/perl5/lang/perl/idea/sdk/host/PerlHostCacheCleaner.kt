/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk.host

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.impl.PerlSdkTable
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VfsUtil
import com.perl5.PerlBundle
import com.perl5.lang.perl.idea.actions.PerlDumbAwareAction
import com.perl5.lang.perl.util.PerlPluginUtil
import com.perl5.lang.perl.util.PerlRunUtil
import java.io.File
import java.io.IOException

private val log = Logger.getInstance(PerlHostCacheCleaner::class.java)

class PerlHostCacheCleaner : ProjectActivity {
  override suspend fun execute(project: Project) {
    val application = ApplicationManager.getApplication()
    if (!application.isUnitTestMode() && application.isDispatchThread()) {
      log.error("This supposed to be invoked from pooled thread")
      return
    }
    val cacheRoot = VfsUtil.findFileByIoFile(File(PerlPluginUtil.getRemotesCachePath()), true)
    val cacheDirs = cacheRoot?.getChildren()
    if (cacheDirs == null || cacheDirs.isEmpty()) {
      return
    }
    val existingCaches = cacheDirs.toMutableSet()
    PerlSdkTable.getInstance().getInterpreters().forEach { sdk ->
      val sdkCacheRoot = PerlHostData.notNullFrom(sdk).getLocalCacheRoot()
      if (StringUtil.isEmpty(sdkCacheRoot)) {
        return
      }
      existingCaches.remove(VfsUtil.findFileByIoFile(File(sdkCacheRoot!!), true))
    }
    if (existingCaches.isEmpty()) {
      return
    }
    val notification = Notification(
      PerlBundle.message("perl.cache.cleaner.notification.group"),
      PerlBundle.message("perl.cache.cleaner.notification.title"),
      PerlBundle.message(
        "perl.cache.cleaner.notification.message",
        existingCaches
          .map { "<li>${it.name}</li>" }
          .sorted()
          .joinToString(separator = "")),
      NotificationType.WARNING
    )

    notification.addAction(object : PerlDumbAwareAction(PerlBundle.message("perl.cache.cleaner.action.clean")) {
      override fun actionPerformed(event: AnActionEvent) {
        object : Task.Backgroundable(project, PerlBundle.message("perl.cache.cleaner.cleaning"), true) {
          override fun run(indicator: ProgressIndicator) {
            notification.expire()
            existingCaches.forEach {
              indicator.checkCanceled()
              try {
                PerlRunUtil.setProgressText(
                  PerlBundle.message(
                    "perl.cache.cleaner.deleting",
                    StringUtil.shortenPathWithEllipsis(FileUtil.toSystemDependentName(it.getPath()), 50)
                  )
                )
                WriteAction.runAndWait<IOException> { it.delete(this) }
              } catch (e: IOException) {
                log.error(e)
              }
            }
          }
        }.queue()
      }
    })

    Notifications.Bus.notify(notification, project)
  }
}