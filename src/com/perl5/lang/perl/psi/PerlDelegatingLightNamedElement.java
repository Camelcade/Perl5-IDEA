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

package com.perl5.lang.perl.psi;

import com.intellij.psi.ElementManipulators;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents one of the declarations of {@link PerlPolyNamedElement}
 */
public class PerlDelegatingLightNamedElement<T extends PerlPolyNamedElement> extends PerlDelegatingLightElement<T>
		implements PsiNameIdentifierOwner
{
	private final String myName;

	public PerlDelegatingLightNamedElement(T delegate, String name)
	{
		super(delegate);
		myName = name;
	}

	@Override
	public String getName()
	{
		return myName;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return myName == null ? null : getDelegate().getNameIdentifierByName(myName);
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		PsiElement nameIdentifier = getNameIdentifier();
		return nameIdentifier == null ? null : ElementManipulators.handleContentChange(nameIdentifier, null);
	}

	@Override
	public void navigate(boolean requestFocus)
	{
		PsiElement nameIdentifier = getNameIdentifier();
		if (nameIdentifier instanceof NavigatablePsiElement)
		{
			((NavigatablePsiElement) nameIdentifier).navigate(requestFocus);
		}
		else
		{
			super.navigate(requestFocus);
		}
	}

	@NotNull
	@Override
	public PsiElement getNavigationElement()
	{
		PsiElement nameIdentifier = getNameIdentifier();
		return nameIdentifier == null ? super.getNavigationElement() : nameIdentifier;
	}

	@Override
	public int getTextOffset()
	{
		PsiElement identifier = getNameIdentifier();
		return identifier == null ? super.getTextOffset() : identifier.getTextOffset();
	}


	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		PerlDelegatingLightNamedElement element = (PerlDelegatingLightNamedElement) o;

		return getDelegate().equals(element.getDelegate()) && myName.equals(element.myName);
	}

	@Override
	public int hashCode()
	{
		return getDelegate().hashCode() * 31 + myName.hashCode();
	}
}
