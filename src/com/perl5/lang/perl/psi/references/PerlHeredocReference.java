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
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlHeredocReference extends PerlReference
{
	public static final ResolveCache.Resolver myResolver = new ResolveCache.Resolver()
	{
		@Override
		public PsiElement resolve(@NotNull PsiReference psiReference, boolean incompleteCode)
		{
			PsiElement element = psiReference.getElement();
			return PerlPsiUtil.findHeredocOpenerByOffset(element.getContainingFile(), element.getText(), element.getTextOffset());
		}
	};

	public PerlHeredocReference(@NotNull PsiElement element, TextRange textRange)
	{
		super(element, textRange);
	}

	// fixme move to PsiUtil ?
	public static PsiElement getClosestHeredocOpener(PsiElement element)
	{
		return PerlPsiUtil.findHeredocOpenerByOffset(element.getContainingFile(), null, element.getTextOffset());
	}

	@Nullable
	@Override
	public PsiElement resolve()
	{
		return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(this, myResolver, true, false);
	}

	@Override
	public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException
	{
		if (newElementName.equals(""))
			throw new IncorrectOperationException("You can't set heredoc terminator to the empty one");

		return myElement.replace(PerlElementFactory.createHereDocTerminator(myElement.getProject(), newElementName));
	}

	@Override
	public TextRange getRangeInElement()
	{
		return new TextRange(0, myElement.getTextLength());
	}


}
