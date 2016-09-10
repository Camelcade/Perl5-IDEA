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

package com.perl5.lang.perl.parser.moose.psi.impl;

import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseInnerKeyword;
import com.perl5.lang.perl.parser.moose.psi.references.PerlMooseInnerReference;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 19.01.2016.
 */
public class PerlMooseInnerKeywordImpl extends PerlMooseKeywordSubNameElementImpl implements PerlMooseInnerKeyword
{
	public PerlMooseInnerKeywordImpl(@NotNull IElementType type, CharSequence text)
	{
		super(type, text);
	}

	@Override
	public void computeReferences(List<PsiReference> psiReferences)
	{
		psiReferences.add(new PerlMooseInnerReference(this, null));
		super.computeReferences(psiReferences);
	}

}
