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

package com.perl5.lang.perl.parser.Class.Accessor.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.parser.Class.Accessor.psi.PerlClassAccessorDeclaration;
import com.perl5.lang.perl.parser.Class.Accessor.psi.PerlClassAccessorFollowBestPractice;
import com.perl5.lang.perl.parser.Class.Accessor.psi.stubs.PerlClassAccessorDeclarationStub;
import com.perl5.lang.perl.psi.PerlNamespaceContainer;
import com.perl5.lang.perl.psi.PsiPerlNestedCall;
import com.perl5.lang.perl.psi.impl.PerlSubDefinitionWithTextIdentifierImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 21.01.2016.
 */
public class PerlClassAccessorDeclarationImpl extends PerlSubDefinitionWithTextIdentifierImpl implements PerlClassAccessorDeclaration
{
	private static final TokenSet READABLE_DECLARATORS = TokenSet.create(
			RESERVED_MK_ACCESSORS,
			RESERVED_MK_RO_ACCESSORS
	);

	private static final TokenSet WRITABLE_DECLARATORS = TokenSet.create(
			RESERVED_MK_ACCESSORS,
			RESERVED_MK_WO_ACCESSORS
	);

	public PerlClassAccessorDeclarationImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlClassAccessorDeclarationImpl(@NotNull PerlSubDefinitionStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public boolean followsBestPractice()
	{
		PerlClassAccessorDeclarationStub stub = (PerlClassAccessorDeclarationStub) getStub();
		if (stub != null)
			return stub.followsBestPractice();

		// fixme here should be some walking up visitor
		PerlNamespaceContainer namespaceContainer = PsiTreeUtil.getParentOfType(this, PerlNamespaceContainer.class);

		if (namespaceContainer != null)
		{
			for (PerlClassAccessorFollowBestPractice followBestPractice : PsiTreeUtil.findChildrenOfType(namespaceContainer, PerlClassAccessorFollowBestPractice.class))
			{
				if (followBestPractice.getTextOffset() > getTextOffset())
					break;
				if (namespaceContainer.equals(PsiTreeUtil.getParentOfType(followBestPractice, PerlNamespaceContainer.class)))
				{
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean isAccessorReadable()
	{
		PerlClassAccessorDeclarationStub stub = (PerlClassAccessorDeclarationStub) getStub();
		if (stub != null)
			return stub.isAccessorReadable();
		return READABLE_DECLARATORS.contains(getDeclaratorElementType());
	}

	@Override
	public boolean isAccessorWritable()
	{
		PerlClassAccessorDeclarationStub stub = (PerlClassAccessorDeclarationStub) getStub();
		if (stub != null)
			return stub.isAccessorWritable();
		return WRITABLE_DECLARATORS.contains(getDeclaratorElementType());
	}

	@Nullable
	protected IElementType getDeclaratorElementType()
	{
		PsiPerlNestedCall nestedCall = PsiTreeUtil.getParentOfType(this, PsiPerlNestedCall.class);
		if (nestedCall != null)
		{
			return nestedCall.getMethod().getFirstChild().getNode().getElementType();
		}
		return null;
	}
}
