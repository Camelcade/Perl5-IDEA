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

package com.perl5.lang.perl.idea.quickfixes;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlMethod;
import com.perl5.lang.perl.psi.PsiPerlSubCallExpr;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 17.07.2015.
 */
public class PerlFancyMethodQuickFix implements LocalQuickFix
{
	String myNewForm;

	public PerlFancyMethodQuickFix(String newForm)
	{
		super();
		myNewForm = newForm;
	}

	@Nls
	@NotNull
	@Override
	public String getName()
	{
		return "Re-format method invocation to " + myNewForm + "(...)";
	}

	@NotNull
	@Override
	public String getFamilyName()
	{
		return "Re-format invocation";
	}

	@Override
	public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor)
	{
		PsiElement method = descriptor.getPsiElement();
		assert method instanceof PerlMethod;

		PsiElement currentCallExpression = method.getParent();
		assert currentCallExpression instanceof PsiPerlSubCallExpr;

		// fixme ok, this is works, but we make syntax tree invalid, will be updated with next reparsing. Not sure it's a problem
		method.replace(PerlElementFactory.createMethodCall(project, ((PerlMethod) method).getNamespaceElement().getCanonicalName(), ((PerlMethod) method).getSubNameElement().getName()));
	}


}
