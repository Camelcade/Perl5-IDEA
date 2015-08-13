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
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.mojolicious.MojoliciousPerlFileElement;
import com.perl5.lang.mojolicious.util.MojoliciousSubUtil;
import com.perl5.lang.perl.psi.PerlMethod;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlNestedCall;
import com.perl5.lang.perl.psi.properties.PerlPackageMember;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 24.05.2015.
 */
public class PerlSubNameElementImpl extends LeafPsiElement implements PerlSubNameElement
{
	public PerlSubNameElementImpl(@NotNull IElementType type, CharSequence text)
	{
		super(type, text);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PerlVisitor) ((PerlVisitor) visitor).visitSubNameElement(this);
		else super.accept(visitor);
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		PerlSubNameElement newFunction = PerlElementFactory.createUserFunction(getProject(), name);
		if (newFunction != null)
			replace(newFunction);
		else
			throw new IncorrectOperationException("Unable to create function from: " + name);
		return this;
	}


	@Override
	public String getPackageName()
	{
		PsiElement parent = getParent();

		if (parent instanceof PerlPackageMember)
			return ((PerlPackageMember) parent).getPackageName();
		else
			return PerlPackageUtil.getContextPackageName(this);
	}

	@Override
	@NotNull
	public String getCanonicalName()
	{
		return getPackageName() + "::" + getName();
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return this;
	}

	@Override
	public String getName()
	{
		return this.getText();
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		return ReferenceProvidersRegistry.getReferencesFromProviders(this);
	}

	@Override
	public PsiReference getReference()
	{
		PsiReference[] references = getReferences();
		if (references.length > 0)
			return references[0];

		return null;
	}

	@Override
	public boolean isBuiltIn()
	{
		// fixme i belive this should be implemented in file element
		if (this.getContainingFile() instanceof MojoliciousPerlFileElement)
			return isPerlBuiltIn() || MojoliciousSubUtil.isBuiltIn(getText());
		else
			return isPerlBuiltIn();
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
							&& (getPrevSibling() == null || "CORE::".equals(getPrevSibling().getText()))
					)
				return PerlSubUtil.isBuiltIn(getText());
		}
		return false;
	}

}


