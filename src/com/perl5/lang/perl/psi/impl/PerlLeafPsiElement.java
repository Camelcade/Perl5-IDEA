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

package com.perl5.lang.perl.psi.impl;

import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.PerlReferenceOwner;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 03.11.2016.
 */
public class PerlLeafPsiElement extends LeafPsiElement implements PerlReferenceOwner
{
	public PerlLeafPsiElement(@NotNull IElementType type, CharSequence text)
	{
		super(type, text);
	}

	@NotNull
	@Override
	public final PsiReference[] getReferences()
	{
		return getReferencesWithCache();
	}

	@Override
	public final PsiReference getReference()
	{
		PsiReference[] references = getReferences();
		return references.length == 0 ? null : references[0];
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "(" + getNode().getElementType().toString() + ")";
	}
}
