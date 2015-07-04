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

package com.perl5.lang.perl.idea.stubs.namespaces;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.impl.PsiPerlNamespaceDefinitionImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 28.05.2015.
 */
public class PerlNamespaceDefinitionStubElementType extends IStubElementType<PerlNamespaceDefinitionStub,PsiPerlNamespaceDefinition>
{
	public PerlNamespaceDefinitionStubElementType(String name)
	{
		super(name, PerlLanguage.INSTANCE);
	}

	@Override
	public PsiPerlNamespaceDefinition createPsi(@NotNull PerlNamespaceDefinitionStub stub)
	{
		return new PsiPerlNamespaceDefinitionImpl(stub,this);
	}

	@Override
	public PerlNamespaceDefinitionStub createStub(@NotNull PsiPerlNamespaceDefinition psi, StubElement parentStub)
	{
		return new PerlNamespaceDefinitionStubImpl(parentStub, psi.getNamespaceElement().getCanonicalName());
	}

	@NotNull
	@Override
	public String getExternalId()
	{
		return "perl."+super.toString();
	}

	@Override
	public void indexStub(@NotNull PerlNamespaceDefinitionStub stub, @NotNull IndexSink sink)
	{
		String name = stub.getPackageName();
		assert name != null;
		sink.occurrence(PerlNamespaceDefinitionStubIndex.KEY, name);
	}

	@Override
	public void serialize(@NotNull PerlNamespaceDefinitionStub stub, @NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(stub.getPackageName());
	}

	@NotNull
	@Override
	public PerlNamespaceDefinitionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		return new PerlNamespaceDefinitionStubImpl(parentStub,dataStream.readName().getString());
	}

	@Override
	public boolean shouldCreateStub(ASTNode node)
	{
		return node.findChildByType(PerlElementTypes.PACKAGE) != null;
	}
}
