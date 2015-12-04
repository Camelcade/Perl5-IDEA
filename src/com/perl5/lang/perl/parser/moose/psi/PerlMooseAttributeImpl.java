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

package com.perl5.lang.perl.parser.moose.psi;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.parser.PerlPsiLists;
import com.perl5.lang.perl.parser.moose.stubs.attribute.PerlMooseAttributeStub;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mixins.PerlSubDefinitionBaseImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 29.11.2015.
 */
public class PerlMooseAttributeImpl extends PerlSubDefinitionBaseImpl<PerlMooseAttributeStub> implements PerlMooseAttribute
{
	public PerlMooseAttributeImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlMooseAttributeImpl(@NotNull PerlMooseAttributeStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public PsiPerlBlock getBlock()
	{
		return null;
	}

	@Nullable
	@Override
	public PsiElement getSignatureContainer()
	{
		return null;
	}

	@Nullable
	protected PerlMooseHasStatement getHasStatement()
	{
		//noinspection unchecked
		return PsiTreeUtil.getParentOfType(this, PerlMooseHasStatement.class, true, PsiPerlStatement.class);
	}

	@NotNull
	@Override
	public List<PsiPerlAnnotation> getAnnotationList()
	{
		PerlMooseHasStatement hasStatement = getHasStatement();
		if (hasStatement != null)
		{
			return hasStatement.getAnnotationList();
		}

		return PerlPsiLists.EMPTY_ANNOTATIONS_LIST;
	}

	@Override
	public boolean isMethod()
	{
		return true;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return getFirstChild();
	}

	@Override
	public PsiElement getSubNameElement()
	{
		return getNameIdentifier();
	}

	@Override
	protected String getSubNameHeavy()
	{
		String realName = getNode().getText();
		if (realName.length() > 0 && realName.charAt(0) == '+')
		{
			return realName.substring(1);
		}
		return realName;
	}

	protected boolean isExtension(PsiElement element)
	{
		String text = element.getNode().getText();
		return text.length() > 0 && text.charAt(0) == '+';
	}

	@Override
	public int getNestedElementStartOffset(LeafPsiElement element, int defaultOffset)
	{
		return isExtension(element) ? defaultOffset + 1 : defaultOffset;
	}

	@Override
	public TextRange getNestedElementTextRange(LeafPsiElement element)
	{
		int defaultStart = element.getStartOffset();
		int defaultLength = element.getNode().getTextLength();
		if (isExtension(element))
		{
			defaultLength--;
		}

		return new TextRange(defaultStart, defaultStart + defaultLength);
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		if (name.isEmpty())
			throw new IncorrectOperationException("You can't set an empty attribute name");

		PsiElement nameIdentifier = getNameIdentifier();
		if (getNode().getText().charAt(0) == '+')
		{
			name = "+" + name;
		}

		if (nameIdentifier instanceof LeafPsiElement)
		{
			((LeafPsiElement) nameIdentifier).replaceWithText(name);
		}

		return this;
	}

}
