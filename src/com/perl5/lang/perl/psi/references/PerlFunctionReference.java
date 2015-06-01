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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.util.PerlFunctionUtil;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PerlFunctionReference extends PerlReferencePoly
{
	String functionName;
	String packageName = null;
	String canonicalName;

	public PerlFunctionReference(@NotNull PsiElement element, TextRange textRange) {
		super(element, textRange);
		assert element instanceof PerlFunction;
		functionName = ((PerlFunction) element).getName();

		PsiElement parent = element.getParent();

		if( packageName == null && parent instanceof PerlMethod)
			packageName = ((PerlMethod) parent).getPackageName();

		// this is currently available in subs
		if( packageName == null )
			packageName = PerlPackageUtil.getContextPackageName(element);

		canonicalName = packageName + "::" + functionName;
	}

	@NotNull
	@Override
	public Object[] getVariants()
	{
		return new Object[0];
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		Project project = myElement.getProject();
		List<ResolveResult> result = new ArrayList<ResolveResult>();

		// subs definitions
		for( PerlSubDefinition sub : PerlFunctionUtil.findSubDefinitions(project, canonicalName))
		{
			PerlFunction perlFunction = sub.getUserFunction();

			if( perlFunction != null && perlFunction != myElement)
				result.add(new PsiElementResolveResult(perlFunction));
		}

		// globs definitions
		for( PerlPerlGlob glob : PerlGlobUtil.findGlobsDefinitions(project, canonicalName))
		{
			result.add(new PsiElementResolveResult(glob.getVariableName()));
		}

		return result.toArray(new ResolveResult[result.size()]);
	}

	@Nullable
	@Override
	public PsiElement resolve()
	{
		ResolveResult[] resolveResults = multiResolve(false);
		return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
	}
}
