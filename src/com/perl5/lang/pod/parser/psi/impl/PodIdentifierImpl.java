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

package com.perl5.lang.pod.parser.psi.impl;

import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.parser.psi.PodSectionTitle;
import com.perl5.lang.pod.parser.psi.references.PodSubReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 05.04.2016.
 */
public class PodIdentifierImpl extends LeafPsiElement
{
	private AtomicNotNullLazyValue<PsiReference[]> myReferences;

	public PodIdentifierImpl(@NotNull IElementType type, CharSequence text)
	{
		super(type, text);
		createReferences();
	}

	private void createReferences()
	{
		myReferences = new AtomicNotNullLazyValue<PsiReference[]>()
		{
			@NotNull
			@Override
			protected PsiReference[] compute()
			{
				final PodIdentifierImpl element = PodIdentifierImpl.this;
				List<PsiReference> references = new ArrayList<PsiReference>();

				if (element.getParent() instanceof PodSectionTitle && element.getPrevSibling() == null)
				{
					references.add(new PodSubReference(element));
				}

				references.addAll(Arrays.asList(ReferenceProvidersRegistry.getReferencesFromProviders(element)));

				return references.toArray(new PsiReference[references.size()]);
			}
		};
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		return myReferences.getValue();
	}

	@Override
	public void clearCaches()
	{
		createReferences();
	}
}
