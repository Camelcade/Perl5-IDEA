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

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.ea.psi.PerlExternalAnnotationsElementTypes;
import com.perl5.lang.perl.idea.completion.util.PerlSubCompletionUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 11.08.2016.
 */
public class PerlExternalAnnotationsKeywordCompletionProvider extends CompletionProvider<CompletionParameters> implements PerlExternalAnnotationsElementTypes
{
	@Override
	protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
	{
		PsiElement element = parameters.getPosition();
		if (element.getPrevSibling() != null)
		{
			return;
		}

		PsiElement parent = element.getParent();
		if (!(parent instanceof PsiErrorElement))
		{
			return;
		}
		parent = parent.getParent();
		if (parent instanceof PsiFile)
		{
			result.addElement(PerlSubCompletionUtil.getBuiltInLookupElement("package"));
		}
		else if (PsiUtilCore.getElementType(parent) == NAMESPACE_CONTENT)
		{
			result.addElement(PerlSubCompletionUtil.getBuiltInLookupElement("package"));
			result.addElement(PerlSubCompletionUtil.getBuiltInLookupElement("sub"));
		}
	}
}
