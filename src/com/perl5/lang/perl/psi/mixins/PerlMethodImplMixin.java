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
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 24.05.2015.
 */
public abstract class PerlMethodImplMixin extends PerlPackageMemberMixin implements PsiPerlMethod
{
	public PerlMethodImplMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public String getExplicitPackageName()
	{
		PerlNamespaceElement namespaceElement = getNamespaceElement();

		if (namespaceElement != null)
			return namespaceElement.getCanonicalName();
		return null;
	}

	@Override
	public String getContextPackageName()
	{
		PsiElement parent = getParent();
		PsiElement grandParent = parent == null ? null : parent.getParent();

		if (grandParent instanceof PsiPerlDerefExpr)
			return ((PsiPerlDerefExpr) grandParent).getPreviousElementType(parent);

		return super.getContextPackageName();
	}

	@Override
	public boolean isObjectMethod()
	{
		boolean hasExplicitNamespace = hasExplicitNamespace();
		return !hasExplicitNamespace && getParent() instanceof PsiPerlNestedCall            // part of ..->method()
				|| hasExplicitNamespace && getFirstChild() instanceof PerlSubNameElement;    // method Foo::Bar
	}

	@Override
	public PerlNamespaceElement getNamespaceElement()
	{
		return findChildByClass(PerlNamespaceElement.class);
	}

	@Override
	public PerlSubNameElement getSubNameElement()
	{
		return findChildByClass(PerlSubNameElement.class);
	}

	@Override
	public boolean hasExplicitNamespace()
	{
		return getNamespaceElement() != null;
	}

}
