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
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.utils.PerlScopeUtil;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 11.11.2015.
 */
public abstract class PerlSubDefinitionBaseImpl<Stub extends PerlSubDefinitionStub> extends PerlSubBaseImpl<Stub> implements PerlSubDefinitionBase<Stub>
{
	public PerlSubDefinitionBaseImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlSubDefinitionBaseImpl(@NotNull Stub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public boolean isMethod()
	{
		if (super.isMethod())
			return true;

		List<PerlSubArgument> arguments = getSubArgumentsList();
		if (arguments.size() == 0)
			return false;

		return arguments.get(0).isSelf(getProject());
	}

	@Override
	public PerlLexicalScope getLexicalScope()
	{
		PerlLexicalScope scope = PsiTreeUtil.getParentOfType(this, PerlLexicalScope.class);
		assert scope != null;
		return scope;
	}

	@Override
	public String getSubArgumentsListAsString()
	{
		List<PerlSubArgument> subArguments = getSubArgumentsList();

		if (isMethod() && subArguments.size() > 0)
			subArguments.remove(0);

		return PerlSubUtil.getArgumentsListAsString(subArguments);
	}


	@Override
	public List<PerlSubArgument> getSubArgumentsList()
	{
		Stub stub = getStub();
		if (stub != null)
			return new ArrayList<PerlSubArgument>(stub.getSubArgumentsList());

		List<PerlSubArgument> arguments = getPerlSubArgumentsFromSignature();

		if (arguments == null)
		{
			arguments = getPerlSubArgumentsFromBody();
		}

		return arguments;
	}


	@NotNull
	protected List<PerlSubArgument> getPerlSubArgumentsFromBody()
	{
		List<PerlSubArgument> arguments = new ArrayList<PerlSubArgument>();

		PsiPerlBlock subBlock = getBlock();

		if (subBlock != null && subBlock.isValid())
		{
			for (PsiElement statement : subBlock.getChildren())
			{
				if (EMPTY_SHIFT_STATEMENT_PATTERN.accepts(statement))
				{
					arguments.add(PerlSubArgument.getEmptyArgument());
				}
				else if (ARGUMENTS_UNPACKING_PATTERN.accepts(statement))
				{
					PerlVariableDeclaration variableDeclaration = PsiTreeUtil.findChildOfType(statement, PerlVariableDeclaration.class);
					if (variableDeclaration != null)
					{
						String variableClass = variableDeclaration.getDeclarationType();
						if (variableClass == null)
							variableClass = "";

						PsiElement currentElement = variableDeclaration.getFirstChild();
						while (currentElement != null)
						{
							if (currentElement instanceof PerlVariableDeclarationWrapper)
							{
								PerlVariable variable = ((PerlVariableDeclarationWrapper) currentElement).getVariable();
								if (variable != null)
								{
									arguments.add(new PerlSubArgument(
											variable.getActualType(),
											variable.getName(),
											variableClass,
											false)
									);
								}
								else
								{
									arguments.add(PerlSubArgument.getEmptyArgument());
								}
							}
							else if (currentElement.getNode().getElementType() == RESERVED_UNDEF)
							{
								arguments.add(PerlSubArgument.getEmptyArgument());
							}
							currentElement = currentElement.getNextSibling();
						}
					}

					if (ARGUMENTS_LAST_UNPACKING_PATTERN.accepts(statement))
						break;
				}
				else
				{
					// unknown statement
					break;
				}
			}
		}

		return arguments;
	}

	@Override
	public ItemPresentation getPresentation()
	{
		return new PerlItemPresentationSimple(this, getPresentableName());
	}

	@Override
	public String getPresentableName()
	{
		String args = getSubArgumentsListAsString();
		return this.getName() + (args.isEmpty() ? "()" : args);
	}

	@Nullable
	@Override
	public List<PerlSubArgument> getPerlSubArgumentsFromSignature()
	{
		List<PerlSubArgument> arguments = null;
		PsiElement signatureContainer = getSignatureContainer();

		if (signatureContainer != null)
		{
			arguments = new ArrayList<PerlSubArgument>();
			//noinspection unchecked

			PsiElement signatureElement = signatureContainer.getFirstChild();

			while (signatureElement != null)
			{
				processSignatureElement(signatureElement, arguments);
				signatureElement = signatureElement.getNextSibling();
			}
		}

		return arguments;
	}

	protected boolean processSignatureElement(PsiElement signatureElement, List<PerlSubArgument> arguments)
	{
		if (signatureElement instanceof PerlVariableDeclarationWrapper)
		{
			PerlVariable variable = ((PerlVariableDeclarationWrapper) signatureElement).getVariable();
			if (variable != null)
			{
				arguments.add(new PerlSubArgument(
						variable.getActualType(),
						variable.getName(),
						"",
						false
				));
			}
			return true;
		}
		return false;
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PerlVisitor) ((PerlVisitor) visitor).visitSubDefinitionBase(this);
		else super.accept(visitor);
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place)
	{
		return PerlScopeUtil.processChildren(
				this,
				processor,
				state,
				lastParent,
				place
		);
	}
}
