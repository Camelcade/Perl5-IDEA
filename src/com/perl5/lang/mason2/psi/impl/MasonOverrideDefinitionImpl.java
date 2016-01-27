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

package com.perl5.lang.mason2.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.mason2.psi.MasonOverrideDefinition;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseOverrideStatementImpl;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.impl.PerlVariableLightImpl;
import com.perl5.lang.perl.psi.mixins.PerlMethodDefinitionImplMixin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 03.01.2016.
 */
public class MasonOverrideDefinitionImpl extends PerlMooseOverrideStatementImpl implements MasonOverrideDefinition
{
	protected List<PerlVariableDeclarationWrapper> IMPLICIT_VARIABLES;

	public MasonOverrideDefinitionImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public MasonOverrideDefinitionImpl(@NotNull PerlSubDefinitionStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	protected void fillImplicitVariables()
	{
		IMPLICIT_VARIABLES = new ArrayList<PerlVariableDeclarationWrapper>();
		if (isValid())
		{
			IMPLICIT_VARIABLES.add(new PerlVariableLightImpl(
					getManager(),
					PerlLanguage.INSTANCE,
					PerlMethodDefinitionImplMixin.getDefaultInvocantName(),
					true,
					false,
					true,
					this
			));
		}
	}

	@Override
	@NotNull
	public PsiPerlBlock getBlock()
	{
		return findNotNullChildByClass(PsiPerlBlock.class);
	}

	@Override
	public PsiElement getSubNameElement()
	{
		return PsiTreeUtil.getChildOfType(this, PerlSubNameElement.class);
	}

	@Override
	protected String getSubNameHeavy()
	{
		PsiElement subNameElement = getSubNameElement();
		return subNameElement == null ? null : subNameElement.getNode().getText();
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		if (name.isEmpty())
		{
			throw new IncorrectOperationException("You can't set an empty method name");
		}

		PsiElement nameIdentifier = getNameIdentifier();
		if (nameIdentifier instanceof LeafPsiElement)
		{
			((LeafPsiElement) nameIdentifier).replaceWithText(name);
		}

		return this;
	}

	@NotNull
	@Override
	public List<PerlVariableDeclarationWrapper> getImplicitVariables()
	{
		if (IMPLICIT_VARIABLES == null)
		{
			fillImplicitVariables();
		}
		return IMPLICIT_VARIABLES;
	}

}
