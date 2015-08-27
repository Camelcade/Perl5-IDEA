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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlHashUtil;
import com.perl5.lang.perl.util.PerlScalarUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hurricup on 27.05.2015.
 */
public class PerlVariableNameReference extends PerlReferencePoly
{

	private PerlVariable myVariable;

	public PerlVariableNameReference(@NotNull PsiElement element, TextRange textRange)
	{
		super(element, textRange);
		assert element instanceof PerlVariableNameElement;

		if (element.getParent() instanceof PerlVariable)
			myVariable = (PerlVariable) element.getParent();
		else
			throw new RuntimeException("Can't be: got myVariable name without a myVariable");
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		List<ResolveResult> result = new ArrayList<ResolveResult>();

		PsiElement variableContainer = myVariable.getParent();
		PerlVariable lexicalDeclaration = null;

		if (variableContainer instanceof PsiPerlVariableDeclarationLexical)
			return new ResolveResult[0];
		else if (!(variableContainer instanceof PsiPerlVariableDeclarationGlobal))
			lexicalDeclaration = myVariable.getLexicalDeclaration();

		if (lexicalDeclaration == null || lexicalDeclaration.getParent() instanceof PsiPerlVariableDeclarationGlobal)
		{
			// not found lexical declaration or our is closes to us

			// imports
			Map<String, Set<String>> importsMap = null;
			PerlVariableType actualType = myVariable.getActualType();
			Project project = myVariable.getProject();
			PerlNamespaceContainer namespaceContainer = PsiTreeUtil.getParentOfType(myVariable, PerlNamespaceContainer.class);
			assert namespaceContainer != null;
			String packageName = namespaceContainer.getPackageName();

			if (packageName != null)
			{
				PsiFile originalFile = myVariable.getContainingFile();
				String variableName = myVariable.getName();

				// fixme DRY this
				if (actualType == PerlVariableType.SCALAR)
				{
					importsMap = PerlScalarUtil.getImportedScalars(project, packageName, originalFile);
					for (Map.Entry<String, Set<String>> importEntry : importsMap.entrySet())
						for (String variable : importEntry.getValue())
							if (variable.substring(1).equals(variableName))
								for (PerlVariable targetVariable : PerlScalarUtil.getGlobalScalarDefinitions(project, importEntry.getKey() + "::" + variableName))
									result.add(new PsiElementResolveResult(targetVariable));
				} else if (actualType == PerlVariableType.ARRAY)
				{
					importsMap = PerlArrayUtil.getImportedArrays(project, packageName, originalFile);
					for (Map.Entry<String, Set<String>> importEntry : importsMap.entrySet())
						for (String variable : importEntry.getValue())
							if (variable.substring(1).equals(variableName))
								for (PerlVariable targetVariable : PerlArrayUtil.getGlobalArrayDefinitions(project, importEntry.getKey() + "::" + variableName))
									result.add(new PsiElementResolveResult(targetVariable));
				} else if (actualType == PerlVariableType.HASH)
				{
					importsMap = PerlHashUtil.getImportedHashes(project, packageName, originalFile);
					for (Map.Entry<String, Set<String>> importEntry : importsMap.entrySet())
						for (String variable : importEntry.getValue())
							if (variable.substring(1).equals(variableName))
								for (PerlVariable targetVariable : PerlHashUtil.getGlobalHashDefinitions(project, importEntry.getKey() + "::" + variableName))
									result.add(new PsiElementResolveResult(targetVariable));
				}

			}

			// our variable declaration
			for (PerlGlobVariable glob : myVariable.getRelatedGlobs())
				result.add(new PsiElementResolveResult(glob));

			// globs
			for (PerlVariable globalDeclaration : myVariable.getGlobalDeclarations())
				result.add(new PsiElementResolveResult(globalDeclaration));
		} else
			result.add(new PsiElementResolveResult(lexicalDeclaration));

		return result.toArray(new ResolveResult[result.size()]);
	}

	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		if (element instanceof PerlVariable || element instanceof PerlGlobVariable)
			return super.isReferenceTo(element);
		else if (element instanceof PerlVariableNameElement)
			return isReferenceTo(element.getParent());

		return false;
	}

	@Nullable
	@Override
	public PsiElement resolve()
	{
		ResolveResult[] resolveResults = multiResolve(false);
		if (resolveResults.length == 0)
			return null;
		else if (resolveResults.length == 1)
			return resolveResults[0].getElement();


		PerlGlobVariable lastGlob = null;
		for (ResolveResult resolveResult : resolveResults)
			if (resolveResult.getElement() instanceof PerlGlobVariable)
			{
				lastGlob = (PerlGlobVariable) resolveResult.getElement();
				if (lastGlob.isLeftSideOfAssignment())
					return lastGlob;
			}

		if (lastGlob != null)
			return lastGlob;

		return resolveResults[0].getElement();
	}
}
