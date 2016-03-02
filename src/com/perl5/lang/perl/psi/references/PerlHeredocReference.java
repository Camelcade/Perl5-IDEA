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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
import com.perl5.lang.perl.psi.references.resolvers.PerlHeredocResolver;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlHeredocReference extends PerlReference<PerlHeredocTerminatorElement>
{
	private static final PerlHeredocResolver RESOLVER = new PerlHeredocResolver();

	public PerlHeredocReference(@NotNull PerlHeredocTerminatorElement element, TextRange textRange)
	{
		super(element, textRange);
	}

	@Nullable
	@Override
	public PsiElement resolve()
	{
		return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(this, RESOLVER, true, false);
	}

	@Override
	public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException
	{
		// fixme this is not invoked!
		boolean appendNewLine = StringUtil.equals(myElement.getText(), "\n");
		PsiElement result = super.handleElementRename(newElementName);

		if (appendNewLine && result.getParent() != null)
		{
			PsiElement newLineElement = PerlElementFactory.createNewLine(myElement.getProject());
			result.getParent().addAfter(result, newLineElement);
		}

		return result;
	}
}
