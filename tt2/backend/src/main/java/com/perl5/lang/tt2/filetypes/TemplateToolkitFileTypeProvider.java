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

package com.perl5.lang.tt2.filetypes;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.fileTypes.PerlFileTypeProvider;
import com.perl5.lang.tt2.idea.settings.TemplateToolkitSettingsBackendHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class TemplateToolkitFileTypeProvider implements PerlFileTypeProvider {
  @Override
  public void addRoots(@NotNull Project project, BiConsumer<? super VirtualFile, Function<VirtualFile, @Nullable FileType>> rootConsumer) {
    var settings = TemplateToolkitSettingsBackendHelper.getInstance(project);
    for (VirtualFile root : settings.getTemplateRoots()) {
      rootConsumer.accept(
        root,
        virtualFile -> settings.isVirtualFileNameMatches(virtualFile) ? TemplateToolkitFileType.INSTANCE : null
      );
    }
  }
}
