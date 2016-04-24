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

package com.perl5.lang.pod.parser.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlSubBase;
import com.perl5.lang.pod.parser.psi.impl.PodIdentifierImpl;
import com.perl5.lang.pod.parser.psi.util.PodFileUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 05.04.2016.
 */
public class PodSubReference extends PodReferenceBase<PodIdentifierImpl>
{
	protected static final ResolveCache.PolyVariantResolver<PodSubReference> RESOLVER = new PodSubReferenceResolver();

	public PodSubReference(PodIdentifierImpl element)
	{
		super(element, new TextRange(0, element.getTextLength()), true);
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
		return (PsiElement) myElement.replaceWithText(newElementName);
	}

	@Override
	public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException
	{
		return super.bindToElement(element);
	}

	private static class PodSubReferenceResolver implements ResolveCache.PolyVariantResolver<PodSubReference>
	{
		@NotNull
		@Override
		public ResolveResult[] resolve(@NotNull PodSubReference podSubReference, boolean incompleteCode)
		{
			PsiElement element = podSubReference.getElement();
			if (element != null)
			{
				String subName = element.getText();
				if (StringUtil.isNotEmpty(subName))
				{
					final PsiFile perlFile = PodFileUtil.getTargetPerlFile(element.getContainingFile());

					if (perlFile != null)
					{
						List<ResolveResult> results = new ArrayList<ResolveResult>();
						for (PerlSubBase subBase : PsiTreeUtil.findChildrenOfType(perlFile, PerlSubBase.class))
						{
							String subBaseName = subBase.getName();
							if (subBaseName != null && StringUtil.equals(subBaseName, subName))
							{
								results.add(new PsiElementResolveResult(subBase));

							}
						}
						return results.toArray(new ResolveResult[results.size()]);
					}
				}
			}

			return ResolveResult.EMPTY_ARRAY;
		}
	}
}
