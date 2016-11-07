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

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.parser.moose.psi.references.PerlMooseInnerReference;
import com.perl5.lang.perl.parser.moose.psi.references.PerlMooseSuperReference;
import com.perl5.lang.perl.psi.PerlSubBase;
import com.perl5.lang.perl.psi.references.PerlSubReference;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.SUB_NAME;
import static com.perl5.lang.perl.parser.moose.MooseElementTypes.RESERVED_INNER;
import static com.perl5.lang.perl.parser.moose.MooseElementTypes.RESERVED_SUPER;

/**
 * Created by hurricup on 07.11.2016.
 */
public class PerlSubReferenceProvider extends PsiReferenceProvider
{
	@NotNull
	@Override
	public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
	{
		if (element.getParent() instanceof PerlSubBase)
		{
			return PsiReference.EMPTY_ARRAY;
		}

		IElementType elementType = PsiUtilCore.getElementType(element);
		if (elementType == SUB_NAME)
		{
			return new PsiReference[]{new PerlSubReference(element)};
		}
		else if (elementType == RESERVED_SUPER)
		{
			return new PsiReference[]{new PerlMooseSuperReference(element)};
		}
		else if (elementType == RESERVED_INNER)
		{
			return new PsiReference[]{new PerlMooseInnerReference(element)};
		}

		return PsiReference.EMPTY_ARRAY;
	}

}
