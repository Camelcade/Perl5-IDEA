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
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mro.PerlMroDfs;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import com.perl5.lang.perl.psi.references.PerlSubReferenceSimple;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 26.11.2015.
 */
public class PerlSubReferenceResolverSimple implements ResolveCache.PolyVariantResolver<PerlSubReferenceSimple>
{
	@NotNull
	@Override
	public ResolveResult[] resolve(@NotNull PerlSubReferenceSimple reference, boolean incompleteCode)
	{
		PsiElement myElement = reference.getElement();
		List<PsiElement> relatedItems = new ArrayList<PsiElement>();

		String packageName = PerlPackageUtil.getContextPackageName(myElement);
		String subName = myElement.getNode().getText();
		Project project = myElement.getProject();

		relatedItems.addAll(PerlMroDfs.resolveSub(
				project,
				packageName,
				subName,
				false
		));

		List<ResolveResult> result = reference.getResolveResults(relatedItems);

		return result.toArray(new ResolveResult[result.size()]);
	}
}
