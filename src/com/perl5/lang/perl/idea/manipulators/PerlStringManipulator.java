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

package com.perl5.lang.perl.idea.manipulators;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.lexer.RegexBlock;
import com.perl5.lang.perl.psi.mixins.PerlStringImplMixin;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 27.02.2016.
 */
public class PerlStringManipulator extends PerlTextContainerManipulator<PerlStringImplMixin>
{
	@Override
	public PerlStringImplMixin handleContentChange(@NotNull PerlStringImplMixin element, @NotNull TextRange range, String newContent) throws IncorrectOperationException
	{
		PsiElement openingQuote = element.getOpeningQuote();
		char closeQuote = RegexBlock.getQuoteCloseChar(openingQuote.getText().charAt(0));
		return super.handleContentChange(element, range, newContent.replaceAll("(?<!\\\\)" + closeQuote, "\\\\" + closeQuote));
	}

	@NotNull
	@Override
	public TextRange getRangeInElement(@NotNull PerlStringImplMixin element)
	{
		return element.getContentTextRangeInParent();
	}
}
