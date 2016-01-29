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

package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.util.PerlSubCompletionUtil;
import com.perl5.lang.perl.psi.PerlSubDefinitionBase;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 29.01.2016.
 */
public class PerlSubNameElementCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public void addCompletions(@NotNull CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull CompletionResultSet resultSet)
	{
		PsiElement element = parameters.getPosition();

		if (element.getParent() instanceof PerlSubDefinitionBase)
		{
			PerlSubDefinitionBase subDefinitionBase = (PerlSubDefinitionBase) element.getParent();
			PerlSubCompletionUtil.fillWithUnresolvedSubs(subDefinitionBase, resultSet);
			PerlSubCompletionUtil.fillWithNotOverridedSubs(subDefinitionBase, resultSet);
		}

	}
}
