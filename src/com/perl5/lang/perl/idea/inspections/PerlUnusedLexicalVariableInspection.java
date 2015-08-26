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

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclaration;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlVariableDeclarationLexical;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by hurricup on 18.07.2015.
 */
public class PerlUnusedLexicalVariableInspection extends PerlInspection
{
	@NotNull
	@Override
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PerlVisitor()
		{
			@Override
			public void visitVariableDeclarationLexical(@NotNull PsiPerlVariableDeclarationLexical o)
			{
				checkDeclaration(holder, o);
			}
		};
	}

	public <T extends PerlVariableDeclaration> void checkDeclaration(ProblemsHolder holder, T declaration)
	{
		checkVariables(holder, declaration.getArrayVariableList());
		checkVariables(holder, declaration.getScalarVariableList());
		checkVariables(holder, declaration.getHashVariableList());
	}

	public <T extends PerlVariable> void checkVariables(ProblemsHolder holder, Collection<T> variableList)
	{
		for (PerlVariable variable : variableList)
			if (isVariableUnused(variable))
				holder.registerProblem(variable, getErrorMessage(variable), ProblemHighlightType.LIKE_UNUSED_SYMBOL);
	}


	public boolean isVariableUnused(PerlVariable variable)
	{
		return ReferencesSearch.search(variable, getSearchScope(variable)).findFirst() == null;
	}

	protected SearchScope getSearchScope(PsiElement element)
	{
		return new LocalSearchScope(element.getContainingFile());
	}

	protected String getErrorMessage(PerlVariable variable)
	{
		return "Unused lexical variable:" + variable.getText();
	}
}
