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

package com.perl5.lang.htmlmason.parser.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.htmlmason.HTMLMasonSyntaxElements;
import com.perl5.lang.perl.psi.PerlString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 19.03.2016.
 */
public class HTMLMasonComponentReferencesProvider extends PsiReferenceProvider implements HTMLMasonSyntaxElements
{
	public static final Pattern METHOD_CALL_PATTERN = Pattern.compile("(.+?):([\\w._-]+\\s*)?");

	@NotNull
	@Override
	public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
	{
		if (element.getChildren().length == 0)
		{
			assert element instanceof PerlString;
			String content = ((PerlString) element).getStringContent();

			if (StringUtil.isNotEmpty(content))
			{
				Matcher m;
				TextRange range = ((PerlString) element).getContentTextRangeInParent();
				List<PsiReference> result = new ArrayList<PsiReference>();
				if ((m = METHOD_CALL_PATTERN.matcher(content)).matches()) // method call
				{
					String fileOrSlug = m.group(1);
					String methodName = m.group(2);

					if (methodName == null)
						methodName = "";

					TextRange componentRange = new TextRange(range.getStartOffset(), range.getStartOffset() + fileOrSlug.length());
					TextRange methodRange = new TextRange(range.getEndOffset() - methodName.length(), range.getEndOffset());

					if (StringUtil.equals(fileOrSlug, COMPONENT_SLUG_SELF))
					{
						result.add(new HTMLMasonComponentSimpleReference((PerlString) element, componentRange));
					}
					else if (StringUtil.equals(fileOrSlug, COMPONENT_SLUG_PARENT))
					{
						result.add(new HTMLMasonComponentParentReference((PerlString) element, componentRange));
					}
					else if (StringUtil.equals(fileOrSlug, COMPONENT_SLUG_REQUEST))
					{
						result.add(new HTMLMasonComponentSimpleReference((PerlString) element, componentRange));
					}
					else // component or subcomponent
					{
						result.add(new HTMLMasonComponentReference((PerlString) element, componentRange));
					}

					if (methodRange.getLength() > 0)
					{
						result.add(new HTMLMasonMethodReference((PerlString) element, methodRange));
					}
				}
				else // it's subcomponent or other component
				{
					result.add(new HTMLMasonComponentReference((PerlString) element, range));
				}
				return result.toArray(new PsiReference[result.size()]);
			}
		}

		return PsiReference.EMPTY_ARRAY;
	}
}
