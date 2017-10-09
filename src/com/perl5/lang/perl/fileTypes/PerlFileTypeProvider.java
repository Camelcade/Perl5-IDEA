/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl.fileTypes;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface PerlFileTypeProvider {
  ExtensionPointName<PerlFileTypeProvider> EP_NAME = ExtensionPointName.create("com.perl5.fileTypeProvider");

  /**
   * Should pass root and type calculation function for each root with custom types
   * @param project project for settings
   * @param rootConsumer function should return a custom fileType or null
   */
  void addRoots(@NotNull Project project, BiConsumer<VirtualFile, Function<VirtualFile, FileType>> rootConsumer);
}
