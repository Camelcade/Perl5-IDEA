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

package com.perl5.lang.perl.idea.inspections;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by hurricup on 16.08.2015.
 */
public class PerlNamespaceRecursiveInheritanceInspection extends PerlInspection
{
	public static boolean hasRecursiveInheritance(PerlNamespaceDefinition definition)
	{
		return hasRecursiveInheritance(definition.getProject(), definition.getPackageName(), new HashSet<String>());
	}

	private static boolean hasRecursiveInheritance(Project project, String packageName, HashSet<String> passedWay)
	{
		Collection<PerlNamespaceDefinition> definitions = PerlPackageUtil.getNamespaceDefinitions(project, packageName, GlobalSearchScope.projectScope(project));
		if (definitions.size() > 0)
		{
			HashSet<String> parents = new HashSet<String>();
			for (PerlNamespaceDefinition definition : definitions)
				parents.addAll(definition.getParentNamespaces());

			if (parents.size() > 0)
			{
				passedWay.add(packageName);
				for (String parent : parents)
					if (passedWay.contains(parent))
						return true;
					else if (hasRecursiveInheritance(project, parent, passedWay))
						return true;
			}
		}
		return false;
	}

	@NotNull
	@Override
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PerlVisitor()
		{
			@Override
			public void visitNamespaceDefinition(@NotNull PsiPerlNamespaceDefinition o)
			{
				PerlNamespaceElement namespaceElement = o.getNamespaceElement();
				if (namespaceElement == null || "main".equals(namespaceElement.getCanonicalName()))
					return;

				if (hasRecursiveInheritance(o))
				{
					registerError(holder, namespaceElement.getContainingFile(), "Namespace " + o.getName() + " has recursive inheritance");
					registerError(holder, namespaceElement, "Namespace " + o.getName() + " has recursive inheritance");

				}

			}
		};
	}

}
