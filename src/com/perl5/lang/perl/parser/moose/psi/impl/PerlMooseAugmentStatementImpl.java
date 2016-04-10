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

package com.perl5.lang.perl.parser.moose.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseAugmentStatement;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseMethodModifier;
import com.perl5.lang.perl.parser.moose.psi.PerlMoosePsiUtil;
import com.perl5.lang.perl.parser.moose.stubs.augment.PerlMooseAugmentStatementStub;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 25.11.2015.
 */
public class PerlMooseAugmentStatementImpl extends PerlStubBasedPsiElementBase<PerlMooseAugmentStatementStub> implements PerlMooseAugmentStatement, PerlMooseMethodModifier
{
	public PerlMooseAugmentStatementImpl(ASTNode node)
	{
		super(node);
	}

	public PerlMooseAugmentStatementImpl(@NotNull PerlMooseAugmentStatementStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Nullable
	@Override
	public String getSubName()
	{
		PerlMooseAugmentStatementStub stub = getStub();
		if (stub != null)
		{
			return stub.getSubName();
		}
		return getSubNameFromPsi();
	}

	@Nullable
	protected String getSubNameFromPsi()
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if (nameIdentifier instanceof PerlString)
		{
			return ((PerlString) nameIdentifier).getStringContent();
		}

		return null;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		PsiElement expr = getExpr();

		if (expr instanceof PsiPerlParenthesisedExpr)
		{
			expr = expr.getFirstChild();
			if (expr != null)
			{
				expr = expr.getNextSibling();
			}
		}

		if (expr instanceof PsiPerlCommaSequenceExpr)
		{
			PsiElement nameElement = expr.getFirstChild();
			if (nameElement instanceof PerlString)
			{
				return nameElement;
			}
		}
		return null;
	}

	@Override
	public int getTextOffset()
	{
		PsiElement nameIdentifier = getNameIdentifier();

		return nameIdentifier == null
				? super.getTextOffset()
				: nameIdentifier.getTextOffset();
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if (nameIdentifier != null)
		{
			PerlPsiUtil.renameElement(nameIdentifier, name);
		}

		return this;
	}

	@Nullable
	@Override
	public PsiReference[] getReferences(PsiElement element)
	{
		return PerlMoosePsiUtil.getModifiersNameReference(getExpr(), element);
	}

	@Override
	@Nullable
	public PsiPerlExpr getExpr()
	{
		return findChildByClass(PsiPerlExpr.class);
	}

	@Override
	@Nullable
	public PsiPerlNoStatement getNoStatement()
	{
		return findChildByClass(PsiPerlNoStatement.class);
	}

	@Override
	@Nullable
	public PsiPerlStatement getStatement()
	{
		return findChildByClass(PsiPerlStatement.class);
	}

	@Override
	@Nullable
	public PsiPerlSubDeclaration getSubDeclaration()
	{
		return findChildByClass(PsiPerlSubDeclaration.class);
	}


}
