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

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;

/**
 * Created by hurricup on 14.06.2015.
 */
public abstract class PerlInspection extends LocalInspectionTool
{
	long startTime;

	protected void registerProblem(ProblemsHolder holder, PsiElement element, String message)
	{
		if (!element.getNode().getText().isEmpty())
			holder.registerProblem(element, message, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
	}

	protected void registerError(ProblemsHolder holder, PsiElement element, String message)
	{
		if (!element.getNode().getText().isEmpty())
			holder.registerProblem(element, message, ProblemHighlightType.GENERIC_ERROR);
	}

	protected void markDeprecated(ProblemsHolder holder, PsiElement element, String message)
	{
		if (!element.getNode().getText().isEmpty())
			holder.registerProblem(element, message, ProblemHighlightType.LIKE_DEPRECATED);
	}

/*
	@Override
	public void inspectionStarted(LocalInspectionToolSession session, boolean isOnTheFly)
	{
		startTime = System.currentTimeMillis() / 1000;
		super.inspectionStarted(session, isOnTheFly);
	}

	@Override
	public void inspectionFinished(LocalInspectionToolSession session, ProblemsHolder problemsHolder)
	{
		super.inspectionFinished(session, problemsHolder);
		long duration = System.currentTimeMillis() / 1000 - startTime;
		System.err.println("Finished inspection: " + getClass() + " in " + duration);
	}
*/
}
