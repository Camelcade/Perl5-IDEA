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
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.embedded.EmbeddedPerlFileType;
import com.perl5.lang.mojolicious.MojoliciousPerlFileType;
import com.perl5.lang.perl.extensions.packageprocessor.IPerlStrictProvider;
import com.perl5.lang.perl.idea.quickfixes.PerlUsePackageQuickFix;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 19.07.2015.
 */
public class PerlUseStrictInspection extends PerlInspection
{
	@NotNull
	@Override
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PerlVisitor()
		{
			@Override
			public void visitFile(PsiFile file)
			{
				// fixme this should be solved using #369
				if (file.getFileType() == EmbeddedPerlFileType.INSTANCE || file.getFileType() == MojoliciousPerlFileType.INSTANCE)
					return;

				for (PerlUseStatement useStatement : PsiTreeUtil.findChildrenOfType(file, PerlUseStatement.class))
					if( useStatement.getPackageProcessor() instanceof IPerlStrictProvider)
						return;

				holder.registerProblem(
						file,
						"No strict pragma found in the file",
						ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
						new PerlUsePackageQuickFix("strict")
				);
			}

		};
	}
}
