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

package com.perl5.lang.htmlmason.parser.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonFlagsStatement;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonFlagsStatementStub;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PerlStubBasedPsiElementBase;
import com.perl5.lang.perl.psi.PsiPerlCommaSequenceExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 09.03.2016.
 */
public class HTMLMasonFlagsStatementImpl extends PerlStubBasedPsiElementBase<HTMLMasonFlagsStatementStub> implements HTMLMasonFlagsStatement
{
	public HTMLMasonFlagsStatementImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public HTMLMasonFlagsStatementImpl(@NotNull HTMLMasonFlagsStatementStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Nullable
	@Override
	public String getParentComponentPath()
	{
		HTMLMasonFlagsStatementStub stub = getStub();

		if (stub != null)
		{
			return stub.getParentComponentPath();
		}

		PsiPerlCommaSequenceExpr expr = PsiTreeUtil.findChildOfType(this, PsiPerlCommaSequenceExpr.class);
		if (expr != null)
		{
			PsiElement firstChild = expr.getFirstChild();
			PsiElement lastChild = expr.getLastChild();

			if (firstChild instanceof PerlString && StringUtil.equals("inherit", ((PerlString) firstChild).getStringContent()) && lastChild instanceof PerlString)
			{
				return ((PerlString) lastChild).getStringContent();
			}
		}

		return UNDEF_RESULT;
	}
}
