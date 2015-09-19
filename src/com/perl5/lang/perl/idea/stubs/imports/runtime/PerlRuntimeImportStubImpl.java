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

package com.perl5.lang.perl.idea.stubs.imports.runtime;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.PerlDoExpr;

/**
 * Created by hurricup on 19.09.2015.
 */
public class PerlRuntimeImportStubImpl extends StubBase<PerlDoExpr> implements PerlRuntimeImportStub
{
	private final String myRelativePath;

	public PerlRuntimeImportStubImpl(StubElement parent, IStubElementType elementType, String myRelativePath)
	{
		super(parent, elementType);
		this.myRelativePath = myRelativePath;
	}

	@Override
	public String getImportPath()
	{
		return myRelativePath;
	}
}
