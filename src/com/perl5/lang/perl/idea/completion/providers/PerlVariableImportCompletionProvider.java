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
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlHashUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlScalarUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hurricup on 24.08.2015.
 */
public class PerlVariableImportCompletionProvider extends CompletionProvider<CompletionParameters>
{
	protected final static Map<String, LookupElementBuilder> SCALAR_LOOKUP_CACHE = new ConcurrentHashMap<String, LookupElementBuilder>();
	protected final static Map<String, LookupElementBuilder> ARRAY_LOOKUP_CACHE = new ConcurrentHashMap<String, LookupElementBuilder>();
	protected final static Map<String, LookupElementBuilder> HASH_LOOKUP_CACHE = new ConcurrentHashMap<String, LookupElementBuilder>();


	public void addCompletions(@NotNull CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull CompletionResultSet resultSet)
	{
		PsiElement variableNameElement = parameters.getPosition();
		PsiElement perlVariable = variableNameElement.getParent();

		// fixme move this to pattern
		if (perlVariable instanceof PerlNamespaceElementContainer && ((PerlNamespaceElementContainer) perlVariable).getNamespaceElement() != null)
			return;

		PerlNamespaceContainer namespaceContainer = PsiTreeUtil.getParentOfType(perlVariable, PerlNamespaceContainer.class);
		assert namespaceContainer != null;
		Project project = perlVariable.getProject();
		String packageName = namespaceContainer.getPackageName();
		PsiFile originalFile = parameters.getOriginalFile();

		if (packageName == null) // incomplete package definition
			return;

		// fixme handle array and hash elements
		if (perlVariable instanceof PsiPerlScalarVariable)
			for (Map.Entry<String, Set<String>> imported : PerlScalarUtil.getImportedScalarNames(project, packageName, originalFile).entrySet())
				for (String name : imported.getValue())
				{
					String lookupKey = imported.getKey() + PerlPackageUtil.PACKAGE_SEPARATOR + name;
					LookupElementBuilder element = SCALAR_LOOKUP_CACHE.get(name);

					if (element == null)
						SCALAR_LOOKUP_CACHE.put(lookupKey, element = LookupElementBuilder
								.create(name)
								.withTypeText(imported.getKey())
								.withIcon(PerlIcons.SCALAR_GUTTER_ICON)
						);

					resultSet.addElement(element);
				}
		else if (perlVariable instanceof PsiPerlArrayVariable || perlVariable instanceof PsiPerlArrayIndexVariable)
			for (Map.Entry<String, Set<String>> imported : PerlArrayUtil.getImportedArraysNames(project, packageName, originalFile).entrySet())
				for (String name : imported.getValue())
				{
					String lookupKey = imported.getKey() + PerlPackageUtil.PACKAGE_SEPARATOR + name;
					LookupElementBuilder element = ARRAY_LOOKUP_CACHE.get(name);

					if (element == null)
						ARRAY_LOOKUP_CACHE.put(lookupKey, element = LookupElementBuilder
								.create(name)
								.withTypeText(imported.getKey())
								.withIcon(PerlIcons.ARRAY_GUTTER_ICON)
						);

					resultSet.addElement(element);
				}
		else if (perlVariable instanceof PsiPerlHashVariable)
			for (Map.Entry<String, Set<String>> imported : PerlHashUtil.getImportedHashesNames(project, packageName, originalFile).entrySet())
				for (String name : imported.getValue())
				{
					String lookupKey = imported.getKey() + PerlPackageUtil.PACKAGE_SEPARATOR + name;
					LookupElementBuilder element = HASH_LOOKUP_CACHE.get(name);

					if (element == null)
						HASH_LOOKUP_CACHE.put(lookupKey, element = LookupElementBuilder
								.create(name)
								.withTypeText(imported.getKey())
								.withIcon(PerlIcons.HASH_GUTTER_ICON)
						);

					resultSet.addElement(element);
				}
	}

}
