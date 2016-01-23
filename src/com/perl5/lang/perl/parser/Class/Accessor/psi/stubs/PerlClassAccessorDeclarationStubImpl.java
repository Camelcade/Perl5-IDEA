/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.Class.Accessor.psi.stubs;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStubImpl;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.util.PerlPackageUtil;

import java.util.List;

/**
 * Created by hurricup on 22.01.2016.
 */
public class PerlClassAccessorDeclarationStubImpl extends PerlSubDefinitionStubImpl implements PerlClassAccessorDeclarationStub
{
	private final boolean myFollowingBestPractice;
	private final boolean myIsAcessorReadable;
	private final boolean myIsAcessorWritable;

	public PerlClassAccessorDeclarationStubImpl(StubElement parent,
												String packageName,
												String subName,
												List<PerlSubArgument> arguments,
												PerlSubAnnotations annotations,
												boolean followingBestPractice,
												boolean isAcessorReadable,
												boolean isAcessorWritable,
												IStubElementType elementType)
	{
		super(parent, packageName, subName, arguments, annotations, elementType);
		myFollowingBestPractice = followingBestPractice;
		myIsAcessorReadable = isAcessorReadable;
		myIsAcessorWritable = isAcessorWritable;
	}

	@Override
	public boolean isFollowsBestPractice()
	{
		return myFollowingBestPractice;
	}

	@Override
	public String getGetterCanonicalName()
	{
		return getPackageName() + PerlPackageUtil.PACKAGE_SEPARATOR + "get_" + getSubName();
	}

	@Override
	public String getSetterCanonicalName()
	{
		return getPackageName() + PerlPackageUtil.PACKAGE_SEPARATOR + "set_" + getSubName();
	}

	@Override
	public boolean isAccessorReadable()
	{
		return myIsAcessorReadable;
	}

	@Override
	public boolean isAccessorWritable()
	{
		return myIsAcessorWritable;
	}
}
