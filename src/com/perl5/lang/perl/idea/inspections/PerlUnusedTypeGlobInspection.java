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
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.util.PerlGlobUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 14.08.2015.
 */
public class PerlUnusedTypeGlobInspection extends PerlInspection
{
	@NotNull
	@Override
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PerlVisitor()
		{
			@Override
			public void visitGlobVariable(@NotNull PsiPerlGlobVariable o)
			{
				if (o.getNamespaceElement() == null && PerlGlobUtil.BUILT_IN.contains(o.getName()))
				{
				}
				else if (ReferencesSearch.search(o, GlobalSearchScope.projectScope(o.getProject())).findFirst() == null)
				{
					holder.registerProblem(o, "Unused typeglob alias", ProblemHighlightType.LIKE_UNUSED_SYMBOL);
				}

			}
		};
	}
}
