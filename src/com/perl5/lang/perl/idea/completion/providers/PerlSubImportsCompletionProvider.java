/*
 * Copyright 2015 Alexandr Evstigneev
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
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.psi.PerlNamespaceContainer;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * Created by hurricup on 24.08.2015.
 */
public class PerlSubImportsCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public void addCompletions(@NotNull final CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull final CompletionResultSet resultSet)
	{
		ApplicationManager.getApplication().runReadAction(new Runnable()
		{
			@Override
			public void run()
			{
				PsiElement method = parameters.getPosition().getParent();
				assert method instanceof PsiPerlMethod;

				if (!((PsiPerlMethod) method).hasExplicitNamespace() && !((PsiPerlMethod) method).isObjectMethod())
				{
					PerlNamespaceContainer namespaceContainer = PsiTreeUtil.getParentOfType(method, PerlNamespaceContainer.class);

					assert namespaceContainer != null;

					for (Map.Entry<String, Set<String>> imported : PerlSubUtil.getImportedSubs(method.getProject(), namespaceContainer.getPackageName(), parameters.getOriginalFile()).entrySet())
						for (String subName : imported.getValue())
							resultSet.addElement(LookupElementBuilder
											.create(subName)
											.withTypeText(imported.getKey())
											.withTailText("(?)")    // fixme here we should have a signature
											.withIcon(PerlIcons.SUB_GUTTER_ICON)
							);
				}
			}
		});

	}

}
