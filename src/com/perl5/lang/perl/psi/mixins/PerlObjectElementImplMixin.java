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

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.perl5.lang.perl.psi.PsiPerlObjectElement;
import com.perl5.lang.perl.psi.PerlVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 24.05.2015.
 * This class represents an object, which method being invoked; At the moment it may be represented with scalar variable only
 */
public abstract class PerlObjectElementImplMixin extends ASTWrapperPsiElement implements PsiPerlObjectElement
{

	public PerlObjectElementImplMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	// todo probably, this should be moved to variable, scalar specifically
	@Nullable
	@Override
	public String guessNamespace()
	{
		// at the moment object is only wrapper for scalar variable, nothing else
		PerlVariable scalar = getPerlScalar();

		if (scalar != null)
			return scalar.guessVariableType();

		return null;
	}
}
