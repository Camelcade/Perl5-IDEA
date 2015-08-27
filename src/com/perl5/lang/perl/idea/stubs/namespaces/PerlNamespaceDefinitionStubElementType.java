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
import com.perl5.lang.perl.idea.stubs.PerlStubSerializationUtil;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.impl.PsiPerlNamespaceDefinitionImpl;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 28.05.2015.
 */
public class PerlNamespaceDefinitionStubElementType extends IStubElementType<PerlNamespaceDefinitionStub, PsiPerlNamespaceDefinition>
{
	public static final HashSet<String> EXCLUSIONS = new HashSet<String>(Arrays.asList(
			"main", "CORE"
	));

	public PerlNamespaceDefinitionStubElementType(String name)
	{
		super(name, PerlLanguage.INSTANCE);
	}

	@Override
	public PsiPerlNamespaceDefinition createPsi(@NotNull PerlNamespaceDefinitionStub stub)
	{
		return new PsiPerlNamespaceDefinitionImpl(stub, this);
	}

	@Override
	public PerlNamespaceDefinitionStub createStub(@NotNull PsiPerlNamespaceDefinition psi, StubElement parentStub)
	{
		return new PerlNamespaceDefinitionStubImpl(
				parentStub,
				psi.getPackageName(),
				psi.getMroType(),
				psi.getParentNamespaces(),
				psi.isDeprecated(),
				psi.getEXPORT(),
				psi.getEXPORT_OK(),
				psi.getEXPORT_TAGS()
		);
	}

	@NotNull
	@Override
	public String getExternalId()
	{
		return "perl." + super.toString();
	}

	@Override
	public void indexStub(@NotNull PerlNamespaceDefinitionStub stub, @NotNull IndexSink sink)
	{
		String name = stub.getPackageName();
		assert name != null;
		sink.occurrence(PerlNamespaceDefinitionStubIndex.KEY, name);

		for (String parent : stub.getParentNamespaces())
			if (parent != null && !parent.isEmpty())
				sink.occurrence(PerlNamespaceDefinitionStubIndex.KEY, "*" + parent);
	}

	@Override
	public void serialize(@NotNull PerlNamespaceDefinitionStub stub, @NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(stub.getPackageName());
		dataStream.writeName(stub.getMroType().toString());
		PerlStubSerializationUtil.writeStringsList(dataStream, stub.getParentNamespaces());
		PerlStubSerializationUtil.writeStringsList(dataStream, stub.getEXPORT());
		PerlStubSerializationUtil.writeStringsList(dataStream, stub.getEXPORT_OK());
		PerlStubSerializationUtil.writeStringListMap(dataStream, stub.getEXPORT_TAGS());
		dataStream.writeBoolean(stub.isDeprecated());
	}

	@NotNull
	@Override
	public PerlNamespaceDefinitionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		String packageName = dataStream.readName().toString();
		PerlMroType mroType = PerlMroType.valueOf(dataStream.readName().toString());
		List<String> parentNamespaces = PerlStubSerializationUtil.readStringsList(dataStream);
		List<String> EXPORT = PerlStubSerializationUtil.readStringsList(dataStream);
		List<String> EXPORT_OK = PerlStubSerializationUtil.readStringsList(dataStream);
		Map<String, List<String>> EXPORT_TAGS = PerlStubSerializationUtil.readStringListMap(dataStream);
		boolean isDeprecated = dataStream.readBoolean();

		return new PerlNamespaceDefinitionStubImpl(
				parentStub,
				packageName,
				mroType,
				parentNamespaces,
				isDeprecated,
				EXPORT,
				EXPORT_OK,
				EXPORT_TAGS
		);
	}

	@Override
	public boolean shouldCreateStub(ASTNode node)
	{
		ASTNode packageNode = node.findChildByType(PerlElementTypes.PACKAGE);
		return packageNode != null && !EXCLUSIONS.contains(packageNode.getText());
	}

}
