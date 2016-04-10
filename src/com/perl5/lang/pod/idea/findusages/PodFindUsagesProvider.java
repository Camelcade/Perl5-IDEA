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

package com.perl5.lang.pod.idea.findusages;

import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 03.04.2016.
 */
public class PodFindUsagesProvider implements FindUsagesProvider
{
	@Nullable
	@Override
	public WordsScanner getWordsScanner()
	{
		return new PodWordsScanner();
	}

	@Override
	public boolean canFindUsagesFor(@NotNull PsiElement psiElement)
	{
		return psiElement instanceof PodTitledSection;
	}

	@Nullable
	@Override
	public String getHelpId(@NotNull PsiElement psiElement)
	{
		return null;
	}

	@NotNull
	@Override
	public String getType(@NotNull PsiElement element)
	{
		return "Unknown POD type: " + element;
	}

	@NotNull
	@Override
	public String getDescriptiveName(@NotNull PsiElement element)
	{
		if (element instanceof ItemPresentation)
		{
			String name = ((ItemPresentation) element).getPresentableText();
			if (StringUtil.isNotEmpty(name))
			{
				return name;
			}
		}
		return "Unknown Pod descriptive name" + element;
	}

	@NotNull
	@Override
	public String getNodeText(@NotNull PsiElement element, boolean useFullName)
	{
		return "Unknown Pod node text" + element;
	}
}
