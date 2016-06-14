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
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.htmlmason.parser.psi.*;
import com.perl5.lang.perl.psi.impl.PsiPerlBlockImpl;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 13.03.2016.
 */
public class HTMLMasonBlockImpl extends PsiPerlBlockImpl implements HTMLMasonBlock
{
	protected MyBlocksCache myBlocksCache;

	public HTMLMasonBlockImpl(ASTNode node)
	{
		super(node);
		myBlocksCache = new MyBlocksCache(this);
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place)
	{
		return lastParent == null || processDeclarationsForReal(processor, state, lastParent, place);
	}

	public boolean processDeclarationsForReal(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place)
	{
		boolean checkInit = false;
		boolean checkCode = false;

		PsiElement argsAnchor = null;
		PsiElement initAnchor = null;

		if (lastParent instanceof HTMLMasonArgsBlockImpl)
		{
			argsAnchor = lastParent;
		}
		else if (lastParent instanceof HTMLMasonInitBlockImpl)
		{
			checkInit = true;
			initAnchor = lastParent;
		}
		else if (!(lastParent instanceof HTMLMasonFilterBlockImpl))
		{
			checkInit = true;
			checkCode = true;
		}

		if (checkCode)
		{
			if (!super.processDeclarations(processor, state, lastParent, place))
			{
				return false;
			}
		}
		if (checkInit)
		{
			if (!checkSubblocks(processor, state, place, HTMLMasonInitBlock.class, initAnchor))
			{
				return false;
			}
		}

		return checkSubblocks(processor, state, place, HTMLMasonArgsBlock.class, argsAnchor);
	}


	@SuppressWarnings("Duplicates")
	protected boolean checkSubblocks(
			@NotNull PsiScopeProcessor processor,
			@NotNull ResolveState state,
			@NotNull PsiElement place,
			@NotNull Class<? extends HTMLMasonCompositeElement> clazz,
			@Nullable PsiElement anchor
	)
	{
		List<HTMLMasonCompositeElement> elements = myBlocksCache.getValue().get(clazz);

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
		myBlocksCache = new MyBlocksCache(this);
	}

	@NotNull
	@Override
	public List<HTMLMasonCompositeElement> getArgsBlocks()
	{
		return myBlocksCache.getValue().get(HTMLMasonArgsBlock.class);
	}

	protected static class MyBlocksCache extends AtomicNotNullLazyValue<Map<Class<? extends HTMLMasonCompositeElement>, List<HTMLMasonCompositeElement>>>
	{
		private final HTMLMasonBlockImpl myBlock;

		public MyBlocksCache(HTMLMasonBlockImpl block)
		{
			myBlock = block;
		}

		@NotNull
		@Override
		protected Map<Class<? extends HTMLMasonCompositeElement>, List<HTMLMasonCompositeElement>> compute()
		{
			Map<Class<? extends HTMLMasonCompositeElement>, List<HTMLMasonCompositeElement>> result = new THashMap<Class<? extends HTMLMasonCompositeElement>, List<HTMLMasonCompositeElement>>();

			final List<HTMLMasonCompositeElement> initResult = new ArrayList<HTMLMasonCompositeElement>();
			final List<HTMLMasonCompositeElement> argsResult = new ArrayList<HTMLMasonCompositeElement>();

			result.put(HTMLMasonInitBlock.class, initResult);
			result.put(HTMLMasonArgsBlock.class, argsResult);

			PsiTreeUtil.processElements(myBlock, new PsiElementProcessor()
			{
				@Override
				public boolean execute(@NotNull PsiElement element)
				{
					if (element instanceof HTMLMasonInitBlock && myBlock.equals(PsiTreeUtil.getParentOfType(element, HTMLMasonArgsContainer.class)))
					{
						initResult.add((HTMLMasonCompositeElement) element);
					}
					else if (element instanceof HTMLMasonArgsBlock && myBlock.equals(PsiTreeUtil.getParentOfType(element, HTMLMasonArgsContainer.class)))
					{
						argsResult.add((HTMLMasonCompositeElement) element);
					}

					return true;
				}
			});

			return result;
		}
	}
}
