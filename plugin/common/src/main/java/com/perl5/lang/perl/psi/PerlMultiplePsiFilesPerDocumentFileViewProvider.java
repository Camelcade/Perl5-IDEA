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
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.templateLanguages.ConfigurableTemplateLanguageFileViewProvider;
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings;
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.PodLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * POD aware 3 branches FileView provider for perl-based templating languages
 */
public abstract class PerlMultiplePsiFilesPerDocumentFileViewProvider
  extends MultiplePsiFilesPerDocumentFileViewProvider
  implements TemplateLanguageFileViewProvider, ConfigurableTemplateLanguageFileViewProvider {
  private final Language myTemplateLanguage;
  private final Set<Language> myRelevantLanguages = new HashSet<>();

  public PerlMultiplePsiFilesPerDocumentFileViewProvider(PsiManager manager, VirtualFile virtualFile, boolean eventSystemEnabled) {
    this(manager, virtualFile, eventSystemEnabled, calcTemplateLanguage(manager, virtualFile));
  }

  public PerlMultiplePsiFilesPerDocumentFileViewProvider(PsiManager manager,
                                                         VirtualFile virtualFile,
                                                         boolean eventSystemEnabled,
                                                         Language templateLanguage) {
    super(manager, virtualFile, eventSystemEnabled);
    myRelevantLanguages.add(getBaseLanguage());
    myRelevantLanguages.add(PodLanguage.INSTANCE);
    myRelevantLanguages.add(myTemplateLanguage = templateLanguage);
  }

  /**
   * Returns TemplateDataElementType for the templating part
   */
  protected abstract @NotNull IElementType getTemplateContentElementType();

  /**
   * Returns TemplateDataElementType for the pod part
   */
  protected abstract @NotNull IElementType getPODContentElementType();

  @Override
  public @NotNull Language getTemplateDataLanguage() {
    return myTemplateLanguage;
  }

  @Override
  public @NotNull Set<Language> getLanguages() {
    return myRelevantLanguages;
  }

  @Override
  protected @Nullable PsiFile createFile(final @NotNull Language lang) {
    if (lang != PodLanguage.INSTANCE && lang != getBaseLanguage() && lang != getTemplateDataLanguage()) {
      return null;
    }

    final ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(lang);

    if (parserDefinition != null) {
      final PsiFileImpl file = (PsiFileImpl)parserDefinition.createFile(this);
      if (lang == getTemplateDataLanguage()) {
        file.setContentElementType(getTemplateContentElementType());
      }
      else if (lang == PodLanguage.INSTANCE) {
        file.setContentElementType(getPODContentElementType());
      }
      return file;
    }
    return null;
  }

  protected static @NotNull Language calcTemplateLanguage(PsiManager manager, VirtualFile file) {
    Language result = TemplateDataLanguageMappings.getInstance(manager.getProject()).getMapping(file);
    return result == null ? HTMLLanguage.INSTANCE : result;
  }
}
