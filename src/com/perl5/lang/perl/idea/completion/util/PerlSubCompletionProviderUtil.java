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

	public static LookupElementBuilder getSubDefinitionLookupElement(PerlSubDefinition subDefinition)
	{
		String canonicalName = subDefinition.getCanonicalName();
		if (!SUB_DEFINITIONS_LOOKUP_ELEMENTS.containsKey(canonicalName))
		{
			String argsString = subDefinition.getSubArgumentsListAsString();

			LookupElementBuilder newElement = LookupElementBuilder
					.create(subDefinition.getSubName())
					.withIcon(PerlIcons.SUBROUTINE_GUTTER_ICON)
					.withStrikeoutness(subDefinition.getSubAnnotations().isDeprecated());

			if (!argsString.isEmpty())
				newElement = newElement
						.withInsertHandler(SUB_SELECTION_HANDLER)
						.withTailText(argsString);

			SUB_DEFINITIONS_LOOKUP_ELEMENTS.put(canonicalName, newElement);
		}
		return SUB_DEFINITIONS_LOOKUP_ELEMENTS.get(canonicalName);
	}

	public static LookupElementBuilder getSubDeclarationLookupElement(PerlSubDeclaration subDeclaration)
	{
		String canonicalName = subDeclaration.getCanonicalName();
		if (!SUB_DECLARATIONS_LOOKUP_ELEMENTS.containsKey(canonicalName))
		{
			LookupElementBuilder newElement = LookupElementBuilder
					.create(subDeclaration.getSubName())
					.withIcon(PerlIcons.SUBROUTINE_GUTTER_ICON)
					.withStrikeoutness(subDeclaration.getSubAnnotations().isDeprecated())
					.withInsertHandler(SUB_SELECTION_HANDLER)
					.withTailText("(?)");

			SUB_DECLARATIONS_LOOKUP_ELEMENTS.put(canonicalName, newElement);
		}
		return SUB_DECLARATIONS_LOOKUP_ELEMENTS.get(canonicalName);
	}

	public static LookupElementBuilder getGlobLookupElement(PerlGlobVariable globVariable)
	{
		String canonicalName = globVariable.getCanonicalName();
		if (!GLOBS_LOOKUP_ELEMENTS.containsKey(canonicalName))
		{
			assert globVariable.getName() != null;
			LookupElementBuilder newElement = LookupElementBuilder
					.create(globVariable.getName())
					.withIcon(PerlIcons.GLOB_GUTTER_ICON)
					.withInsertHandler(SUB_SELECTION_HANDLER)
					.withTailText("(?)");

			GLOBS_LOOKUP_ELEMENTS.put(canonicalName, newElement);
		}
		return GLOBS_LOOKUP_ELEMENTS.get(canonicalName);
	}

	public static LookupElementBuilder getConstantLookupElement(PerlConstant constant)
	{
		String canonicalName = constant.getCanonicalName();
		if (!CONSTANTS_LOOKUP_ELEMENTS.containsKey(canonicalName))
		{
			assert constant.getName() != null;
			LookupElementBuilder newElement = LookupElementBuilder
					.create(constant.getName())
					.withIcon(PerlIcons.CONSTANT_GUTTER_ICON);

			CONSTANTS_LOOKUP_ELEMENTS.put(canonicalName, newElement);
		}
		return CONSTANTS_LOOKUP_ELEMENTS.get(canonicalName);
	}


}
