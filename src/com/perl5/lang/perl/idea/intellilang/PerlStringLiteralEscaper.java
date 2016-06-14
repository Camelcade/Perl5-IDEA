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

package com.perl5.lang.perl.idea.intellilang;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.perl5.lang.perl.psi.mixins.PerlStringImplMixin;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 27.02.2016.
 */
public class PerlStringLiteralEscaper extends LiteralTextEscaper<PerlStringImplMixin>
{
	public PerlStringLiteralEscaper(@NotNull PerlStringImplMixin host)
	{
		super(host);
	}

	@Override
	public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars)
	{
		outChars.append(rangeInsideHost.subSequence(myHost.getText()));
		// fixme this is beginning of real decoding
//		PsiElement openingQuote = myHost.getOpeningQuote();
//		char closeQuote = RegexBlock.getQuoteCloseChar(openingQuote.getText().charAt(0));
//		String rawText = rangeInsideHost.subSequence(myHost.getText()).toString();
//		outChars.append(rawText.replaceAll("(?<!\\\\)\\\\"+ closeQuote, "" + closeQuote));
		return true;
	}

	@Override
	public int getOffsetInHost(int offsetInDecoded, @NotNull TextRange rangeInsideHost)
	{
		int offset = offsetInDecoded + rangeInsideHost.getStartOffset();
		if (offset < rangeInsideHost.getStartOffset())
		{
			offset = rangeInsideHost.getStartOffset();
		}
		if (offset > rangeInsideHost.getEndOffset())
		{
			offset = rangeInsideHost.getEndOffset();
		}
		return offset;
	}

	@Override
	public boolean isOneLine()
	{
		return false;
	}

	@NotNull
	@Override
	public TextRange getRelevantTextRange()
	{
		return myHost.getContentTextRangeInParent();
	}
}
