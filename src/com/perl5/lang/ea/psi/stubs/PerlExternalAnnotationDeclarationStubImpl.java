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

package com.perl5.lang.ea.psi.stubs;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.ea.psi.PerlExternalAnnotationDeclaration;
import com.perl5.lang.ea.psi.PerlExternalAnnotationsPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 07.08.2016.
 */
public class PerlExternalAnnotationDeclarationStubImpl extends StubBase<PerlExternalAnnotationDeclaration> implements PerlExternalAnnotationDeclarationStub
{
	private final String myPackageName;
	private final String mySubName;
	private final PerlSubAnnotations myPerlSubAnnotations;

	public PerlExternalAnnotationDeclarationStubImpl(
			StubElement parent,
			IStubElementType elementType,
			@Nullable String packageName,
			@Nullable String subName,
			@Nullable PerlSubAnnotations subAnnotations
	)
	{
		super(parent, elementType);
		myPackageName = packageName;
		mySubName = subName;
		myPerlSubAnnotations = subAnnotations;
	}

	@Nullable
	@Override
	public PerlSubAnnotations getSubAnnotations()
	{
		return myPerlSubAnnotations;
	}

	@Override
	public String getSubName()
	{
		return mySubName;
	}

	@Nullable
	@Override
	public String getPackageName()
	{
		return myPackageName;
	}

	@Nullable
	@Override
	public String getCanonicalName()
	{
		return PerlExternalAnnotationsPsiUtil.getCanonicalName(this);
	}
}
