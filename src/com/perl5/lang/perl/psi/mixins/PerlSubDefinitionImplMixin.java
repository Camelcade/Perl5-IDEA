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
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.SmartList;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.stubs.subs.PerlSubDefinitionStub;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 25.05.2015.
 *
 */
public abstract class PerlSubDefinitionImplMixin extends StubBasedPsiElementBase<PerlSubDefinitionStub> implements PsiPerlSubDefinition
{
	private List<PerlSubArgument> myArguments = null;

	public PerlSubDefinitionImplMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlSubDefinitionImplMixin(@NotNull PerlSubDefinitionStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public String getPackageName()
	{
		getArgumentsList();

		PerlSubDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getPackageName();

		String namespace = getExplicitPackageName();

		if (namespace == null)
			namespace = getContextPackageName();


		return namespace;
	}

	@Override
	public String getFunctionName()
	{
		PerlSubDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getFunctionName();

		PerlSubName function = getSubNameElement();
		return function.getName();
	}

	@Override
	public String getContextPackageName()
	{
		return PerlPackageUtil.getContextPackageName(this);
	}

	@Override
	public String getExplicitPackageName()
	{
		PerlNamespace namespace = getNamespaceElement();
		return namespace != null ? namespace.getName() : null;
	}

	@Override
	public PerlLexicalScope getLexicalScope()
	{
		PerlLexicalScope scope = PsiTreeUtil.getParentOfType(this, PerlLexicalScope.class);
		assert scope != null;
		return scope;
	}

	@Override
	public PerlNamespace getNamespaceElement()
	{
		return findChildByClass(PerlNamespace.class);
	}

	@Override
	public PerlSubName getSubNameElement()
	{
		return findChildByClass(PerlSubName.class);
	}


	@Override
	public boolean isMethod()
	{
		PerlSubDefinitionStub stub = getStub();
		if( stub != null )
			return stub.isMethod();

		List<PerlSubArgument> arguments = getArgumentsList();
		if (arguments.size() == 0)
			return false;

		PerlSubArgument firstArgument = arguments.get(0);

		return firstArgument.getArgumentType() == PerlVariableType.SCALAR && PerlThisNames.NAMES_SET.contains(firstArgument.getArgumentName());
	}

	@Override
	public List<PerlSubArgument> getArgumentsList()
	{
		if (myArguments != null)
			return myArguments;

		PerlSubDefinitionStub stub = getStub();
		if( stub != null )
			return stub.getArgumentsList();

		List<PerlSubArgument> arguments = new ArrayList<>();

		// todo add stubs reading here

		PsiPerlBlock subBlock = getBlock();
		for (PsiElement statement : subBlock.getChildren())
		{
			PsiPerlAssignExpr assignExpression = PsiTreeUtil.findChildOfType(statement, PsiPerlAssignExpr.class);
			if (assignExpression != null)
			{
				Collection<PsiPerlExpr> assignTerms = assignExpression.getExprList();
				assert assignTerms instanceof SmartList;
				if (assignTerms.size() == 2)
				{
					PsiPerlExpr leftTerm = (PsiPerlExpr) ((SmartList) assignTerms).get(0);
					PsiPerlExpr rightTerm = (PsiPerlExpr) ((SmartList) assignTerms).get(1);

					PsiPerlVariableDeclarationLexical declaration = PsiTreeUtil.findChildOfType(leftTerm, PsiPerlVariableDeclarationLexical.class);
					if (declaration != null)
					{
						PerlNamespace variableClass = declaration.getNamespaceElement();
						String definitionClassName = "";
						if (variableClass != null)
							definitionClassName = variableClass.getName();

						if ("@_".equals(rightTerm.getText()))
						{
							for (PerlVariable variable : PsiTreeUtil.findChildrenOfType(declaration, PerlVariable.class))
							{
								PerlVariableName variableName = variable.getVariableName();

								if (variableName != null)
									arguments.add(new PerlSubArgument(variable.getActualType(), variableName.getName(), definitionClassName, true));
							}
							break;

						} else if ("shift".equals(rightTerm.getText()))
						{
							PerlVariable variable = PsiTreeUtil.findChildOfType(declaration, PerlVariable.class);

							if (variable != null)
							{
								PerlVariableName variableName = variable.getVariableName();

								if (variableName != null)
									arguments.add(new PerlSubArgument(variable.getActualType(), variableName.getName(), definitionClassName, true));
							}
						}
					} else
						// todo dunno what can else be here
						break;
				} else
					// todo dunno how to handle this yet like my $scalar = my $something = shift;
					break;
			} else
				// not an assignment here
				break;
		}

		myArguments = arguments;
		return myArguments;
	}
}
