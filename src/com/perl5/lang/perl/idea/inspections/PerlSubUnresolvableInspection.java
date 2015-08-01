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
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 14.06.2015.
 */
public class PerlSubUnresolvableInspection extends PerlInspection
{
	@NotNull
	@Override
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PerlVisitor()
		{
			@Override
			public void visitPerlMethod(@NotNull PerlMethod o)
			{
				PerlNamespaceElement namespaceElement = o.getNamespaceElement();
				PerlSubNameElement subNameElement = o.getSubNameElement();

				boolean hasExplicitNamespace = namespaceElement != null && !"CORE".equals(namespaceElement.getCanonicalName());

				if (subNameElement == null || (namespaceElement != null && namespaceElement.isBuiltin()) || (!hasExplicitNamespace && subNameElement.isBuiltIn()))
					return;

				// todo globs aliasing must be resolved
				List<PerlSubDefinition> subDefinitions = subNameElement.getSubDefinitions();
				List<PerlSubDeclaration> subDeclarations = subNameElement.getSubDeclarations();
				List<PerlGlobVariable> relatedGlobs = subNameElement.getRelatedGlobs();

				if (subDefinitions.size() == 0 && subDeclarations.size() == 0 && relatedGlobs.size() == 0)
					registerProblem(holder, subNameElement, "Unable to find sub definition, declaration or typeglob aliasing");
			}
		};
	}
}
