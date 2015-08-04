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
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 14.06.2015.
 */
public class PerlSubMultipleDefinitionsInspection extends PerlInspection
{
	@NotNull
	@Override
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PerlVisitor()
		{
			@Override
			public void visitSubNameElement(@NotNull PerlSubNameElement o)
			{
				List<PerlSubDefinition> definitionList = o.getSubDefinitions();
				List<PerlGlobVariable> relatedGlobs = o.getRelatedGlobs();

				boolean isDefinition = o.getParent() instanceof PerlSubDefinition;

				if (definitionList.size() > 1 || definitionList.size() > 0 && isDefinition)
					registerProblem(holder, o, "Multiple sub definitions found");
				else if (relatedGlobs.size() > 0 && (definitionList.size() > 0 || isDefinition))
					registerProblem(holder, o, "Sub definition and typeglob aliasing found");
			}
		};
	}

}
