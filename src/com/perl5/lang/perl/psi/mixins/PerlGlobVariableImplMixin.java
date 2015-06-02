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

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import com.perl5.lang.perl.psi.stubs.globs.PerlGlobStub;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 25.05.2015.
 *
 */
public abstract class PerlGlobVariableImplMixin extends StubBasedPsiElementBase<PerlGlobStub> implements PsiPerlGlobVariable
{
	public PerlGlobVariableImplMixin(@NotNull ASTNode node){
		super(node);
	}

	public PerlGlobVariableImplMixin(@NotNull PerlGlobStub stub, @NotNull IStubElementType nodeType) {
		super(stub,nodeType);
	}

	@Override
	public String getPackageName()
	{
		PerlGlobStub stub = getStub();
		if( stub != null)
			return stub.getPackageName();

		String namespace = getExplicitPackageName();

		if( namespace == null )
			namespace = getContextPackageName();

		return namespace;
	}

	@Override
	public String getGlobName()
	{
		PerlGlobStub stub = getStub();
		if( stub != null)
			return stub.getGlobName();

		return getVariableNameElement().getName();
	}

	@Override
	public String getContextPackageName()
	{
		return PerlPackageUtil.getContextPackageName(this);
	}

	@Override
	public String getExplicitPackageName()
	{
		PerlNamespaceElement namespaceElement = getNamespaceElement();
		return namespaceElement != null ? namespaceElement.getName(): null;
	}

	@Override
	public PerlNamespaceElement getNamespaceElement()
	{
		return findChildByClass(PerlNamespaceElement.class);
	}

	@Override
	public PerlVariableNameElement getVariableNameElement()
	{
		return findChildByClass(PerlVariableNameElement.class);
	}

	@Override
	public boolean isBuiltIn()
	{
		if( getNamespaceElement() != null )
			return false;

		String globName = getGlobName();
		if( globName == null )
			return false;

		return PerlGlobUtil.BUILT_IN.contains(globName);
	}
}
