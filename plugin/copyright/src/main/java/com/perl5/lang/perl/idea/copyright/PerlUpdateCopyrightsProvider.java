/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.copyright;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.maddyhome.idea.copyright.CopyrightProfile;
import com.maddyhome.idea.copyright.options.LanguageOptions;
import com.maddyhome.idea.copyright.psi.UpdateCopyright;
import com.maddyhome.idea.copyright.psi.UpdateCopyrightsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlUpdateCopyrightsProvider extends UpdateCopyrightsProvider {
  @Override
  public @Nullable UpdateCopyright createInstance(Project project,
                                                  Module module,
                                                  VirtualFile virtualFile,
                                                  FileType fileType,
                                                  CopyrightProfile options) {
    return new UpdatePerlPackageFileCopyright(project, module, virtualFile, options);
  }

  @Override
  public @NotNull LanguageOptions getDefaultOptions() {
    final LanguageOptions options = new LanguageOptions();
    options.setBlock(false);
    options.setFileTypeOverride(LanguageOptions.USE_TEXT);
    options.setPrefixLines(false);
    return options;
  }
}
