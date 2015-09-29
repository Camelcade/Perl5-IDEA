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

package com.perl5.lang.perl.idea.structureView.elements;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationBase;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by hurricup on 15.08.2015.
 */
public class PerlStructureViewElement implements StructureViewTreeElement, SortableTreeElement
{
	protected PsiElement myElement;
	protected boolean isInherited;
	protected boolean isImported;

	public PerlStructureViewElement(PsiElement element)
	{
		myElement = element;
	}

	public PerlStructureViewElement setInherited()
	{
		this.isInherited = true;
		return this;
	}

	public PerlStructureViewElement setImported()
	{
		this.isImported = true;
		return this;
	}

	public boolean isInherited()
	{
		return isInherited;
	}

	public boolean isImported()
	{
		return isImported;
	}

	@Override
	public Object getValue()
	{
		return myElement;
	}

	@Override
	public void navigate(boolean requestFocus)
	{
		if (myElement instanceof NavigationItem)
		{
			((NavigationItem) myElement).navigate(requestFocus);
		}
	}

	@Override
	public boolean canNavigate()
	{
		return myElement instanceof NavigationItem &&
				((NavigationItem) myElement).canNavigate();
	}

	@Override
	public boolean canNavigateToSource()
	{
		return myElement instanceof NavigationItem &&
				((NavigationItem) myElement).canNavigateToSource();
	}

	@NotNull
	@Override
	public String getAlphaSortKey()
	{
		assert myElement instanceof PsiNamedElement;
		String name = ((PsiNamedElement) myElement).getName();
		assert name != null;
		return name;
	}

	@NotNull
	@Override
	public ItemPresentation getPresentation()
	{

		ItemPresentation itemPresentation = null;

		if (myElement instanceof NavigationItem)
			itemPresentation = ((NavigationItem) myElement).getPresentation();

		if (itemPresentation == null)
			itemPresentation = new PerlItemPresentationSimple(myElement, "FIXME");
		else if ((isInherited() || isImported()) && itemPresentation instanceof PerlItemPresentationBase)
			if (getValue() instanceof PerlDeprecatable && ((PerlDeprecatable) getValue()).isDeprecated())
				((PerlItemPresentationBase) itemPresentation).setAttributesKey(PerlSyntaxHighlighter.UNUSED_DEPRECATED);
			else
				((PerlItemPresentationBase) itemPresentation).setAttributesKey(CodeInsightColors.NOT_USED_ELEMENT_ATTRIBUTES);

		return itemPresentation;
	}

	@NotNull
	@Override
	public TreeElement[] getChildren()
	{
		List<TreeElement> result = new ArrayList<TreeElement>();

		HashSet<String> implementedMethods = new HashSet<String>();

		if (myElement instanceof PerlFile || myElement instanceof PerlNamespaceDefinition)
		{
			// nested namespaces
			for (PerlNamespaceDefinition child : PsiTreeUtil.findChildrenOfType(myElement, PerlNamespaceDefinition.class))
				if (myElement.isEquivalentTo(PsiTreeUtil.getParentOfType(child, PerlNamespaceContainer.class)))
					result.add(new PerlStructureViewElement(child));

			// global variables
			for (PerlVariableDeclarationWrapper child : PsiTreeUtil.findChildrenOfType(myElement, PerlVariableDeclarationWrapper.class))
			{
				if (child.isGlobalDeclaration() && myElement.isEquivalentTo(PsiTreeUtil.getParentOfType(child, PerlNamespaceContainer.class)))
				{
					result.add(new PerlVariableDeclarationStructureViewElement(child));
				}
			}
			
			Project project = myElement.getProject();
			GlobalSearchScope projectScope = GlobalSearchScope.projectScope(project);

			// imported scalars
			for (Map.Entry<String, Set<String>> importEntry : ((PerlNamespaceContainer) myElement).getImportedScalarNames().entrySet())
				for (String variableName : importEntry.getValue())
				{
					String canonicalName = importEntry.getKey() + "::" + variableName;

					Collection<PerlVariableDeclarationWrapper> variables = PerlScalarUtil.getGlobalScalarDefinitions(project, canonicalName);

					for (PerlVariableDeclarationWrapper variable : variables)
						result.add(new PerlVariableDeclarationStructureViewElement(variable).setImported());

					// globs
					Collection<PsiPerlGlobVariable> items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName, projectScope);
					if (items.size() == 0)
						items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName);

					for (PerlGlobVariable item : items)
						result.add(new PerlGlobStructureViewElement(item).setImported());
				}

