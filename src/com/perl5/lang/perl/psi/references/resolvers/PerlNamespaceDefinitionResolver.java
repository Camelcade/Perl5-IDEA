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

package com.perl5.lang.perl.psi.references.resolvers;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.references.PerlNamespaceReference;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 27.09.2015.
 */
public class PerlNamespaceDefinitionResolver implements ResolveCache.PolyVariantResolver<PerlNamespaceReference>
{
	@NotNull
	@Override
	public ResolveResult[] resolve(@NotNull PerlNamespaceReference perlNamespaceReference, boolean incompleteCode)
	{
		PsiElement myElement = perlNamespaceReference.getElement();
		Project project = myElement.getProject();
		List<ResolveResult> result = new ArrayList<ResolveResult>();

		PsiElement parent = myElement.getParent();

		for (PerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, perlNamespaceReference.getCanonicalName()))
		{
			if (!parent.isEquivalentTo(namespaceDefinition))
				result.add(new PsiElementResolveResult(namespaceDefinition));
		}

		return result.toArray(new ResolveResult[result.size()]);
	}
}
