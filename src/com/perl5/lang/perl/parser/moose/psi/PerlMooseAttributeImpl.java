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

package com.perl5.lang.perl.parser.moose.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.parser.PerlPsiLists;
import com.perl5.lang.perl.parser.moose.stubs.attribute.PerlMooseAttributeStub;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PsiPerlAnnotation;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.mixins.PerlSubDefinitionBaseImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 29.11.2015.
 */
public class PerlMooseAttributeImpl extends PerlSubDefinitionBaseImpl<PerlMooseAttributeStub> implements PerlMooseAttribute
{
	public PerlMooseAttributeImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlMooseAttributeImpl(@NotNull PerlMooseAttributeStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public PsiPerlBlock getBlock()
	{
		return null;
	}

	@Nullable
	@Override
	public PsiElement getSignatureContainer()
	{
		return null;
	}

	@Nullable
	protected PerlMooseHasStatement getHasStatement()
	{
		//noinspection unchecked
		return PsiTreeUtil.getParentOfType(this, PerlMooseHasStatement.class, true, PsiPerlStatement.class);
	}

	@NotNull
	@Override
	public List<PsiPerlAnnotation> getAnnotationList()
	{
		PerlMooseHasStatement hasStatement = getHasStatement();
		if (hasStatement != null)
		{
			return hasStatement.getAnnotationList();
		}

		return PerlPsiLists.EMPTY_ANNOTATIONS_LIST;
	}

	@Override
	public boolean isMethod()
	{
		return true;
	}

	@Nullable
	@Override
	public PerlString getNameIdentifier()
	{
		return PsiTreeUtil.getChildOfType(this, PerlString.class);
	}
}
