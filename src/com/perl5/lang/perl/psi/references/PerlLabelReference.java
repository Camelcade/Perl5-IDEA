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

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.perl5.lang.perl.psi.PerlLabel;
import com.perl5.lang.perl.psi.PerlLabelDeclaration;
import com.perl5.lang.perl.psi.references.resolvers.PerlLabelResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 04.03.2016.
 */
public class PerlLabelReference extends PerlReference<PerlLabel>
{
	private static final ResolveCache.AbstractResolver<PerlLabelReference, PerlLabelDeclaration> RESOLVER = new PerlLabelResolver();

	public PerlLabelReference(@NotNull PerlLabel element, TextRange textRange)
	{
		super(element, textRange);
	}

	@Nullable
	@Override
	public PsiElement resolve()
	{
		return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(this, RESOLVER, true, false);
	}
}
