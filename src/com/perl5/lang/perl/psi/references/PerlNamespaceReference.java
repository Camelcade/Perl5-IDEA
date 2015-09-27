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
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.perl5.lang.perl.psi.references.resolvers.PerlNamespaceDefinitionResolver;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 28.05.2015.
 */
public class PerlNamespaceReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
	protected static final ResolveCache.PolyVariantResolver<PerlNamespaceReference> RESOLVER = new PerlNamespaceDefinitionResolver();

	public PerlNamespaceReference(@NotNull PsiElement element, TextRange textRange)
	{
		super(element, textRange);
	}

	@NotNull
	@Override
	public Object[] getVariants()
	{
		return new Object[0];
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(this, RESOLVER, true, false);
	}

	@Override
	public TextRange getRangeInElement()
	{
		return PerlPackageUtil.getPackageRangeFromOffset(0, myElement.getText());
	}

	public String getCanonicalName()
	{
		return PerlPackageUtil.getCanonicalPackageName(myElement.getText());
	}

	@Nullable
	@Override
	public PsiElement resolve()
	{
		ResolveResult[] resolveResults = multiResolve(false);
		return resolveResults.length > 0 ? resolveResults[0].getElement() : null;
	}
}
