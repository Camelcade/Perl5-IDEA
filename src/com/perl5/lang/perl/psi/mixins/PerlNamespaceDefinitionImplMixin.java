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
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.Stub;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.extensions.packageprocessor.PerlMroProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageParentsProvider;
import com.perl5.lang.perl.extensions.parser.PerlRuntimeParentsProvider;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.psi.mro.PerlMroC3;
import com.perl5.lang.perl.psi.mro.PerlMroDfs;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

/**
 * Created by hurricup on 28.05.2015.
 */
public abstract class PerlNamespaceDefinitionImplMixin extends StubBasedPsiElementBase<PerlNamespaceDefinitionStub> implements PerlNamespaceDefinition
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
		{
			PerlPsiUtil.renameElement(namespaceElement, name);
		}
		return this;
	}


	@Override
	public String getPackageName()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
		{
			return stub.getPackageName();
		}

		return getPackageNameHeavy();
	}

	protected String getPackageNameHeavy()
	{
		PerlNamespaceElement namespaceElement = getNamespaceElement();
		if (namespaceElement != null)
		{
			return namespaceElement.getCanonicalName();
		}

		return null;
	}

	@Override
	public List<PerlNamespaceDefinition> getParentNamespaceDefinitions()
	{
		return PerlPackageUtil.collectNamespaceDefinitions(getProject(), getParentNamepsacesNames());
	}

	@NotNull
	@Override
	public List<String> getParentNamepsacesNames()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
		{
			return stub.getParentNamespaces();
		}

		return getParentNamespacesFromPsi();
	}

	@NotNull
	@Override
	public List<PerlNamespaceDefinition> getChildNamespaceDefinitions()
	{
		Project project = getProject();
		return PerlPackageUtil.getDerivedNamespaceDefinitions(project, getPackageName());
	}

	@NotNull
	public List<String> getParentNamespacesFromPsi()
	{
		List<String> parentClasses = new ArrayList<String>();

		collectCompileTimeParentNamespaces(parentClasses);
		collectRuntimeParentNamespaces(parentClasses);

		return parentClasses;
	}

	protected void collectCompileTimeParentNamespaces(List<String> parentClasses)
	{
		for (PsiPerlUseStatement useStatement : PsiTreeUtil.findChildrenOfType(this, PsiPerlUseStatement.class))
		{
			if (PsiTreeUtil.getParentOfType(useStatement, PerlNamespaceDefinition.class) == this && useStatement.getPackageProcessor() instanceof PerlPackageParentsProvider)
			{
				((PerlPackageParentsProvider) useStatement.getPackageProcessor()).changeParentsList(useStatement, parentClasses);
			}
		}
	}

	protected void collectRuntimeParentNamespaces(List<String> parentClasses)
	{
		// checking runtime modifications
		for (PerlRuntimeParentsProvider provider : PsiTreeUtil.findChildrenOfType(this, PerlRuntimeParentsProvider.class))
		{
			if (PsiTreeUtil.getParentOfType(provider, PerlNamespaceDefinition.class) == this)
			{
				provider.changeParentsList(parentClasses);
			}
		}

		// checking runtime @ISA
		List<String> isa = getISA();

		// fixme, this is weak, isa may modify inheritance, not only overide
		if (isa != null)
		{
			parentClasses.clear();
			parentClasses.addAll(isa);
		}
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
		return new PerlItemPresentationSimple(this, getPresentableName());
	}

	@Override
	public PerlMroType getMroType()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
		{
			return stub.getMroType();
		}

		for (PsiPerlUseStatement useStatement : PsiTreeUtil.findChildrenOfType(this, PsiPerlUseStatement.class))
		{
			if (PsiTreeUtil.getParentOfType(useStatement, PerlNamespaceDefinition.class) == this && useStatement.getPackageProcessor() instanceof PerlMroProvider)
			{

				return ((PerlMroProvider) useStatement.getPackageProcessor()).getMroType(useStatement);
			}
		}

		return PerlMroType.DFS;
	}

	@Override
	public PerlMro getMro()
	{
		// fixme this should be another EP
		if (getMroType() == PerlMroType.C3)
		{
			return PerlMroC3.INSTANCE;
		}
		else
		{
			return PerlMroDfs.INSTANCE;
		}
	}

	@Override
	public void getLinearISA(HashSet<String> recursionMap, ArrayList<String> result)
	{
		getMro().getLinearISA(getProject(), getParentNamespaceDefinitions(), recursionMap, result);
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
		{
			return stub.isDeprecated();
		}
		return getAnnotationDeprecated() != null;
	}

	@Nullable
	@Override
	public List<String> getISA()
	{
		return getArrayAsList("ISA");
	}

	@NotNull
	@Override
	public List<String> getEXPORT()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getEXPORT();

		List<String> result = getArrayAsList("EXPORT");
		return result == null ? Collections.<String>emptyList() : result;
	}

	@NotNull
	@Override
	public List<String> getEXPORT_OK()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getEXPORT_OK();

		List<String> result = getArrayAsList("EXPORT_OK");
		return result == null ? Collections.<String>emptyList() : result;
	}

	@NotNull
	@Override
	public Map<String, List<String>> getEXPORT_TAGS()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getEXPORT_TAGS();

		Map<String, List<String>> result = getHashAsMap("EXPORT_TAGS");
		return result == null ? Collections.<String, List<String>>emptyMap() : result;
	}

	@Override
	public Map<String, Set<String>> getImportedSubsNames()
	{
		return PerlSubUtil.getImportedSubs(getProject(), getPackageName(), getContainingFile());
	}

	@Override
	public Map<String, Set<String>> getImportedScalarNames()
	{
		return PerlScalarUtil.getImportedScalars(getProject(), getPackageName(), getContainingFile());
	}

	@Override
	public Map<String, Set<String>> getImportedArrayNames()
	{
		return PerlArrayUtil.getImportedArrays(getProject(), getPackageName(), getContainingFile());
	}

	@Override
	public Map<String, Set<String>> getImportedHashNames()
	{
		return PerlHashUtil.getImportedHashes(getProject(), getPackageName(), getContainingFile());
	}

	/**
	 * Searches for array by name and returns distinct list of string values
	 *
	 * @param arrayName array name without a sigil
	 * @return distinct list of string values
	 */
	@Nullable
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

				if (assignExpression instanceof PerlVariableDeclarationWrapper && ((PerlVariableDeclarationWrapper) assignExpression).isGlobalDeclaration()) // proceed our @ARRAY =
				{
					assignElement = assignExpression.getParent();
					assignExpression = assignElement.getParent();

				}

				// checks for @ARRAY = ...
				if (assignExpression instanceof PsiPerlAssignExpr && assignElement.getNextSibling() != null)// not leftside element
					for (PerlStringContentElement element : PerlPsiUtil.collectStringElements(assignExpression.getLastChild()))
					{
						if (result == null)
							result = new HashSet<String>();
						result.add(element.getText());
					}
			}

