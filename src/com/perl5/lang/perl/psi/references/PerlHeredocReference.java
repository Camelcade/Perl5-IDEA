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
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PsiPerlHeredocOpener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlHeredocReference extends PerlReference
{
	private String myMarker;

	public PerlHeredocReference(@NotNull PsiElement element, TextRange textRange)
	{
		super(element, textRange);
		assert element instanceof PsiNamedElement;
		myMarker = ((PsiNamedElement) element).getName();
	}

	public static PsiElement getClosestHeredocOpener(PsiElement element)
	{
		return findHeredocOpenerByOffset(element.getContainingFile(), null, element.getTextOffset());
	}

	public static PsiElement findHeredocOpenerByOffset(PsiElement file, String marker, int offset)
	{
		PsiElement result = null;
		for (PsiPerlHeredocOpener opener : PsiTreeUtil.findChildrenOfType(file, PsiPerlHeredocOpener.class))
		{
			if (opener.getTextOffset() < offset)
			{
				if (marker == null || marker.equals(opener.getName()))
					result = opener.getNameIdentifier();
			} else
			{
				break;
			}
		}
		return result;
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
		return findHeredocOpenerByOffset(myElement.getContainingFile(), myMarker, myElement.getTextOffset());
	}

	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		//fixme need to figure out what is wrong here
//		System.err.println("Checking for " + element);
//		boolean result = super.isReferenceTo(element);
//		System.err.println("Result for " + element + " - "+ result);
//		return result;
		if (element instanceof PerlStringContentElement)
			return super.isReferenceTo(element);
//		else if (element instanceof PerlHeredocOpener)
//			return isReferenceTo(((PerlHeredocOpener) element).getNameIdentifier());
//
		return false;
	}

}
