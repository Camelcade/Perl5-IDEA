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

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.SmartList;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.psi.utils.PerlThisNames;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 25.05.2015.
 */
public abstract class PerlSubDefinitionImplMixin extends PerlSubBaseMixin<PerlSubDefinitionStub> implements PsiPerlSubDefinition
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
	public PerlLexicalScope getLexicalScope()
	{
		PerlLexicalScope scope = PsiTreeUtil.getParentOfType(this, PerlLexicalScope.class);
		assert scope != null;
		return scope;
	}


	@Override
	public boolean isMethod()
	{
		PerlSubDefinitionStub stub = getStub();
		if (stub != null)
			return stub.isMethod();

		if (getSubAnnotations().isMethod())
			return true;

		List<PerlSubArgument> arguments = getSubArgumentsList();
		if (arguments.size() == 0)
			return false;

		PerlSubArgument firstArgument = arguments.get(0);

		return firstArgument.getArgumentType() == PerlVariableType.SCALAR && PerlThisNames.NAMES_SET.contains(firstArgument.getArgumentName());
	}

	@Override
	public String getSubArgumentsListAsString()
	{
		List<PerlSubArgument> subArguments = getSubArgumentsList();

		if (isMethod() && subArguments.size() > 0)
			subArguments.remove(0);

		int argumentsNumber = subArguments.size();

		List<String> argumentsList = new ArrayList<String>();
		for (PerlSubArgument argument : subArguments)
		{
			// todo we can mark optional subArguments after prototypes implementation
			argumentsList.add(argument.toStringShort());

			int compiledListSize = argumentsList.size();
			if (compiledListSize > 4 && argumentsNumber > compiledListSize)
			{
				argumentsList.add("...");
				break;
			}
		}

		return argumentsList.size() > 0
				? "(" + StringUtils.join(argumentsList, ", ") + ")"
				: "";
	}

	@Override
	public List<PerlSubArgument> getSubArgumentsList()
	{
		PerlSubDefinitionStub stub = getStub();
		if (stub != null)
			return stub.getSubArgumentsList();

		List<PerlSubArgument> arguments = new ArrayList<PerlSubArgument>();

		// todo add stubs reading here

		PsiPerlBlock subBlock = getBlock();

		if (subBlock != null)
		{

			for (PsiElement statement : subBlock.getChildren())
			{
				PsiPerlAssignExpr assignExpression = PsiTreeUtil.findChildOfType(statement, PsiPerlAssignExpr.class);
				if (assignExpression != null)
				{
					Collection<PsiPerlExpr> assignTerms = assignExpression.getExprList();
					assert assignTerms instanceof SmartList;
					if (assignTerms.size() == 2)
					{
						// fixme need to implement things in assignExpr
						PsiPerlExpr leftTerm = (PsiPerlExpr) ((SmartList) assignTerms).get(0);
						PsiPerlExpr rightTerm = (PsiPerlExpr) ((SmartList) assignTerms).get(1);

						if (leftTerm instanceof PsiPerlVariableDeclarationLexical)
						{
							PsiPerlVariableDeclarationLexical declaration = (PsiPerlVariableDeclarationLexical) leftTerm;
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
		}

		return arguments;
	}

	@Override
	public ItemPresentation getPresentation()
	{
		return new PerlItemPresentationSimple(this, "Sub definition");
	}

}
