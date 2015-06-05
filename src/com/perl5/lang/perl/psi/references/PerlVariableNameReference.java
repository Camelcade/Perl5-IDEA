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
import com.intellij.psi.ResolveResult;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 27.05.2015.
 */
public class PerlVariableNameReference extends PerlReferencePoly
{

	private PerlNamespaceElement myNamespaceElement;
	private String myVariableName;
	private PerlVariableType actualType;
	private PerlVariable myVariable;

	public PerlVariableNameReference(@NotNull PsiElement element, TextRange textRange)
	{
		super(element, textRange);
		assert element instanceof PerlVariableNameElement;
		myVariableName = ((PerlVariableNameElement) element).getName();

		if (element.getParent() instanceof PerlVariable)
		{
			myVariable = (PerlVariable) element.getParent();
			myNamespaceElement = myVariable.getNamespaceElement();
			actualType = myVariable.getActualType();
		} else
			throw new RuntimeException("Can't be: got myVariable name without a myVariable");
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		List<ResolveResult> result;

		PsiElement variableContainer = myVariable.getParent();

		if (variableContainer instanceof PsiPerlVariableDeclarationLexical)
			return new ResolveResult[0];
		else if (variableContainer instanceof PsiPerlVariableDeclarationGlobal)
		{
			// our variable declaration
			result = new ArrayList<>();
			Project project = myVariable.getProject();
			String canonicalName = myVariable.getCanonicalName();

			for (PerlGlobVariable glob : PerlGlobUtil.findGlobsDefinitions(project, canonicalName))
				result.add(new PsiElementResolveResult(glob));

			// other declarations of the same variable
			if (actualType == PerlVariableType.SCALAR)
			{
				for (PerlVariable variable : PerlScalarUtil.findGlobalScalarDefinitions(project, canonicalName))
					if (!variable.equals(myVariable))
						result.add(new PsiElementResolveResult(variable));
			} else if (actualType == PerlVariableType.ARRAY)
			{
				for (PerlVariable variable : PerlArrayUtil.findGlobalArrayDefinitions(project, canonicalName))
					if (!variable.equals(myVariable))
						result.add(new PsiElementResolveResult(variable));
			} else if (actualType == PerlVariableType.HASH)
			{
				for (PerlVariable variable : PerlHashUtil.findGlobalHashDefinitions(project, canonicalName))
					if (!variable.equals(myVariable))
						result.add(new PsiElementResolveResult(variable));
			}

		} else if (actualType == PerlVariableType.SCALAR)
			result = resolveScalarName();
		else if (actualType == PerlVariableType.ARRAY)
			result = resolveArrayName();
		else if (actualType == PerlVariableType.HASH)
			result = resolveHashName();
		else
			throw new RuntimeException("Can't be: resolving variable of unknown type");

		return result.toArray(new ResolveResult[result.size()]);
	}

	/**
	 * Resolving names for scalars, array elements and hash elements
	 *
	 * @return declared variable name
	 */
	List<ResolveResult> resolveScalarName()
	{
		List<ResolveResult> result = new ArrayList<>();

		if (myNamespaceElement == null)
		{
			Collection<PerlVariable> variables = PerlUtil.findDeclaredLexicalVariables(myElement.getParent());
			// trying to find lexical variable
			for (PerlVariable variable : variables)
			{
				if (variable instanceof PsiPerlScalarVariable && myVariableName.equals(variable.getName()))
				{
					result.add(new PsiElementResolveResult(variable));
					break;
				}
			}
		}

		if (result.size() == 0)
		{
			// not found, trying to find global variables
			String packageName = myVariable.getPackageName();
			String canonicalName = packageName + "::" + myVariableName;

			for (PerlVariable variable : PerlScalarUtil.findGlobalScalarDefinitions(myElement.getProject(), canonicalName))
				result.add(new PsiElementResolveResult(variable));

			// globs definitions
			// todo here we should validate glob if it's wildcard or {SCALAR}
			for (PsiPerlGlobVariable glob : PerlGlobUtil.findGlobsDefinitions(myElement.getProject(), canonicalName))
				result.add(new PsiElementResolveResult(glob));
		}

		return result;
	}

	/**
	 * Resolving names for arrays, hash slices and scalar dereference to array
	 *
	 * @return declared variable name
	 */
	List<ResolveResult> resolveArrayName()
	{
		List<ResolveResult> result = new ArrayList<>();

		if (myNamespaceElement == null)
		{
			Collection<PerlVariable> variables = PerlUtil.findDeclaredLexicalVariables(myElement.getParent());
			// trying to find lexical variable
			for (PerlVariable variable : variables)
			{
				if (variable instanceof PsiPerlArrayVariable && myVariableName.equals(variable.getName()))
				{
					result.add(new PsiElementResolveResult(variable));
					break;
				}
			}
		}

		if (result.size() == 0)
		{
			// not found, trying to find global variables
			String packageName = myVariable.getPackageName();
			String canonicalName = packageName + "::" + myVariableName;

			for (PerlVariable variable : PerlArrayUtil.findGlobalArrayDefinitions(myElement.getProject(), canonicalName))
				result.add(new PsiElementResolveResult(variable));

			// globs definitions
			// todo here we should validate glob if it's wildcard or {ARRAY}
			for (PsiPerlGlobVariable glob : PerlGlobUtil.findGlobsDefinitions(myElement.getProject(), canonicalName))
				result.add(new PsiElementResolveResult(glob));
		}

		return result;
	}

	/**
	 * Resolving names for hashes and scalar dereference to hash
	 *
	 * @return declared variable name
	 */
	List<ResolveResult> resolveHashName()
	{
		List<ResolveResult> result = new ArrayList<>();

		if (myNamespaceElement == null)
		{
			Collection<PerlVariable> variables = PerlUtil.findDeclaredLexicalVariables(myElement.getParent());
			// trying to find lexical variable
			for (PerlVariable variable : variables)
			{
				if (variable instanceof PsiPerlHashVariable && myVariableName.equals(variable.getName()))
				{
					result.add(new PsiElementResolveResult(variable));
					break;
				}
			}
		}

		if (result.size() == 0)
		{
			// not found, trying to find global variables
			String packageName = myVariable.getPackageName();

			String canonicalName = packageName + "::" + myVariableName;

			for (PerlVariable variable : PerlHashUtil.findGlobalHashDefinitions(myElement.getProject(), canonicalName))
				result.add(new PsiElementResolveResult(variable));

			// globs definitions
			// todo here we should validate glob if it's wildcard or {HASH}
			for (PsiPerlGlobVariable glob : PerlGlobUtil.findGlobsDefinitions(myElement.getProject(), canonicalName))
				result.add(new PsiElementResolveResult(glob));

		}

		return result;
	}

	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		PsiElement parent = element.getParent();
		if (parent instanceof PerlVariable || parent instanceof PerlGlobVariable)
			return super.isReferenceTo(parent) || super.isReferenceTo(element);

		return super.isReferenceTo(element);
	}

	@Nullable
	@Override
	public PsiElement resolve()
	{
		ResolveResult[] resolveResults = multiResolve(false);
		if( resolveResults.length == 0)
			return null;

		for( ResolveResult resolveResult: resolveResults)
			if( resolveResult.getElement() instanceof PerlGlobVariable)
				return resolveResult.getElement();

		return resolveResults[0].getElement();
	}
}
