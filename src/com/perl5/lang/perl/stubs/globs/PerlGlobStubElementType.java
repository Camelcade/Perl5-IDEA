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

package com.perl5.lang.perl.stubs.globs;

import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.PerlPerlGlob;
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.impl.PerlPerlGlobImpl;
import com.perl5.lang.perl.psi.impl.PerlSubDefinitionImpl;
import com.perl5.lang.perl.stubs.subs.definitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.stubs.subs.definitions.PerlSubDefinitionStubImpl;
import com.perl5.lang.perl.stubs.subs.definitions.PerlSubDefinitionsStubIndex;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 25.05.2015.
 */
public class PerlGlobStubElementType extends IStubElementType<PerlGlobStub,PerlPerlGlob>
{

	public PerlGlobStubElementType(String name)
	{
		super(name, PerlLanguage.INSTANCE);
	}

	@Override
	public PerlPerlGlob createPsi(@NotNull PerlGlobStub stub)
	{
		return new PerlPerlGlobImpl(stub,this);
	}

	@Override
	public PerlGlobStub createStub(@NotNull PerlPerlGlob psi, StubElement parentStub)
	{
		return new PerlGlobStubImpl(parentStub, psi.getPackageName(), psi.getGlobName());
	}

	@NotNull
	@Override
	public String getExternalId()
	{
		return "perl."+super.toString();
	}

	@Override
	public void indexStub(@NotNull PerlGlobStub stub, @NotNull IndexSink sink)
	{
		String name = stub.getPackageName() + "::" + stub.getGlobName();
		sink.occurrence(PerlGlobsStubIndex.KEY, name);
	}

	@Override
	public void serialize(@NotNull PerlGlobStub stub, @NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(stub.getPackageName());
		dataStream.writeName(stub.getGlobName());
	}

	@NotNull
	@Override
	public PerlGlobStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		return new PerlGlobStubImpl(parentStub,dataStream.readName().getString(),dataStream.readName().getString());
	}
}
