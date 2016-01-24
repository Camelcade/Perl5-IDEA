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

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.completion.providers.*;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 25.04.2015.
 */
public class PerlCompletionContributor extends CompletionContributor implements PerlElementTypes, PerlElementPatterns
{

	public PerlCompletionContributor()
	{
		extend(
				CompletionType.BASIC,
				STRING_CONTENT_PATTERN,
				new PerlStringContentCompletionProvider()
		);

		extend(
				CompletionType.BASIC,
				SIMPLE_HASH_INDEX,
				new PerlHashIndexCompletionProvider()
		);

		// refactored
		extend(
				CompletionType.BASIC,
				STRING_CONTENT_IN_DQ_STRING_BEGIN,
				new PerlPackageNamesCompletionProvider()
		);

		extend(
				CompletionType.BASIC,
				STRING_CONTENT_IN_SQ_STRING_BEGIN,
				new PerlPackageNamesCompletionProvider()
		);

		extend(
				CompletionType.BASIC,
				STRING_CONTENT_IN_QW_STRING_LIST,
				new PerlPackageNamesCompletionProvider()
		);

		// refactored
		extend(
				CompletionType.BASIC,
				STRING_CONTENT_PATTERN.inside(USE_STATEMENT_PATTERN),
				new PerlUseParametersCompletionProvider()
		);


		// refactored
		extend(
				CompletionType.BASIC,
				VARIABLE_COMPLETION_PATTERN,
				new PerlVariableBuiltInCompletionProvider()
		);

		extend(
				CompletionType.BASIC,
				VARIABLE_COMPLETION_PATTERN,
				new PerlVariableImportCompletionProvider()
		);

		// refactored
		extend(
				CompletionType.BASIC,
				VARIABLE_COMPLETION_PATTERN,
				new PerlVariableGlobalCompletionProvider()
		);

		// refactored
		extend(
				CompletionType.BASIC,
				VARIABLE_COMPLETION_PATTERN,
				new PerlVariableLexicalCompletionProvider()
		);

		// refactored
		extend(
				CompletionType.BASIC,
				SUB_NAME_PATTERN.and(IN_STATIC_METHOD_PATTERN),
				new PerlSubBuiltInCompletionProvider()
		);

		extend(
				CompletionType.BASIC,
				SUB_NAME_PATTERN.and(IN_STATIC_METHOD_PATTERN),
				new PerlSubImportsCompletionProvider()
		);

		// refactored
		extend(
				CompletionType.BASIC,
				SUB_NAME_PATTERN.and(IN_STATIC_METHOD_PATTERN),
				new PerlSubStaticCompletionProvider()
		);

		// refactored
		extend(
				CompletionType.BASIC,
				SUB_NAME_PATTERN.and(IN_OBJECT_METHOD_PATTERN),
				new PerlSubMethodCompletionProvider()
		);

		// refactored, adds packages when it's appropriate
		extend(
				CompletionType.BASIC,
				SUB_NAME_PATTERN.inside(METHOD_PATTERN),
				new PerlPackageSubCompletionProvider()
		);

		// refactored
		extend(
				CompletionType.BASIC,
				NAMESPACE_NAME_PATTERN,
				new PerlPackageFilesCompletionProvider()
		);

		// refactored
		extend(
				CompletionType.BASIC,
				NAMESPACE_NAME_PATTERN,
				new PerlPackageBuiltInCompletionProvider()
		);


		// refactored
		extend(
				CompletionType.BASIC,
				UNKNOWN_ANNOTATION_PATTERN,
				new PerlAnnotationCompletionProvider()
		);
	}

	@Override
	public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result)
	{
		super.fillCompletionVariants(parameters, result);
	}
}
