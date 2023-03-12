/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import org.jetbrains.annotations.NotNull;

public interface PerlDirectoryConfigurationProvider {
  static final ExtensionPointName<PerlDirectoryConfigurationProvider> EP_NAME = ExtensionPointName.create("com.perl5.directoryConfigurationProvider");

  /**
   * Extension may analyze {@code contentRoot} of the {@code module} and submit known roots/exclusion to the {@code collector}
   */
  void configureContentRoot(@NotNull Module module, @NotNull VirtualFile contentRoot, @NotNull PerlDirectoryInfoCollector collector);
}
