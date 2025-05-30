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

package com.perl5.lang.tt2;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.templateLanguages.ConfigurableTemplateLanguageFileViewProvider;
import com.intellij.psi.templateLanguages.OuterLanguageElement;
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.ReflectionUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;


public class TemplateToolkitFileViewProvider extends MultiplePsiFilesPerDocumentFileViewProvider
  implements ConfigurableTemplateLanguageFileViewProvider {
  private final Language myBaseLanguage = TemplateToolkitLanguage.INSTANCE;
  private final Language myTemplateLanguage;
  private final Set<Language> myRelevantLanguages = new HashSet<>();

  public TemplateToolkitFileViewProvider(@NotNull PsiManager manager, @NotNull VirtualFile virtualFile, boolean eventSystemEnabled) {
    super(manager, virtualFile, eventSystemEnabled);
    myRelevantLanguages.add(getBaseLanguage());
    myRelevantLanguages.add(myTemplateLanguage = calcTemplateLanguage(manager, virtualFile));
  }

  @Override
  public @NotNull Set<Language> getLanguages() {
    return myRelevantLanguages;
  }

  @Override
  public @NotNull Language getBaseLanguage() {
    return myBaseLanguage;
  }

  @Override
  protected @NotNull MultiplePsiFilesPerDocumentFileViewProvider cloneInner(@NotNull VirtualFile fileCopy) {
    return new TemplateToolkitFileViewProvider(getManager(), fileCopy, false);
  }

  @Override
  public @NotNull Language getTemplateDataLanguage() {
    return myTemplateLanguage;
  }

  @Override
  protected @Nullable PsiFile createFile(@NotNull Language lang) {
    if (lang == getTemplateDataLanguage()) {
      ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(getTemplateDataLanguage());

      if (parserDefinition == null) {
        return null;
      }

      final PsiFileImpl file = (PsiFileImpl)parserDefinition.createFile(this);
      file.setContentElementType(TemplateToolkitElementTypes.TT2_TEMPLATE_DATA);
      return file;
    }

    if (lang == getBaseLanguage()) {
      return LanguageParserDefinitions.INSTANCE.forLanguage(lang).createFile(this);
    }
    return null;
  }

  @Override
  public @Nullable PsiElement findElementAt(int offset, @NotNull Class<? extends Language> lang) {
    final PsiFile mainRoot = getPsi(getBaseLanguage());
    PsiElement ret = null;
    for (final Language language : getLanguages()) {
      if (!ReflectionUtil.isAssignable(lang, language.getClass())) {
        continue;
      }
      if (lang.equals(Language.class) && !getLanguages().contains(language)) {
        continue;
      }

      final PsiFile psiRoot = getPsi(language);
      final PsiElement psiElement = findElementAt(psiRoot, offset);
      if (psiElement == null || psiElement instanceof OuterLanguageElement) {
        continue;
      }
      if (ret == null || psiRoot != mainRoot) {
        ret = psiElement;
        // fixme this hack is to avoid detecting perl code on lexing phase, guess there are more bugs of this kind, so we better do
        if (psiElement.getLanguage() == PerlLanguage.INSTANCE) {
          break;
        }
      }
    }
    return ret;
  }

  public static Language calcTemplateLanguage(@Nullable Project project, @Nullable VirtualFile file) {
    return calcTemplateLanguage(project == null ? null : PsiManager.getInstance(project), file);
  }

  public static Language calcTemplateLanguage(@Nullable PsiManager manager, @Nullable VirtualFile file) {
    while (file instanceof LightVirtualFile lightVirtualFile) {
      VirtualFile originalFile = lightVirtualFile.getOriginalFile();
      if (originalFile == null || originalFile == file) {
        break;
      }
      file = originalFile;
    }

    Language result = manager == null ? null : TemplateDataLanguageMappings.getInstance(manager.getProject()).getMapping(file);
    return result == null ? HTMLLanguage.INSTANCE : result;
  }
}
