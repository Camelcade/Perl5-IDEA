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

package com.perl5.lang.mojolicious.psi.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 23.04.2016.
 */
public class MojoliciousHelperReferencesProvider extends PsiReferenceProvider
{
	@NotNull
	@Override
	public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
	{
		PsiElement method = element.getParent();
		if (method instanceof PsiPerlMethod && !((PsiPerlMethod) method).hasExplicitNamespace() && !((PsiPerlMethod) method).isObjectMethod())
		{
			return new PsiReference[]{new MojoliciousHelperReference(element)};
		}

		return PsiReference.EMPTY_ARRAY;
	}
}
