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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PerlReferenceContributor extends PsiReferenceContributor implements PerlElementPatterns
{
	@Override
	public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar)
	{
		registrar.registerReferenceProvider(
				HEREDOC_TERMINATOR_PATTERN,
				new PsiReferenceProvider()
				{
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
					{
						assert element instanceof PerlHeredocTerminatorElementImpl;
						return new PsiReference[]{new PerlHeredocReference(element, new TextRange(0, element.getTextLength()))};
					}
				}
		);
		registrar.registerReferenceProvider(
				SUB_NAME_PATTERN,
				new PsiReferenceProvider()
				{
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
					{
						return new PsiReference[]{new PerlSubReference(element, new TextRange(0, element.getTextLength()))};
					}
				}
		);
		registrar.registerReferenceProvider(
				VARIABLE_NAME_PATTERN.withParent(VARIABLE_PATTERN),
				new PsiReferenceProvider()
				{
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
					{
						assert element instanceof PerlVariableNameElement;
						assert !(element.getParent() instanceof PerlGlobVariable);

						return new PsiReference[]{new PerlVariableNameReference(element, new TextRange(0, element.getTextLength()))};
					}
				}
		);
		registrar.registerReferenceProvider(
				VARIABLE_NAME_PATTERN.withParent(GLOB_PATTERN),
				new PsiReferenceProvider()
				{
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
					{
						assert element instanceof PerlVariableNameElement;
						// check if it's dereference
						PsiElement parent = element.getParent();
						assert parent instanceof PerlGlobVariable: "Got: " + parent.getClass() + " in " + parent.getText();

						return new PsiReference[]{new PerlGlobVariableNameReference(element, new TextRange(0, element.getTextLength()))};
					}
				}
		);
		registrar.registerReferenceProvider(
				NAMESPACE_NAME_PATTERN,
				new PsiReferenceProvider()
				{
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
					{
						PsiElement nameSpaceContainer = element.getParent();

						ArrayList<PsiReference> result = new ArrayList<>();

						// fixme this should be done using patterns
						if (nameSpaceContainer instanceof PsiPerlUseStatement
								|| nameSpaceContainer instanceof PsiPerlRequireExpr
								)
							result.add(new PerlNamespaceFileReference(element, new TextRange(0, element.getTextLength())));
						else
							result.add(new PerlNamespaceReference(element, new TextRange(0, element.getTextLength())));

						return result.toArray(new PsiReference[result.size()]);
					}
				}
		);
		registrar.registerReferenceProvider(STRING_CONENT_PATTERN, new PsiReferenceProvider()
		{
			@NotNull
			@Override
			public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
			{
				assert element instanceof PerlStringContentElement;
				if( ((PerlStringContentElement) element).looksLikePackage())
					return new PsiReference[]{new PerlNamespaceReference(element, new TextRange(0, element.getTextLength()))};
				return new PsiReference[0];
			}
		});
	}
}
