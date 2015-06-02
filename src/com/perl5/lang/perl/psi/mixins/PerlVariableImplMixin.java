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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.references.PerlVariableNameReference;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableStub;
import com.perl5.lang.perl.psi.utils.PerlThisNames;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 24.05.2015.
 */
public abstract class PerlVariableImplMixin extends StubBasedPsiElementBase<PerlVariableStub> implements PerlElementTypes, PerlVariable //
{
	public PerlVariableImplMixin(PerlVariableStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public PerlVariableImplMixin(ASTNode node)
	{
		super(node);
	}

	@Override
	public PerlLexicalScope getLexicalScope()
	{
		return PsiTreeUtil.getParentOfType(this, PerlLexicalScope.class);
	}

	@Override
	public String getPackageName()
	{
		PerlVariableStub stub = getStub();
		if (stub != null)
			return stub.getPackageName();

		String namespace = getExplicitPackageName();

		if (namespace == null)
			namespace = getContextPackageName();

		return namespace;
	}

	@Override
	public String getContextPackageName()
	{
		return PerlPackageUtil.getContextPackageName(this);
	}

	@Override
	public String getExplicitPackageName()
	{
		PerlNamespaceElement namespaceElement = getNamespaceElement();
		return namespaceElement != null ? namespaceElement.getName() : null;
	}

	@Override
	public PerlNamespaceElement getNamespaceElement()
	{
		return findChildByClass(PerlNamespaceElement.class);
	}

	@Nullable
	@Override
	public PerlVariableNameElement getVariableNameElement()
	{
		return findChildByClass(PerlVariableNameElement.class);
	}

	@Nullable
	@Override
	public String guessVariableType()
	{
		PerlVariableNameElement variableNameObject = getVariableNameElement();

		if (variableNameObject != null)
		{
			String variableName = variableNameObject.getName();

			if (this instanceof PsiPerlScalarVariable && PerlThisNames.NAMES_SET.contains(variableName))
				return PerlPackageUtil.getContextPackageName(this);

			// find declaration and check type
			PsiReference[] references = variableNameObject.getReferences();

			for (PsiReference reference : references)
			{
				assert reference instanceof PerlVariableNameReference;
				ResolveResult[] results = ((PerlVariableNameReference) reference).multiResolve(false);

				for (ResolveResult result : results)
				{
					PsiElement decalarationVariableName = result.getElement();
					PerlVariableDeclaration declaration = PsiTreeUtil.getParentOfType(decalarationVariableName, PerlVariableDeclaration.class);
					if (declaration != null)
					{
						PerlNamespaceElement declarationNamespaceElelement = declaration.getNamespaceElement();
						if (declarationNamespaceElelement != null)
						{
							return declarationNamespaceElelement.getName();
						}
					}
				}
			}

			// todo check assignment expression with this variable in the left and guess from there (constructors, other vars that can have known type
		}

		return null;
	}

	@Override
	public PerlVariableType getActualType()
	{
		PsiElement variableContainer = this.getParent();
		boolean gotScalarSigils = this.getScalarSigils() != null;

		if (
				variableContainer instanceof PsiPerlScalarHashElement
						|| variableContainer instanceof PsiPerlArrayHashSlice
						|| (this instanceof PsiPerlHashVariable && !gotScalarSigils)
				)
			return PerlVariableType.HASH;
		else if (
						variableContainer instanceof PsiPerlArrayArraySlice
						|| variableContainer instanceof PsiPerlScalarArrayElement
						|| (this instanceof PsiPerlArrayIndexVariable && !gotScalarSigils)
						|| (this instanceof PsiPerlArrayVariable && !gotScalarSigils)
				)
			return PerlVariableType.ARRAY;
		else if (
				variableContainer instanceof PsiPerlDerefExpr
						|| this instanceof PsiPerlScalarVariable
						|| gotScalarSigils
				)
			return PerlVariableType.SCALAR;
		else
			throw new RuntimeException("Can't be: could not detect actual type of myVariable: " + getText());

	}

	@Override
	public boolean isBuiltIn()
	{
		if (getNamespaceElement() != null)
			return false;

		if (getVariableNameElement() == null)
			return false;

		PerlVariableType variableType = getActualType();

		// tood make getter for this
		String variableName = getVariableNameElement().getName();

		if (variableType == PerlVariableType.SCALAR)
			return PerlScalarUtil.BUILT_IN.contains(variableName);
		if (variableType == PerlVariableType.ARRAY)
			return PerlArrayUtil.BUILT_IN.contains(variableName);

		return variableType == PerlVariableType.HASH && PerlHashUtil.BUILT_IN.contains(variableName);
	}
}
