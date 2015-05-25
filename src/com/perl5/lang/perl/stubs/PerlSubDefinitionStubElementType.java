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

package com.perl5.lang.perl.stubs;

import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.impl.PerlSubDefinitionImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 25.05.2015.
 */
public class PerlSubDefinitionStubElementType extends IStubElementType<PerlSubDefinitionStub,PerlSubDefinition>
{

	public PerlSubDefinitionStubElementType(String name)
	{
		super(name, PerlLanguage.INSTANCE);
	}

	@Override
	public PerlSubDefinition createPsi(@NotNull PerlSubDefinitionStub stub)
	{
		return new PerlSubDefinitionImpl(stub,this);
	}

	@Override
	public PerlSubDefinitionStub createStub(@NotNull PerlSubDefinition psi, StubElement parentStub)
	{
		return new PerlSubDefinitionStubImpl(parentStub, psi.getPackageName(), psi.getUserFunction().getName());
	}

	@NotNull
	@Override
	public String getExternalId()
	{
		return "perl."+super.toString();
	}

	@Override
	public void indexStub(@NotNull PerlSubDefinitionStub stub, @NotNull IndexSink sink)
	{
		String name = stub.getPackageName() + "::" + stub.getFunctionName();
		sink.occurrence(PerlSubDefinitionStubIndex.SUB_DEFINITION, name);
	}

	@Override
	public void serialize(@NotNull PerlSubDefinitionStub stub, @NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(stub.getPackageName());
		dataStream.writeName(stub.getFunctionName());
	}

	@NotNull
	@Override
	public PerlSubDefinitionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		return new PerlSubDefinitionStubImpl(parentStub,dataStream.readName().getString(),dataStream.readName().getString());
	}
}
