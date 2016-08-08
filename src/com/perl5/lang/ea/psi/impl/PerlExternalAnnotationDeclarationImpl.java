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
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.ea.psi.PerlExternalAnnotationDeclaration;
import com.perl5.lang.ea.psi.PerlExternalAnnotationNamespace;
import com.perl5.lang.ea.psi.PerlExternalAnnotationsPsiUtil;
import com.perl5.lang.ea.psi.stubs.PerlExternalAnnotationDeclarationStub;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 06.08.2016.
 */
public class PerlExternalAnnotationDeclarationImpl extends StubBasedPsiElementBase<PerlExternalAnnotationDeclarationStub> implements PerlExternalAnnotationDeclaration
{
	public PerlExternalAnnotationDeclarationImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlExternalAnnotationDeclarationImpl(@NotNull PerlExternalAnnotationDeclarationStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Nullable
	@Override
	public String getPackageName()
	{
		PerlExternalAnnotationDeclarationStub stub = getStub();
		if (stub != null)
		{
			return stub.getPackageName();
		}

		PerlExternalAnnotationNamespace namespace = PsiTreeUtil.getParentOfType(this, PerlExternalAnnotationNamespace.class);
		return namespace == null ? null : namespace.getPackageName();
	}

	@Nullable
	@Override
	public PerlSubAnnotations getSubAnnotations()
	{
		PerlExternalAnnotationDeclarationStub stub = getStub();
		if (stub != null)
		{
			return stub.getSubAnnotations();
		}
		return PerlSubAnnotations.createFromAnnotationsList(PerlPsiUtil.collectAnnotations(this));
	}

	@Override
	public String getSubName()
	{
		PerlExternalAnnotationDeclarationStub stub = getStub();
		if (stub != null)
		{
			return stub.getSubName();
		}

		PerlSubNameElement subNameElement = findChildByClass(PerlSubNameElement.class);
		return subNameElement == null ? null : subNameElement.getName();
	}

	@Nullable
	@Override
	public String getCanonicalName()
	{
		PerlExternalAnnotationDeclarationStub stub = getStub();
		if (stub != null)
		{
			return stub.getCanonicalName();
		}

		return PerlExternalAnnotationsPsiUtil.getCanonicalName(this);
	}

	@Override
	public String toString()
	{
		return getCanonicalName();
	}
}
