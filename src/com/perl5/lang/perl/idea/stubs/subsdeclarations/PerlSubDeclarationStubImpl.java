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

package com.perl5.lang.perl.idea.stubs.subsdeclarations;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.PsiPerlSubDeclaration;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.util.PerlPackageUtil;

/**
 * Created by hurricup on 05.06.2015.
 */
public class PerlSubDeclarationStubImpl extends StubBase<PsiPerlSubDeclaration> implements PerlSubDeclarationStub
{
	private final String packageName;
	private final String subName;
	private final PerlSubAnnotations myAnnotations;

	public PerlSubDeclarationStubImpl(final StubElement parent, final String packageName, final String subName, PerlSubAnnotations annotations, IStubElementType elementType)
	{
		super(parent, elementType);
		this.packageName = packageName;
		this.subName = subName;
		myAnnotations = annotations;
	}

	@Override
	public String getPackageName()
	{
		return packageName;
	}

	@Override
	public String getSubName()
	{
		return subName;
	}

	@Override
	public PerlSubAnnotations getSubAnnotations()
	{
		return myAnnotations;
	}

	@Override
	public String getCanonicalName()
	{
		return getPackageName() + PerlPackageUtil.PACKAGE_SEPARATOR + getSubName();
	}
}
