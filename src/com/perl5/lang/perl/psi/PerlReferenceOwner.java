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

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;

/**
 * Created by hurricup on 03.11.2016.
 */
public interface PerlReferenceOwner extends PsiElement
{
	/**
	 * default getReferences[] method should call this one
	 */
	default PsiReference[] getReferencesWithCache()
	{
		return hasReferences() ?
				CachedValuesManager.getCachedValue(
						this,
						() -> CachedValueProvider.Result.create(computeReferences(),
								getReferencesCacheDependencies())) :
				PsiReference.EMPTY_ARRAY;
	}

	/**
	 * Indicates that element must have a reference
	 */
	default boolean hasReferences()
	{
		return false;
	}

	/**
	 * Computing references for psi element
	 */
	default PsiReference[] computeReferences()
	{
		return ReferenceProvidersRegistry.getReferencesFromProviders(this);
	}

	/**
	 * Returns dependencies array for references caching
	 *
	 * @return ref array
	 */
	default Object[] getReferencesCacheDependencies()
	{
		return new Object[]{this};
	}
}
