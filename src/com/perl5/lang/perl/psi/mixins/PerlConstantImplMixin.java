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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.completion.util.PerlSubCompletionProviderUtil;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.idea.stubs.constants.PerlConstantStub;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PsiPerlConstantName;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 04.08.2015.
 */
public abstract class PerlConstantImplMixin extends StubBasedPsiElementBase<PerlConstantStub> implements PsiPerlConstantName
{
	public PerlConstantImplMixin(PerlConstantStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public PerlConstantImplMixin(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public PerlString getNameIdentifier()
	{
		return PsiTreeUtil.getChildOfType(this, PerlString.class);
	}

	@Override
	public int getTextOffset()
	{
		PsiElement nameIdentifier = getNameIdentifier();

		return nameIdentifier == null
				? super.getTextOffset()
				: getNameIdentifier().getTextOffset();
	}

	@Override
	public String getName()
	{
		PerlConstantStub stub = getStub();
		if (stub != null)
			return stub.getName();

		PerlString nameIdentifier = getNameIdentifier();
		return nameIdentifier == null ? null : nameIdentifier.getStringContent();
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		if (name.isEmpty())
			throw new IncorrectOperationException("You can't set an empty constant name");

		PerlString nameIdentifier = getNameIdentifier();
		if (nameIdentifier != null)
			nameIdentifier.setStringContent(name);

		return this;
	}

	@Override
	public String getContextPackageName()
	{
		return PerlPackageUtil.getContextPackageName(this);
	}

	@Override
	public String getExplicitPackageName()
	{
		// fixme package name may be specified in constant in text
		return null;
	}

	@Override
	public String getPackageName()
	{
		// fixme check stub
		PerlConstantStub stub = getStub();
		if (stub != null)
			return stub.getPackageName();

		String namespace = getExplicitPackageName();

		if (namespace == null)
			namespace = getContextPackageName();

		return namespace;
	}

	@Override
	public String getCanonicalName()
	{
		return getPackageName() + "::" + getName();
	}

	@Override
	public ItemPresentation getPresentation()
	{
		return new PerlItemPresentationSimple(this, getName());
	}

	@Nullable
	@Override
	public Icon getIcon(int flags)
	{
		return PerlIcons.CONSTANT_GUTTER_ICON;
	}

	@Override
	public String getPresentableName()
	{
		return getName();
	}

	@Override
	public void subtreeChanged()
	{
		PerlSubCompletionProviderUtil.removeFromLookupCache(getCanonicalName());
		super.subtreeChanged();
	}
}
