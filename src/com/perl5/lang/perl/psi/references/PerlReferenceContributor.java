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
						assert element instanceof PerlHeredocTerminatorImpl;
						return new PsiReference[]{new PerlHeredocReference(element, new TextRange(0, element.getTextLength()))};
					}
				}
		);
		registrar.registerReferenceProvider(
				FUNCTION_PATTERN,
				new PsiReferenceProvider()
				{
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
					{
						assert element instanceof PerlFunction;

						// fixme this should be done using patterns
						if( element.getParent() instanceof PerlSubDefinitionImpl)
							return new PsiReference[]{new PerlUserFunctionDeclarationReference(element, new TextRange(0, element.getTextLength()))};
						else
						{
							PsiReference reference = new PerlUserFunctionReference(element, new TextRange(0, element.getTextLength()));
							if( ((PerlUserFunctionReference)reference).multiResolve(false).length == 0 )
								reference = new PerlUserFunctionDeclarationReference(element, new TextRange(0, element.getTextLength()));

							return new PsiReference[]{reference};
						}
					}
				}
		);
		registrar.registerReferenceProvider(
				VARIABLE_NAME_PATTERN.inside(VARIABLE_PATTERN),
				new PsiReferenceProvider()
				{
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
					{
						assert element instanceof PerlVariableName;
						PsiElement container = element.getParent();

						if( container instanceof PerlPerlGlob )
							return new PsiReference[0];
						else
							return new PsiReference[]{new PerlVariableReference(element, new TextRange(0, element.getTextLength()))};

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
						if( nameSpaceContainer instanceof PerlUseStatement
								|| nameSpaceContainer instanceof PerlRequireTerm
								)
							result.add(new PerlNamespaceFileReference(element, new TextRange(0, element.getTextLength())));
						else
							result.add(new PerlNamespaceReference(element, new TextRange(0, element.getTextLength())));

						return result.toArray(new PsiReference[result.size()]);
					}
				}
		);
		registrar.registerReferenceProvider(STRING_CONENT_PATTERN.inside(USE_STATEMENT_PATTERN), new PsiReferenceProvider()
		{
			@NotNull
			@Override
			public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
			{
				return new PsiReference[]{new PerlNamespaceReference(element, new TextRange(0, element.getTextLength()))};
			}
		});
	}
}
