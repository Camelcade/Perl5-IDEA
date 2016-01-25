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

package com.perl5.lang.perl.parser.moose.stubs.augment;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseAugmentStatement;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseAugmentStatementImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Created by hurricup on 25.01.2016.
 */
public class PerlMooseAugmentStatementElementType extends IStubElementType<PerlMooseAugmentStatementStub, PerlMooseAugmentStatement> implements PsiElementProvider
{
	public PerlMooseAugmentStatementElementType(@NotNull @NonNls String debugName)
	{
		this(debugName, PerlLanguage.INSTANCE);
	}


	public PerlMooseAugmentStatementElementType(@NotNull @NonNls String debugName, @Nullable Language language)
	{
		super(debugName, language);
	}

	@Override
	public PerlMooseAugmentStatement createPsi(@NotNull PerlMooseAugmentStatementStub stub)
	{
		return new PerlMooseAugmentStatementImpl(stub, this);
	}

	@Override
	public PerlMooseAugmentStatementStub createStub(@NotNull PerlMooseAugmentStatement psi, StubElement parentStub)
	{
		return new PerlMooseAugmentStatementStubImpl(parentStub, this, psi.getSubName());
	}

	@NotNull
	@Override
	public String getExternalId()
	{
		return "perl." + super.toString();
	}

	@Override
	public void serialize(@NotNull PerlMooseAugmentStatementStub stub, @NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(stub.getSubName());
	}

	@NotNull
	@Override
	public PerlMooseAugmentStatementStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		return new PerlMooseAugmentStatementStubImpl(parentStub, this, dataStream.readName().toString());
	}

	@Override
	public void indexStub(@NotNull PerlMooseAugmentStatementStub stub, @NotNull IndexSink sink)
	{
	}

	@NotNull
	@Override
	public PsiElement getPsiElement(@NotNull ASTNode node)
	{
		return new PerlMooseAugmentStatementImpl(node);
	}

	@Override
	public boolean shouldCreateStub(ASTNode node)
	{
		PsiElement element = node.getPsi();
		return element instanceof PerlMooseAugmentStatement && element.isValid() && StringUtil.isNotEmpty(((PerlMooseAugmentStatement) element).getSubName());
	}
}
