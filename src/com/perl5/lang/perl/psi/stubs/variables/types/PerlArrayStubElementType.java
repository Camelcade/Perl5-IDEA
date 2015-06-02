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

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubIndexKey;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.impl.PsiPerlArrayVariableImpl;
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableStub;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableStubIndexKeys;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 30.05.2015.
 */
public class PerlArrayStubElementType extends PerlVariableStubElementType
{
	public PerlArrayStubElementType(@NotNull String debugName)
	{
		super(debugName);
	}

	@Override
	public PerlVariable createPsi(@NotNull PerlVariableStub stub)
	{
		return new PsiPerlArrayVariableImpl(stub,this);
	}

	@Override
	protected IStubElementType getStubElementType()
	{
		return PerlStubElementTypes.PERL_ARRAY;
	}

	protected StubIndexKey getStubIndexKey()
	{
		return PerlVariableStubIndexKeys.KEY_ARRAY;
	}}
