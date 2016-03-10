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

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.utils.PerlScopeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 31.01.2016.
 */
public class PerlStubBasedPsiElementBase<T extends StubElement> extends StubBasedPsiElementBase<T>
{
	public PerlStubBasedPsiElementBase(@NotNull T stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public PerlStubBasedPsiElementBase(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "(" + getNode().getElementType().toString() + ")";
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place)
	{
//		System.err.println(this);
		return PerlScopeUtil.processChildren(
				this,
				processor,
				state,
				lastParent,
				place
		);
	}
}
