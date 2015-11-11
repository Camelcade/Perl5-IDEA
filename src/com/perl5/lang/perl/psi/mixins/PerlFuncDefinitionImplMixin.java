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
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.func.PerlFuncDefinitionStub;
import com.perl5.lang.perl.psi.PerlFuncDefinition;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 10.11.2015.
 */
public abstract class PerlFuncDefinitionImplMixin extends PerlSubDefinitionBaseImpl<PerlFuncDefinitionStub> implements PerlFuncDefinition
{
	public PerlFuncDefinitionImplMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlFuncDefinitionImplMixin(@NotNull PerlFuncDefinitionStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public boolean isMethod()
	{
		return false;
	}

	@Nullable
	@Override
	public List<PerlSubArgument> getPerlSubArgumentsFromSignature()
	{
		return null;
	}
}
