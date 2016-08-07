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

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlSubDeclaration;
import com.perl5.lang.perl.psi.impl.PsiPerlSubDeclarationImpl;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Created by hurricup on 05.06.2015.
 */
public class PerlSubDeclarationStubElementType extends IStubElementType<PerlSubDeclarationStub, PerlSubDeclaration> implements PsiElementProvider
{
	public PerlSubDeclarationStubElementType(String name)
	{
		this(name, PerlLanguage.INSTANCE);
	}

	public PerlSubDeclarationStubElementType(@NotNull @NonNls String debugName, @Nullable Language language)
	{
		super(debugName, language);
	}

	@Override
	public PerlSubDeclaration createPsi(@NotNull PerlSubDeclarationStub stub)
	{
		return new PsiPerlSubDeclarationImpl(stub, this);
	}

	@NotNull
	@Override
	public PsiElement getPsiElement(@NotNull ASTNode node)
	{
		return new PsiPerlSubDeclarationImpl(node);
	}

	@Override
	public PerlSubDeclarationStub createStub(@NotNull PerlSubDeclaration psi, StubElement parentStub)
	{
		return new PerlSubDeclarationStubImpl(parentStub, psi.getPackageName(), psi.getSubName(), psi.getLocalSubAnnotations(), this);
	}

	@NotNull
	@Override
	public String getExternalId()
	{
		return "perl." + super.toString();
	}

	@Override
	public void serialize(@NotNull PerlSubDeclarationStub stub, @NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(stub.getPackageName());
		dataStream.writeName(stub.getSubName());
		PerlSubAnnotations subAnnotations = stub.getSubAnnotations();
		if (subAnnotations == null)
		{
			dataStream.writeBoolean(false);
		}
		else
		{
			dataStream.writeBoolean(true);
			subAnnotations.serialize(dataStream);
		}
	}

	@NotNull
	@Override
	public PerlSubDeclarationStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		String packageName = dataStream.readName().toString();
		String subName = dataStream.readName().toString();
		PerlSubAnnotations annotations = null;
		if (dataStream.readBoolean())
		{
			annotations = PerlSubAnnotations.deserialize(dataStream);
		}
		return new PerlSubDeclarationStubImpl(parentStub, packageName, subName, annotations, this);
	}

	@Override
	public void indexStub(@NotNull PerlSubDeclarationStub stub, @NotNull IndexSink sink)
	{
		sink.occurrence(PerlSubDeclarationStubIndex.KEY, stub.getCanonicalName());
		sink.occurrence(PerlSubDeclarationStubIndex.KEY, "*" + stub.getPackageName());
	}

	@Override
	public boolean shouldCreateStub(ASTNode node)
	{
		PsiElement psi = node.getPsi();
		return psi instanceof PerlSubDeclaration &&
				StringUtil.isNotEmpty(((PerlSubDeclaration) psi).getPackageName()) &&
				StringUtil.isNotEmpty(((PerlSubDeclaration) psi).getName())
				;
	}
}
