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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PsiPerlHeredocOpener;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlHeredocReference extends PerlReference
{
	private String marker;

	public PerlHeredocReference(@NotNull PsiElement element, TextRange textRange) {
		super(element, textRange);
		assert element instanceof PsiNamedElement;
		marker = ((PsiNamedElement) element).getName();
	}

	@NotNull
	@Override
	public Object[] getVariants()
	{
		return new Object[0];
	}

	@Nullable
	@Override
	public PsiElement resolve()
	{
		PsiElement result = null;
		for( PsiPerlHeredocOpener opener: PsiTreeUtil.findChildrenOfType(myElement.getContainingFile(), PsiPerlHeredocOpener.class))
		{
			PerlStringContentElementImpl stringContent = PsiTreeUtil.findChildOfType(opener, PerlStringContentElementImpl.class);

			if( stringContent != null && marker.equals(stringContent.getName()))
			{
				if (opener.getTextOffset() < myElement.getTextOffset())
				{
					result = stringContent;
				}
				else
					break;
			}
		}
		return result;
	}
}
