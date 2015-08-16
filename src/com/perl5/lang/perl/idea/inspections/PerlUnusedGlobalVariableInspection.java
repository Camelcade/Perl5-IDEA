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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlVariableDeclarationGlobal;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by hurricup on 14.08.2015.
 */
public class PerlUnusedGlobalVariableInspection extends PerlUnusedLexicalVariableInspection
{
	public static final HashSet<String> EXCLUSIONS = new HashSet<String>(Arrays.asList(
			"@ISA",
			"@EXPORT_OK",
			"@EXPORT",

			"%EXPORT_TAGS",

			"$VERSION"
	));

	@NotNull
	@Override
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PerlVisitor()
		{
			@Override
			public void visitVariableDeclarationGlobal(@NotNull PsiPerlVariableDeclarationGlobal o)
			{
				checkDeclaration(holder, o);
			}
		};
	}

	@Override
	public boolean isVariableUnused(PerlVariable variable)
	{
		// fixme maybe we should check for @{ISA} too, but it's rare and this one works faster
		return !EXCLUSIONS.contains(variable.getText()) && super.isVariableUnused(variable);
	}

	@Override
	protected SearchScope getSearchScope(PsiElement element)
	{
		return GlobalSearchScope.projectScope(element.getProject());
	}

	@Override
	protected String getErrorMessage(PerlVariable variable)
	{
		return "Unused global variable " + variable.getText();
	}
}
