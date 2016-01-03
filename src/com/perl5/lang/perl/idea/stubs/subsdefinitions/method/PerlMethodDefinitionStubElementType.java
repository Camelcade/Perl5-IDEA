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

package com.perl5.lang.perl.idea.stubs.subsdefinitions.method;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStubElementType;
import com.perl5.lang.perl.psi.PerlMethodDefinition;
import com.perl5.lang.perl.psi.PerlSubDefinitionBase;
import com.perl5.lang.perl.psi.impl.PsiPerlMethodDefinitionImpl;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 10.11.2015.
 */
public class PerlMethodDefinitionStubElementType extends PerlSubDefinitionStubElementType
{
	public PerlMethodDefinitionStubElementType(String name)
	{
		super(name);
	}

	@Override
	public PerlSubDefinitionBase<PerlMethodDefinitionStub> createPsi(@NotNull PerlSubDefinitionStub stub)
	{
		return new PsiPerlMethodDefinitionImpl((PerlMethodDefinitionStub) stub, this);
	}

	@NotNull
	protected PerlSubDefinitionStub createStubElement(StubElement parentStub, String packageName, String functionName, List<PerlSubArgument> arguments, PerlSubAnnotations annotations, boolean isMethod)
	{
		return new PerlMethodDefinitionStubImpl(parentStub, packageName, functionName, arguments, annotations, this);
	}

	@Override
	public boolean shouldCreateStub(ASTNode node)
	{
		PsiElement element = node.getPsi();
		return element instanceof PerlMethodDefinition
				&& element.isValid()
				&& StringUtil.isNotEmpty(((PerlMethodDefinition) element).getSubName());
	}
}
