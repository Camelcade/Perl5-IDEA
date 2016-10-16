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

package com.perl5.lang.perl.parser.trycatch;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.parser.elementTypes.PerlElementTypeEx;
import com.perl5.lang.perl.parser.trycatch.psi.impl.TryCatchCompoundStatementImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlVariableDeclarationLexicalImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 17.04.2016.
 */
public interface TryCatchElementTypes
{
	IElementType TRY_COMPOUND = new PerlElementTypeEx("TRY_COMPOUND")
	{
		@NotNull
		@Override
		public PsiElement getPsiElement(@NotNull ASTNode node)
		{
			return new TryCatchCompoundStatementImpl(node);
		}
	};
	IElementType CATCH_COMPOUND = new PerlElementTypeEx("CATCH_COMPOUND")
	{
		@NotNull
		@Override
		public PsiElement getPsiElement(@NotNull ASTNode node)
		{
			return new TryCatchCompoundStatementImpl(node);
		}
	};
	IElementType CATCH_CONDITION = new PerlElementTypeEx("CATCH_CONDITION")
	{
		@NotNull
		@Override
		public PsiElement getPsiElement(@NotNull ASTNode node)
		{
			return new PsiPerlVariableDeclarationLexicalImpl(node);
		}
	};
}
