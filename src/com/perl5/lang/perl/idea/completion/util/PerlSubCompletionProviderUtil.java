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
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hurricup on 09.08.2015.
 */
public class PerlSubCompletionProviderUtil
{
	public static final SubSelectionHandler SUB_SELECTION_HANDLER = new SubSelectionHandler();

	public static final ConcurrentHashMap<String, LookupElementBuilder> SUB_DEFINITIONS_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();
	public static final ConcurrentHashMap<String, LookupElementBuilder> SUB_DECLARATIONS_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();
	public static final ConcurrentHashMap<String, LookupElementBuilder> GLOBS_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();
	public static final ConcurrentHashMap<String, LookupElementBuilder> CONSTANTS_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();

	public static final ConcurrentHashMap<String, LookupElementBuilder> INCOMPLETE_SUB_DEFINITIONS_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();
	public static final ConcurrentHashMap<String, LookupElementBuilder> INCOMPLETE_SUB_DECLARATIONS_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();
	public static final ConcurrentHashMap<String, LookupElementBuilder> INCOMPLETE_GLOBS_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();
	public static final ConcurrentHashMap<String, LookupElementBuilder> INCOMPLETE_CONSTANTS_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();

	public static void removeFromLookupCache(String canonicalName)
	{
		SUB_DEFINITIONS_LOOKUP_ELEMENTS.remove(canonicalName);
		SUB_DECLARATIONS_LOOKUP_ELEMENTS.remove(canonicalName);
		GLOBS_LOOKUP_ELEMENTS.remove(canonicalName);
		CONSTANTS_LOOKUP_ELEMENTS.remove(canonicalName);

		INCOMPLETE_SUB_DEFINITIONS_LOOKUP_ELEMENTS.remove(canonicalName);
		INCOMPLETE_SUB_DECLARATIONS_LOOKUP_ELEMENTS.remove(canonicalName);
		INCOMPLETE_GLOBS_LOOKUP_ELEMENTS.remove(canonicalName);
		INCOMPLETE_CONSTANTS_LOOKUP_ELEMENTS.remove(canonicalName);
	}

	public static LookupElementBuilder getSubDefinitionLookupElement(PerlSubDefinition subDefinition)
	{
		String indexKeyName = subDefinition.getCanonicalName();
		if (!SUB_DEFINITIONS_LOOKUP_ELEMENTS.containsKey(indexKeyName))
		{
			String argsString = subDefinition.getSubArgumentsListAsString();

			LookupElementBuilder newElement = LookupElementBuilder
					.create(subDefinition.getSubName())
					.withIcon(PerlIcons.SUB_GUTTER_ICON)
					.withStrikeoutness(subDefinition.getSubAnnotations().isDeprecated());

			if (!argsString.isEmpty())
				newElement = newElement
						.withInsertHandler(SUB_SELECTION_HANDLER)
						.withTailText(argsString);

			SUB_DEFINITIONS_LOOKUP_ELEMENTS.put(indexKeyName, newElement);
		}
		return SUB_DEFINITIONS_LOOKUP_ELEMENTS.get(indexKeyName);
	}

	public static LookupElementBuilder getSubDeclarationLookupElement(PerlSubDeclaration subDeclaration)
	{
		String indexKeyName = subDeclaration.getCanonicalName();
		if (!SUB_DECLARATIONS_LOOKUP_ELEMENTS.containsKey(indexKeyName))
		{
			LookupElementBuilder newElement = LookupElementBuilder
					.create(subDeclaration.getSubName())
					.withIcon(PerlIcons.SUB_GUTTER_ICON)
					.withStrikeoutness(subDeclaration.getSubAnnotations().isDeprecated())
					.withInsertHandler(SUB_SELECTION_HANDLER)
					.withTailText("(?)");

			SUB_DECLARATIONS_LOOKUP_ELEMENTS.put(indexKeyName, newElement);
		}
		return SUB_DECLARATIONS_LOOKUP_ELEMENTS.get(indexKeyName);
	}

	public static LookupElementBuilder getGlobLookupElement(PerlGlobVariable globVariable)
	{
		assert globVariable.getName() != null;
		String indexKeyName = globVariable.getCanonicalName();
		if (!GLOBS_LOOKUP_ELEMENTS.containsKey(indexKeyName))
		{
			LookupElementBuilder newElement = LookupElementBuilder
					.create(globVariable.getName())
					.withIcon(PerlIcons.GLOB_GUTTER_ICON)
					.withInsertHandler(SUB_SELECTION_HANDLER)
					.withTailText("(?)");

			GLOBS_LOOKUP_ELEMENTS.put(indexKeyName, newElement);
		}
		return GLOBS_LOOKUP_ELEMENTS.get(indexKeyName);
	}

