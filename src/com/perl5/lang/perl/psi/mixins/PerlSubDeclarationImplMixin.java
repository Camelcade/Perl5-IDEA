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

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import com.perl5.lang.perl.psi.stubs.subsdeclarations.PerlSubDeclarationStub;
import com.perl5.lang.perl.psi.utils.PerlReturnType;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 31.05.2015.
 */
public abstract class PerlSubDeclarationImplMixin extends StubBasedPsiElementBase<PerlSubDeclarationStub> implements PsiPerlSubDeclaration
{
	public PerlSubDeclarationImplMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlSubDeclarationImplMixin(@NotNull PerlSubDeclarationStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public String getPackageName()
	{
		PerlSubDeclarationStub stub = getStub();
		if (stub != null)
			return stub.getPackageName();

		String namespace = getExplicitPackageName();
		if (namespace == null)
			namespace = getContextPackageName();

		return namespace;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return getSubNameElement();
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		PerlSubNameElement subNameElement = getSubNameElement();
		if (subNameElement != null)
			subNameElement.setName(name);
		return this;
	}

	@Override
	public String getSubName()
	{
		PerlSubDeclarationStub stub = getStub();
		if (stub != null)
			return stub.getSubName();

		PerlSubNameElement subNameElement = getSubNameElement();

		if (subNameElement != null)
			return subNameElement.getName();

		return null;
	}

	@Override
	public String getName()
	{
		return getSubName();
	}

	@Override
	public String getContextPackageName()
	{
		return PerlPackageUtil.getContextPackageName(this);
	}

	@Override
	public String getExplicitPackageName()
	{
		PerlNamespaceElement namespace = getNamespaceElement();
		return namespace != null ? namespace.getName() : null;
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
	public boolean isMethod()
	{
		return getSubAnnotations().isMethod();
	}

	@Override
	public String getCanonicalName()
	{
		return getPackageName() + "::" + getSubName();
	}

	@Override
	public PerlSubAnnotations getSubAnnotations()
	{
		PerlSubDeclarationStub stub = getStub();
		if( stub != null )
			return stub.getSubAnnotations();

		PerlSubAnnotations myAnnotations = new PerlSubAnnotations();

		for (PsiPerlAnnotation annotation : getAnnotationList())
		{
			if (annotation instanceof PsiPerlAnnotationAbstract)
				myAnnotations.setIsAbstract(true);
			else if (annotation instanceof PsiPerlAnnotationDeprectaed)
				myAnnotations.setIsDeprecated(true);
			else if (annotation instanceof PsiPerlAnnotationMethod)
				myAnnotations.setIsMethod(true);
			else if (annotation instanceof PsiPerlAnnotationOverride)
				myAnnotations.setIsOverride(true);
			else if (annotation instanceof PerlNamespaceElementContainer) // returns
			{
				PerlNamespaceElement ns = ((PerlNamespaceElementContainer) annotation).getNamespaceElement();
				if (ns != null)
				{
					myAnnotations.setReturns(ns.getName());
					myAnnotations.setReturnType(PerlReturnType.REF);
					// todo implement brackets and braces
				}
			}
		}

		return myAnnotations;
	}

}
