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
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.stubs.PerlSubBaseStub;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.util.PerlAnnotationsUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * Created by hurricup on 05.06.2015.
 */
public abstract class PerlSubBaseImpl<Stub extends PerlSubBaseStub> extends PerlStubBasedPsiElementBase<Stub> implements PerlSubBase<Stub>
{
	public PerlSubBaseImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlSubBaseImpl(@NotNull Stub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Nullable
	@Override
	public String getPackageName()
	{
		Stub stub = getStub();
		if (stub != null)
		{
			return stub.getPackageName();
		}

		String namespace = getExplicitPackageName();
		if (namespace == null)
		{
			namespace = getContextPackageName();
		}

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
		PsiElement subNameElement = getSubNameElement();
		if (subNameElement != null)
		{
			PerlPsiUtil.renameElement(subNameElement, name);
		}

		return this;
	}

	@Override
	public String getName()
	{
		return getSubName();
	}

	@Nullable
	@Override
	public String getCanonicalName()
	{
		String packageName = getPackageName();
		if (packageName == null)
		{
			return null;
		}

		return packageName + PerlPackageUtil.PACKAGE_SEPARATOR + getSubName();
	}

	@Override
	public String getSubName()
	{
		Stub stub = getStub();
		if (stub != null)
		{
			return stub.getSubName();
		}

		return getSubNameHeavy();
	}

	protected String getSubNameHeavy()
	{
		PsiElement subNameElement = getSubNameElement();
		if (subNameElement != null)
		{
			return subNameElement.getText();
		}

		return null;
	}

	@Nullable
	@Override
	public String getContextPackageName()
	{
		return PerlPackageUtil.getContextPackageName(this);
	}

	@Override
	public String getExplicitPackageName()
	{
		PerlNamespaceElement namespaceElement = getNamespaceElement();
		return namespaceElement != null ? namespaceElement.getCanonicalName() : null;
	}

	@Override
	public PerlNamespaceElement getNamespaceElement()
	{
		return findChildByClass(PerlNamespaceElement.class);
	}

	@Override
	public PsiElement getSubNameElement()
	{
		return findChildByClass(PerlSubNameElement.class);
	}

	@NotNull
	@Override
	public List<PerlAnnotation> getAnnotationList()
	{
		return PerlPsiUtil.collectAnnotations(this);
	}

	@Nullable
	@Override
	public PerlSubAnnotations getAnnotations()
	{
		PerlSubAnnotations annotations = getStubbedOrLocalAnnotations();

		if (annotations != null)
		{
			return annotations;
		}

		return getExternalAnnotations();
	}

	@Nullable
	@Override
	public PerlSubAnnotations getStubbedOrLocalAnnotations()
	{
		Stub stub = getStub();
		if (stub != null)
		{
			return stub.getAnnotations();
		}
		else
		{
			// re-parsing
			return getLocalAnnotations();
		}
	}

	@Nullable
	@Override
	public PerlSubAnnotations getLocalAnnotations()
	{
		return PerlSubAnnotations.createFromAnnotationsList(getAnnotationList());
	}

	@Nullable
	@Override
	public PerlSubAnnotations getExternalAnnotations()
	{
		return PerlAnnotationsUtil.getExternalAnnotations(this);
	}

	@Override
	public boolean isDeprecated()
	{
		PerlSubAnnotations subAnnotations = getAnnotations();
		return subAnnotations != null && subAnnotations.isDeprecated();
	}

	@Override
	public boolean isMethod()
	{
		PerlSubAnnotations subAnnotations = getAnnotations();
		return subAnnotations != null && subAnnotations.isMethod();
	}

	@Nullable
	@Override
	public String getReturns()
	{
		PerlSubAnnotations subAnnotations = getAnnotations();
		return subAnnotations != null ? subAnnotations.getReturns() : null;
	}

	@Override
	public boolean isStatic()
	{
		return !isMethod();
	}

	@Override
	public boolean isXSub()
	{
		return false;
	}

	@Nullable
	@Override
	public Icon getIcon(int flags)
	{
		if (isMethod())
		{
			return PerlIcons.METHOD_GUTTER_ICON;
		}
		else
		{
			return PerlIcons.SUB_GUTTER_ICON;
		}
	}


	@Override
	public int getTextOffset()
	{
		PsiElement nameIdentifier = getNameIdentifier();

		return nameIdentifier == null
				? super.getTextOffset()
				: nameIdentifier.getTextOffset();
	}

	@Override
	public String toString()
	{
		return getCanonicalName();
	}
}
