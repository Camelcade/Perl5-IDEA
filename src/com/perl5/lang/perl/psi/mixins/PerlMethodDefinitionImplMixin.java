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

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.method.PerlMethodDefinitionStub;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 10.11.2015.
 */
public abstract class PerlMethodDefinitionImplMixin extends PerlSubDefinitionBaseImpl<PerlMethodDefinitionStub> implements PerlMethodDefinition
{
	// fixme see the #717
	public static final String DEFAULT_INVOCANT_NAME = "self";

	public PerlMethodDefinitionImplMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlMethodDefinitionImplMixin(@NotNull PerlMethodDefinitionStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public boolean isMethod()
	{
		return true;
	}

	@Nullable
	@Override
	public PsiElement getSignatureContainer()
	{
		return getMethodSignatureContent();
	}

	@Override
	protected boolean processSignatureElement(PsiElement signatureElement, List<PerlSubArgument> arguments)
	{
		if (signatureElement instanceof PsiPerlMethodSignatureInvocant)    // explicit invocant
		{
			PerlVariable variable = PsiTreeUtil.findChildOfType(signatureElement, PerlVariable.class);
			if (variable != null)
			{
				arguments.add(new PerlSubArgument(
						variable.getActualType(),
						variable.getName(),
						"",
						false
				));
			}
		}
		else if (signatureElement instanceof PerlVariableDeclarationWrapper)
		{
			if (arguments.isEmpty()) // implicit invocant
			{
				arguments.add(new PerlSubArgument(
						PerlVariableType.SCALAR,
						getDefaultInvocantName(),
						"",    // here we could push context package, but now it's unnecessary
						false
				));
			}

			return super.processSignatureElement(signatureElement, arguments);
		}
		return false;
	}

	@NotNull
	@Override
	public String getDefaultInvocantName()
	{
		return DEFAULT_INVOCANT_NAME;
	}

	/**
	 * Checks if method has an explicit invocant
	 *
	 * @return check result
	 */
	private boolean hasExplicitInvocant()
	{
		PsiPerlMethodSignatureContent methodSignatureContent = getMethodSignatureContent();
		return methodSignatureContent != null && methodSignatureContent.getFirstChild() instanceof PsiPerlMethodSignatureInvocant;
	}

	@Override
	public boolean isKnownVariable(@NotNull PerlVariable variable)
	{
		return !hasExplicitInvocant() && variable.getActualType() == PerlVariableType.SCALAR && getDefaultInvocantName().equals(variable.getName());
	}

	@NotNull
	@Override
	public List<String> getFullQualifiedVariablesList()
	{
		if (hasExplicitInvocant())
		{
			return Collections.emptyList();
		}
		else
		{
			return Collections.singletonList("$" + getDefaultInvocantName());
		}
	}
}
