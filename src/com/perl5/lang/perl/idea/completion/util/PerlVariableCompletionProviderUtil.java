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
import com.perl5.lang.perl.idea.completion.PerlInsertHandlers;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hurricup on 25.07.2015.
 */
public class PerlVariableCompletionProviderUtil
{
	public static final ConcurrentHashMap<String, LookupElementBuilder> SCALAR_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();
	public static final ConcurrentHashMap<String, LookupElementBuilder> ARRAY_ELEMENT_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();
	public static final ConcurrentHashMap<String, LookupElementBuilder> HASH_ELEMENT_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();

	public static final ConcurrentHashMap<String, LookupElementBuilder> ARRAY_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();
	public static final ConcurrentHashMap<String, LookupElementBuilder> HASH_SLICE_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();

	public static final ConcurrentHashMap<String, LookupElementBuilder> HASH_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();

	public static final ConcurrentHashMap<String, LookupElementBuilder> GLOB_LOOKUP_ELEMENTS = new ConcurrentHashMap<String, LookupElementBuilder>();

	public static LookupElementBuilder getScalarLookupElement(String name)
	{
		LookupElementBuilder result = SCALAR_LOOKUP_ELEMENTS.get(name);

		if (result == null)
		{
			result = LookupElementBuilder
					.create(name)
					.withIcon(PerlIcons.SCALAR_GUTTER_ICON);
			SCALAR_LOOKUP_ELEMENTS.put(name, result);
		}

		return result;
	}

	public static LookupElementBuilder getGlobLookupElement(String name)
	{
		LookupElementBuilder result = GLOB_LOOKUP_ELEMENTS.get(name);

		if (result == null)
		{
			result = LookupElementBuilder
					.create(name)
					.withIcon(PerlIcons.GLOB_GUTTER_ICON);
			GLOB_LOOKUP_ELEMENTS.put(name, result);
		}

		return result;
	}

	public static LookupElementBuilder getArrayLookupElement(String name)
	{
		LookupElementBuilder result = ARRAY_LOOKUP_ELEMENTS.get(name);

		if (result == null)
		{
			result = LookupElementBuilder
					.create(name)
					.withIcon(PerlIcons.ARRAY_GUTTER_ICON);
			ARRAY_LOOKUP_ELEMENTS.put(name, result);
		}

		return result;
	}

	public static LookupElementBuilder getArrayElementLookupElement(String name)
	{
		LookupElementBuilder result = ARRAY_ELEMENT_LOOKUP_ELEMENTS.get(name);

		if (result == null)
		{
			result = getArrayLookupElement(name)
					.withInsertHandler(PerlInsertHandlers.ARRAY_ELEMENT_INSERT_HANDLER)
					.withTailText("[]");

			ARRAY_ELEMENT_LOOKUP_ELEMENTS.put(name, result);
		}

		return result;
	}

	public static LookupElementBuilder getHashLookupElement(String name)
	{
		LookupElementBuilder result = HASH_LOOKUP_ELEMENTS.get(name);

		if (result == null)
		{
			result = LookupElementBuilder
					.create(name)
					.withIcon(PerlIcons.HASH_GUTTER_ICON);
			HASH_LOOKUP_ELEMENTS.put(name, result);
		}

		return result;
	}

	public static LookupElementBuilder getHashElementLookupElement(String name)
	{
		LookupElementBuilder result = HASH_ELEMENT_LOOKUP_ELEMENTS.get(name);

		if (result == null)
		{
			result = getHashLookupElement(name)
					.withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER)
					.withTailText("{}");
			HASH_ELEMENT_LOOKUP_ELEMENTS.put(name, result);
		}

		return result;
	}

	public static LookupElementBuilder getHashSliceElementLookupElement(String name)
	{
		LookupElementBuilder result = HASH_SLICE_LOOKUP_ELEMENTS.get(name);

		if (result == null)
		{
			result = getHashLookupElement(name)
					.withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER) // slice here
					.withTailText("{}");
			HASH_SLICE_LOOKUP_ELEMENTS.put(name, result);
		}

		return result;
	}

}
