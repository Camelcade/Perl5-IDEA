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
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.references.PerlGlobVariableReference;
import com.perl5.lang.perl.util.PerlGlobUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 27.09.2015.
 */
public class PerlGlobVariableReferenceResolver implements ResolveCache.PolyVariantResolver<PerlGlobVariableReference>
{
	@NotNull
	@Override
	public ResolveResult[] resolve(@NotNull PerlGlobVariableReference perlGlobVariableReference, boolean incompleteCode)
	{
		List<ResolveResult> result = new ArrayList<ResolveResult>();

		PerlGlobVariable myVariable = perlGlobVariableReference.getVariable();
		String canonicalName = myVariable.getCanonicalName();
		Project project = myVariable.getProject();

		// resolve to other globs
		for (PerlGlobVariable glob : PerlGlobUtil.getGlobsDefinitions(project, canonicalName))
			if (!glob.equals(myVariable))
				result.add(new PsiElementResolveResult(glob));

		return result.toArray(new ResolveResult[result.size()]);
	}
}
