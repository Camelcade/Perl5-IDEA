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

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.perl5.lang.perl.psi.PerlNamedElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 26.04.2015.
 */
public abstract class PerlNamedElementImpl extends ASTWrapperPsiElement implements PerlNamedElement
{
	public PerlNamedElementImpl(@NotNull ASTNode node){
		super(node);
	}

	@Override
	public String getName()
	{
		PsiElement nameElement = getNameIdentifier();
		return nameElement == null ? null : nameElement.getText();
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		return ReferenceProvidersRegistry.getReferencesFromProviders(this);
	}
}
