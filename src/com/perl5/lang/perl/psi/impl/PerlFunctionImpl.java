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
import com.perl5.lang.perl.psi.PerlFunction;
import com.perl5.lang.perl.psi.PerlElementFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 24.05.2015.
 *
 */
public class PerlFunctionImpl extends LeafPsiElement implements PerlFunction
{
	public PerlFunctionImpl(@NotNull IElementType type, CharSequence text) {
		super(type, text);
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		PerlFunction newFunction = PerlElementFactory.createUserFunction(getProject(), name);
		if( newFunction != null )
			replace(newFunction);
		else
			throw new IncorrectOperationException("Unable to create function from: "+ name);
		return this;
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
		return this.getText();
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		return ReferenceProvidersRegistry.getReferencesFromProviders(this);
	}


}
