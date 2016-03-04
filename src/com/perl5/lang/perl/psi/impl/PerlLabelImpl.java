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

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.PerlLabel;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.references.PerlLabelReference;
import com.perl5.lang.perl.psi.references.PerlSubReference;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 04.03.2016.
 */
public class PerlLabelImpl extends LeafPsiElement implements PerlLabel
{
	protected final PsiReference[] myReferences = new PsiReference[]{
			new PerlLabelReference(this, null)
	};

	public PerlLabelImpl(@NotNull IElementType type, CharSequence text)
	{
		super(type, text);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PerlVisitor) ((PerlVisitor) visitor).visitLabel(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public String getName()
	{
		return this.getText();
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		if (!this.equals(myReferences[0].getElement()))
		{
			myReferences[0] = new PerlSubReference(this, null);
		}
		return myReferences; //(PsiReference[]) ArrayUtils.addAll(, ReferenceProvidersRegistry.getReferencesFromProviders(this));
	}

	@Override
	public PsiReference getReference()
	{
		return getReferences()[0];
	}

}
