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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.perl5.lang.perl.psi.PerlVariableDeclarationWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 17.02.2016.
 */
public class PerlVariableDeclarationSearcher implements PsiScopeProcessor
{
	private final String myName;
	private PerlVariableDeclarationWrapper myResult;


	public PerlVariableDeclarationSearcher(String myName)
	{
		this.myName = myName;
	}

	@Override
	public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state)
	{
		return true;
	}

	@Nullable
	@Override
	public <T> T getHint(@NotNull Key<T> hintKey)
	{
		return null;
	}

	@Override
	public void handleEvent(@NotNull Event event, @Nullable Object associated)
	{
	}

	public PerlVariableDeclarationWrapper getResult()
	{
		return myResult;
	}
}
