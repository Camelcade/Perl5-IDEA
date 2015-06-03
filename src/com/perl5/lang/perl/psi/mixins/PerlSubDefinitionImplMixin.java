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
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.SmartList;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import com.perl5.lang.perl.psi.stubs.subs.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.*;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 25.05.2015.
 *
 */
public abstract class PerlSubDefinitionImplMixin extends StubBasedPsiElementBase<PerlSubDefinitionStub> implements PsiPerlSubDefinition
{
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
		getSubArgumentsList();

		PerlSubDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getPackageName();

		String namespace = getExplicitPackageName();

		if (namespace == null)
			namespace = getContextPackageName();


		return namespace;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return getSubNameElement();
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		PerlSubNameElement subNameElement = getSubNameElement();
		if( subNameElement != null)
			subNameElement.setName(name);
		return this;
	}

	@Override
	public String getName()
	{
		return getSubName();
	}

	@Override
	public String getCanonicalName()
	{
		return getPackageName() + "::" + getSubName();
	}

	@Override
	public String getSubName()
	{
		PerlSubDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getSubName();

		PerlSubNameElement subNameElement = getSubNameElement();
		return subNameElement.getName();
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
	public PerlLexicalScope getLexicalScope()
	{
		PerlLexicalScope scope = PsiTreeUtil.getParentOfType(this, PerlLexicalScope.class);
		assert scope != null;
		return scope;
	}


	@Override
	public PerlNamespaceElement getNamespaceElement()
	{
		return findChildByClass(PerlNamespaceElement.class);
	}

	@Override
	public PerlSubNameElement getSubNameElement()
	{
		return findChildByClass(PerlSubNameElement.class);
	}


	@Override
	public boolean isMethod()
	{
		// todo check annotation for method
		PerlSubDefinitionStub stub = getStub();
		if( stub != null )
			return stub.isMethod();

		List<PerlSubArgument> arguments = getSubArgumentsList();
		if (arguments.size() == 0)
			return false;

		PerlSubArgument firstArgument = arguments.get(0);

		return firstArgument.getArgumentType() == PerlVariableType.SCALAR && PerlThisNames.NAMES_SET.contains(firstArgument.getArgumentName());
	}

	@Override
	public List<PerlSubArgument> getSubArgumentsList()
	{
		PerlSubDefinitionStub stub = getStub();
		if( stub != null )
			return stub.getSubArgumentsList();

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
						PerlNamespaceElement variableClass = declaration.getNamespaceElement();
						String definitionClassName = "";
						if (variableClass != null)
							definitionClassName = variableClass.getName();

						if ("@_".equals(rightTerm.getText()))
						{
							for (PerlVariable variable : PsiTreeUtil.findChildrenOfType(declaration, PerlVariable.class))
							{
								PerlVariableNameElement variableNameElement = variable.getVariableNameElement();

								if (variableNameElement != null)
									arguments.add(new PerlSubArgument(variable.getActualType(), variableNameElement.getName(), definitionClassName, true));
							}
							break;

						} else if ("shift".equals(rightTerm.getText()))
						{
							PerlVariable variable = PsiTreeUtil.findChildOfType(declaration, PerlVariable.class);

							if (variable != null)
							{
								PerlVariableNameElement variableNameElement = variable.getVariableNameElement();

								if (variableNameElement != null)
									arguments.add(new PerlSubArgument(variable.getActualType(), variableNameElement.getName(), definitionClassName, true));
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

		return arguments;
	}

	@Override
	public PerlSubAnnotations getSubAnnotations()
	{
		PerlSubDefinitionStub stub = getStub();
		if( stub != null )
			return stub.getSubAnnotations();

		PerlSubAnnotations myAnnotations = new PerlSubAnnotations();

		for(PsiPerlAnnotation annotation: getAnnotationList())
		{
			if (annotation instanceof PsiPerlAnnotationAbstract)
				myAnnotations.setIsAbstract(true);
			else if (annotation instanceof PsiPerlAnnotationDeprectaed)
				myAnnotations.setIsDeprecated(true);
			else if (annotation instanceof PsiPerlAnnotationMethod)
				myAnnotations.setIsMethod(true);
			else if (annotation instanceof PsiPerlAnnotationOverride)
				myAnnotations.setIsOverride(true);
			else if (annotation instanceof PerlNamespaceElementContainer) // returns
			{
				PerlNamespaceElement ns = ((PerlNamespaceElementContainer) annotation).getNamespaceElement();
				if( ns != null)
				{
					myAnnotations.setReturns(ns.getName());
					myAnnotations.setReturnType(PerlReturnType.REF);
					// todo implement brackets and braces
				}
			}
		}

		return myAnnotations;
	}
}
