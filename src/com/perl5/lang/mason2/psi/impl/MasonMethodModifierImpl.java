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

package com.perl5.lang.mason2.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.perl5.lang.mason2.psi.MasonMethodModifier;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseMethodModifierImpl;
import com.perl5.lang.perl.psi.PerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.impl.PerlVariableLightImpl;
import com.perl5.lang.perl.psi.mixins.PerlMethodDefinitionImplMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 08.01.2016.
 */
public class MasonMethodModifierImpl extends PerlMooseMethodModifierImpl implements MasonMethodModifier
{
	protected List<PerlVariableDeclarationWrapper> IMPLICIT_VARIABLES = null;

	public MasonMethodModifierImpl(ASTNode node)
	{
		super(node);
	}


	@Nullable
	@Override
	public PsiReference[] getReferences(PsiElement element)
	{
		return null;
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
