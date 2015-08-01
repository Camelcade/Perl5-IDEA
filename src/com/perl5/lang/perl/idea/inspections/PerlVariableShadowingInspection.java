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
import com.perl5.lang.perl.psi.PerlVariable;

import java.util.List;

/**
 * Created by hurricup on 14.06.2015.
 */
public class PerlVariableShadowingInspection extends PerlVariableDeclarationInspection
{
	@Override
	public <T extends PerlVariable> void checkVariables(ProblemsHolder holder, List<T> variableList)
	{
		for (PerlVariable variable : variableList)
		{
			PerlVariable lexicalDeclaration = variable.getLexicalDeclaration();
			if (lexicalDeclaration != null)
				registerProblem(holder, variable, "Current variable declaration shadows previous declaration of the same variable at line " + lexicalDeclaration.getLineNumber());
		}
	}
}
