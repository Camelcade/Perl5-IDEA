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
import com.intellij.codeInsight.completion.PlainPrefixMatcher;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ProcessingContext;
import com.intellij.util.Processor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.util.*;
import com.perl5.lang.perl.util.processors.PerlNamespaceEntityProcessor;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.util.processors.PerlInternalIndexKeysProcessor.adjustName;

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
		boolean isFullQualified = PerlPackageUtil.isFullQualifiedName(variableNameElement.getText());

		PsiElement originalPosition = parameters.getOriginalPosition();
		if (originalPosition instanceof PerlVariableNameElement)
		{
			resultSet = resultSet.withPrefixMatcher(new PlainPrefixMatcher(originalPosition.getText()));
		}

		// declaration helper
		if (isDeclaration)
		{
			PerlVariableCompletionUtil.fillWithUnresolvedVars((PerlVariableNameElement) variableNameElement, resultSet);
		}
		else if (!isFullQualified)
		{
			PerlVariableCompletionUtil.fillWithLExicalVariables(variableNameElement, resultSet);
		}

		// built ins
		if (!isFullQualified && (!isDeclaration || VARIABLE_NAME_IN_LOCAL_DECLARATION_PATTERN.accepts(variableNameElement)))
		{
			PerlVariableCompletionUtil.fillWithBuiltInVariables(variableNameElement, resultSet);
		}

		// imports
		if (!isDeclaration && !isFullQualified)
		{
			fillWithImportedVariables(variableNameElement, resultSet);
		}

		// fqn names
		fillWithFullQualifiedVariables(variableNameElement, resultSet);

	}

	private void fillWithFullQualifiedVariables(@NotNull PsiElement variableNameElement, @NotNull CompletionResultSet resultSet)
	{
		PsiElement perlVariable = variableNameElement.getParent();
		Project project = variableNameElement.getProject();
		GlobalSearchScope resolveScope = variableNameElement.getResolveScope();
		String variableName = variableNameElement.getText();

		boolean forceShortMain = StringUtil.startsWith(variableName, PerlPackageUtil.PACKAGE_SEPARATOR);

		final CompletionResultSet finalResultSet = resultSet;

		Processor<PerlVariableDeclarationWrapper> scalarDefaultProcessor = wrapper ->
		{
			String fullQualifiedName = wrapper.getFullQualifiedName();
			if (fullQualifiedName != null)
			{
				finalResultSet.addElement(PerlVariableCompletionUtil.getScalarLookupElement(adjustName(fullQualifiedName, forceShortMain)));
			}
			return true;
		};

		Processor<PerlVariableDeclarationWrapper> arrayDefaultProcessor = wrapper ->
		{
			String fullQualifiedName = wrapper.getFullQualifiedName();
			if (fullQualifiedName != null)
			{
				finalResultSet.addElement(PerlVariableCompletionUtil.getArrayLookupElement(adjustName(fullQualifiedName, forceShortMain)));
			}
			return true;
		};

		Processor<PerlVariableDeclarationWrapper> hashDefaultProcessor = wrapper ->
		{
			String fullQualifiedName = wrapper.getFullQualifiedName();
			if (fullQualifiedName != null)
			{
				finalResultSet.addElement(PerlVariableCompletionUtil.getHashLookupElement(adjustName(fullQualifiedName, forceShortMain)));
			}
			return true;
		};

		if (perlVariable instanceof PsiPerlScalarVariable)
		{
			PerlScalarUtil.processDefinedGlobalScalars(project, resolveScope, scalarDefaultProcessor);
			PerlArrayUtil.processDefinedGlobalArrays(project, resolveScope, wrapper ->
			{
				String fullQualifiedName = wrapper.getFullQualifiedName();
				if (fullQualifiedName != null)
				{
					finalResultSet.addElement(PerlVariableCompletionUtil.getArrayElementLookupElement(adjustName(fullQualifiedName, forceShortMain)));
				}
				return true;
			});
			PerlHashUtil.processDefinedGlobalHashes(project, resolveScope, wrapper ->
			{
				String fullQualifiedName = wrapper.getFullQualifiedName();
				if (fullQualifiedName != null)
				{
					finalResultSet.addElement(PerlVariableCompletionUtil.getHashElementLookupElement(adjustName(fullQualifiedName, forceShortMain)));
				}
				return true;
			});
		}
		else if (perlVariable instanceof PerlGlobVariable)
		{
			PerlScalarUtil.processDefinedGlobalScalars(project, resolveScope, scalarDefaultProcessor);
			PerlArrayUtil.processDefinedGlobalArrays(project, resolveScope, arrayDefaultProcessor);
			PerlHashUtil.processDefinedGlobalHashes(project, resolveScope, hashDefaultProcessor);

			// globs
			PerlGlobUtil.processDefinedGlobsNames(project, resolveScope, typeglob ->
			{
				finalResultSet.addElement(PerlVariableCompletionUtil.getGlobLookupElement(adjustName(typeglob.getCanonicalName(), forceShortMain)));
				return true;
			});
		}
		else if (perlVariable instanceof PsiPerlArrayVariable)
		{
			PerlArrayUtil.processDefinedGlobalArrays(project, resolveScope, arrayDefaultProcessor);
			PerlHashUtil.processDefinedGlobalHashes(project, resolveScope, wrapper ->
			{
				String fullQualifiedName = wrapper.getFullQualifiedName();
				if (fullQualifiedName != null)
				{
					finalResultSet.addElement(PerlVariableCompletionUtil.getHashSliceLookupElement(adjustName(fullQualifiedName, forceShortMain)));
				}
				return true;
			});

		}
		else if (perlVariable instanceof PsiPerlArrayIndexVariable)
		{
			// global arrays
			PerlArrayUtil.processDefinedGlobalArrays(project, resolveScope, arrayDefaultProcessor);
		}
		else if (perlVariable instanceof PsiPerlHashVariable)
		{
			// global hashes
			PerlHashUtil.processDefinedGlobalHashes(project, resolveScope, hashDefaultProcessor);
		}

	}

	private void fillWithImportedVariables(@NotNull PsiElement variableNameElement, @NotNull CompletionResultSet resultSet)
	{
		PerlNamespaceContainer namespaceContainer = PerlPackageUtil.getNamespaceContainerForElement(variableNameElement);

		if (namespaceContainer == null)
		{
			return;
		}

		String packageName = namespaceContainer.getPackageName();

		if (StringUtil.isEmpty(packageName)) // incomplete package definition
		{
			return;
		}

		PerlNamespaceEntityProcessor<PerlExportDescriptor> processor = null;
		PsiElement perlVariable = variableNameElement.getParent();

		if (perlVariable instanceof PsiPerlScalarVariable)
		{
			processor = new PerlNamespaceEntityProcessor<PerlExportDescriptor>()
			{
				@Override
				public boolean process(String namespaceName, PerlExportDescriptor entity)
				{
					LookupElementBuilder lookupElement = null;
					if (entity.isScalar())
					{
						lookupElement = PerlVariableCompletionUtil.getScalarLookupElement(entity.getTargetName());
					}
					else if (entity.isArray())
					{
						lookupElement = PerlVariableCompletionUtil.getArrayElementLookupElement(entity.getTargetName());
					}
					else if (entity.isHash())
					{
						lookupElement = PerlVariableCompletionUtil.getHashElementLookupElement(entity.getTargetName());
					}

					if (lookupElement != null)
					{
						resultSet.addElement(lookupElement.withTypeText(entity.getTargetPackage(), true));
					}
					return true;
				}
			};
		}
		else if (perlVariable instanceof PsiPerlArrayVariable || perlVariable instanceof PsiPerlArrayIndexVariable)
		{
			processor = new PerlNamespaceEntityProcessor<PerlExportDescriptor>()
			{
				@Override
				public boolean process(String namespaceName, PerlExportDescriptor entity)
				{
					LookupElementBuilder lookupElement = null;
					if (entity.isArray())
					{
						lookupElement = PerlVariableCompletionUtil.getArrayLookupElement(entity.getTargetName());
					}
					else if (entity.isHash())
					{
						lookupElement = PerlVariableCompletionUtil.getHashSliceLookupElement(entity.getTargetName());
					}

					if (lookupElement != null)
					{
						resultSet.addElement(lookupElement.withTypeText(entity.getTargetPackage(), true));
					}
					return true;
				}
			};
		}
		else if (perlVariable instanceof PsiPerlHashVariable)
		{
			processor = new PerlNamespaceEntityProcessor<PerlExportDescriptor>()
			{
				@Override
				public boolean process(String namespaceName, PerlExportDescriptor entity)
				{
					LookupElementBuilder lookupElement = null;
					if (entity.isHash())
					{
						lookupElement = PerlVariableCompletionUtil.getHashLookupElement(entity.getTargetName());
					}

					if (lookupElement != null)
					{
						resultSet.addElement(lookupElement.withTypeText(entity.getTargetPackage(), true));
					}
					return true;
				}
			};
		}

		if (processor != null)
		{
			PerlUtil.processImportedEntities(namespaceContainer, processor);
		}
	}

}
