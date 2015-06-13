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

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 13.06.2015.
 *
 */
public class PerlUndefinedVariableInspection extends LocalInspectionTool
{
	@NotNull
	@Override
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly)
	{
		return new PsiPerlVisitor(){
			@Override
			public void visitScalarVariable(@NotNull PsiPerlScalarVariable o)
			{
				visitVariable(o);
			}

			@Override
			public void visitArrayIndexVariable(@NotNull PsiPerlArrayIndexVariable o)
			{
				visitVariable(o);
			}

			@Override
			public void visitArrayVariable(@NotNull PsiPerlArrayVariable o)
			{
				visitVariable(o);
			}

			@Override
			public void visitHashVariable(@NotNull PsiPerlHashVariable o)
			{
				visitVariable(o);
			}

			private void visitVariable(PerlVariable element)
			{
				// todo move to inspections
				PsiElement parent = element.getParent();

				PerlVariable lexicalDeclaration = element.getLexicalDeclaration();

				boolean isGlobalDeclaration = parent instanceof PsiPerlVariableDeclarationGlobal;
				boolean isLexicalDeclaration = parent instanceof PsiPerlVariableDeclarationLexical;
				PerlNamespaceElement namespaceElement = element.getNamespaceElement();
				PerlVariableNameElement variableNameElement = element.getVariableNameElement();

				if (variableNameElement == null || element.isBuiltIn())
					return;

				boolean hasExplicitNamespace = namespaceElement != null;

				if (!hasExplicitNamespace)
				{
					if (isLexicalDeclaration && lexicalDeclaration != null)
						registerProblem(holder, variableNameElement, "Current lexical variable declaration shadows previous declaration of the same variable at line " + lexicalDeclaration.getLineNumber() + ". It's not a error, but not recommended.");
					else if (isGlobalDeclaration && lexicalDeclaration != null)
						registerProblem(holder, variableNameElement, "Current global variable declaration shadows previous declaration of the same variable at line " + lexicalDeclaration.getLineNumber() + ". It's not a error, but not recommended.");
					else if (lexicalDeclaration == null && !isGlobalDeclaration && !isLexicalDeclaration)
						registerProblem(holder, variableNameElement, "Unable to find variable declaration in the current file. Forgot to use our for package variable in the current file?");
				} else
				{
					List<PerlVariable> globalDeclarations = element.getGlobalDeclarations();
					List<PerlGlobVariable> relatedGlobs = element.getRelatedGlobs();

					if (globalDeclarations.size() == 0 && relatedGlobs.size() == 0)
						registerProblem(holder, variableNameElement, "Unable to find global variable declaration or typeglob aliasing for variable. It's not a error, but you should declare it using our() or typeglob alias to make refactoring work properly.");
					else if (globalDeclarations.size() > 0 && relatedGlobs.size() > 0)
						registerProblem(holder, variableNameElement, "Both global declaration and typeglob aliasing found for variable. It's not a error, but we are not recommend such practice to avoid mistakes.");
					// fixme not sure it's good idea, at least, should be optional
//				else if( relatedGlobs.size() > 1  )
//					holder.createWarningAnnotation(element, "Multiple typeglob aliasing found. It's not a error, but we are not recommend such practice to avoid mistakes.");
				}

				// todo annotate found variables here, not in the beginnig
			}

			protected void registerProblem(ProblemsHolder holder, PsiElement element, String message)
			{
				holder.registerProblem(element, message, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
			}
		};
	}
}
