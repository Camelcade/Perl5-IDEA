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

package com.perl5.lang.htmlmason;

import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.PerlMultiplePsiFilesPerDocumentFileViewProvider;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes.HTML_MASON_HTML_TEMPLATE_DATA;
import static com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes.HTML_MASON_POD_TEMPLATE_DATA;


public class HTMLMasonFileViewProvider extends PerlMultiplePsiFilesPerDocumentFileViewProvider {
  public HTMLMasonFileViewProvider(final PsiManager manager, final VirtualFile virtualFile, final boolean physical) {
    super(manager, virtualFile, physical);
  }

  public HTMLMasonFileViewProvider(PsiManager manager, VirtualFile virtualFile, boolean eventSystemEnabled, Language templateLanguage) {
    super(manager, virtualFile, eventSystemEnabled, templateLanguage);
  }


  @Override
  public @NotNull Language getBaseLanguage() {
    return HTMLMasonLanguage.INSTANCE;
  }

  @Override
  protected @NotNull IElementType getTemplateContentElementType() {
    return HTML_MASON_HTML_TEMPLATE_DATA;
  }

  @Override
  protected @NotNull IElementType getPODContentElementType() {
    return HTML_MASON_POD_TEMPLATE_DATA;
  }

  @Override
  protected @NotNull HTMLMasonFileViewProvider cloneInner(final @NotNull VirtualFile copy) {
    return new HTMLMasonFileViewProvider(getManager(), copy, false, getTemplateDataLanguage());
  }
}
