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

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.references.PerlHeredocReference;
import org.jetbrains.annotations.NotNull;

public class PerlHeredocTerminatorElementImpl extends PsiCommentImpl implements PerlHeredocTerminatorElement
{
	protected final PsiReference[] myReferences = new PsiReference[]{new PerlHeredocReference(this, null)};

	public PerlHeredocTerminatorElementImpl(IElementType type, CharSequence text)
	{
		super(type, text);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PerlVisitor) ((PerlVisitor) visitor).visitHeredocTeminator(this);
		else super.accept(visitor);
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		return myReferences;
	}

	@Override
	public PsiReference getReference()
	{
		return myReferences[0];
	}

	@Override
	public boolean isValidHost()
	{
		return false;
	}
}
