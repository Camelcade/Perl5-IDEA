/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.tt2.idea;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.inspections.PerlInspection;
import com.perl5.lang.tt2.parser.TemplateToolkitParserUtil;
import com.perl5.lang.tt2.psi.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 14.06.2016.
 */
public class TemplateToolkitOrphanDirectiveInspection extends PerlInspection
{
	@NotNull
	@Override
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new TemplateToolkitVisitor()
		{
			@Override
			public void visitEndDirective(@NotNull PsiEndDirective o)
			{
				checkInAnyBlock(o);
			}

			@Override
			public void visitElseDirective(@NotNull PsiElseDirective o)
			{
				checkInIf(o);
			}

			@Override
			public void visitElsifDirective(@NotNull PsiElsifDirective o)
			{
				checkInIf(o);
			}

			@Override
			public void visitCatchDirective(@NotNull PsiCatchDirective o)
			{
				checkInTry(o);
			}

			@Override
			public void visitFinalDirective(@NotNull PsiFinalDirective o)
			{
				checkInTry(o);
			}

			@Override
			public void visitCaseDirective(@NotNull PsiCaseDirective o)
			{
				if (isInSwitch(o))
				{
					return;
				}

				registerError(holder, o, PerlBundle.message("tt2.error.case.outside.switch"));
			}

			private boolean isInSwitch(@NotNull PsiElement element)
			{
				PsiElement parent = element.getParent();
				if (parent == null) // branch
				{
					return false;
				}
				return parent.getParent() instanceof PsiSwitchBlock;
			}

			private void checkInIf(@NotNull PsiElement element)
			{
				if (isInIf(element))
				{
					return;
				}

				registerError(holder, element, PerlBundle.message("tt2.error.outside.if.block"));
			}

			private boolean isInIf(@NotNull PsiElement element)
			{
				PsiElement parent = element.getParent();
				if (parent == null) // branch
				{
					return false;
				}
				parent = parent.getParent();
				return parent instanceof PsiIfBlock || parent instanceof PsiUnlessBlock;
			}

			private void checkInTry(@NotNull PsiElement element)
			{
				if (isInTry(element))
				{
					return;
				}

				registerError(holder, element, PerlBundle.message("tt2.error.outside.try.block"));
			}

			private boolean isInTry(@NotNull PsiElement element)
			{
				PsiElement parent = element.getParent();
				if (parent == null) // branch
				{
					return false;
				}
				return parent.getParent() instanceof PsiTryCatchBlock;
			}


			private void checkInAnyBlock(@NotNull PsiElement element)
			{
				PsiElement parent = element.getParent();
				if (parent != null && TemplateToolkitParserUtil.BLOCK_CONTAINERS.contains(parent.getNode().getElementType()))
				{
					return;
				}
				registerError(holder, element, PerlBundle.message("tt2.error.end.outside.block"));
			}

		};
	}
}
