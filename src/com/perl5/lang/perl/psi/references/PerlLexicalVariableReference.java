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

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlHeredocOpenerImpl;
import com.perl5.lang.perl.util.PerlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created by hurricup on 27.05.2015.
 */
public class PerlLexicalVariableReference extends PerlReference
{
	private String variableName;

	public PerlLexicalVariableReference(@NotNull PsiElement element, TextRange textRange) {
		super(element, textRange);
		assert element instanceof PerlVariableName;
		variableName = ((PerlVariableName) element).getName();
	}

	@NotNull
	@Override
	public Object[] getVariants()
	{
		return new Object[0];
	}

	@Nullable
	@Override
	public PsiElement resolve()
	{
		PsiElement variable = myElement.getParent();

		if( variable instanceof PerlVariable )
		{
			// todo we could move this to reference contributor
			if( ((PerlVariable) variable).getNamespace() != null  )	// global variable
				return null;

			PsiElement variableContainer = variable.getParent();

			Collection<PerlVariable> declaredVariables = PerlUtil.findDeclaredLexicalVariables(myElement.getParent());

			if(
				variableContainer instanceof PerlScalarHashElement
				|| variableContainer instanceof PerlArrayHashSlice
				|| variable instanceof PerlPerlHash
			)
				return resolveHashName(declaredVariables);
			else if(
				variableContainer instanceof PerlDerefExpr
				|| variable instanceof PerlPerlScalar
			)
				return resolveScalarName(declaredVariables);
			else if(
					variableContainer instanceof PerlArrayArraySlice
				|| variableContainer instanceof PerlScalarArrayElement
				|| variable instanceof PerlPerlArray
			)
				return resolveArrayName(declaredVariables);
			else
				System.err.println(String.format("Unable to detect type for %s in %s", variable.getClass(), variableContainer.getClass()));
		}
		else
		{
			System.err.println(String.format("Attempt to find a reference for %s - %s", variable.getClass(), myElement.getText()));
		}

		return null;
	}

	/**
	 * Resolving names for scalars, array elements and hash elements
	 * @param variables list of lexical declared variables
	 * @return declared variable name
	 */
	PsiElement resolveScalarName(Collection<PerlVariable> variables)
	{
		for( PerlVariable variable: variables)
		{
			PerlVariableName variableName = variable.getVariableName();
			if( variable instanceof PerlPerlScalar && variableName != null && this.variableName.equals(variableName.getName()) )
				return variableName;
		}

		return null;
	}

	/**
	 * Resolving names for arrays, hash slices and scalar dereference to array
	 * @param variables list of lexical declared variables
	 * @return declared variable name
	 */
	PsiElement resolveArrayName(Collection<PerlVariable> variables)
	{
		for( PerlVariable variable: variables)
		{
			PerlVariableName variableName = variable.getVariableName();
			if( variable instanceof PerlPerlArray && variableName != null && this.variableName.equals(variableName.getName()) )
				return variableName;
		}

		return null;
	}

	/**
	 * Resolving names for hashes and scalar dereference to hash
	 * @param variables list of lexical declared variables
	 * @return declared variable name
	 */
	PsiElement resolveHashName(Collection<PerlVariable> variables)
	{
		for( PerlVariable variable: variables)
		{
			PerlVariableName variableName = variable.getVariableName();
			if( variable instanceof PerlPerlHash && variableName != null && this.variableName.equals(variableName.getName()) )
				return variableName;
		}

		return null;
	}
}
