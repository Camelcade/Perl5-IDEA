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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.extensions.PerlRenameUsagesSubstitutor;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import com.perl5.lang.perl.psi.references.resolvers.PerlSubReferenceResolverSimple;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 26.11.2015.
 * Basic class for sub reference. Uses context package to resolve. Used in string contents, moose, etc.
 */
public class PerlSubReferenceSimple extends PerlPolyVariantReference<PsiElement>
{
	protected static final int FLAG_AUTOLOADED = 1;
	protected static final int FLAG_CONSTANT = 2;
	protected static final int FLAG_DECLARED = 4;
	protected static final int FLAG_DEFINED = 8;
	protected static final int FLAG_ALIASED = 16;
	protected static final int FLAG_IMPORTED = 32;    // fixme this is not set anyway
	protected static final int FLAG_XSUB = 64;

	private static final ResolveCache.PolyVariantResolver<PerlSubReferenceSimple> RESOLVER = new PerlSubReferenceResolverSimple();
	protected int FLAGS = 0;

	public PerlSubReferenceSimple(@NotNull PsiElement element, TextRange textRange)
	{
		super(element, textRange);
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(this, RESOLVER, true, false);
	}

	public boolean isAutoloaded()
	{
		return (FLAGS & FLAG_AUTOLOADED) > 0;
	}

	public boolean isDefined()
	{
		return (FLAGS & FLAG_DEFINED) > 0;
	}

	public boolean isDeclared()
	{
		return (FLAGS & FLAG_DECLARED) > 0;
	}

	public boolean isAliased()
	{
		return (FLAGS & FLAG_ALIASED) > 0;
	}

	public boolean isConstant()
	{
		return (FLAGS & FLAG_CONSTANT) > 0;
	}

	public boolean isImported()
	{
		return (FLAGS & FLAG_IMPORTED) > 0;
	}

	public boolean isXSub()
	{
		return (FLAGS & FLAG_XSUB) > 0;
	}

	public void resetFlags()
	{
		FLAGS = 0;
	}

	public void setAutoloaded()
	{
		FLAGS |= FLAG_AUTOLOADED;
	}

	public void setDefined()
	{

		FLAGS |= FLAG_DEFINED;
	}

	public void setXSub()
	{

		FLAGS |= FLAG_XSUB;
	}

	public void setDeclared()
	{

		FLAGS |= FLAG_DECLARED;
	}

	public void setAliased()
	{
		FLAGS |= FLAG_ALIASED;
	}

	public void setConstant()
	{

		FLAGS |= FLAG_CONSTANT;
	}

	public void setImported()
	{
		FLAGS |= FLAG_IMPORTED;
	}

	@NotNull
	public List<ResolveResult> getResolveResults(List<PsiElement> relatedItems)
	{
		List<ResolveResult> result = new ArrayList<ResolveResult>();

		resetFlags();

		for (PsiElement element : relatedItems)
		{
			if (!isAutoloaded() && element instanceof PerlNamedElement && PerlSubUtil.SUB_AUTOLOAD.equals(((PerlNamedElement) element).getName()))
			{
				setAutoloaded();
			}

			if (!isConstant() && element instanceof PerlConstant)
			{
				setConstant();
			}

			if (!isDeclared() && element instanceof PerlSubDeclaration)
			{
				setDeclared();
			}

			if (!isDefined() && element instanceof PerlSubDefinitionBase)
			{
				setDefined();
			}

			if (!isXSub() && element instanceof PerlSubBase && ((PerlSubBase) element).isXSub())
			{
				setXSub();
			}

			if (!isAliased() && element instanceof PerlGlobVariable)
			{
				setAliased();
			}

			result.add(new PsiElementResolveResult(element));
		}
		return result;
	}


	@Override
	public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException
	{
		PsiElement target = resolve();
		if (target instanceof PerlRenameUsagesSubstitutor)
		{
			newElementName = ((PerlRenameUsagesSubstitutor) target).getSubstitutedUsageName(newElementName, myElement);
		}
		return super.handleElementRename(newElementName);
	}


}
