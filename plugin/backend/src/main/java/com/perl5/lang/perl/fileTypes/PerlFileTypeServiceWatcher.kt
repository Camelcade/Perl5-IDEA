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

package com.perl5.lang.perl.fileTypes

import com.intellij.openapi.module.Module
import com.intellij.openapi.project.ModuleListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootEvent
import com.intellij.openapi.roots.ModuleRootListener
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.util.Function
import com.perl5.lang.perl.util.PerlPluginUtil

class PerlFileTypeServiceWatcher : ProjectActivity, ModuleListener, ModuleRootListener {
  override suspend fun execute(project: Project) {
    project.messageBus.connect(PerlPluginUtil.getUnloadAwareDisposable(project)).let {
      it.subscribe(ModuleRootListener.TOPIC, this)
      it.subscribe(ModuleListener.TOPIC, this)
    }
    reset()
  }

  override fun modulesAdded(project: Project, modules: List<Module?>) = reset()

  override fun modulesRenamed(project: Project, modules: List<Module?>, oldNameProvider: Function<in Module, String?>) = reset()

  override fun rootsChanged(event: ModuleRootEvent) = reset()

  private fun reset() = PerlFileTypeService.getInstance().reset()
}