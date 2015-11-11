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
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.PsiPerlSubDefinition;
import com.perl5.lang.perl.psi.PsiPerlSubSignatureElementIgnore;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 25.05.2015.
 */
public abstract class PerlSubDefinitionImplMixin extends PerlSubDefinitionBaseImpl<PerlSubDefinitionStub> implements PsiPerlSubDefinition
{
	public PerlSubDefinitionImplMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlSubDefinitionImplMixin(@NotNull PerlSubDefinitionStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Nullable
	@Override
	public List<PerlSubArgument> getPerlSubArgumentsFromSignature()
	{
		List<PerlSubArgument> arguments = null;

		if (this.getSubSignatureContent() != null)
		{
			arguments = new ArrayList<PerlSubArgument>();
			//noinspection unchecked

			PsiElement signatureElement = this.getSubSignatureContent().getFirstChild();

			while (signatureElement != null)
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
				}
				else if (signatureElement instanceof PsiPerlSubSignatureElementIgnore)
				{
					arguments.add(new PerlSubArgument(
							PerlVariableType.SCALAR,
							"",
							"",
							signatureElement.getFirstChild() != signatureElement.getLastChild()    // has elements inside, means optional
					));
				}
				else if (signatureElement.getNode().getElementType() == PerlElementTypes.OPERATOR_ASSIGN)
				{
					// setting last element as optional
					arguments.get(arguments.size() - 1).setOptional(true);
				}

				signatureElement = signatureElement.getNextSibling();
			}
		}

		return arguments;
	}


}
