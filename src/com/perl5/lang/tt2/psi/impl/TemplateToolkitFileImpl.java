/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.tt2.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.tt2.TemplateToolkitLanguage;
import com.perl5.lang.tt2.filetypes.TemplateToolkitFileType;
import com.perl5.lang.tt2.psi.TemplateToolkitFile;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 05.06.2016.
 */
public class TemplateToolkitFileImpl extends PsiFileBase implements TemplateToolkitFile {
  public TemplateToolkitFileImpl(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, TemplateToolkitLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return TemplateToolkitFileType.INSTANCE;
  }

  @Override
  public PerlLexicalScope getLexicalScope() {
    return null;
  }
}
