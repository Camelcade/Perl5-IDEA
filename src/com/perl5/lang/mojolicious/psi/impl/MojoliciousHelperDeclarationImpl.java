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

package com.perl5.lang.mojolicious.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.mojolicious.psi.MojoliciousHelperDeclaration;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlSubDefinitionWithTextIdentifierImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 23.04.2016.
 */
public class MojoliciousHelperDeclarationImpl extends PerlSubDefinitionWithTextIdentifierImpl implements MojoliciousHelperDeclaration
{
	public static final String HELPER_NAMESPACE_NAME = "Mojolicious::Controller";

	public MojoliciousHelperDeclarationImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public MojoliciousHelperDeclarationImpl(@NotNull PerlSubDefinitionStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		PsiPerlCommaSequenceExpr commaSequence = getArgumentsSequence();

		if (commaSequence != null)
		{
			PsiElement name = commaSequence.getFirstChild();
			if (name instanceof PsiPerlStringBare)
			{
				return name.getFirstChild();
			}
			else if (name instanceof PerlString)
			{
				PsiElement run = name.getFirstChild();
				if (run != null)
				{
					run = run.getNextSibling();
					if (run != null && run.getNode().getElementType() == STRING_IDENTIFIER)
					{
						return run;
					}
				}
			}
		}

		return null;
	}


	@Override
	public PsiPerlBlock getBlock()
	{
		PsiPerlCommaSequenceExpr argumentsSequence = getArgumentsSequence();
		if (argumentsSequence != null)
		{
			PsiPerlSubExpr subArgument = PsiTreeUtil.getChildOfType(argumentsSequence, PsiPerlSubExpr.class);
			if (subArgument != null)
			{
				return subArgument.getBlock();
			}

		}
		return super.getBlock();
	}

	@Nullable
	protected PsiPerlCommaSequenceExpr getArgumentsSequence()
	{
		PsiPerlCallArguments callArguments = getCallArguments();

		if (callArguments != null)
		{
			PsiElement commaSequence = callArguments.getFirstChild();
			if (commaSequence instanceof PsiPerlCommaSequenceExpr)
			{
				return (PsiPerlCommaSequenceExpr) commaSequence;
			}
		}
		return null;
	}

	@Override
	public String getExplicitPackageName()
	{
		return HELPER_NAMESPACE_NAME;
	}

	@Override
	@Nullable
	public PsiPerlCallArguments getCallArguments()
	{
		return findChildByClass(PsiPerlCallArguments.class);
	}

	@Override
	@Nullable
	public PsiPerlExpr getExpr()
	{
		return findChildByClass(PsiPerlExpr.class);
	}

	@Override
	@Nullable
	public PsiPerlMethod getMethod()
	{
		return findChildByClass(PsiPerlMethod.class);
	}


	@Override
	public boolean isMethod()
	{
		return true;
	}
}
