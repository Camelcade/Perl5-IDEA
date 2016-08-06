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
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IndexSink;
import com.perl5.lang.ea.PerlExternalAnnotationsLanguage;
import com.perl5.lang.ea.psi.impl.PerlExternalAnnotationsPseudoDeclarationImpl;
import com.perl5.lang.ea.psi.stubs.PerlExternalAnnotationsStubIndex;
import com.perl5.lang.perl.idea.stubs.subsdeclarations.PerlSubDeclarationStub;
import com.perl5.lang.perl.idea.stubs.subsdeclarations.PerlSubDeclarationStubElementType;
import com.perl5.lang.perl.psi.PerlSubDeclaration;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 06.08.2016.
 */
public class PerlExternalAnnotationsPseudoDeclarationElementType extends PerlSubDeclarationStubElementType
{
	public PerlExternalAnnotationsPseudoDeclarationElementType(String name)
	{
		super(name, PerlExternalAnnotationsLanguage.INSTANCE);
	}

	@Override
	public PerlSubDeclaration createPsi(@NotNull PerlSubDeclarationStub stub)
	{
		return new PerlExternalAnnotationsPseudoDeclarationImpl(stub, this);
	}

	@NotNull
	@Override
	public PsiElement getPsiElement(@NotNull ASTNode node)
	{
		return new PerlExternalAnnotationsPseudoDeclarationImpl(node);
	}

	@Override
	public void indexStub(@NotNull PerlSubDeclarationStub stub, @NotNull IndexSink sink)
	{
		sink.occurrence(PerlExternalAnnotationsStubIndex.KEY, stub.getCanonicalName());
	}
}
