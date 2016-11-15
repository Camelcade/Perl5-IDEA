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
import com.intellij.util.Processor;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PsiPerlCallArgumentsImpl;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.util.PerlArrayUtil;
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
		{
			return true;
		}

		List<PerlSubArgument> arguments = getSubArgumentsList();
		return !arguments.isEmpty() && arguments.get(0).isSelf(getProject());

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

		if (isMethod() && !subArguments.isEmpty())
		{
			subArguments.remove(0);
		}

		return PerlSubUtil.getArgumentsListAsString(subArguments);
	}


	@NotNull
	@Override
	public List<PerlSubArgument> getSubArgumentsList()
	{
		Stub stub = getStub();
		if (stub != null)
		{
			return new ArrayList<PerlSubArgument>(stub.getSubArgumentsList());
		}

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
		PerlSubArgumentsExtractor extractor = new PerlSubArgumentsExtractor();
		PsiPerlBlock subBlock = getBlock();

		if (subBlock != null && subBlock.isValid())
		{
			for (PsiElement statement : subBlock.getChildren())
			{
				if (statement instanceof PsiPerlStatement)
				{
					if (!extractor.process((PsiPerlStatement) statement))
					{
						break;
					}
				}
				else if (!(statement instanceof PerlAnnotation))
				{
					break;
				}
			}
		}

		return extractor.getArguments();
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
		if (visitor instanceof PerlVisitor)
		{
			((PerlVisitor) visitor).visitSubDefinitionBase(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	@Override
	public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place)
	{
		return PerlResolveUtil.processChildren(
				this,
				processor,
				state,
				lastParent,
				place
		);
	}

	protected static class PerlSubArgumentsExtractor implements Processor<PsiPerlStatement>
	{
		private List<PerlSubArgument> myArguments = new ArrayList<PerlSubArgument>();

		@Override
		public boolean process(PsiPerlStatement statement)
		{
			if (myArguments.isEmpty() && PerlPsiUtil.isSelfShortcutStatement(statement))
			{
				myArguments.add(PerlSubArgument.getSelfArgument());
				return true;
			}
			else if (EMPTY_SHIFT_STATEMENT_PATTERN.accepts(statement))
			{
				myArguments.add(myArguments.isEmpty() ? PerlSubArgument.getSelfArgument() : PerlSubArgument.getEmptyArgument());
				return true;
			}
			else if (DECLARATION_ASSIGNING_PATTERN.accepts(statement))
			{
				PerlAssignExpression assignExpression = PsiTreeUtil.getChildOfType(statement, PerlAssignExpression.class);

				if (assignExpression == null)
				{
					return false;
				}

				PsiElement leftSide = assignExpression.getLeftSide();
				PsiElement rightSide = assignExpression.getRightSide();

				if (rightSide == null)
				{
					return false;
				}

				PerlVariableDeclaration variableDeclaration = PsiTreeUtil.findChildOfType(leftSide, PerlVariableDeclaration.class, false);

				if (variableDeclaration == null)
				{
					return false;
				}

				String variableClass = variableDeclaration.getDeclarationType();
				if (variableClass == null)
				{
					variableClass = "";
				}

				List<PsiElement> rightSideElements = PerlArrayUtil.getElementsAsPlainList(rightSide, null);
				int sequenceIndex = 0;

				boolean processNextStatement = true;
				PsiElement run = variableDeclaration.getFirstChild();
				while (run != null)
				{
					PerlSubArgument newArgument = null;

					if (run instanceof PerlVariableDeclarationWrapper)
					{
						PerlVariable variable = ((PerlVariableDeclarationWrapper) run).getVariable();
						if (variable != null)
						{
							newArgument = new PerlSubArgument(
									variable.getActualType(),
									variable.getName(),
									variableClass,
									false
							);
						}
						else
						{
							newArgument = PerlSubArgument.getEmptyArgument();
						}
					}
					else if (run.getNode().getElementType() == RESERVED_UNDEF)
					{
						newArgument = myArguments.isEmpty() ? PerlSubArgument.getSelfArgument() : PerlSubArgument.getEmptyArgument();
					}

					if (newArgument != null)
					{
						// we've found argument of left side
						if (rightSideElements.size() > sequenceIndex)
						{
							PsiElement rightSideElement = rightSideElements.get(sequenceIndex);
							boolean addArgument = false;

							if (SHIFT_PATTERN.accepts(rightSideElement)) // shift on the left side
							{
								assert rightSideElement instanceof PsiPerlSubCallExpr;
								PsiPerlCallArguments callArguments = ((PsiPerlSubCallExpr) rightSideElement).getCallArguments();
								List<PsiPerlExpr> argumentsList = callArguments == null ? null : ((PsiPerlCallArgumentsImpl) callArguments).getArgumentsList();
								if (argumentsList == null || argumentsList.isEmpty() || ALL_ARGUMENTS_PATTERN.accepts(argumentsList.get(0)))
								{
									addArgument = true;
									sequenceIndex++;
								}
							}
							else if (ALL_ARGUMENTS_ELEMENT_PATTERN.accepts(rightSideElement)) // $_[smth] on the left side
							{
								addArgument = true;
								sequenceIndex++;
							}
							else if (ALL_ARGUMENTS_PATTERN.accepts(rightSideElement))    // @_ on the left side
							{
								addArgument = true;
								processNextStatement = false;
							}

							if (addArgument)
							{
								myArguments.add(newArgument);
							}

						}
					}

					run = run.getNextSibling();
				}

				return processNextStatement;
			}
			return false;
		}

		public List<PerlSubArgument> getArguments()
		{
			return myArguments;
		}
	}

}
