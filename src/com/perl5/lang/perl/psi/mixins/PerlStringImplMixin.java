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
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.parser.PerlParserUtil;
import com.perl5.lang.perl.psi.impl.PerlParsableStringWrapperlImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 08.08.2015.
 */
public abstract class PerlStringImplMixin extends PerlStringBareImplMixin
{
	public PerlStringImplMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	@NotNull
	@Override
	public String getStringContent()
	{
		int baseOffset = getNode().getStartOffset();
		return getText().substring(getOpenQuoteOffset() - baseOffset + 1, getCloseQuoteOffset() - baseOffset);
	}

	@Override
	public void setStringContent(String newContent)
	{
		int baseOffset = getNode().getStartOffset();
		String currentContent = getNode().getText();

		String newNodeContent = currentContent.substring(0, getOpenQuoteOffset() - baseOffset + 1) + newContent + currentContent.substring(currentContent.length() - 1);

		replace(PerlElementFactory.createString(getProject(), newNodeContent));
	}

	@Override
	public int getTextLength()
	{
		return getTextRange().getLength();
	}

	protected int getOpenQuoteOffset()
	{
		PsiElement firstChild = getFirstChild();

		if (firstChild instanceof PerlParsableStringWrapperlImpl)
			firstChild = firstChild.getFirstChild().getFirstChild();

		ASTNode currentNode = firstChild.getNode();

		while (currentNode != null)
		{
			if (PerlParserUtil.OPEN_QUOTES.contains(currentNode.getElementType()))
				return currentNode.getStartOffset();
			currentNode = currentNode.getTreeNext();
		}
		throw new RuntimeException("Unable to find opening quote inside: " + getText() + " " + getContainingFile().getVirtualFile());
	}

	protected int getCloseQuoteOffset()
	{
		PsiElement lastChild = getLastChild();

		if (lastChild instanceof PerlParsableStringWrapperlImpl)
			lastChild = lastChild.getLastChild().getLastChild();

		ASTNode currentNode = lastChild.getNode();

		while (currentNode != null)
		{
			if (PerlParserUtil.CLOSE_QUOTES.contains(currentNode.getElementType()))
				return currentNode.getStartOffset();
			currentNode = currentNode.getTreePrev();
		}
		throw new RuntimeException("Unable to find closing quote inside: " + getText() + " " + getContainingFile().getVirtualFile());
	}

	@Override
	public TextRange getTextRange()
	{
		return new TextRange(getTextOffset(), getCloseQuoteOffset());
	}

	@Override
	public int getTextOffset()
	{
		return getOpenQuoteOffset() + 1;
	}

}
