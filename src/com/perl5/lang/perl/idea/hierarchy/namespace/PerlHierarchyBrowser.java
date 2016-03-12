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

package com.perl5.lang.perl.idea.hierarchy.namespace;

import com.intellij.ide.hierarchy.HierarchyNodeDescriptor;
import com.intellij.ide.hierarchy.HierarchyTreeStructure;
import com.intellij.ide.hierarchy.TypeHierarchyBrowserBase;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.hierarchy.namespace.treestructures.PerlSubTypesHierarchyTreeStructure;
import com.perl5.lang.perl.idea.hierarchy.namespace.treestructures.PerlSuperTypesHierarchyTreeStructure;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by hurricup on 16.08.2015.
 */
public class PerlHierarchyBrowser extends TypeHierarchyBrowserBase
{
	public PerlHierarchyBrowser(PsiElement element)
	{
		super(element.getProject(), element);
	}

	@Override
	protected boolean isInterface(PsiElement psiElement)
	{
		return false;
	}

	@Override
	protected boolean canBeDeleted(PsiElement psiElement)
	{
		return false;
	}

	@Override
	protected String getQualifiedName(PsiElement psiElement)
	{
		if (psiElement instanceof PerlNamedElement)
			return ((PerlNamedElement) psiElement).getPresentableName();

		return null;
	}

	@Nullable
	@Override
	protected PsiElement getElementFromDescriptor(@NotNull HierarchyNodeDescriptor descriptor)
	{
		if (!(descriptor instanceof PerlHierarchyNodeDescriptor))
		{
			return null;
		}
		return ((PerlHierarchyNodeDescriptor) descriptor).getPerlElement();
	}

	@Override
	protected void createTrees(@NotNull Map<String, JTree> trees)
	{
		trees.put(SUPERTYPES_HIERARCHY_TYPE, createTree(true));
		trees.put(SUBTYPES_HIERARCHY_TYPE, createTree(true));
		trees.put(TYPE_HIERARCHY_TYPE, createTree(true));
	}

	@Nullable
	@Override
	protected JPanel createLegendPanel()
	{
		return null;
	}

	@Override
	protected boolean isApplicableElement(@NotNull PsiElement element)
	{
		return element instanceof PerlNamespaceDefinition;
	}

	@Nullable
	@Override
	protected HierarchyTreeStructure createHierarchyTreeStructure(@NotNull String typeName, @NotNull PsiElement psiElement)
	{
		if (SUPERTYPES_HIERARCHY_TYPE.equals(typeName))
		{
			return getSuperTypesHierarchyStructure(psiElement);
		}
		else if (SUBTYPES_HIERARCHY_TYPE.equals(typeName))
		{
			return getSubTypesHierarchyStructure(psiElement);
		}
		else if (TYPE_HIERARCHY_TYPE.equals(typeName))
		{
			return getTypesHierarchyStructure(psiElement);
		}
		return null;
	}

	@Nullable
	@Override
	protected Comparator<NodeDescriptor> getComparator()
	{
		return null;
	}

	@Nullable
	protected HierarchyTreeStructure getSuperTypesHierarchyStructure(PsiElement psiElement)
	{
		return new PerlSuperTypesHierarchyTreeStructure(psiElement);
	}

	@Nullable
	protected HierarchyTreeStructure getSubTypesHierarchyStructure(PsiElement psiElement)
	{
		return new PerlSubTypesHierarchyTreeStructure(psiElement);
	}

	@Nullable
	protected HierarchyTreeStructure getTypesHierarchyStructure(PsiElement psiElement)
	{
		return null;
	}
}
