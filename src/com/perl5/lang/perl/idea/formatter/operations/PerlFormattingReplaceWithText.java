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
import com.intellij.psi.impl.source.tree.LeafPsiElement;

/**
 * Created by hurricup on 22.11.2015.
 */
public class PerlFormattingReplaceWithText implements PerlFormattingOperation
{
	private final PsiElement myElementToChange;
	private final String myNewElementContent;

	public PerlFormattingReplaceWithText(PsiElement elementToChange, String newElementContent)
	{
		this.myElementToChange = elementToChange;
		this.myNewElementContent = newElementContent;
	}

	@Override
	public int apply()
	{
		int delta = 0;

		if (myElementToChange.isValid() && myElementToChange instanceof LeafPsiElement)
		{
			delta = myNewElementContent.length() - myElementToChange.getNode().getTextLength();
			((LeafPsiElement) myElementToChange).replaceWithText(myNewElementContent);
		}

		return delta;
	}
}
