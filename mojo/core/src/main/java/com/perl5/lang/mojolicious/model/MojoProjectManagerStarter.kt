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

package com.perl5.lang.mojolicious.model

import com.intellij.openapi.application.readAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

private val log = Logger.getInstance(MojoProjectManagerStarter::class.java)

class MojoProjectManagerStarter : ProjectActivity {
  override suspend fun execute(project: Project) {
    if (project.isDefault()) {
      return
    }
    readAction {
      if (!project.isDisposed()) {
        log.debug("Project is initialized")
        MojoProjectManager.getInstance(project).scheduleUpdate()
      }
    }
  }
}