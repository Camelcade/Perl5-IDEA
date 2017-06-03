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

package com.perl5.lang.mason2.psi.impl;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.perl5.lang.htmlmason.MasonCoreUtil;
import com.perl5.lang.mason2.Mason2Language;
import com.perl5.lang.mason2.Mason2Util;
import com.perl5.lang.mason2.filetypes.MasonPurePerlComponentFileType;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 20.12.2015.
 */
public class MasonFileImpl extends PerlFileImpl {
  public MasonFileImpl(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, Mason2Language.INSTANCE);
  }

  public MasonFileImpl(@NotNull FileViewProvider viewProvider, Language language) {
    super(viewProvider, language);
  }

  @Override
  public String toString() {
    return "Mason2 pure perl file";
  }

  @Override
  protected FileType getDefaultFileType() {
    return MasonPurePerlComponentFileType.INSTANCE;
  }

  /**
   * Returns VFS object representing component root
   *
   * @return component root
   */
  @Nullable
  public VirtualFile getComponentRoot() {
    return Mason2Util.getComponentRoot(getProject(), MasonCoreUtil.getContainingVirtualFile(this));
  }
}
