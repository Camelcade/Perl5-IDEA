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
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlRequireExpr;
import com.perl5.lang.perl.psi.PsiPerlUseStatement;
import com.perl5.lang.perl.psi.impl.PerlFileElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 14.06.2015.
 */
public class PerlPackageFileInspection extends PerlInspection
{
	@NotNull
	@Override
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PerlVisitor()
		{

			@Override
			public void visitUseStatement(@NotNull PsiPerlUseStatement o)
			{
				if (o.getNamespaceElement() != null)
					checkPackageFile(o.getNamespaceElement());
			}

			@Override
			public void visitRequireExpr(@NotNull PsiPerlRequireExpr o)
			{
				if (o.getNamespaceElement() != null)
					checkPackageFile(o.getNamespaceElement());
			}

			public void checkPackageFile(PerlNamespaceElement o)
			{
				List<PerlFileElement> namespaceFiles = o.getNamespaceFiles();

				if (namespaceFiles.size() == 0)
					registerProblem(holder, o, "Unable to find package file");
			}
		};
	}
}
