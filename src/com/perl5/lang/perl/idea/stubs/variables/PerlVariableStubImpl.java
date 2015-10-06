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

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubIndexKey;
import com.perl5.lang.perl.psi.PerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.utils.PerlVariableType;

/**
 * Created by hurricup on 30.05.2015.
 */
public class PerlVariableStubImpl extends StubBase<PerlVariableDeclarationWrapper> implements PerlVariableStub
{
	private final String myPackageName;
	private final String myVariableName;
	private final String myDeclaredType;
	private final PerlVariableType myVariableType;

	public PerlVariableStubImpl(StubElement parent, IStubElementType elementType, String myPackageName, String myVariableName, String myDeclaredType, PerlVariableType variableType)
	{
		super(parent, elementType);
		this.myPackageName = myPackageName;
		this.myVariableName = myVariableName;
		this.myDeclaredType = myDeclaredType;
		this.myVariableType = variableType;
	}

	@Override
	public String getPackageName()
	{
		return myPackageName;
	}

	@Override
	public String getVariableName()
	{
		return myVariableName;
	}

	@Override
	public String getDeclaredType()
	{
		return myDeclaredType;
	}

	@Override
	public PerlVariableType getActualType()
	{
		return myVariableType;
	}

	@Override
	public StubIndexKey<String, PerlVariableDeclarationWrapper> getIndexKey()
	{
		if (myVariableType == PerlVariableType.ARRAY)
		{
			return PerlVariablesStubIndex.KEY_ARRAY;
		}
		else if (myVariableType == PerlVariableType.SCALAR)
		{
			return PerlVariablesStubIndex.KEY_SCALAR;
		}
		else if (myVariableType == PerlVariableType.HASH)
		{
			return PerlVariablesStubIndex.KEY_HASH;
		}
		throw new RuntimeException("Don't have key for " + myVariableType);
	}
}
