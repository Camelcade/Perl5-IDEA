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

package com.perl5.lang.ea.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.ea.psi.PerlExternalAnnotationNamespace;
import com.perl5.lang.ea.psi.PerlExternalAnnotationsVisitor;
import com.perl5.lang.ea.psi.stubs.PerlExternalAnnotationNamespaceStub;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlVersionElement;
import com.perl5.lang.perl.psi.PsiPerlNamespaceContent;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 07.08.2016.
 */
public class PerlExternalAnnotationNamespaceImpl extends StubBasedPsiElementBase<PerlExternalAnnotationNamespaceStub> implements PerlExternalAnnotationNamespace
{
	public PerlExternalAnnotationNamespaceImpl(@NotNull PerlExternalAnnotationNamespaceStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public PerlExternalAnnotationNamespaceImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public String getPackageName()
	{
		PerlExternalAnnotationNamespaceStub stub = getStub();
		if (stub != null)
		{
			return stub.getPackageName();
		}


		PerlNamespaceElement packageElement = getNameIdentifier();
		return packageElement == null ? null : packageElement.getCanonicalName();
	}

	@Nullable
	@Override
	public PerlNamespaceElement getNameIdentifier()
	{
		return findChildByClass(PerlNamespaceElement.class);
	}

	@Nullable
	@Override
	public String getPackageVersion()
	{
		PerlExternalAnnotationNamespaceStub stub = getStub();
		if (stub != null)
		{
			return stub.getPackageVersion();
		}
		PsiElement versionElement = findChildByClass(PerlVersionElement.class);
		return versionElement == null ? null : versionElement.getText();
	}

	@Nullable
	@Override
	public PsiPerlNamespaceContent getNamespaceContent()
	{
		return findChildByType(NAMESPACE_CONTENT);
	}


	@Override
	public String toString()
	{
		return getPackageName();
	}

	@Nullable
	@Override
	public PerlNamespaceAnnotations getAnnotations()
	{
		PerlExternalAnnotationNamespaceStub stub = getStub();
		if (stub != null)
		{
			return stub.getAnnotations();
		}

		return PerlNamespaceAnnotations.createFromAnnotationsList(PerlPsiUtil.collectAnnotations(this));
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PerlExternalAnnotationsVisitor)
		{
			((PerlExternalAnnotationsVisitor) visitor).visitExternalAnnotationNamespace(this);
		}
		else
		{
			super.accept(visitor);
		}
	}
}
