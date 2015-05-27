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

package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PerlLexicalScope;
import com.perl5.lang.perl.psi.PerlNamespace;
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.PerlUserFunction;
import com.perl5.lang.perl.stubs.subs.definitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 25.05.2015.
 */
public abstract class PerlSubDefinitionImplMixin extends StubBasedPsiElementBase<PerlSubDefinitionStub> implements PerlSubDefinition
{
	public PerlSubDefinitionImplMixin(@NotNull ASTNode node){
		super(node);
	}

	public PerlSubDefinitionImplMixin(@NotNull PerlSubDefinitionStub stub, @NotNull IStubElementType nodeType) {
		super(stub,nodeType);
	}

	public String getPackageName()
	{
		PerlSubDefinitionStub stub = getStub();
		if( stub != null)
			return stub.getPackageName();

		String namespace = getExplicitPackageName();

		if( namespace == null )
			namespace = getContextPackageName();

		return namespace;
	}

	public String getFunctionName()
	{
		PerlSubDefinitionStub stub = getStub();
		if( stub != null)
			return stub.getFunctionName();

		PerlUserFunction function = getUserFunction();
		return function.getName();
	}

	@Override
	public String getContextPackageName()
	{
		return PerlPackageUtil.getElementPackageName(this);
	}

	@Override
	public String getExplicitPackageName()
	{
		PerlNamespace namespace = getNamespace();
		return namespace != null ? namespace.getName(): null;
	}

	@Override
	public PerlLexicalScope getLexicalScope()
	{
		PerlLexicalScope scope = PsiTreeUtil.getParentOfType(this, PerlLexicalScope.class);
		assert scope != null;
		return scope;
	}
}
