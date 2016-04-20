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

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.PerlAnnotation;
import com.perl5.lang.perl.psi.PerlSubDefinitionWitTextIdentifier;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.mixins.PerlSubDefinitionBaseImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 22.01.2016.
 */
public abstract class PerlSubDefinitionWithTextIdentifierImpl extends PerlSubDefinitionBaseImpl<PerlSubDefinitionStub> implements PerlSubDefinitionWitTextIdentifier
{
	public PerlSubDefinitionWithTextIdentifierImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlSubDefinitionWithTextIdentifierImpl(@NotNull PerlSubDefinitionStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public PsiPerlBlock getBlock()
	{
		return null;
	}

	@Nullable
	@Override
	public PsiElement getSignatureContainer()
	{
		return null;
	}

	@Override
	public boolean isMethod()
	{
		return true;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return getFirstChild();
	}

	@Override
	public PsiElement getSubNameElement()
	{
		return getNameIdentifier();
	}

	@Override
	protected String getSubNameHeavy()
	{
		return getNode().getText();
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		if (name.isEmpty())
			throw new IncorrectOperationException("You can't set an empty name");

		PsiElement nameIdentifier = getNameIdentifier();
		if (nameIdentifier instanceof LeafPsiElement)
		{
			((LeafPsiElement) nameIdentifier).replaceWithText(name);
		}

		return this;
	}

	@NotNull
	@Override
	public List<PerlAnnotation> getAnnotationList()
	{
		return PerlPsiUtil.collectAnnotations(this);
	}

}
