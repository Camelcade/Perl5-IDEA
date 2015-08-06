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
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.idea.stubs.strings.PerlStringStub;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 04.08.2015.
 */
public class PerlStringImplMixin extends StubBasedPsiElementBase<PerlStringStub> implements PerlString
{
	public PerlStringImplMixin(PerlStringStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	public PerlStringImplMixin(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return getStringContentElement();
	}

	@Override
	public PerlStringContentElement getStringContentElement()
	{
		return findChildByClass(PerlStringContentElement.class);
	}

	@Override
	public String getName()
	{
		// fixme check stub

		PerlStringContentElement perlStringContentElement = getStringContentElement();
		if (perlStringContentElement != null)
			return perlStringContentElement.getName();
		return super.getName();
	}

	@Override
	public PsiElement setName(String name) throws IncorrectOperationException
	{
		PerlStringContentElement stringContentElement = getStringContentElement();
		if (stringContentElement != null)
			return stringContentElement.setName(name);

		return null;
	}


	@Override
	public String getContextPackageName()
	{
		return PerlPackageUtil.getContextPackageName(this);
	}

	@Override
	public String getExplicitPackageName()
	{
		return null;
	}

	@Override
	public String getPackageName()
	{
		// fixme check stub
		String namespace = getExplicitPackageName();

		if (namespace == null)
			namespace = getContextPackageName();

		return namespace;
	}

	@Override
	public String getCanonicalName()
	{
		return getPackageName() + "::" + getName();
	}
}
