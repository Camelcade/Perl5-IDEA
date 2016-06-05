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

package com.perl5.lang.tt2;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.StdLanguages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.templateLanguages.ConfigurableTemplateLanguageFileViewProvider;
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings;
import com.perl5.lang.tt2.psi.TemplateToolkitElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 05.06.2016.
 */
public class TemplateToolkitFileViewProvider extends MultiplePsiFilesPerDocumentFileViewProvider implements ConfigurableTemplateLanguageFileViewProvider
{
	private final Language myBaseLanguage = TemplateToolkitLanguage.INSTANCE;
	private final Language myTemplateLanguage;

	public TemplateToolkitFileViewProvider(PsiManager manager, VirtualFile virtualFile, boolean eventSystemEnabled)
	{
		super(manager, virtualFile, eventSystemEnabled);
		myTemplateLanguage = calcTemplateLanguage(manager, virtualFile);
	}

	protected Language calcTemplateLanguage(PsiManager manager, VirtualFile file)
	{
		Language result = TemplateDataLanguageMappings.getInstance(manager.getProject()).getMapping(file);
		return result == null ? StdLanguages.HTML : result;
	}

	@NotNull
	@Override
	public Language getBaseLanguage()
	{
		return myBaseLanguage;
	}

	@Override
	protected MultiplePsiFilesPerDocumentFileViewProvider cloneInner(VirtualFile fileCopy)
	{
		return new TemplateToolkitFileViewProvider(getManager(), fileCopy, false);
	}

	@NotNull
	@Override
	public Language getTemplateDataLanguage()
	{
		return myTemplateLanguage;
	}

	@Nullable
	@Override
	protected PsiFile createFile(@NotNull Language lang)
	{
		if (lang == getTemplateDataLanguage())
		{
			final PsiFileImpl file = (PsiFileImpl) LanguageParserDefinitions.INSTANCE.forLanguage(StdLanguages.HTML).createFile(this);
			file.setContentElementType(TemplateToolkitElementTypes.TT2_TEMPLATE_DATA);
			return file;
		}

		if (lang == getBaseLanguage())
		{
			return LanguageParserDefinitions.INSTANCE.forLanguage(lang).createFile(this);
		}
		return null;
	}
}
