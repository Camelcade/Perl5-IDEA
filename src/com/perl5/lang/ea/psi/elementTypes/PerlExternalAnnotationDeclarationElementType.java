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

package com.perl5.lang.ea.psi.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.ea.psi.PerlExternalAnnotationDeclaration;
import com.perl5.lang.ea.psi.impl.PerlExternalAnnotationDeclarationImpl;
import com.perl5.lang.ea.psi.stubs.PerlExternalAnnotationDeclarationStub;
import com.perl5.lang.ea.psi.stubs.PerlExternalAnnotationDeclarationStubImpl;
import com.perl5.lang.ea.psi.stubs.PerlExternalAnnotationDeclarationStubIndex;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 06.08.2016.
 */
public class PerlExternalAnnotationDeclarationElementType extends IStubElementType<PerlExternalAnnotationDeclarationStub, PerlExternalAnnotationDeclaration> implements PsiElementProvider
{
	public PerlExternalAnnotationDeclarationElementType(String name)
	{
		super(name, PerlLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public PsiElement getPsiElement(@NotNull ASTNode node)
	{
		return new PerlExternalAnnotationDeclarationImpl(node);
	}

	@Override
	public PerlExternalAnnotationDeclaration createPsi(@NotNull PerlExternalAnnotationDeclarationStub stub)
	{
		return new PerlExternalAnnotationDeclarationImpl(stub, this);
	}

	@Override
	public PerlExternalAnnotationDeclarationStub createStub(@NotNull PerlExternalAnnotationDeclaration psi, StubElement parentStub)
	{
		return new PerlExternalAnnotationDeclarationStubImpl(parentStub, this, psi.getPackageName(), psi.getSubName(), psi.getSubAnnotations());
	}

	@NotNull
	@Override
	public String getExternalId()
	{
		return "perl.ea.declaration";
	}

	@Override
	public void serialize(@NotNull PerlExternalAnnotationDeclarationStub stub, @NotNull StubOutputStream dataStream) throws IOException
	{
		// fixme not dry with PerlSubDeclarationStubElementType
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
	public PerlExternalAnnotationDeclarationStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		String packageName = dataStream.readName().toString();
		String subName = dataStream.readName().toString();
		PerlSubAnnotations annotations = null;
		if (dataStream.readBoolean())
		{
			annotations = PerlSubAnnotations.deserialize(dataStream);
		}
		return new PerlExternalAnnotationDeclarationStubImpl(parentStub, this, packageName, subName, annotations);
	}

	@Override
	public void indexStub(@NotNull PerlExternalAnnotationDeclarationStub stub, @NotNull IndexSink sink)
	{
		//noinspection ConstantConditions
		sink.occurrence(PerlExternalAnnotationDeclarationStubIndex.KEY, stub.getCanonicalName());
	}

	@Override
	public boolean shouldCreateStub(ASTNode node)
	{
		PsiElement psi = node.getPsi();
		return psi instanceof PerlExternalAnnotationDeclaration && StringUtil.isNotEmpty(((PerlExternalAnnotationDeclaration) psi).getCanonicalName());
	}
}
