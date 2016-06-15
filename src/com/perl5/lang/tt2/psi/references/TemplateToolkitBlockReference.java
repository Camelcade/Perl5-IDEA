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

package com.perl5.lang.tt2.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.tt2.psi.TemplateToolkitNamedBlock;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 15.06.2016.
 */
public class TemplateToolkitBlockReference extends PsiPolyVariantReferenceBase<PsiElement>
{
	private static final ResolveCache.PolyVariantResolver<TemplateToolkitBlockReference> RESOLVER = new TemplateToolkitBlockReferenceResolver();

	public TemplateToolkitBlockReference(PsiElement psiElement)
	{
		super(psiElement);
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(this, RESOLVER, true, false);
	}

	@NotNull
	@Override
	public Object[] getVariants()
	{
		return new Object[0];
	}

	private static class TemplateToolkitBlockReferenceResolver implements ResolveCache.PolyVariantResolver<TemplateToolkitBlockReference>
	{
		private static final ResolveResult[] EMPTY_RESULT = new ResolveResult[0];

		@NotNull
		@Override
		public ResolveResult[] resolve(@NotNull TemplateToolkitBlockReference templateToolkitBlockReference, boolean incompleteCode)
		{
			PsiElement element = templateToolkitBlockReference.getElement();
			if (element == null)
			{
				return EMPTY_RESULT;
			}

			TextRange range = ElementManipulators.getValueTextRange(element);
			if (range == null)
			{
				return EMPTY_RESULT;
			}

			final CharSequence targetName = range.subSequence(element.getText());
			if (StringUtil.isEmpty(targetName))
			{
				return EMPTY_RESULT;
			}

			final List<ResolveResult> result = new ArrayList<ResolveResult>();

			PsiTreeUtil.processElements(element.getContainingFile(), new PsiElementProcessor()
			{
				@Override
				public boolean execute(@NotNull PsiElement element)
				{
					if (element instanceof TemplateToolkitNamedBlock && StringUtil.equals(((TemplateToolkitNamedBlock) element).getName(), targetName))
					{
						result.add(new PsiElementResolveResult(element));
					}
					return true;
				}
			});

			return result.toArray(new ResolveResult[result.size()]);
		}
	}


}
