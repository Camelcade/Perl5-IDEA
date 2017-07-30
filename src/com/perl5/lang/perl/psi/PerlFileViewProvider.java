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
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.pod.PodLanguage;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 30.03.2016.
 */
public class PerlFileViewProvider extends MultiplePsiFilesPerDocumentFileViewProvider
  implements TemplateLanguageFileViewProvider, PerlElementTypes {
  private static final Set<Language> myLanguages = new THashSet<>(Arrays.asList(
    PerlLanguage.INSTANCE,
    PodLanguage.INSTANCE
  ));
  private boolean myActAsSingleFile = false;

  public PerlFileViewProvider(PsiManager manager, VirtualFile virtualFile, boolean eventSystemEnabled) {
    super(manager, virtualFile, eventSystemEnabled);
  }

  @Nullable
  @Override
  protected PsiFile createFile(@NotNull Language lang) {
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

  @NotNull
  @Override
  public Set<Language> getLanguages() {
    if (myActAsSingleFile) {
      return Collections.singleton(getBaseLanguage());
    }
    else {
      return myLanguages;
    }
  }

  @NotNull
  @Override
  public Language getBaseLanguage() {
    return PerlLanguage.INSTANCE;
  }

  @Override
  protected MultiplePsiFilesPerDocumentFileViewProvider cloneInner(VirtualFile fileCopy) {
    return new PerlFileViewProvider(getManager(), fileCopy, false);
  }

  @NotNull
  @Override
  public Language getTemplateDataLanguage() {
    return PodLanguage.INSTANCE;
  }

  public void setActAsSingleFile(boolean myActAsSingleFile) {
    this.myActAsSingleFile = myActAsSingleFile;
  }

  @NotNull
  @Override
  public List<PsiFile> getAllFiles() {
    if (myActAsSingleFile) {
      return ContainerUtil.createMaybeSingletonList(getPsi(getBaseLanguage()));
    }
    else {
      return super.getAllFiles();
    }
  }
}
