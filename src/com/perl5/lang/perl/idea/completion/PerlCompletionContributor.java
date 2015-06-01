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

package com.perl5.lang.perl.idea.completion;

import com.intellij.codeInsight.completion.*;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.completion.providers.*;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 25.04.2015.
 *
 */
public class PerlCompletionContributor extends CompletionContributor implements PerlElementTypes, PerlElementPatterns
{

	public PerlCompletionContributor()
    {

        extend(
                CompletionType.BASIC,
                VARIABLE_NAME_PATTERN,
                new PerlBuiltInVariableCompletionProvider()
        );

        extend(
                CompletionType.BASIC,
                VARIABLE_NAME_PATTERN.inside(VARIABLE_PATTERN),
                new PerlVariableCompletionProvider()
        );

        extend(
                CompletionType.BASIC,
                FUNCTION_PATTERN,
                new PerlFunctionCompletionProvider()
        );

        extend(
                CompletionType.BASIC,
                FUNCTION_PATTERN,
                new PerlBuiltInFunctionCompletionProvider()
        );

        extend(
                CompletionType.BASIC,
                NAMESPACE_NAME_PATTERN,
                new PerlPackageCompletionProvider()
        );

        extend(
                CompletionType.BASIC,
                NAMESPACE_NAME_PATTERN,
                new PerlBuiltInPackageCompletionProvider()
        );

        extend(
                CompletionType.BASIC,
                STRING_CONENT_PATTERN.inside(USE_STATEMENT_PATTERN),
                new PerlUseParametersCompletionProvider()
        );
    }

	@Override
	public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result)
	{
		super.fillCompletionVariants(parameters, result);
	}
}
