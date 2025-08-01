/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.pod.PodLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.perl5.lang.perl.lexer.PerlElementTypes.POD_BLOCK;


public class PerlFileViewProvider extends MultiplePsiFilesPerDocumentFileViewProvider
  implements TemplateLanguageFileViewProvider {
  private static final Set<Language> myLanguages = Set.of(
    PerlLanguage.INSTANCE,
    PodLanguage.INSTANCE
  );
  private boolean myActAsSingleFile = false;

  public PerlFileViewProvider(PsiManager manager, VirtualFile virtualFile, boolean eventSystemEnabled) {
    super(manager, virtualFile, eventSystemEnabled);
  }

  @Override
  protected @Nullable PsiFile createFile(@NotNull Language lang) {
    if (lang != PerlLanguage.INSTANCE && lang != PodLanguage.INSTANCE) {
      return null;
    }

    final ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(lang);
    if (parserDefinition != null) {

      final PsiFileImpl psiFile = (PsiFileImpl)parserDefinition.createFile(this);

      if (lang == PodLanguage.INSTANCE) {
        psiFile.setContentElementType(POD_BLOCK);
      }

      return psiFile;
    }

    return null;
  }

  @Override
  public @NotNull Set<Language> getLanguages() {
    if (myActAsSingleFile) {
      return Collections.singleton(getBaseLanguage());
    }
    else {
      return myLanguages;
    }
  }

  @Override
  public IElementType getContentElementType(@NotNull Language language) {
    return language == PodLanguage.INSTANCE ? POD_BLOCK : null;
  }

  @Override
  public @NotNull Language getBaseLanguage() {
    return PerlLanguage.INSTANCE;
  }

  @Override
  protected @NotNull MultiplePsiFilesPerDocumentFileViewProvider cloneInner(@NotNull VirtualFile fileCopy) {
    return new PerlFileViewProvider(getManager(), fileCopy, false);
  }

  @Override
  public @NotNull Language getTemplateDataLanguage() {
    return PodLanguage.INSTANCE;
  }

  public void setActAsSingleFile(boolean myActAsSingleFile) {
    this.myActAsSingleFile = myActAsSingleFile;
  }

  @Override
  public @NotNull List<PsiFile> getAllFiles() {
    if (myActAsSingleFile) {
      return ContainerUtil.createMaybeSingletonList(getPsi(getBaseLanguage()));
    }
    else {
      return super.getAllFiles();
    }
  }
}
