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

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.extensions.packageprocessor.IPerlPackageParentsProvider;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.psi.mro.PerlMroC3;
import com.perl5.lang.perl.psi.mro.PerlMroDfs;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hurricup on 28.05.2015.
 */
public abstract class PerlNamespaceDefinitionImplMixin extends StubBasedPsiElementBase<PerlNamespaceDefinitionStub> implements PsiPerlNamespaceDefinition
{
	public PerlNamespaceDefinitionImplMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlNamespaceDefinitionImplMixin(@NotNull PerlNamespaceDefinitionStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public String getName()
	{
		return getPackageName();
	}

	@Override
	public PerlNamespaceElement getNamespaceElement()
	{
		return findChildByClass(PerlNamespaceElement.class);
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return getNamespaceElement();
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		PerlNamespaceElement namespaceElement = getNamespaceElement();
		if (namespaceElement != null)
			namespaceElement.setName(name);
		return this;
	}

	@Override
	public String getPackageName()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getPackageName();

		PerlNamespaceElement namespaceElement = getNamespaceElement();
		if (namespaceElement != null)
			return namespaceElement.getCanonicalName();

		return null;
	}

	@Override
	public List<String> getParentNamespaces()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getParentNamespaces();

		List<String> result = new ArrayList<String>();
		// fixme check for push @ISA, assign to @ISA

		PsiElement namespaceBlock = getBlock();

		for (PsiPerlUseStatement useStatement : PsiTreeUtil.findChildrenOfType(namespaceBlock, PsiPerlUseStatement.class))
			if (useStatement.getPackageProcessor() instanceof IPerlPackageParentsProvider)
				if (PsiTreeUtil.getParentOfType(useStatement, PerlNamespaceDefinition.class) == this)    // check that it's not nested package use
					result.addAll(((IPerlPackageParentsProvider) useStatement.getPackageProcessor()).getParentsList(useStatement));

		return result;
	}

	@Nullable
	@Override
	public Icon getIcon(int flags)
	{
		return PerlIcons.PACKAGE_GUTTER_ICON;
	}

	@Override
	public ItemPresentation getPresentation()
	{
		return new PerlItemPresentationSimple(this, getName());
	}

	@Override
	public PerlMroType getMroType()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getMroType();

		PerlUseStatement useMroStatement = PsiTreeUtil.findChildOfType(this, PsiPerlUseStatementMro.class);

		if (useMroStatement != null && PsiTreeUtil.getParentOfType(useMroStatement, PerlNamespaceDefinition.class) == this)
		{
			List<String> parameters = useMroStatement.getStringParameters();
			if (parameters.size() > 0 && "c3".equals(parameters.get(0)))
				return PerlMroType.C3;
		}

		return PerlMroType.DFS;
	}

	@Override
	public PerlMro getMro()
	{
		if (getMroType() == PerlMroType.C3)
			return PerlMroC3.INSTANCE;
		else
			return PerlMroDfs.INSTANCE;
	}

	@Override
	public void getLinearISA(HashSet<String> recursionMap, ArrayList<String> result)
	{
		getMro().getLinearISA(getProject(), getParentNamespaces(), recursionMap, result);
	}

	@Override
	public String getPresentableName()
	{
		return getName();
	}

	@Override
	public boolean isDeprecated()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
			return stub.isDeprecated();
		return getAnnotationDeprectaed() != null;
	}
}
