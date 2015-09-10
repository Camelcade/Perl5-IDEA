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

package com.perl5.lang.perl.idea.stubs.variables.types;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.stubs.variables.PerlVariableStub;
import com.perl5.lang.perl.idea.stubs.variables.PerlVariableStubImpl;
import com.perl5.lang.perl.idea.stubs.variables.PerlVariableStubIndexKeys;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.utils.PerlASTUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 30.05.2015.
 */
public abstract class PerlVariableStubElementType extends IStubElementType<PerlVariableStub, PerlVariable> implements PerlVariableStubIndexKeys, PerlElementTypes
{
	public PerlVariableStubElementType(@NotNull String debugName)
	{
		super(debugName, PerlLanguage.INSTANCE);
	}

	protected abstract StubIndexKey getStubIndexKey();

	@Override
	public PerlVariableStub createStub(@NotNull PerlVariable psi, StubElement parentStub)
	{
		assert psi.getVariableNameElement() != null;
		return new PerlVariableStubImpl(parentStub, this, psi.getPackageName(), psi.getVariableNameElement().getName(), psi.getDeclaredType());
	}


	@Override
	public boolean shouldCreateStub(ASTNode node)
	{
		if (node.getElementType() == ARRAY_INDEX_VARIABLE)
			return false;

		if (PerlASTUtil.getParentNodeOfType(node, VARIABLE_DECLARATION_GLOBAL) != null)    // our declaration
			return true;
		else if (PerlASTUtil.getParentNodeOfType(node, USE_VARS_STATEMENT) != null    // use vars declaration
				&& PerlASTUtil.getParentNodeOfType(node, PARSABLE_STRING_QQ) != null
				)
			return true;

		return false;
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
			dataStream.writeName("");
		else
			dataStream.writeName(stub.getDeclaredType());
		dataStream.writeName(stub.getPackageName());
		dataStream.writeName(stub.getVariableName());
	}

	@NotNull
	@Override
	public PerlVariableStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		String variableType = dataStream.readName().toString();
		if (variableType.isEmpty())
			variableType = null;

		return new PerlVariableStubImpl(parentStub, this, dataStream.readName().toString(), dataStream.readName().toString(), variableType);
	}

	@Override
	public void indexStub(@NotNull PerlVariableStub stub, @NotNull IndexSink sink)
	{
		String variableName = stub.getPackageName() + "::" + stub.getVariableName();
		sink.occurrence(getStubIndexKey(), variableName);
		sink.occurrence(getStubIndexKey(), "*" + stub.getPackageName());
	}
}