//		System.err.println("Searched for @" + arrayName + " found: " + result);

		return result == null ? null : new ArrayList<String>(result);
	}

	public Map<String, List<String>> getHashAsMap(String hashMap)
	{
		// fixme NYI
		return Collections.emptyMap();
	}

	@Override
	public int getTextOffset()
	{
		PsiElement nameIdentifier = getNameIdentifier();

		return nameIdentifier == null
				? super.getTextOffset()
				: getNameIdentifier().getTextOffset();
	}


	public List<PsiElement> getDeclaredSubs()
	{
		PerlNamespaceDefinitionStub stub = getStub();
		List<PsiElement> result = new ArrayList<PsiElement>();

		if (stub != null)
		{
			collectDeclaredSubsFromStubs(stub, result);
		}
		else
		{
			collectDeclaredSubsFromPsi(result);
		}
		return result;
	}

	protected void collectDeclaredSubsFromStubs(Stub stub, List<PsiElement> result)
	{
		for (Stub element : stub.getChildrenStubs())
		{
			// fixme add parent stub checking here
			if (element instanceof PerlSubDefinitionStub)
			{
				result.add(((PerlSubDefinitionStub) element).getPsi());
			}
			collectDeclaredSubsFromStubs(element, result);
		}
	}

	protected void collectDeclaredSubsFromPsi(List<PsiElement> result)
	{
		for (PerlSubDefinition subDefinition : PsiTreeUtil.findChildrenOfType(this, PerlSubDefinition.class))
		{
			if (subDefinition.isValid() && this.equals(PsiTreeUtil.getParentOfType(subDefinition, PerlNamespaceDefinition.class)))
			{
				result.add(subDefinition);
			}
		}
	}

}
