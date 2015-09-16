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

package com.perl5.lang.perl.idea.refactoring;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import com.perl5.lang.perl.psi.PsiPerlVariableDeclarationLexical;
import com.perl5.lang.perl.psi.PsiPerlVariableDeclarationLocal;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by evstigneev on 14.07.2015.
 */
public class PerlRenameVariableProcessor extends RenamePsiElementProcessor
{
	@Override
	public boolean canProcessElement(@NotNull PsiElement element)
	{
		return element instanceof PerlVariableNameElement && element.getParent() instanceof PerlVariable;
	}

	@NotNull
	@Override
	public Collection<PsiReference> findReferences(PsiElement element)
	{
		PsiElement variableElement = element.getParent();
		PsiElement parentElement = variableElement.getParent();

		if (variableElement instanceof PerlVariable
				&& (parentElement instanceof PsiPerlVariableDeclarationLexical || parentElement instanceof PsiPerlVariableDeclarationLocal)
				)
		{
			return ReferencesSearch.search(element, new LocalSearchScope(((PerlVariable) variableElement).getLexicalScope())).findAll();
		}

		return super.findReferences(element);
	}
}
