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

package com.perl5.lang.perl.idea.presentations;

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PsiPerlVariableDeclarationGlobal;
import com.perl5.lang.perl.psi.PsiPerlVariableDeclarationLexical;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 05.06.2015.
 */
public class PerlVariablePresentation extends PerlItemPresentationBase
{
	PerlVariableType myVariableType;

	public PerlVariablePresentation(@NotNull PerlVariable element)
	{
		super(element);
		myVariableType = element.getActualType();
	}

	@Nullable
	@Override
	public String getPresentableText()
	{
		// default getName || getText
		String variableText = null;
		if( myVariableType == PerlVariableType.ARRAY)
			variableText = "array";
		else if( myVariableType == PerlVariableType.HASH)
			variableText = "hash";
		else if( myVariableType == PerlVariableType.SCALAR)
			variableText = "scalar";

		if( variableText != null )
		{
			String contextText = null;
			PsiElement parent = getElement().getParent();

			if( parent instanceof PsiPerlVariableDeclarationLexical)
				contextText = "Lexical %s variable declaration";
			else if( parent instanceof PsiPerlVariableDeclarationGlobal)
				contextText = "Global %s variable declaration";
			else
				contextText = "%s variable in unknown context";

			return String.format(contextText, variableText);
		}

		return null;
	}
}
