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

package com.perl5.lang.perl.psi;

import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PerlHeredocTerminatorElementImpl;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 14.06.2015.
 * Extension of generated visitor
 */
public class PerlVisitor extends PsiPerlVisitor
{
	public void visitNamespaceElement(@NotNull PerlNamespaceElement o)
	{
		visitPsiElement(o);
	}

	public void visitVariableNameElement(@NotNull PerlVariableNameElement o)
	{
		visitPsiElement(o);
	}

	public void visitSubNameElement(@NotNull PerlSubNameElement o)
	{
		visitPsiElement(o);
	}

	public void visitLabel(@NotNull PerlLabel o)
	{
		visitPsiElement(o);
	}

	public void visitStringContentElement(@NotNull PerlStringContentElementImpl o)
	{
		visitPsiElement(o);
	}

	public void visitHeredocTeminator(@NotNull PerlHeredocTerminatorElementImpl o)
	{
		visitComment(o);
	}

	public void visitHeredocElement(@NotNull PerlHeredocElementImpl o)
	{
		visitPsiElement(o);
	}


	public void visitPerlVariable(@NotNull PerlVariable o)
	{

	}

	public void visitPerlCastExpression(@NotNull PerlCastExpression o)
	{
	}

	public void visitSubDefinitionBase(@NotNull PerlSubDefinitionBase o)
	{
	}


	public void visitArrayIndexVariable(@NotNull PsiPerlArrayIndexVariable o)
	{
		super.visitArrayIndexVariable(o);
		visitPerlVariable(o);
	}

	public void visitArrayVariable(@NotNull PsiPerlArrayVariable o)
	{
		super.visitArrayVariable(o);
		visitPerlVariable(o);
	}

	public void visitHashVariable(@NotNull PsiPerlHashVariable o)
	{
		super.visitHashVariable(o);
		visitPerlVariable(o);
	}

	public void visitScalarVariable(@NotNull PsiPerlScalarVariable o)
	{
		super.visitScalarVariable(o);
		visitPerlVariable(o);
	}

	@Override
	public void visitScalarCastExpr(@NotNull PsiPerlScalarCastExpr o)
	{
		super.visitScalarCastExpr(o);
		visitPerlCastExpression(o);
	}

	@Override
	public void visitArrayCastExpr(@NotNull PsiPerlArrayCastExpr o)
	{
		super.visitArrayCastExpr(o);
		visitPerlCastExpression(o);
	}

	@Override
	public void visitHashCastExpr(@NotNull PsiPerlHashCastExpr o)
	{
		super.visitHashCastExpr(o);
		visitPerlCastExpression(o);
	}

	@Override
	public void visitCodeCastExpr(@NotNull PsiPerlCodeCastExpr o)
	{
		super.visitCodeCastExpr(o);
		visitPerlCastExpression(o);
	}

	@Override
	public void visitGlobCastExpr(@NotNull PsiPerlGlobCastExpr o)
	{
		super.visitGlobCastExpr(o);
		visitPerlCastExpression(o);
	}

	@Override
	public void visitSubDefinition(@NotNull PsiPerlSubDefinition o)
	{
		super.visitSubDefinition(o);
		visitSubDefinitionBase(o);
	}

	@Override
	public void visitMethodDefinition(@NotNull PsiPerlMethodDefinition o)
	{
		super.visitMethodDefinition(o);
		visitSubDefinitionBase(o);
	}

	@Override
	public void visitFuncDefinition(@NotNull PsiPerlFuncDefinition o)
	{
		super.visitFuncDefinition(o);
		visitSubDefinitionBase(o);
	}
}