	public static LookupElementBuilder getConstantLookupElement(PerlConstant constant)
	{
		assert constant.getName() != null;
		String indexKeyName = constant.getCanonicalName();
		if (!CONSTANTS_LOOKUP_ELEMENTS.containsKey(indexKeyName))
		{
			LookupElementBuilder newElement = LookupElementBuilder
					.create(constant.getName())
					.withIcon(PerlIcons.CONSTANT_GUTTER_ICON);

			CONSTANTS_LOOKUP_ELEMENTS.put(indexKeyName, newElement);
		}
		return CONSTANTS_LOOKUP_ELEMENTS.get(indexKeyName);
	}

	// fixme dont know why modifying of definition lookup element doesn't work
	public static LookupElementBuilder getIncompleteSubDefinitionLookupElement(PerlSubDefinition subDefinition, String prefix)
	{
		String indexKeyName =
				prefix +
						(subDefinition.isMethod() ? "->" : "::") +
						subDefinition.getSubName();

		if (!INCOMPLETE_SUB_DEFINITIONS_LOOKUP_ELEMENTS.containsKey(indexKeyName))
		{
			String argsString = subDefinition.getSubArgumentsListAsString();

			LookupElementBuilder newElement = LookupElementBuilder
					.create(indexKeyName)
					.withIcon(PerlIcons.SUB_GUTTER_ICON)
					.withPresentableText(subDefinition.getSubName())
					.withStrikeoutness(subDefinition.getSubAnnotations().isDeprecated());

			if (!argsString.isEmpty())
				newElement = newElement
						.withInsertHandler(SUB_SELECTION_HANDLER)
						.withTailText(argsString);

			INCOMPLETE_SUB_DEFINITIONS_LOOKUP_ELEMENTS.put(indexKeyName, newElement);
		}
		return INCOMPLETE_SUB_DEFINITIONS_LOOKUP_ELEMENTS.get(indexKeyName);
	}

	public static LookupElementBuilder getIncompleteSubDeclarationLookupElement(PerlSubDeclaration subDeclaration, String prefix)
	{
		String indexKeyName =
				prefix +
						(subDeclaration.isMethod() ? "->" : "::") +
						subDeclaration.getSubName();

		if (!INCOMPLETE_SUB_DECLARATIONS_LOOKUP_ELEMENTS.containsKey(indexKeyName))
		{
			PerlSubAnnotations subAnnotations = subDeclaration.getSubAnnotations();

			LookupElementBuilder newElement = LookupElementBuilder
					.create(indexKeyName)
					.withIcon(PerlIcons.SUB_GUTTER_ICON)
					.withStrikeoutness(subAnnotations.isDeprecated())
					.withPresentableText(subDeclaration.getSubName())
					.withInsertHandler(SUB_SELECTION_HANDLER)
					.withTailText("(?)");
			;

			INCOMPLETE_SUB_DECLARATIONS_LOOKUP_ELEMENTS.put(indexKeyName, newElement);
		}
		return INCOMPLETE_SUB_DECLARATIONS_LOOKUP_ELEMENTS.get(indexKeyName);
	}

	public static LookupElementBuilder getIncompleteGlobLookupElement(PerlGlobVariable globVariable, String prefix)
	{
		assert globVariable.getName() != null;
		String indexKeyName = prefix + "::" + globVariable.getName();

		if (!INCOMPLETE_GLOBS_LOOKUP_ELEMENTS.containsKey(indexKeyName))
		{
			LookupElementBuilder newElement = LookupElementBuilder
					.create(indexKeyName)
					.withIcon(PerlIcons.GLOB_GUTTER_ICON)
					.withPresentableText(globVariable.getName())
					.withInsertHandler(SUB_SELECTION_HANDLER)
					.withTailText("(?)");

			INCOMPLETE_GLOBS_LOOKUP_ELEMENTS.put(indexKeyName, newElement);
		}
		return INCOMPLETE_GLOBS_LOOKUP_ELEMENTS.get(indexKeyName);
	}

	public static LookupElementBuilder getIncompleteConstantLookupElement(PerlConstant constant, String prefix)
	{
		assert constant.getName() != null;
		String indexKeyName = prefix + "::" + constant.getName();

		if (!INCOMPLETE_CONSTANTS_LOOKUP_ELEMENTS.containsKey(indexKeyName))
		{
			LookupElementBuilder newElement = LookupElementBuilder
					.create(indexKeyName)
					.withIcon(PerlIcons.CONSTANT_GUTTER_ICON)
					.withPresentableText(constant.getName());

			INCOMPLETE_CONSTANTS_LOOKUP_ELEMENTS.put(indexKeyName, newElement);
		}
		return INCOMPLETE_CONSTANTS_LOOKUP_ELEMENTS.get(indexKeyName);
	}


}
