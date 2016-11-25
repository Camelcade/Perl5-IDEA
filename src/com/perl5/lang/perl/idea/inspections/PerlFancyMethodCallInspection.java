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
import com.perl5.lang.perl.idea.quickfixes.PerlFancyMethodQuickFix;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 17.07.2015.
 */
public class PerlFancyMethodCallInspection extends PerlInspection
{
	@NotNull
	@Override
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PerlVisitor()
		{
			@Override
			public void visitMethod(@NotNull PsiPerlMethod o)
			{
				if (o.isObjectMethod() && o.getLastChild() instanceof PerlNamespaceElement)
				{
					String packageName = o.getPackageName();
					if (packageName == null)
					{
						return;
					}
					PerlSubNameElement subNameElement = o.getSubNameElement();
					if (subNameElement == null)
					{
						return;
					}
					String properForm = String.format("%s->%s", packageName, subNameElement.getName());
					holder.registerProblem(
							o,
							String.format("Using of fancy calls is not recommended, use %s(...)", properForm),
							ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
							new PerlFancyMethodQuickFix(properForm)
					);
				}
			}
		};
	}

}
