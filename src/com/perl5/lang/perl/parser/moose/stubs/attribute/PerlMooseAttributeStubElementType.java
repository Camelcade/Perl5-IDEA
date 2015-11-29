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

package com.perl5.lang.perl.parser.moose.stubs.attribute;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStubElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseAttribute;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseAttributeImpl;
import com.perl5.lang.perl.psi.PerlSubDefinitionBase;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 29.11.2015.
 */
public class PerlMooseAttributeStubElementType extends PerlSubDefinitionStubElementType implements PerlElementTypes
{
	public PerlMooseAttributeStubElementType(String name)
	{
		super(name);
	}

	@Override
	public PerlSubDefinitionBase createPsi(@NotNull PerlSubDefinitionStub stub)
	{
		return new PerlMooseAttributeImpl((PerlMooseAttributeStub) stub, this);
	}

	@NotNull
	@Override
	public PerlSubDefinitionStub createStubElement(
			StubElement parentStub,
			String packageName,
			String functionName,
			List<PerlSubArgument> arguments,
			PerlSubAnnotations annotations,
			boolean isMethod
	)
	{
		return new PerlMooseAttributeStubImpl(parentStub, packageName, functionName, arguments, annotations, this);
	}

	@Override
	public boolean shouldCreateStub(ASTNode node)
	{
		PsiElement psi = node.getPsi();
		return psi instanceof PerlMooseAttribute && ((PerlMooseAttribute) psi).getSubNameElement() != null;
	}

}
