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

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 01.06.2015.
 */
public abstract class PerlVariableDeclarationMixin extends PerlLexicalScopeMemberMixin implements PerlVariableDeclaration
{
	public PerlVariableDeclarationMixin(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public PerlNamespaceElement getNamespaceElement()
	{
		return findChildByClass(PerlNamespaceElement.class);
	}

	@Override
	@NotNull
	public List<PsiPerlArrayVariable> getArrayVariableList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlArrayVariable.class);
	}

	@Override
	@NotNull
	public List<PsiPerlHashVariable> getHashVariableList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlHashVariable.class);
	}

	@Override
	@NotNull
	public List<PsiPerlScalarVariable> getScalarVariableList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiPerlScalarVariable.class);
	}

}
