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

package com.perl5.lang.perl.psi.references.providers;

import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.references.PerlNamespaceReference;
import com.perl5.lang.perl.psi.references.PerlVariableReference;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 03.11.2016.
 */
public class PerlVariableReferencesProvider extends PsiReferenceProvider
{
	@NotNull
	@Override
	public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
	{
		PsiElement parent = element.getParent();

		if (parent instanceof PerlVariable && !((PerlVariable) parent).isDeclaration())
		{
			String elementText = element.getText();
			if (PerlPackageUtil.isFullQualifiedName(elementText))
			{
				Pair<TextRange, TextRange> qualifiedRanges = PerlPackageUtil.getQualifiedRanges(elementText);
				// two refs
				return new PsiReference[]{
						new PerlNamespaceReference(element, qualifiedRanges.first),
						new PerlVariableReference(element, qualifiedRanges.second)
				};
			}
			else
			{
				return new PsiReference[]{new PerlVariableReference(element, TextRange.allOf(elementText))};
			}
		}
		else if (parent instanceof PerlGlobVariable)
		{
			String elementText = element.getText();
			if (PerlPackageUtil.isFullQualifiedName(elementText))
			{
				Pair<TextRange, TextRange> qualifiedRanges = PerlPackageUtil.getQualifiedRanges(elementText);
				// two refs
				return new PsiReference[]{new PerlNamespaceReference(element, qualifiedRanges.first)};
			}
		}

		return PsiReference.EMPTY_ARRAY;
	}
}