			// imported arrays
			for (Map.Entry<String, Set<String>> importEntry : ((PerlNamespaceContainer) myElement).getImportedArrayNames().entrySet())
				for (String variableName : importEntry.getValue())
				{
					String canonicalName = importEntry.getKey() + "::" + variableName;

					Collection<PerlVariableDeclarationWrapper> variables = PerlArrayUtil.getGlobalArrayDefinitions(project, canonicalName);

					for (PerlVariableDeclarationWrapper variable : variables)
						result.add(new PerlVariableDeclarationStructureViewElement(variable).setImported());

					// globs
					Collection<PsiPerlGlobVariable> items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName, projectScope);
					if (items.size() == 0)
						items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName);

					for (PerlGlobVariable item : items)
						result.add(new PerlGlobStructureViewElement(item).setImported());
				}

			// imported hashes
			for (Map.Entry<String, Set<String>> importEntry : ((PerlNamespaceContainer) myElement).getImportedHashNames().entrySet())
				for (String variableName : importEntry.getValue())
				{
					String canonicalName = importEntry.getKey() + "::" + variableName;

					Collection<PerlVariableDeclarationWrapper> variables = PerlHashUtil.getGlobalHashDefinitions(project, canonicalName);

					for (PerlVariableDeclarationWrapper variable : variables)
						result.add(new PerlVariableDeclarationStructureViewElement(variable).setImported());

					// globs
					Collection<PsiPerlGlobVariable> items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName, projectScope);
					if (items.size() == 0)
						items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName);

					for (PerlGlobVariable item : items)
						result.add(new PerlGlobStructureViewElement(item).setImported());
				}


			// Imported subs
			for (Map.Entry<String, Set<String>> importEntry : ((PerlNamespaceContainer) myElement).getImportedSubsNames().entrySet())
				for (String subName : importEntry.getValue())
				{
					String canonicalName = importEntry.getKey() + "::" + subName;

					// declarations
					Collection<PsiPerlSubDeclaration> subDeclarations = PerlSubUtil.getSubDeclarations(project, canonicalName, projectScope);
					if (subDeclarations.size() == 0)
						subDeclarations = PerlSubUtil.getSubDeclarations(project, canonicalName);

					for (PsiPerlSubDeclaration item : subDeclarations)
						result.add(new PerlSubStructureViewElement(item).setImported());

					// definitions
					Collection<PsiPerlSubDefinition> subDefinitions = PerlSubUtil.getSubDefinitions(project, canonicalName, projectScope);
					if (subDefinitions.size() == 0)
						subDefinitions = PerlSubUtil.getSubDefinitions(project, canonicalName);

					for (PerlSubDefinition item : subDefinitions)
						result.add(new PerlSubStructureViewElement(item).setImported());

					// constants
					Collection<PerlConstant> constantsDefinitions = PerlSubUtil.getConstantsDefinitions(project, canonicalName, projectScope);
					if (constantsDefinitions.size() == 0)
						constantsDefinitions = PerlSubUtil.getConstantsDefinitions(project, canonicalName);

					for (PerlConstant item : constantsDefinitions)
						result.add(new PerlConstantStructureViewElement(item).setImported());

					// globs
					Collection<PsiPerlGlobVariable> items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName, projectScope);
					if (items.size() == 0)
						items = PerlGlobUtil.getGlobsDefinitions(project, canonicalName);

					for (PerlGlobVariable item : items)
						result.add(new PerlGlobStructureViewElement(item).setImported());
				}


			for (PerlGlobVariable child : PsiTreeUtil.findChildrenOfType(myElement, PerlGlobVariable.class))
				if (child.isLeftSideOfAssignment() && myElement.isEquivalentTo(PsiTreeUtil.getParentOfType(child, PerlNamespaceContainer.class)))
				{
					implementedMethods.add(child.getName());
					result.add(new PerlGlobStructureViewElement(child));
				}

			for (PerlConstant child : PsiTreeUtil.findChildrenOfType(myElement, PerlConstant.class))
				if (myElement.isEquivalentTo(PsiTreeUtil.getParentOfType(child, PerlNamespaceContainer.class)))
				{
					implementedMethods.add(child.getName());
					result.add(new PerlConstantStructureViewElement(child));
				}

			for (PerlSubDeclaration child : PsiTreeUtil.findChildrenOfType(myElement, PerlSubDeclaration.class))
				if (myElement.isEquivalentTo(PsiTreeUtil.getParentOfType(child, PerlNamespaceContainer.class)))
					result.add(new PerlSubStructureViewElement(child));

			for (PerlSubDefinition child : PsiTreeUtil.findChildrenOfType(myElement, PerlSubDefinition.class))
				if (myElement.isEquivalentTo(PsiTreeUtil.getParentOfType(child, PerlNamespaceContainer.class)))
				{
					implementedMethods.add(child.getName());
					result.add(new PerlSubStructureViewElement(child));
				}

		}

		if (myElement instanceof PerlNamespaceDefinition)
		{
			List<TreeElement> inheritedResult = new ArrayList<TreeElement>();

			for (PsiElement element : PerlMro.getVariants(myElement.getProject(), ((PerlNamespaceDefinition) myElement).getName(), true))
				if (element instanceof PerlNamedElement && !implementedMethods.contains(((PerlNamedElement) element).getName()))
				{
					if (element instanceof PerlSubDefinition)
						inheritedResult.add(new PerlSubStructureViewElement((PerlSubDefinition) element).setInherited());
					else if (element instanceof PerlSubDeclaration)
						inheritedResult.add(new PerlSubStructureViewElement((PerlSubDeclaration) element).setInherited());
					else if (element instanceof PerlGlobVariable && ((PerlGlobVariable) element).isLeftSideOfAssignment() && ((PerlGlobVariable) element).getName() != null)
						inheritedResult.add(new PerlGlobStructureViewElement((PerlGlobVariable) element).setInherited());
					else if (element instanceof PerlConstant && ((PerlConstant) element).getName() != null)
						inheritedResult.add(new PerlConstantStructureViewElement((PerlConstant) element).setInherited());
				}

			if (inheritedResult.size() > 0)
				result.addAll(0, inheritedResult);
		}

		return result.toArray(new TreeElement[result.size()]);
	}

}
