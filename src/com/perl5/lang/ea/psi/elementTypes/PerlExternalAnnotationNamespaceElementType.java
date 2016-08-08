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
import com.intellij.util.io.StringRef;
import com.perl5.lang.ea.psi.PerlExternalAnnotationNamespace;
import com.perl5.lang.ea.psi.impl.PerlExternalAnnotationNamespaceImpl;
import com.perl5.lang.ea.psi.stubs.PerlExternalAnnotationNamespaceStub;
import com.perl5.lang.ea.psi.stubs.PerlExternalAnnotationNamespaceStubImpl;
import com.perl5.lang.ea.psi.stubs.PerlExternalAnnotationNamespaceStubIndex;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Created by hurricup on 07.08.2016.
 */
public class PerlExternalAnnotationNamespaceElementType extends IStubElementType<PerlExternalAnnotationNamespaceStub, PerlExternalAnnotationNamespace> implements PsiElementProvider
{
	public PerlExternalAnnotationNamespaceElementType(@NotNull @NonNls String debugName)
	{
		super(debugName, PerlLanguage.INSTANCE);
	}

	@Override
	public PerlExternalAnnotationNamespace createPsi(@NotNull PerlExternalAnnotationNamespaceStub stub)
	{
		return new PerlExternalAnnotationNamespaceImpl(stub, this);
	}

	@Override
	public PerlExternalAnnotationNamespaceStub createStub(@NotNull PerlExternalAnnotationNamespace psi, StubElement parentStub)
	{
		return new PerlExternalAnnotationNamespaceStubImpl(
				parentStub,
				this,
				psi.getPackageName(),
				psi.getPackageVersion(),
				psi.getAnnotations()
		);
	}

	@NotNull
	@Override
	public String getExternalId()
	{
		return "perl.ea.namespace";
	}

	@Override
	public void serialize(@NotNull PerlExternalAnnotationNamespaceStub stub, @NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(stub.getPackageName());
		dataStream.writeName(stub.getPackageVersion());
		PerlNamespaceAnnotations annotations = stub.getAnnotations();
		if (annotations == null)
		{
			dataStream.writeBoolean(false);
		}
		else
		{
			dataStream.writeBoolean(true);
			annotations.serialize(dataStream);
		}
	}

	@NotNull
	@Override
	public PerlExternalAnnotationNamespaceStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		StringRef packageNameRef = dataStream.readName();
		StringRef versionRef = dataStream.readName();
		return new PerlExternalAnnotationNamespaceStubImpl(
				parentStub,
				this,
				packageNameRef.getString(),
				versionRef == null ? null : versionRef.getString(),
				deserializeAnnotations(dataStream)
		);
	}

	@Nullable
	private PerlNamespaceAnnotations deserializeAnnotations(@NotNull StubInputStream dataStream) throws IOException
	{
		return dataStream.readBoolean() ? PerlNamespaceAnnotations.deserialize(dataStream) : null;
	}

	@Override
	public void indexStub(@NotNull PerlExternalAnnotationNamespaceStub stub, @NotNull IndexSink sink)
	{
		//noinspection ConstantConditions
		sink.occurrence(PerlExternalAnnotationNamespaceStubIndex.KEY, stub.getPackageName());
	}

	@NotNull
	@Override
	public PsiElement getPsiElement(@NotNull ASTNode node)
	{
		return new PerlExternalAnnotationNamespaceImpl(node);
	}

	@Override
	public boolean shouldCreateStub(ASTNode node)
	{
		PsiElement psi = node.getPsi();

		return psi instanceof PerlExternalAnnotationNamespace && StringUtil.isNotEmpty(((PerlExternalAnnotationNamespace) psi).getPackageName());
	}
}

