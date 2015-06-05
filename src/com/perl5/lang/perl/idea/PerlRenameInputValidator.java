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

package com.perl5.lang.perl.idea;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.ElementPatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.rename.RenameInputValidator;
import com.intellij.util.ProcessingContext;

/**
 * Created by hurricup on 29.05.2015.
 */
public class PerlRenameInputValidator implements RenameInputValidator
{
	@Override
	public ElementPattern<? extends PsiElement> getPattern()
	{

		// invoked from rename dialog
		return new ElementPattern<PsiElement>()
		{
			@Override
			public boolean accepts(Object o)
			{
				return false;
			}

			@Override
			public boolean accepts(Object o, ProcessingContext context)
			{
				return false;
			}

			@Override
			public ElementPatternCondition<PsiElement> getCondition()
			{
				return null;
			}
		};
	}

	@Override
	public boolean isInputValid(String newName, PsiElement element, ProcessingContext context)
	{
		return false;
	}


}
