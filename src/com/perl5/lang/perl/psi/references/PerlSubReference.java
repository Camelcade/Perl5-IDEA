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
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.perl5.lang.perl.psi.references.resolvers.PerlSubReferenceResolver;
import org.jetbrains.annotations.NotNull;

public class PerlSubReference extends PerlPolyVariantReference
{
	protected static final int FLAG_AUTOLOADED = 1;
	protected static final int FLAG_CONSTANT = 2;
	protected static final int FLAG_DECLARED = 4;
	protected static final int FLAG_DEFINED = 8;
	protected static final int FLAG_ALIASED = 16;
	protected static final int FLAG_IMPORTED = 32;    // fixme this is not set anyway
	private static final ResolveCache.PolyVariantResolver<PerlSubReference> RESOLVER = new PerlSubReferenceResolver();
	protected int FLAGS = 0;

	public PerlSubReference(@NotNull PsiElement element, TextRange textRange)
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

}
