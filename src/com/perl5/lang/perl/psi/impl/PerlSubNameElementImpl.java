/*
 * Copyright 2015 Alexandr Evstigneev
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

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mojolicious.psi.impl.MojoliciousFileImpl;
import com.perl5.lang.mojolicious.util.MojoliciousSubUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlPackageMember;
import com.perl5.lang.perl.psi.references.PerlPolyVariantReference;
import com.perl5.lang.perl.psi.references.PerlSubReference;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 24.05.2015.
 */
public class PerlSubNameElementImpl extends PerlLeafPsiElementWithCachingReference implements PerlSubNameElement
{
	public PerlSubNameElementImpl(@NotNull IElementType type, CharSequence text)
	{
		super(type, text);
	}

	@Override
	public void computeReferences(List<PsiReference> psiReferences)
	{
		psiReferences.add(new PerlSubReference(this, null));
		super.computeReferences(psiReferences);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PerlVisitor)
		{
			((PerlVisitor) visitor).visitSubNameElement(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	@Nullable
	@Override
	public String getPackageName()
	{
		PsiElement parent = getParent();

		if (parent instanceof PerlPackageMember)
		{
			return ((PerlPackageMember) parent).getPackageName();
		}
		else
		{
			return PerlPackageUtil.getContextPackageName(this);
		}
	}

	@Override
	@NotNull
	public String getCanonicalName()
	{
		return getPackageName() + PerlPackageUtil.PACKAGE_SEPARATOR + getName();
	}

	@Override
	@NotNull
	public String getName()
	{
		return this.getText();
	}

	@Override
	public boolean isBuiltIn()
	{
		// fixme i belive this should be implemented in file element
		if (this.getContainingFile() instanceof MojoliciousFileImpl)
		{
			return isPerlBuiltIn() || MojoliciousSubUtil.isBuiltIn(getText());
		}
		else
		{
			return isPerlBuiltIn();
		}
	}

	// fixme move to file element
	protected boolean isPerlBuiltIn()
	{
		PsiElement parent = getParent();
		if (parent instanceof PerlMethod)
		{
			PsiElement grandParent = parent.getParent();

			if (
					!(grandParent instanceof PsiPerlNestedCall)
							&& (getPrevSibling() == null || PerlPackageUtil.CORE_PACKAGE_FULL.equals(getPrevSibling().getText()))
					)
			{
				return PerlSubUtil.isBuiltIn(getText());
			}
		}
		return false;
	}

	@Nullable
	@Override
	public List<PerlSubBase> getSubDefinitions()
	{
		List<PerlSubBase> definitions = null;

		PsiReference[] references = getReferences();

		for (PsiReference reference : references)
		{
			if (reference instanceof PerlPolyVariantReference)
			{
				ResolveResult[] results = ((PerlPolyVariantReference) reference).multiResolve(false);

				for (ResolveResult result : results)
				{
					PsiElement targetElement = result.getElement();
					if (targetElement instanceof PerlSubBase)
					{
						if (definitions == null)
						{
							definitions = new ArrayList<PerlSubBase>();
						}
						definitions.add((PerlSubBase) targetElement);
					}
				}
			}
		}
		return definitions;
	}
}


