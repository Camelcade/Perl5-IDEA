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
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 05.06.2015.
 */
public class PerlGlobVariableNameReference extends PerlReferencePoly
{
	PerlGlobVariable myVariable;
	boolean isScalar;

	public PerlGlobVariableNameReference(@NotNull PsiElement element, TextRange textRange)
	{
		super(element, textRange);
		PsiElement parent = element.getParent();
		assert parent instanceof PerlGlobVariable;
		myVariable = (PerlGlobVariable)parent;
		isScalar = myVariable.getScalarSigils() != null;
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		if( isScalar )
			return multiResolveScalar(incompleteCode);

		List<ResolveResult> result = new ArrayList<>();

		String canonicalName = myVariable.getCanonicalName();
		Project project = myVariable.getProject();

		// resolve to other globs
		for(PerlGlobVariable glob: PerlGlobUtil.findGlobsDefinitions(project, canonicalName))
			if( !glob.equals(myVariable))
				result.add(new PsiElementResolveResult(glob));


		return result.toArray(new ResolveResult[result.size()]);
	}

	// fixme not dry, duplicates from variablename
	public ResolveResult[] multiResolveScalar(boolean incompleteCode)
	{
		return new ResolveResult[0];
	}


	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		PsiElement parent = element.getParent();
		if( parent instanceof PerlVariable || parent instanceof PerlGlobVariable || parent instanceof PerlSubBase)
			return super.isReferenceTo(parent) || super.isReferenceTo(element);

		return super.isReferenceTo(element);
	}

	@Nullable
	@Override
	// fixme not dry, duplicates from variablename
	public PsiElement resolve()
	{
		ResolveResult[] resolveResults = multiResolve(false);
		if( resolveResults.length == 0)
			return null;
		else if( resolveResults.length == 1 )
			return resolveResults[0].getElement();

		for( ResolveResult resolveResult: resolveResults)
			if( resolveResult.getElement() instanceof PerlGlobVariable)
				return resolveResult.getElement();

		return resolveResults[0].getElement();
	}


}
