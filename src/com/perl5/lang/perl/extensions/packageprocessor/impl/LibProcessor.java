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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.extensions.packageprocessor.PerlLibProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPragmaProcessorBase;
import com.perl5.lang.perl.psi.PerlUseStatement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 19.09.2015.
 */
public class LibProcessor extends PerlPragmaProcessorBase implements PerlLibProvider {
  @Override
  public void addLibDirs(@NotNull PerlUseStatement useStatement, @NotNull List<VirtualFile> libDirs) {
    int fileIndex = 0;

    List<String> importParameters = useStatement.getImportParameters();
    if (importParameters != null) {
      for (String parameter : importParameters) {
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(parameter);
        if (file != null && file.isDirectory()) {
          libDirs.add(fileIndex++, file);
        }
      }
    }
  }
}
