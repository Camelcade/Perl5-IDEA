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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 10.09.2016.
 */
public class PerlLeafPsiElement extends LeafPsiElement
{
	public PerlLeafPsiElement(@NotNull IElementType type, CharSequence text)
	{
		super(type, text);
	}

	@NotNull
	@Override
	public final PsiReference[] getReferences()
	{
		// fixme still not sure if cloning handled by this
		return CachedValuesManager.getCachedValue(this, new CachedValueProvider<PsiReference[]>()
		{
			@Nullable
			@Override
			public Result<PsiReference[]> compute()
			{
				List<PsiReference> referencesList = new ArrayList<PsiReference>();
				computeReferences(referencesList);
				return Result.create(referencesList.toArray(new PsiReference[referencesList.size()]), getReferencesCacheDependencies());
			}
		});
	}

	/**
	 * Computing references for current element
	 */
	protected void computeReferences(List<PsiReference> result)
	{
		Collections.addAll(result, ReferenceProvidersRegistry.getReferencesFromProviders(this));
	}

	/**
	 * Returns objects array for references caching
	 *
	 * @return array of dependency objects
	 */
	protected Object[] getReferencesCacheDependencies()
	{
		return new Object[]{this};
	}

	@Override
	public final PsiReference getReference()
	{
		PsiReference[] references = getReferences();
		return references.length > 0 ? references[0] : null;
	}
}
