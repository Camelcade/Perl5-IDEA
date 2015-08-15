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

package com.perl5.lang.perl.idea.structureView;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 15.08.2015.
 */
public class PerlStructureViewElement implements StructureViewTreeElement, SortableTreeElement
{
	protected PsiElement myElement;

	public PerlStructureViewElement(PsiElement element)
	{
		myElement = element;
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

		return itemPresentation;
	}

	@NotNull
	@Override
	public TreeElement[] getChildren()
	{
		List<TreeElement> result = new ArrayList<TreeElement>();

		if (myElement instanceof PerlFile || myElement instanceof PerlNamespaceDefinition || myElement instanceof PerlSubDefinition)
			for (PerlNamespaceDefinition child : PsiTreeUtil.findChildrenOfType(myElement, PerlNamespaceDefinition.class))
				if (myElement.isEquivalentTo(PsiTreeUtil.getParentOfType(child, PerlNamespaceContainer.class)))
					result.add(new PerlStructureViewElement(child));

		if (myElement instanceof PerlFile || myElement instanceof PerlNamespaceDefinition)
		{
			for (PsiPerlVariableDeclarationGlobal child : PsiTreeUtil.findChildrenOfType(myElement, PsiPerlVariableDeclarationGlobal.class))
				if (myElement.isEquivalentTo(PsiTreeUtil.getParentOfType(child, PerlNamespaceContainer.class)))
				{
					for (PerlVariable variable : child.getScalarVariableList())
						result.add(new PerlStructureViewElement(variable));
					for (PerlVariable variable : child.getArrayVariableList())
						result.add(new PerlStructureViewElement(variable));
					for (PerlVariable variable : child.getHashVariableList())
						result.add(new PerlStructureViewElement(variable));
				}

			for (PerlGlobVariable child : PsiTreeUtil.findChildrenOfType(myElement, PerlGlobVariable.class))
				if (child.isLeftSideOfAssignment() && myElement.isEquivalentTo(PsiTreeUtil.getParentOfType(child, PerlNamespaceContainer.class)))
					result.add(new PerlStructureViewElement(child));

			for (PerlConstant child : PsiTreeUtil.findChildrenOfType(myElement, PerlConstant.class))
				if (myElement.isEquivalentTo(PsiTreeUtil.getParentOfType(child, PerlNamespaceContainer.class)))
					result.add(new PerlStructureViewElement(child));

			for (PerlSubDeclaration child : PsiTreeUtil.findChildrenOfType(myElement, PerlSubDeclaration.class))
				if (myElement.isEquivalentTo(PsiTreeUtil.getParentOfType(child, PerlNamespaceContainer.class)))
					result.add(new PerlStructureViewElement(child));

			for (PerlSubDefinition child : PsiTreeUtil.findChildrenOfType(myElement, PerlSubDefinition.class))
				if (myElement.isEquivalentTo(PsiTreeUtil.getParentOfType(child, PerlNamespaceContainer.class)))
					result.add(new PerlStructureViewElement(child));
		}

		return result.toArray(new TreeElement[result.size()]);
	}
}
