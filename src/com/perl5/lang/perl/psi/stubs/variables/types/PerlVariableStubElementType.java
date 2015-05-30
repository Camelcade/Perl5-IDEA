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

package com.perl5.lang.perl.psi.stubs.variables.types;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlPerlArrayImpl;
import com.perl5.lang.perl.psi.impl.PerlPerlHashImpl;
import com.perl5.lang.perl.psi.impl.PerlPerlScalarImpl;
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableStub;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableStubImpl;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableStubIndexKeys;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 30.05.2015.
 *
 */
public abstract class PerlVariableStubElementType extends IStubElementType<PerlVariableStub,PerlVariable> implements PerlVariableStubIndexKeys, PerlElementTypes
{
	public PerlVariableStubElementType(@NotNull String debugName)
	{
		super(debugName, PerlLanguage.INSTANCE);
	}

	protected abstract IStubElementType getStubElementType();
	protected abstract StubIndexKey getStubIndexKey();

	@Override
	public PerlVariableStub createStub(@NotNull PerlVariable psi, StubElement parentStub)
	{
		assert psi.getVariableName() != null;
		return new PerlVariableStubImpl(parentStub,getStubElementType(),psi.getPackageName(),psi.getVariableName().getName());
	}


	@Override
	public boolean shouldCreateStub(ASTNode node)
	{
		ASTNode parent = node.getTreeParent();
		if( parent != null )
		{
			if( parent.getElementType() == VARIABLE_DECLARATION_GLOBAL )
				return true;
		}

		return false; //super.shouldCreateStub(node);
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
		dataStream.writeName(stub.getPackageName());
		dataStream.writeName(stub.getVariableName());
	}

	@NotNull
	@Override
	public PerlVariableStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		return new PerlVariableStubImpl(parentStub, getStubElementType(), dataStream.readName().toString(), dataStream.readName().toString());
	}

	@Override
	public void indexStub(@NotNull PerlVariableStub stub, @NotNull IndexSink sink)
	{
		String variableName = stub.getPackageName() + "::" + stub.getVariableName();
		sink.occurrence(getStubIndexKey(), variableName);

	}
}
