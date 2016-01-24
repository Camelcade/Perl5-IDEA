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
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.psi.PerlRecursiveVisitor;
import com.perl5.lang.perl.psi.PsiPerlConstantName;
import com.perl5.lang.perl.psi.PsiPerlSubDeclaration;
import com.perl5.lang.perl.psi.PsiPerlSubDefinition;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 24.01.2016.
 */
public class PerlStringContentCompletionProvider extends CompletionProvider<CompletionParameters> implements PerlElementPatterns
{
	@Override
	protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull final CompletionResultSet result)
	{
		PsiElement element = parameters.getPosition();

		if (EXPORT_ASSIGNED_STRING_CONTENT.accepts(element)) // exporter assignments
		{
			final String contextPackageName = PerlPackageUtil.getContextPackageName(element);

			element.getContainingFile().accept(
					new PerlRecursiveVisitor()
					{
						@Override
						public void visitSubDeclaration(@NotNull PsiPerlSubDeclaration o)
						{
							if (contextPackageName.equals(o.getPackageName()))
							{
								result.addElement(LookupElementBuilder.create(o.getSubName()));
							}
							super.visitSubDeclaration(o);
						}

						@Override
						public void visitSubDefinition(@NotNull PsiPerlSubDefinition o)
						{
							if (contextPackageName.equals(o.getPackageName()))
							{
								result.addElement(LookupElementBuilder.create(o.getSubName()));
							}
							super.visitSubDefinition(o);
						}

						@Override
						public void visitConstantName(@NotNull PsiPerlConstantName o)
						{
							if (contextPackageName.equals(o.getPackageName()))
							{
								result.addElement(LookupElementBuilder.create(o.getText()));
							}
							super.visitConstantName(o);
						}
					}
			);
		}
	}
}
