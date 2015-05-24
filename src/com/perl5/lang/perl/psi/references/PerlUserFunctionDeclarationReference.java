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
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.impl.PerlSubDeclarationImpl;
import com.perl5.lang.perl.psi.impl.PerlSubDefinitionImpl;
import com.perl5.lang.perl.psi.impl.PerlUserFunctionImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 24.05.2015.
 */
public class PerlUserFunctionDeclarationReference extends PerlUserFunctionReference
{
	public PerlUserFunctionDeclarationReference(@NotNull PsiElement element, TextRange textRange) {
		super(element, textRange);
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		Project project = myElement.getProject();
		PsiFile file = myElement.getContainingFile();
		List<ResolveResult> result = new ArrayList<ResolveResult>();

		// declarations
		for( PerlSubDeclarationImpl sub : PsiTreeUtil.findChildrenOfType(file, PerlSubDeclarationImpl.class))
		{
			if( functionName.equals(sub.getUserFunction().getText()))
				result.add(new PsiElementResolveResult(sub.getUserFunction()));
		}

		// search in $self ierarchy - first occurance
		// search in package with full qualified name
		// search in used packages (poly reference)

		return result.toArray(new ResolveResult[result.size()]);
	}
}
