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
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 26.08.2015.
 */
public class PerlUseVarsMixin extends PerlVariableDeclarationMixin implements IPerlUseVars
{
	public PerlUseVarsMixin(ASTNode node)
	{
		super(node);
	}

	/**
	 * following trash required to extend use_statement with statement fixme do something about it
	 **/
	@Override
	@Nullable
	public PsiPerlForStatementModifier getForStatementModifier()
	{
		return findChildByClass(PsiPerlForStatementModifier.class);
	}

	@Override
	@Nullable
	public PsiPerlForeachStatementModifier getForeachStatementModifier()
	{
		return findChildByClass(PsiPerlForeachStatementModifier.class);
	}

	@Override
	@Nullable
	public PsiPerlIfStatementModifier getIfStatementModifier()
	{
		return findChildByClass(PsiPerlIfStatementModifier.class);
	}

	@Override
	@Nullable
	public PsiPerlLabelDeclaration getLabelDeclaration()
	{
		return findChildByClass(PsiPerlLabelDeclaration.class);
	}

	@Override
	@Nullable
	public PsiPerlNoStatement getNoStatement()
	{
		return findChildByClass(PsiPerlNoStatement.class);
	}

	@Override
	@Nullable
	public PsiPerlStatement getStatement()
	{
		return findChildByClass(PsiPerlStatement.class);
	}

	@Override
	@Nullable
	public PsiPerlSubDeclaration getSubDeclaration()
	{
		return findChildByClass(PsiPerlSubDeclaration.class);
	}

	@Override
	@Nullable
	public PsiPerlUnlessStatementModifier getUnlessStatementModifier()
	{
		return findChildByClass(PsiPerlUnlessStatementModifier.class);
	}

	@Override
	@Nullable
	public PsiPerlUntilStatementModifier getUntilStatementModifier()
	{
		return findChildByClass(PsiPerlUntilStatementModifier.class);
	}

	@Override
	@Nullable
	public PsiPerlWhenStatementModifier getWhenStatementModifier()
	{
		return findChildByClass(PsiPerlWhenStatementModifier.class);
	}

	@Override
	@Nullable
	public PsiPerlWhileStatementModifier getWhileStatementModifier()
	{
		return findChildByClass(PsiPerlWhileStatementModifier.class);
	}

	@Nullable
	@Override
	public PsiPerlExpr getExpr()
	{
		return null;
	}
}
