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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.perl5.lang.perl.idea.intellilang.PerlStringLiteralEscaper;
import com.perl5.lang.perl.parser.PerlParserUtil;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.impl.PerlParsableStringWrapperlImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 08.08.2015.
 */
public abstract class PerlStringImplMixin extends PerlStringBareImplMixin implements PsiLanguageInjectionHost
{
	public PerlStringImplMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	@NotNull
	@Override
	public String getStringContent()
	{
		TextRange contentRange = getContentTextRangeInParent();
		return contentRange.substring(getText());
	}

	@Override
	public void setStringContent(String newContent)
	{
		String currentContent = getNode().getText();

		String newNodeContent =
				currentContent.substring(0, getOpenQuoteOffsetInParent() + 1) +    // opening sequence
						newContent +                                                    // new content
						currentContent.substring(currentContent.length() - 1)            // close quote fixme handle incomplete strings
				;

		replace(PerlElementFactory.createString(getProject(), newNodeContent));
	}

	@NotNull
	public PsiElement getOpeningQuote()
	{
		PsiElement firstChild = getRealString().getFirstChild();

		while (firstChild != null)
		{
			if (PerlParserUtil.OPEN_QUOTES.contains(firstChild.getNode().getElementType()))
			{
				return firstChild;
			}
			firstChild = firstChild.getNextSibling();
		}
		throw new RuntimeException("Unable to find opening quote in: " + getText());
	}

	@Nullable
	public PsiElement getClosingQuote()
	{
		PsiElement lastChild = getRealString().getLastChild();
		if (lastChild != null && PerlParserUtil.CLOSE_QUOTES.contains(lastChild.getNode().getElementType()))
		{
			return lastChild;
		}
		return null;
	}

	@NotNull
	protected PerlString getRealString()
	{
		if (getFirstChild() instanceof PerlParsableStringWrapperlImpl)
		{
			PsiElement realString = getFirstChild().getFirstChild();
			assert realString instanceof PerlString : "Got " + realString + " instead of string";
			return (PerlString) realString;
		}
		return this;
	}

	protected int getOpenQuoteOffsetInParent()
	{
		return getOpeningQuote().getNode().getStartOffset() - getNode().getStartOffset();
	}

	protected int getCloseQuoteOffsetInParent()
	{
		PsiElement closingQuote = getClosingQuote();
		ASTNode node = getNode();
		if (closingQuote == null) // unclosed string
		{
			return node.getTextLength();
		}
		return closingQuote.getNode().getStartOffset() - node.getStartOffset();
	}


	@NotNull
	@Override
	public TextRange getContentTextRangeInParent()
	{
		return new TextRange(getOpenQuoteOffsetInParent() + 1, getCloseQuoteOffsetInParent());
	}

	@Override
	public PsiLanguageInjectionHost updateText(@NotNull String text)
	{
		return null;
	}

	@NotNull
	@Override
	public LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper()
	{
		return new PerlStringLiteralEscaper(this);
	}

	@Override
	public boolean isValidHost()
	{
		return true;
	}
}
