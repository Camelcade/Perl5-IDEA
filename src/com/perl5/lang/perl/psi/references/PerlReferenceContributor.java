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
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.psi.impl.*;
import org.jetbrains.annotations.NotNull;

public class PerlReferenceContributor extends PsiReferenceContributor
{
	@Override
	public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar)
	{
		registrar.registerReferenceProvider(
				PlatformPatterns.psiElement(PerlHeredocTerminatorImpl.class),
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
				PlatformPatterns.psiElement(PerlUserFunctionImpl.class),
				new PsiReferenceProvider()
				{
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
					{
						assert element instanceof PerlUserFunctionImpl;

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
	}
}
