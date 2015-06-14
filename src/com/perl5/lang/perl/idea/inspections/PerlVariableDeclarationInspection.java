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

package com.perl5.lang.perl.idea.inspections;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 14.06.2015.
 */
public class PerlVariableDeclarationInspection extends PerlInspection
{
	@NotNull
	@Override
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PerlVisitor()
		{
			@Override
			public void visitPerlVariable(@NotNull PerlVariable element)
			{
				PsiElement parent = element.getParent();

				boolean isGlobalDeclaration = parent instanceof PsiPerlVariableDeclarationGlobal;
				boolean isLexicalDeclaration = parent instanceof PsiPerlVariableDeclarationLexical;
				boolean isLocalDeclaration = parent instanceof PsiPerlVariableDeclarationLocal;

				if (!isGlobalDeclaration && !isLexicalDeclaration && !isLocalDeclaration)
					return;

				PerlVariableNameElement variableNameElement = element.getVariableNameElement();
				if (variableNameElement == null)
					return;

				PerlVariable lexicalDeclaration = element.getLexicalDeclaration();
				PerlNamespaceElement namespaceElement = element.getNamespaceElement();

				boolean hasExplicitNamespace = namespaceElement != null;

				if (!hasExplicitNamespace)
				{
					if (!isLocalDeclaration && element.isBuiltIn())
						registerProblem(holder, variableNameElement, "It's not allowed to declare built-in variable as our/my/state");
					else if (isLexicalDeclaration && lexicalDeclaration != null)
						registerProblem(holder, variableNameElement, "Current lexical variable declaration shadows previous declaration of the same variable at line " + lexicalDeclaration.getLineNumber() + ". It's not a error, but not recommended.");
					else if (isGlobalDeclaration && lexicalDeclaration != null)
						registerProblem(holder, variableNameElement, "Current global variable declaration shadows previous declaration of the same variable at line " + lexicalDeclaration.getLineNumber() + ". It's not a error, but not recommended.");
				} else
				{
					List<PerlVariable> globalDeclarations = element.getGlobalDeclarations();
					List<PerlGlobVariable> relatedGlobs = element.getRelatedGlobs();

					if (globalDeclarations.size() > 0 && relatedGlobs.size() > 0)
						registerProblem(holder, variableNameElement, "Both global declaration and typeglob aliasing found for variable. It's not a error, but we are not recommend such practice to avoid mistakes.");
					// fixme not sure it's good idea, at least, should be optional
//					else if( relatedGlobs.size() > 1  )
//						registerProblem(holder, variableNameElement, "Multiple typeglob aliasing found. It's not a error, but we are not recommend such practice to avoid mistakes.");
				}
			}
		};
	}
}
