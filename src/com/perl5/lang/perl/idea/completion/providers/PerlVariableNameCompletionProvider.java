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

package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionUtil;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 29.01.2016.
 */
public class PerlVariableNameCompletionProvider extends CompletionProvider<CompletionParameters> implements PerlElementPatterns
{

	public void addCompletions(@NotNull CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull CompletionResultSet resultSet)
	{
		PsiElement variableNameElement = parameters.getPosition();

		boolean isDeclaration = VARIABLE_NAME_IN_DECLARATION_PATTERN.accepts(variableNameElement);
		boolean hasExplicitNamespace = variableNameElement.getPrevSibling() instanceof PerlNamespaceElement;

		// declaration helper
		if (isDeclaration)
		{
			PerlVariableCompletionUtil.fillWithUnresolvedVars((PerlVariableNameElement) variableNameElement, resultSet);
		}

		// built ins
		if( !hasExplicitNamespace && (!isDeclaration || VARIABLE_NAME_IN_LOCAL_DECLARATION_PATTERN.accepts(variableNameElement)))
		{
			PerlVariableCompletionUtil.fillWithBuiltInVariables(variableNameElement, resultSet);
		}


	}

}
