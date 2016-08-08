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

package com.perl5.lang.perl.idea.completion.util;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.completion.inserthandlers.SubSelectionHandler;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.util.PerlPackageUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Created by hurricup on 09.08.2015.
 */
public class PerlSubCompletionUtil
{
	public static final SubSelectionHandler SUB_SELECTION_HANDLER = new SubSelectionHandler();

	public static LookupElementBuilder getSubDefinitionLookupElement(String subName, String argsString, PerlSubDefinitionBase subDefinition)
	{
		LookupElementBuilder newElement = LookupElementBuilder
				.create(subName)
				.withIcon(subDefinition.getIcon(0))
				.withStrikeoutness(subDefinition.isDeprecated())
				.withTypeText(subDefinition.getPackageName(), true);

		if (!argsString.isEmpty())
		{
			newElement = newElement
					.withInsertHandler(SUB_SELECTION_HANDLER)
					.withTailText(argsString);
		}

		return newElement;
	}

	public static LookupElementBuilder getSmartLookupElement(@NotNull PsiElement element)
	{
		if (element instanceof PerlSubDefinitionBase)
		{
			return getSubDefinitionLookupElement((PerlSubDefinitionBase) element);
		}
		else if (element instanceof PerlSubDeclaration)
		{
			return getSubDeclarationLookupElement((PerlSubBase) element);
		}
		else if (element instanceof PerlGlobVariable)
		{
			return getGlobLookupElement((PerlGlobVariable) element);
		}
		throw new RuntimeException("Don't know how to make lookup element for " + element.getClass());
	}

	@NotNull
	public static LookupElementBuilder getSubDeclarationLookupElement(PerlSubBase subDeclaration)
	{
		return LookupElementBuilder
				.create(subDeclaration.getSubName())
				.withIcon(subDeclaration.getIcon(0))
				.withStrikeoutness(subDeclaration.isDeprecated())
				.withInsertHandler(SUB_SELECTION_HANDLER)
				.withTypeText(subDeclaration.getPackageName(), true)
				;

	}

	@NotNull
	public static LookupElementBuilder getGlobLookupElement(PerlGlobVariable globVariable)
	{
		return LookupElementBuilder
				.create(globVariable.getName())
				.withIcon(globVariable.getIcon(0))
				.withInsertHandler(SUB_SELECTION_HANDLER)
				.withTypeText(globVariable.getPackageName(), true)
				;

	}

	@NotNull
	public static LookupElementBuilder getSubDefinitionLookupElement(PerlSubDefinitionBase subDefinition)
	{
		return getSubDefinitionLookupElement(
				subDefinition.getSubName(),
				subDefinition.getSubArgumentsListAsString(),
				subDefinition);
	}

	public static void fillWithUnresolvedSubs(final PerlSubBase subDefinition, final CompletionResultSet resultSet)
	{
		final String packageName = subDefinition.getPackageName();
		if (packageName == null)
		{
			return;
		}

		final Set<String> namesSet = new THashSet<String>();
		PsiFile containingFile = subDefinition.getContainingFile();
		containingFile.accept(new PerlRecursiveVisitor()
		{
			@Override
			public void visitMethod(@NotNull PsiPerlMethod method)
			{
				if (packageName.equals(method.getPackageName()))
				{
					PerlSubNameElement subNameElement = method.getSubNameElement();

					if (subNameElement.isValid())
					{

						String subName = subNameElement.getName();

						if (StringUtil.isNotEmpty(subName) && !namesSet.contains(subName))
						{
							PsiReference[] references = subNameElement.getReferences();
							if (references.length == 0)
							{
								super.visitMethod(method);
								return;
							}

							for (PsiReference reference : references)
							{
								if (reference.resolve() != null)
								{
									super.visitMethod(method);
									return;
								}
							}
							// unresolved
							namesSet.add(subName);
							resultSet.addElement(LookupElementBuilder.create(subName));
						}
					}
				}
				super.visitMethod(method);
			}
		});

	}

	public static void fillWithNotOverridedSubs(final PerlSubBase subDefinition, final CompletionResultSet resultSet)
	{
		PerlPackageUtil.processNotOverridedMethods(
				PsiTreeUtil.getParentOfType(subDefinition, PerlNamespaceDefinition.class),
				subDefinitionBase ->
				{
					resultSet.addElement(LookupElementBuilder.create(subDefinitionBase.getSubName()));
					return true;
				}
		);
	}
}
