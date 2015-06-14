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

package com.perl5.lang.perl.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 23.05.2015.
 */
public class PerlStringContentElementImpl extends LeafPsiElement implements PerlNamedElement
{
	public PerlStringContentElementImpl(@NotNull IElementType type, CharSequence text) {
		super(type, text);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor) {
		if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitStringContentElement(this);
		else super.accept(visitor);
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return this;
	}

	@Override
	public String getName()
	{
		return getText();
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		if( name.equals(""))
			throw new IncorrectOperationException("You can't rename a string to the empty one");

		PerlStringContentElementImpl newName = PerlElementFactory.createStringContent(getProject(), name);
		if( newName != null )
			replace(newName);
		else
			throw new IncorrectOperationException("Unable to create string from: "+ name);
		return this;
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		return ReferenceProvidersRegistry.getReferencesFromProviders(this);
	}

}
