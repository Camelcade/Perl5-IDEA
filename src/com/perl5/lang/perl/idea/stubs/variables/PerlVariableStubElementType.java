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

package com.perl5.lang.perl.idea.stubs.variables;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.impl.PsiPerlVariableDeclarationWrapperImpl;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 30.05.2015.
 */
public class PerlVariableStubElementType extends IStubElementType<PerlVariableStub, PerlVariableDeclarationWrapper> implements PerlElementTypes, PsiElementProvider
{
	public PerlVariableStubElementType(@NotNull String debugName)
	{
		super(debugName, PerlLanguage.INSTANCE);
	}

	@Override
	public PerlVariableStub createStub(@NotNull PerlVariableDeclarationWrapper psi, StubElement parentStub)
	{
		return new PerlVariableStubImpl(parentStub, this, psi.getPackageName(), psi.getName(), psi.getDeclaredType(), psi.getActualType(), psi.isDeprecated());
	}

	@Override
	public PerlVariableDeclarationWrapper createPsi(@NotNull PerlVariableStub stub)
	{
		return new PsiPerlVariableDeclarationWrapperImpl(stub, this);
	}

	@NotNull
	@Override
	public PsiElement getPsiElement(@NotNull ASTNode node)
	{
		return new PsiPerlVariableDeclarationWrapperImpl(node);
	}

	@Override
	public boolean shouldCreateStub(ASTNode node)
	{
		PsiElement psi = node.getPsi();
		return psi instanceof PerlVariableDeclarationWrapper &&
				psi.isValid() &&
				((PerlVariableDeclarationWrapper) psi).isGlobalDeclaration() &&
				StringUtil.isNotEmpty(((PerlVariableDeclarationWrapper) psi).getName());
	}

	@NotNull
	@Override
	public String getExternalId()
	{
		return "perl." + super.toString();
	}

	@Override
	public void serialize(@NotNull PerlVariableStub stub, @NotNull StubOutputStream dataStream) throws IOException
	{
		if (stub.getDeclaredType() == null)
		{
			dataStream.writeName("");
		}
		else
		{
			dataStream.writeName(stub.getDeclaredType());
		}
		dataStream.writeName(stub.getPackageName());
		dataStream.writeName(stub.getVariableName());
		dataStream.writeByte(stub.getActualType().ordinal());
		dataStream.writeBoolean(stub.isDeprecated());
	}

	@NotNull
	@Override
	public PerlVariableStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		String variableType = dataStream.readName().toString();
		if (variableType.isEmpty())
		{
			variableType = null;
		}

		return new PerlVariableStubImpl(
				parentStub,
				this,
				dataStream.readName().toString(),
				dataStream.readName().toString(),
				variableType,
				PerlVariableType.values()[dataStream.readByte()],
				dataStream.readBoolean()
		);
	}

	@Override
	public void indexStub(@NotNull PerlVariableStub stub, @NotNull IndexSink sink)
	{
		String variableName = stub.getPackageName() + PerlPackageUtil.PACKAGE_SEPARATOR + stub.getVariableName();
		sink.occurrence(stub.getIndexKey(), variableName);
		sink.occurrence(stub.getIndexKey(), "*" + stub.getPackageName());
	}
}
