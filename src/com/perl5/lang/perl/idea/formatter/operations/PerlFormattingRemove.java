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

package com.perl5.lang.perl.idea.formatter.operations;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 15.11.2015.
 */
public class PerlFormattingRemove implements PerlFormattingOperation
{
	private final PsiElement myFromElement;
	private final PsiElement myToElement;

	public PerlFormattingRemove(@NotNull PsiElement element)
	{
		this(element, element);
	}

	public PerlFormattingRemove(@NotNull PsiElement myFromElement, @NotNull PsiElement myToElement)
	{
		assert myFromElement.getParent() == myToElement.getParent() : "Psi elements range must be under same parent";
		assert myFromElement.getStartOffsetInParent() <= myToElement.getStartOffsetInParent() : "From element must came first";
		this.myFromElement = myFromElement;
		this.myToElement = myToElement;
	}

	@Override
	public int apply()
	{
		if (!myFromElement.isValid() || !myToElement.isValid())    // seems something happened on the upper level
		{
			return 0;
		}

		int result = 0;
		PsiElement currentElement = myFromElement;

		while (true)
		{
			result -= currentElement.getNode().getTextLength();
			PsiElement nextElement = currentElement.getNextSibling();
			boolean isLast = currentElement == myToElement;

			currentElement.delete();
			if (isLast || nextElement == null)
			{
				break;
			}
			currentElement = nextElement;
			assert currentElement.isValid() : "Element become invalid";
		}

		return result;
	}
}
