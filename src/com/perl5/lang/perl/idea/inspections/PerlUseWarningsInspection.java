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
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.extensions.packageprocessor.PerlWarningsProvider;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import com.perl5.lang.perl.idea.quickfixes.PerlUsePackageQuickFix;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.PerlVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 19.07.2015.
 */
public class PerlUseWarningsInspection extends PerlInspection
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
				FileType fileType = file.getFileType();
				if (!(fileType instanceof PerlFileType) || !((PerlFileType) fileType).checkWarningsPragma())
				{
					return;
				}

				for (PerlUseStatement useStatement : PsiTreeUtil.findChildrenOfType(file, PerlUseStatement.class))
					if (useStatement.getPackageProcessor() instanceof PerlWarningsProvider)
						return;
				holder.registerProblem(
						file,
						"No warnings pragma found in the file",
						ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
						new PerlUsePackageQuickFix("warnings FATAL => 'all'"),
						new PerlUsePackageQuickFix("warnings")
				);
			}
		};
	}
}
