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

package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlHashUtil;
import com.perl5.lang.perl.util.PerlScalarUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 24.08.2015.
 */
public class PerlVariableImportCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public void addCompletions(@NotNull CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull CompletionResultSet resultSet)
	{
		PsiElement variableNameElement = parameters.getPosition();
		PsiElement perlVariable = variableNameElement.getParent();

		// fixme move this to pattern
		if (perlVariable instanceof PerlNamespaceElementContainer && ((PerlNamespaceElementContainer) perlVariable).getNamespaceElement() != null)
		{
			return;
		}

		PerlNamespaceContainer namespaceContainer = PsiTreeUtil.getParentOfType(perlVariable, PerlNamespaceContainer.class);
		assert namespaceContainer != null;
		Project project = perlVariable.getProject();
		String packageName = namespaceContainer.getPackageName();
		PsiFile originalFile = parameters.getOriginalFile();

		if (packageName == null) // incomplete package definition
		{
			return;
		}

		// fixme handle array and hash elements
		if (perlVariable instanceof PsiPerlScalarVariable)
		{
			for (PerlExportDescriptor exportDescriptor : PerlScalarUtil.getImportedScalarsDescritptors(project, packageName, originalFile))
			{
				resultSet.addElement(LookupElementBuilder
						.create(exportDescriptor.getExportedName())
						.withTypeText(exportDescriptor.getTargetCanonicalName())
						.withIcon(PerlIcons.SCALAR_GUTTER_ICON)
				);
			}
		}
		else if (perlVariable instanceof PsiPerlArrayVariable || perlVariable instanceof PsiPerlArrayIndexVariable)
		{
			for (PerlExportDescriptor exportDescriptor : PerlArrayUtil.getImportedArraysDescriptors(project, packageName, originalFile))
			{
				resultSet.addElement(LookupElementBuilder
						.create(exportDescriptor.getExportedName())
						.withTypeText(exportDescriptor.getTargetCanonicalName())
						.withIcon(PerlIcons.ARRAY_GUTTER_ICON)
				);
			}
		}
		else if (perlVariable instanceof PsiPerlHashVariable)
		{
			for (PerlExportDescriptor exportDescriptor : PerlHashUtil.getImportedHashesDescriptors(project, packageName, originalFile))
			{
				resultSet.addElement(LookupElementBuilder
						.create(exportDescriptor.getExportedName())
						.withTypeText(exportDescriptor.getTargetCanonicalName())
						.withIcon(PerlIcons.HASH_GUTTER_ICON)
				);
			}
		}
	}

}
