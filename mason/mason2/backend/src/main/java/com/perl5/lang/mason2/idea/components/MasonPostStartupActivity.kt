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
package com.perl5.lang.mason2.idea.components

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.LocalFileSystem
import com.perl5.lang.mason2.MasonPluginUtil
import com.perl5.lang.mason2.idea.vfs.MasonVirtualFileListener

internal class MasonPostStartupActivity : ProjectActivity {
  override suspend fun execute(project: Project) =
    MasonVirtualFileListener(project).let { listener ->
      LocalFileSystem.getInstance().addVirtualFileListener(listener)
      Disposer.register(MasonPluginUtil.getUnloadAwareDisposable(project)) {
        LocalFileSystem.getInstance().removeVirtualFileListener(listener)
      }
    }
}
