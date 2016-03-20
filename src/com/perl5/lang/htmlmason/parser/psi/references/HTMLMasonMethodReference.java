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
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonMethodDefinition;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonNamedElement;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.references.PerlPolyVariantReference;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 19.03.2016.
 */
public class HTMLMasonMethodReference extends PerlPolyVariantReference<PerlString>
{
	protected static final ResolveCache.PolyVariantResolver<HTMLMasonMethodReference> RESOLVER = new HTMLMasonMethodResolver();

	public HTMLMasonMethodReference(@NotNull PerlString element, TextRange textRange)
	{
		super(element, textRange);
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(this, RESOLVER, true, incompleteCode);
	}

	@Override
	public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException
	{
		String currentContent = myElement.getStringContent();
		TextRange contentRange = myElement.getContentTextRangeInParent();

		if (HTMLMasonNamedElement.HTML_MASON_IDENTIFIER_PATTERN.matcher(newElementName).matches())
		{
			TextRange range = getRangeInElement();
			String newContent = currentContent.substring(0, range.getStartOffset() - contentRange.getStartOffset()) + newElementName;
			myElement.setStringContent(newContent);
		}
		return myElement;
	}

	private static class HTMLMasonMethodResolver implements ResolveCache.PolyVariantResolver<HTMLMasonMethodReference>
	{
		@NotNull
		@Override
		public ResolveResult[] resolve(@NotNull HTMLMasonMethodReference reference, boolean incompleteCode)
		{
			PerlString element = reference.getElement();
			String methodName = reference.getRangeInElement().substring(element.getText());

			if (StringUtil.isNotEmpty(methodName))
			{
				PsiReference[] references = element.getReferences();
				if (references.length == 2)
				{
					PsiReference componentReference = references[0];
					PsiElement startComponent = componentReference.resolve();
					if (startComponent instanceof HTMLMasonFileImpl)
					{
						HTMLMasonMethodDefinition methodDefinition = ((HTMLMasonFileImpl) startComponent).findMethodDefinitionByNameInThisOrParents(methodName);
						if (methodDefinition != null)
						{
							return new ResolveResult[]{new PsiElementResolveResult(methodDefinition)};
						}
					}
				}
			}
			return ResolveResult.EMPTY_ARRAY;
		}
	}
}
