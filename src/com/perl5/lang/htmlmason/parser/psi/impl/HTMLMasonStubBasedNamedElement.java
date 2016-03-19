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

package com.perl5.lang.htmlmason.parser.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonNamedElement;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 19.03.2016.
 */
public abstract class HTMLMasonStubBasedNamedElement<T extends StubElement> extends HTMLMasonStubBasedElement<T> implements HTMLMasonNamedElement
{
	public HTMLMasonStubBasedNamedElement(@NotNull T stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public HTMLMasonStubBasedNamedElement(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if (nameIdentifier instanceof LeafPsiElement)
			((LeafPsiElement) nameIdentifier).replaceWithText(name);

		return this;
	}

	@Nullable
	protected abstract String getNameFromStub();


	@Override
	public String getName()
	{
		String name = getNameFromStub();
		if (name != null)
		{
			return name;
		}

		PsiElement nameIdentifier = getNameIdentifier();
		if (nameIdentifier != null)
			return nameIdentifier.getText();
		return super.getName();
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		PsiElement firstChild = getFirstChild();
		if (firstChild != null)
		{
			firstChild = PerlPsiUtil.getNextSignificantSibling(firstChild);
			if (firstChild instanceof PerlSubNameElement)
			{
				return firstChild;
			}
		}
		return null;
	}

	@Override
	public int getTextOffset()
	{
		PsiElement nameIdentifier = getNameIdentifier();

		return nameIdentifier == null
				? super.getTextOffset()
				: getNameIdentifier().getTextOffset();
	}

}
