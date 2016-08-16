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
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.references.PerlSubReference;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 14.06.2015.
 */
public class PerlSubDeprecatedInspection extends PerlInspection
{
	@NotNull
	@Override
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PerlVisitor()
		{
			@Override
			public void visitSubDefinitionBase(@NotNull PerlSubDefinitionBase o)
			{
				processSubBase(o);
				super.visitSubDefinitionBase(o);
			}

			@Override
			public void visitSubDeclaration(@NotNull PsiPerlSubDeclaration o)
			{
				processSubBase(o);
				super.visitSubDeclaration(o);
			}

			private void processSubBase(@NotNull PerlSubBase o)
			{
				if (o.isDeprecated())
				{
					PsiElement nameIdentifier = o.getNameIdentifier();
					if (nameIdentifier != null)
					{
						markDeprecated(holder, nameIdentifier, PerlBundle.message("perl.sub.deprecated"));
					}
				}
			}

			@Override
			public void visitSubNameElement(@NotNull PerlSubNameElement o)
			{
				if (o.getParent() instanceof PerlMethod)
				{
					PsiReference reference = o.getReference();

					if (reference instanceof PerlSubReference)
					{
						for (ResolveResult resolveResult : ((PerlSubReference) reference).multiResolve(false))
						{
							PsiElement targetElement = resolveResult.getElement();
							if (targetElement instanceof PerlSubDefinitionBase && ((PerlSubDefinitionBase) targetElement).isDeprecated()
									|| targetElement instanceof PerlSubDeclaration && ((PerlSubDeclaration) targetElement).isDeprecated()
									)
							{
								markDeprecated(holder, o, PerlBundle.message("perl.sub.deprecated"));
								return;
							}
						}
					}
				}
			}
		};
	}

}
