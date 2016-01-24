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

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.completion.inserthandlers.SubSelectionHandler;
import com.perl5.lang.perl.psi.PerlConstant;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlSubDeclaration;
import com.perl5.lang.perl.psi.PerlSubDefinitionBase;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.util.PerlPackageUtil;

/**
 * Created by hurricup on 09.08.2015.
 */
public class PerlSubCompletionProviderUtil
{
	public static final SubSelectionHandler SUB_SELECTION_HANDLER = new SubSelectionHandler();

	public static LookupElementBuilder getSubDefinitionLookupElement(String subName, String argsString, PerlSubDefinitionBase subDefinition)
	{
		LookupElementBuilder newElement = LookupElementBuilder
				.create(subName)
				.withIcon(PerlIcons.SUB_GUTTER_ICON)
				.withStrikeoutness(subDefinition.getSubAnnotations().isDeprecated());

		if (!argsString.isEmpty())
		{
			newElement = newElement
					.withInsertHandler(SUB_SELECTION_HANDLER)
					.withTailText(argsString);
		}
		return newElement;
	}

	public static LookupElementBuilder getSubDefinitionLookupElement(PerlSubDefinitionBase subDefinition)
	{
		return getSubDefinitionLookupElement(subDefinition.getSubName(), subDefinition.getSubArgumentsListAsString(), subDefinition);
	}

	public static LookupElementBuilder getSubDeclarationLookupElement(PerlSubDeclaration subDeclaration)
	{
		return LookupElementBuilder
				.create(subDeclaration.getSubName())
				.withIcon(PerlIcons.SUB_GUTTER_ICON)
				.withStrikeoutness(subDeclaration.getSubAnnotations().isDeprecated())
				.withInsertHandler(SUB_SELECTION_HANDLER)
				.withTailText("(?)");

	}

	public static LookupElementBuilder getGlobLookupElement(PerlGlobVariable globVariable)
	{
		return LookupElementBuilder
				.create(globVariable.getName())
				.withIcon(PerlIcons.GLOB_GUTTER_ICON)
				.withInsertHandler(SUB_SELECTION_HANDLER)
				.withTailText("(?)");

	}

	public static LookupElementBuilder getConstantLookupElement(PerlConstant constant)
	{
		return LookupElementBuilder
				.create(constant.getName())
				.withIcon(PerlIcons.CONSTANT_GUTTER_ICON);

	}

	// fixme dont know why modifying of definition lookup element doesn't work
	public static LookupElementBuilder getIncompleteSubDefinitionLookupElement(PerlSubDefinitionBase subDefinition, String prefix)
	{
		String indexKeyName =
				prefix +
						(subDefinition.isMethod() ? "->" : PerlPackageUtil.PACKAGE_SEPARATOR) +
						subDefinition.getSubName();

		String argsString = subDefinition.getSubArgumentsListAsString();

		LookupElementBuilder newElement = LookupElementBuilder
				.create(indexKeyName)
				.withIcon(PerlIcons.SUB_GUTTER_ICON)
				.withPresentableText(subDefinition.getSubName())
				.withStrikeoutness(subDefinition.getSubAnnotations().isDeprecated());

		if (!argsString.isEmpty())
		{
			newElement = newElement
					.withInsertHandler(SUB_SELECTION_HANDLER)
					.withTailText(argsString);
		}

		return newElement;
	}

	public static LookupElementBuilder getIncompleteSubDeclarationLookupElement(PerlSubDeclaration subDeclaration, String prefix)
	{
		String indexKeyName =
				prefix +
						(subDeclaration.isMethod() ? "->" : PerlPackageUtil.PACKAGE_SEPARATOR) +
						subDeclaration.getSubName();

		PerlSubAnnotations subAnnotations = subDeclaration.getSubAnnotations();

		return LookupElementBuilder
				.create(indexKeyName)
				.withIcon(PerlIcons.SUB_GUTTER_ICON)
				.withStrikeoutness(subAnnotations.isDeprecated())
				.withPresentableText(subDeclaration.getSubName())
				.withInsertHandler(SUB_SELECTION_HANDLER)
				;
	}

	public static LookupElementBuilder getIncompleteGlobLookupElement(PerlGlobVariable globVariable, String prefix)
	{
		String indexKeyName = prefix + PerlPackageUtil.PACKAGE_SEPARATOR + globVariable.getName();

		return LookupElementBuilder
				.create(indexKeyName)
				.withIcon(PerlIcons.GLOB_GUTTER_ICON)
				.withPresentableText(globVariable.getName())
				.withInsertHandler(SUB_SELECTION_HANDLER)
				.withTailText("(?)");

	}

	public static LookupElementBuilder getIncompleteConstantLookupElement(PerlConstant constant, String prefix)
	{
		String indexKeyName = prefix + PerlPackageUtil.PACKAGE_SEPARATOR + constant.getName();

		return LookupElementBuilder
				.create(indexKeyName)
				.withIcon(PerlIcons.CONSTANT_GUTTER_ICON)
				.withPresentableText(constant.getName());

	}


}
