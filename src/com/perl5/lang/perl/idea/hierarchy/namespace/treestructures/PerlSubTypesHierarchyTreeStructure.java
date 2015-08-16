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

package com.perl5.lang.perl.idea.hierarchy.namespace.treestructures;

import com.intellij.ide.hierarchy.HierarchyNodeDescriptor;
import com.intellij.ide.hierarchy.HierarchyTreeStructure;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.idea.hierarchy.namespace.PerlPackageHierarchyNodeDescriptor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 16.08.2015.
 */
public class PerlSubTypesHierarchyTreeStructure extends HierarchyTreeStructure
{
	public PerlSubTypesHierarchyTreeStructure(@NotNull PsiElement element)
	{
		super(element.getProject(), new PerlPackageHierarchyNodeDescriptor(null, element, true));
	}

	@NotNull
	@Override
	protected Object[] buildChildren(HierarchyNodeDescriptor descriptor)
	{
		List<PerlPackageHierarchyNodeDescriptor> result = new ArrayList<PerlPackageHierarchyNodeDescriptor>();

		if (descriptor instanceof PerlPackageHierarchyNodeDescriptor)
		{
			PsiElement element = ((PerlPackageHierarchyNodeDescriptor) descriptor).getPsiElement();
			Project project = element.getProject();
			GlobalSearchScope allScope = GlobalSearchScope.allScope(project);
			GlobalSearchScope projectScope = GlobalSearchScope.projectScope(project);

			if (element instanceof PerlNamespaceDefinition)
			{
				String packageName = ((PerlNamespaceDefinition) element).getPackageName();
				Collection<PsiPerlNamespaceDefinition> definitions = PerlPackageUtil.getDerivedNamespaceDefinitions(project, packageName, projectScope);

				if (definitions.size() == 0)
					definitions = PerlPackageUtil.getDerivedNamespaceDefinitions(project, packageName, allScope);

				for (PsiPerlNamespaceDefinition definition : definitions)
					result.add(new PerlPackageHierarchyNodeDescriptor(descriptor, definition, false));
			}
		}

		return result.toArray();
	}
}
