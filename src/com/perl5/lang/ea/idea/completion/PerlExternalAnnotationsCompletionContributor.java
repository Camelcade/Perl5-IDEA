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

package com.perl5.lang.ea.idea.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.ea.psi.PerlExternalAnnotationsElementPatterns;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 11.08.2016.
 */
public class PerlExternalAnnotationsCompletionContributor extends CompletionContributor implements PerlExternalAnnotationsElementPatterns
{
	public PerlExternalAnnotationsCompletionContributor()
	{
		extend(
				CompletionType.BASIC,
				NAMESPACE_ELEMENT_PATTERN,
				new PerlExternalAnnotationsNamespaceNamesCompletionProvider()
		);

		extend(
				CompletionType.BASIC,
				SUB_ELEMENT_PATTERN,
				new PerlExternalAnnotationsSubNamesCompletionProvider()
		);

		extend(
				CompletionType.BASIC,
				KEYWORD_ELEMENT_PATTERN,
				new PerlExternalAnnotationsKeywordCompletionProvider()
		);
	}

	@Override
	public boolean invokeAutoPopup(@NotNull PsiElement position, char typeChar)
	{
		if (typeChar == ' ')
		{
			IElementType elementType = PsiUtilCore.getElementType(position);
			return elementType == RESERVED_PACKAGE || elementType == RESERVED_SUB;
		}
		return super.invokeAutoPopup(position, typeChar);
	}
}
