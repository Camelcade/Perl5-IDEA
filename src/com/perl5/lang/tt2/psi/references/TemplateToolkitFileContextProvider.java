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

package com.perl5.lang.tt2.psi.references;

import com.intellij.psi.FileContextProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.perl5.lang.tt2.idea.settings.TemplateToolkitSettings;
import com.perl5.lang.tt2.psi.TemplateToolkitFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created by hurricup on 15.06.2016.
 */
public class TemplateToolkitFileContextProvider extends FileContextProvider {
  @Override
  protected boolean isAvailable(PsiFile file) {
    return file instanceof TemplateToolkitFile;
  }

  @NotNull
  @Override
  public Collection<PsiFileSystemItem> getContextFolders(PsiFile file) {
    return TemplateToolkitSettings.getInstance(file.getProject()).getTemplatePsiRoots();
  }

  @Nullable
  @Override
  public PsiFile getContextFile(PsiFile file) {
    return null;
  }
}
