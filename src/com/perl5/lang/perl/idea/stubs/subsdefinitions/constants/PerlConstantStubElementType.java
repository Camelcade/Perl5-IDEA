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

package com.perl5.lang.perl.idea.stubs.subsdefinitions.constants;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlConstant;
import com.perl5.lang.perl.psi.impl.PsiPerlConstantNameImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 03.08.2015.
 */
public class PerlConstantStubElementType extends IStubElementType<PerlConstantStub, PerlConstant> implements PerlElementTypes
{

	public PerlConstantStubElementType(String name)
	{
		super(name, PerlLanguage.INSTANCE);
	}

	@Override
	public PerlConstantStub createStub(@NotNull PerlConstant psi, StubElement parentStub)
	{
		return new PerlConstantStubImpl(parentStub, this, psi.getPackageName(), psi.getName());
	}

	@Override
	public PerlConstant createPsi(@NotNull PerlConstantStub stub)
	{
		return new PsiPerlConstantNameImpl(stub, this);
	}

	@NotNull
	@Override
	public String getExternalId()
	{
		return "perl." + super.toString();
	}

	@Override
	public void serialize(@NotNull PerlConstantStub stub, @NotNull StubOutputStream dataStream) throws IOException
	{
		assert stub.getPackageName() != null;
		dataStream.writeName(stub.getPackageName());
		assert stub.getName() != null;
		dataStream.writeName(stub.getName());
	}

	@NotNull
	@Override
	public PerlConstantStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		return new PerlConstantStubImpl(parentStub, this, dataStream.readName().toString(), dataStream.readName().toString());
	}

	@Override
	public void indexStub(@NotNull PerlConstantStub stub, @NotNull IndexSink sink)
	{
		String name = stub.getPackageName() + "::" + stub.getName();
		sink.occurrence(PerlConstantsStubIndex.KEY, name);
		sink.occurrence(PerlConstantsStubIndex.KEY, "*" + stub.getPackageName());
	}

	@Override
	public boolean shouldCreateStub(ASTNode node)
	{
		PsiElement psi = node.getPsi();
		return psi instanceof PerlConstant &&
				psi.isValid() &&
				StringUtil.isNotEmpty(((PerlConstant) psi).getName());
	}
}
