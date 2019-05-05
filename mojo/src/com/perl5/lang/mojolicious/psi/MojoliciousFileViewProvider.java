/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.mojolicious.psi;

import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.mojolicious.MojoliciousLanguage;
import com.perl5.lang.perl.psi.PerlMultiplePsiFilesPerDocumentFileViewProvider;
import org.jetbrains.annotations.NotNull;


public class MojoliciousFileViewProvider extends PerlMultiplePsiFilesPerDocumentFileViewProvider implements MojoliciousElementTypes {
  public MojoliciousFileViewProvider(final PsiManager manager, final VirtualFile virtualFile, final boolean physical) {
    super(manager, virtualFile, physical);
  }

  public MojoliciousFileViewProvider(PsiManager manager, VirtualFile virtualFile, boolean eventSystemEnabled, Language templateLanguage) {
    super(manager, virtualFile, eventSystemEnabled, templateLanguage);
  }

  @Override
  @NotNull
  public Language getBaseLanguage() {
    return MojoliciousLanguage.INSTANCE;
  }

  @NotNull
  @Override
  protected IElementType getTemplateContentElementType() {
    return MOJO_HTML_TEMPLATE_DATA;
  }

  @NotNull
  @Override
  protected IElementType getPODContentElementType() {
    return MOJO_POD_TEMPLATE_DATA;
  }

  @NotNull
  @Override
  protected MojoliciousFileViewProvider cloneInner(@NotNull final VirtualFile copy) {
    return new MojoliciousFileViewProvider(getManager(), copy, false, getTemplateDataLanguage());
  }
}
