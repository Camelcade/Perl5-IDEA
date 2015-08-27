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
import com.perl5.lang.perl.extensions.packageprocessor.IPerlMroProvider;
import com.perl5.lang.perl.extensions.packageprocessor.IPerlPackageParentsProvider;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.psi.mro.PerlMroC3;
import com.perl5.lang.perl.psi.mro.PerlMroDfs;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

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

		LinkedHashSet<String> result = new LinkedHashSet<String>();

		for (PsiPerlUseStatement useStatement : PsiTreeUtil.findChildrenOfType(this, PsiPerlUseStatement.class))
			if (PsiTreeUtil.getParentOfType(useStatement, PerlNamespaceDefinition.class) == this && useStatement.getPackageProcessor() instanceof IPerlPackageParentsProvider)
				result.addAll(((IPerlPackageParentsProvider) useStatement.getPackageProcessor()).getParentsList(useStatement));

		List<String> isa = getArrayAsList("ISA");

		// fixme, acutally isa might overwrite isa from packages
		if (isa != null)
			result.addAll(isa);

		return new ArrayList<String>(result);
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

		for (PsiPerlUseStatement useStatement : PsiTreeUtil.findChildrenOfType(this, PsiPerlUseStatement.class))
			if (PsiTreeUtil.getParentOfType(useStatement, PerlNamespaceDefinition.class) == this && useStatement.getPackageProcessor() instanceof IPerlMroProvider)
				return ((IPerlMroProvider) useStatement.getPackageProcessor()).getMroType(useStatement);

		return PerlMroType.DFS;
	}

	@Override
	public PerlMro getMro()
	{
		// fixme this should be another EP
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
		return getAnnotationDeprecated() != null;
	}

	@Override
	public List<String> getISA()
	{
		return getArrayAsList("ISA");
	}

	@Override
	public List<String> getEXPORT()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getEXPORT();

		return getArrayAsList("EXPORT");
	}

	@Override
	public List<String> getEXPORT_OK()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getEXPORT_OK();

		return getArrayAsList("EXPORT_OK");
	}

	@Override
	public Map<String, List<String>> getEXPORT_TAGS()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getEXPORT_TAGS();

		return getHashAsMap("EXPORT_TAGS");
	}

	@Override
	public Map<String, Set<String>> getImportedSubsNames()
	{
		return PerlSubUtil.getImportedSubs(getProject(), getPackageName(), getContainingFile());
	}

	/**
	 * Searches for array by name and returns distinct list of string values
	 *
	 * @param arrayName array name without a sigil
	 * @return distinct list of string values
	 */
	public List<String> getArrayAsList(String arrayName)
	{
		HashSet<String> result = null;
		for (PerlVariable arrayVariable : PsiTreeUtil.findChildrenOfType(this, PsiPerlArrayVariable.class))
			if (arrayVariable.getNamespaceElement() == null
					&& arrayName.equals(arrayVariable.getName())
					&& PsiTreeUtil.getParentOfType(arrayVariable, PerlNamespaceDefinition.class) == this
					)
			{
				PsiElement assignExpression = arrayVariable.getParent();
				PsiElement assignElement = arrayVariable;

				if (assignExpression instanceof PsiPerlVariableDeclarationGlobal)    // proceed our @ARRAY =
				{
					assignElement = assignExpression;
					assignExpression = assignExpression.getParent();
				}

				// checks for @ARRAY = ...
				if (assignExpression instanceof PsiPerlAssignExpr && assignElement.getNextSibling() != null)// not leftside element
					for (PerlStringContentElement element : PerlPsiUtil.findStringElments(assignExpression.getLastChild()))
					{
						if (result == null)
							result = new HashSet<String>();
						result.add(element.getText());
					}
			}

//		System.err.println("Searched for @" + arrayName + " found: " + result);

		return result == null ? Collections.<String>emptyList() : new ArrayList<String>(result);
	}

	public Map<String, List<String>> getHashAsMap(String hashMap)
	{
		// fixme NYI
		return Collections.emptyMap();
	}
}
