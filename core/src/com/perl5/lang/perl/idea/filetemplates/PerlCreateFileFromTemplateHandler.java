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

package com.perl5.lang.perl.idea.filetemplates;

import com.intellij.ide.fileTemplates.DefaultCreateFromTemplateHandler;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import com.intellij.psi.PsiDirectory;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 03.06.2016.
 */
public class PerlCreateFileFromTemplateHandler extends DefaultCreateFromTemplateHandler {
  public static final PerlCreateFileFromTemplateHandler INSTANCE = new PerlCreateFileFromTemplateHandler();

  @Override
  public boolean handlesTemplate(@NotNull FileTemplate template) {
    FileType templateFileType = FileTypeManagerEx.getInstanceEx().getFileTypeByExtension(template.getExtension());
    return templateFileType instanceof PerlFileType;
  }

  @Override
  public boolean canCreate(@NotNull PsiDirectory[] dirs) {
    return false;
  }
}
