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

package com.perl5.lang.perl.idea.formatter.operations;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;

/**
 * Created by evstigneev on 16.11.2015.
 * Converting $$var{key} to $var->{key}
 */
public class PerlFormattingScalarDerefExpand implements PerlFormattingOperation
{
	protected final PsiPerlScalarCastExpr myScalarDereference;

	public PerlFormattingScalarDerefExpand(PsiPerlScalarCastExpr scalarDereference)
	{
		myScalarDereference = scalarDereference;
	}

	@Override
	public int apply()
	{
		int delta = 0;

		if (myScalarDereference.isValid())
		{
			PsiElement parent = myScalarDereference.getParent();
			PsiPerlScalarVariable scalarVariable = PsiTreeUtil.findChildOfType(myScalarDereference, PsiPerlScalarVariable.class);
			if (parent.isValid() &&
					(parent instanceof PsiPerlHashElement || parent instanceof PsiPerlArrayElement) &&
					scalarVariable != null &&
					scalarVariable.isValid())
			{
				PsiElement indexElement = parent.getLastChild();
				if (indexElement.isValid() && (indexElement instanceof PsiPerlHashIndex || indexElement instanceof PsiPerlArrayIndex))
				{
					String newCode = scalarVariable.getNode().getText() + "->" + indexElement.getText();
					PerlFileImpl newFile = PerlElementFactory.createFile(myScalarDereference.getProject(), newCode);
					PsiPerlDerefExpr derefExpr = PsiTreeUtil.findChildOfType(newFile, PsiPerlDerefExpr.class);
					if (derefExpr != null)
					{
						if (parent.getParent() instanceof PsiPerlDerefExpr)
						{
							delta = new PerlFormattingReplace(parent, parent, derefExpr.getFirstChild(), derefExpr.getLastChild()).apply();
						}
						else
						{
							delta = new PerlFormattingReplace(parent, derefExpr).apply();
						}
					}
				}
			}
		}

		return delta;
	}

}
