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
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.QUALIFYING_PACKAGE;

/**
 * Created by hurricup on 24.05.2015.
 */
public abstract class PerlMethodImplMixin extends PerlCompositeElementImpl implements PerlMethod
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
		{
			return namespaceElement.getCanonicalName();
		}
		return null;
	}

	@NotNull
	@Override
	public String getContextPackageName()
	{
		PsiFile file = getContainingFile();
		if (file instanceof PerlFileImpl)
		{
			return ((PerlFileImpl) file).getMethodNamespace(this);
		}
		else
		{
			String contextPackageNameHeavy = getContextPackageNameHeavy();
			return contextPackageNameHeavy == null ? "" : contextPackageNameHeavy;
		}
	}


	@NotNull
	@Override
	public String getPackageName()
	{
		String namespace = getExplicitPackageName();

		if (namespace == null)
		{
			namespace = getContextPackageName();
		}

		return namespace;
	}

	@NotNull
	@Override
	public String getCanonicalName()
	{
		return getPackageName() + PerlPackageUtil.PACKAGE_SEPARATOR + getName();
	}


	@Override
	public String getName()
	{
		PerlSubNameElement subNameElement = getSubNameElement();
		assert subNameElement != null;
		return subNameElement.getText();
	}

	@Nullable
	@Override
	public String getContextPackageNameHeavy()
	{
//		System.err.println("Guessing type for method " + getText() + " at " + getTextOffset());

		PsiElement parent = getParent();
		PsiElement grandParent = parent == null ? null : parent.getParent();

		if (grandParent instanceof PsiPerlDerefExpr)
		{
			return ((PsiPerlDerefExpr) grandParent).getPreviousElementType(parent);
		}

		return PerlPackageUtil.getContextPackageName(this);
	}

	@Override
	public boolean isObjectMethod()
	{
		boolean hasExplicitNamespace = hasExplicitNamespace();
		boolean isNestedCall = getParent() instanceof PsiPerlNestedCall;

		return !hasExplicitNamespace && isNestedCall            // part of ..->method()
				|| hasExplicitNamespace && getFirstChild() instanceof PerlSubNameElement    // method Foo::Bar
				|| hasExplicitNamespace        // SUPER::method
				&& isNestedCall
				&& getFirstChild() instanceof PerlNamespaceElement
				&& ((PerlNamespaceElement) getFirstChild()).isSUPER()
				;
	}

	@Nullable
	@Override
	public PerlNamespaceElement getNamespaceElement()
	{
		PsiElement childByType = findChildByType(QUALIFYING_PACKAGE);
		if (childByType == null)
		{
			return null;
		}
		assert childByType instanceof PerlNamespaceElement;
		return (PerlNamespaceElement) childByType;
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
