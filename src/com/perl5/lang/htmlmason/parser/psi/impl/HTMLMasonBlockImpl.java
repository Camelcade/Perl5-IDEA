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

package com.perl5.lang.htmlmason.parser.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonArgsBlock;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonCompositeElement;
import com.perl5.lang.perl.psi.impl.PsiPerlBlockImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 13.03.2016.
 */
public class HTMLMasonBlockImpl extends PsiPerlBlockImpl
{
	protected List<HTMLMasonCompositeElement> myArgsElements = null;

	public HTMLMasonBlockImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place)
	{
		return lastParent == null || processDeclarationsForReal(processor, state, lastParent, place);
	}

	public boolean processDeclarationsForReal(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place)
	{
		return super.processDeclarations(processor, state, lastParent, place) && processArgsBlock(processor, state, place, lastParent);
	}

	@SuppressWarnings("Duplicates")
	protected boolean processArgsBlock(
			@NotNull PsiScopeProcessor processor,
			@NotNull ResolveState state,
			@NotNull PsiElement place,
			@Nullable PsiElement anchor
	)
	{
		if (!(anchor instanceof HTMLMasonArgsBlock))
			anchor = null;

		List<HTMLMasonCompositeElement> elements = myArgsElements;

		if (elements == null)
		{
			elements = new ArrayList<HTMLMasonCompositeElement>(PsiTreeUtil.findChildrenOfType(this, HTMLMasonArgsBlock.class));
			myArgsElements = elements;
		}

		for (int i = elements.size() - 1; i >= 0; i--)
		{
			HTMLMasonCompositeElement element = elements.get(i);
			if (anchor == null && !element.processDeclarationsForReal(processor, state, null, place))
			{
				return false;
			}
			else if (anchor != null && anchor.equals(element))
			{
				anchor = null;
			}
		}

		return true;
	}

	@Override
	public void subtreeChanged()
	{
		super.subtreeChanged();
		myArgsElements = null;
	}

}
