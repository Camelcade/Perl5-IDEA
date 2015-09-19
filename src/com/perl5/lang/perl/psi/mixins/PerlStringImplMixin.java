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
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.parser.PerlParserUtil;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.impl.PerlNamedElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 08.08.2015.
 */
public abstract class PerlStringImplMixin extends PerlNamedElementImpl
{
	public PerlStringImplMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if( nameIdentifier != this && nameIdentifier instanceof PsiNamedElement)
			return ((PsiNamedElement) nameIdentifier).setName(name);

		return null;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		if( getFirstChild() instanceof PerlStringContentElement )
			return getFirstChild();
		return this;
	}

	@Nullable
	@Override
	public String getName()
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if( nameIdentifier != this && nameIdentifier instanceof PsiNamedElement)
			return ((PsiNamedElement) nameIdentifier).getName();

		int baseOffset = getTextOffset();
		return getText().substring(getOpenQuoteOffset()-baseOffset + 1, getCloseQuoteOffset() - baseOffset);
	}


	protected int getOpenQuoteOffset()
	{
		ASTNode currentNode = getFirstChild().getNode();
		while( currentNode != null )
		{
			if(PerlParserUtil.OPEN_QUOTES.contains(currentNode.getElementType()))
				return currentNode.getStartOffset();
			currentNode = currentNode.getTreeNext();
		}
		throw new RuntimeException("Unable to find opening quote");
	}

	protected int getCloseQuoteOffset()
	{
		ASTNode currentNode = getLastChild().getNode();
		while( currentNode != null )
		{
			if(PerlParserUtil.CLOSE_QUOTES.contains(currentNode.getElementType()))
				return currentNode.getStartOffset();
			currentNode = currentNode.getTreePrev();
		}
		throw new RuntimeException("Unable to find closing quote");
	}

	@Override
	public TextRange getTextRange()
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if( nameIdentifier != this && nameIdentifier != null )
			return nameIdentifier.getTextRange();
		return new TextRange(getOpenQuoteOffset() + 1, getCloseQuoteOffset() );
	}
}
