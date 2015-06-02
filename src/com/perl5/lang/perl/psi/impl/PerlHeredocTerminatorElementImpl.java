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
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlHeredocTerminatorElementImpl extends PsiCommentImpl implements PerlNamedElement, PerlElementTypes
{
	public PerlHeredocTerminatorElementImpl(IElementType type, CharSequence text){ super(type, text);}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		if( name.equals(""))
			throw new IncorrectOperationException("You can't set heredoc terminator to the empty one");

		replace(PerlElementFactory.createHereDocTerminator(getProject(), name));
		return this;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return getPsi();
	}

	// todo we should move this to some superclass
	@Override
	public String getName()
	{
		PsiElement nameIdentifier = getNameIdentifier();
		return nameIdentifier == null ? null: nameIdentifier.getText();
	}
}
