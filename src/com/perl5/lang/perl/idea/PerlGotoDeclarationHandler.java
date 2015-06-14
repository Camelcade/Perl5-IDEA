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

package com.perl5.lang.perl.idea;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import com.perl5.lang.perl.psi.PsiPerlVariableDeclarationGlobal;
import com.perl5.lang.perl.psi.PsiPerlVariableDeclarationLexical;
import com.perl5.lang.perl.psi.impl.PerlFileElement;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Created by hurricup on 05.06.2015.
 */
public class PerlGotoDeclarationHandler implements GotoDeclarationHandler
{
	@Nullable
	@Override
	public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement sourceElement, int offset, Editor editor)
	{
		ArrayList<PsiElement> result = new ArrayList<>();
		if (sourceElement != null)
			for (PsiReference reference : sourceElement.getReferences())
				if (reference instanceof PsiPolyVariantReference)
					for (ResolveResult resolveResult : ((PsiPolyVariantReference) reference).multiResolve(false))
						result.add(resolveResult.getElement());
				else
					result.add(reference.resolve());

		// add shadowed variables declaration
		if (sourceElement instanceof PerlVariableNameElement)
		{
			PsiElement variable = sourceElement.getParent();

			if (variable instanceof PerlVariable)
			{
				PsiElement variableContainer = sourceElement.getParent().getParent();
				PsiFile myFile = sourceElement.getContainingFile();

				if (myFile instanceof PerlFileElement && (variableContainer instanceof PsiPerlVariableDeclarationLexical || variableContainer instanceof PsiPerlVariableDeclarationGlobal))
				{
					PerlVariable shadowedVariable = ((PerlFileElement) myFile).getLexicalDeclaration((PerlVariable)variable);
					if (shadowedVariable != null && !result.contains(shadowedVariable))
						result.add(shadowedVariable);
				}
			}
		}

		return result.toArray(new PsiElement[result.size()]);
	}

	@Nullable
	@Override
	public String getActionText(DataContext context)
	{
		return null;

	}

}
