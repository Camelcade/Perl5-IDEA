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
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.psi.PerlNamespaceContainer;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hurricup on 24.08.2015.
 */
public class PerlSubImportsCompletionProvider extends CompletionProvider<CompletionParameters>
{
	// fixme make a wrapper for lazy cache
	protected final static Map<String, LookupElementBuilder> LOOKUP_CACHE = new ConcurrentHashMap<String, LookupElementBuilder>();

	public void addCompletions(@NotNull final CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull final CompletionResultSet resultSet)
	{
		PsiElement method = parameters.getPosition().getParent();
		assert method instanceof PsiPerlMethod;

		if (!((PsiPerlMethod) method).isObjectMethod())
		{
			Project project = method.getProject();
			if (!((PsiPerlMethod) method).hasExplicitNamespace())
			{
				PerlNamespaceContainer namespaceContainer = PsiTreeUtil.getParentOfType(method, PerlNamespaceContainer.class);

				assert namespaceContainer != null;

				for (Map.Entry<String, Set<String>> imported : PerlSubUtil.getImportedSubs(project, namespaceContainer.getPackageName(), parameters.getOriginalFile()).entrySet())
					for (String subName : imported.getValue())
					{
						String lookupKey = imported.getKey() + "::" + subName;
						LookupElementBuilder element = LOOKUP_CACHE.get(subName);

						if (element == null)
							LOOKUP_CACHE.put(lookupKey, element = LookupElementBuilder
											.create(subName)
											.withTypeText(imported.getKey())
											.withTailText("(?)")    // fixme here we should have a signature
											.withIcon(PerlIcons.SUB_GUTTER_ICON)
							);

						resultSet.addElement(element);
					}
			} else
			{    // not an object method, but has explicit namespace
				PerlNamespaceElement namespaceElement = ((PsiPerlMethod) method).getNamespaceElement();
				if (namespaceElement != null)
				{
					String targetPackageName = namespaceElement.getCanonicalName();
					if (targetPackageName != null)
					{    // fixme partially not dry with above block
						for (PerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, targetPackageName))
							for (Map.Entry<String, Set<String>> imported : namespaceDefinition.getImportedSubsNames().entrySet())
								for (String subName : imported.getValue())
								{
									String lookupKey = imported.getKey() + "::" + subName;
									LookupElementBuilder element = LOOKUP_CACHE.get(subName);

									if (element == null)
										LOOKUP_CACHE.put(lookupKey, element = LookupElementBuilder
														.create(subName)
														.withTypeText(imported.getKey())
														.withTailText("(?)")    // fixme here we should have a signature
														.withIcon(PerlIcons.SUB_GUTTER_ICON)
										);

									resultSet.addElement(element);
								}

					}
				}
			}
		}

	}

}
