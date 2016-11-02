/* Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.idea.stubs.globs.PerlGlobStub;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlStubBasedPsiElementBase;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import com.perl5.lang.perl.psi.PsiPerlAssignExpr;
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 25.05.2015.
 */
public abstract class PerlGlobVariableImplMixin extends PerlStubBasedPsiElementBase<PerlGlobStub> implements PsiPerlGlobVariable, PerlElementTypes
{
	public PerlGlobVariableImplMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	public PerlGlobVariableImplMixin(@NotNull PerlGlobStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@NotNull
	@Override
	public String getPackageName()
	{
		PerlGlobStub stub = getStub();
		if (stub != null)
		{
			return stub.getPackageName();
		}

		String namespace = getExplicitPackageName();

		if (namespace == null)
		{
			namespace = getContextPackageName();
		}

		return namespace;
	}

	@Override
	public String getName()
	{
		PerlGlobStub stub = getStub();
		if (stub != null)
		{
			return stub.getName();
		}

		PerlVariableNameElement variableNameElement = getVariableNameElement();
		if (variableNameElement == null)
		{
			return null;
		}
		String variableNameText = variableNameElement.getText();
		int delimiterIndex = variableNameText.lastIndexOf(':');
		return delimiterIndex == -1 ? variableNameText : variableNameText.substring(delimiterIndex + 1);
	}

	@NotNull
	@Override
	public String getContextPackageName()
	{
		return PerlPackageUtil.getContextPackageName(this);
	}

	@Nullable
	@Override
	public String getExplicitPackageName()
	{
		PerlVariableNameElement variableNameElement = getVariableNameElement();
		if (variableNameElement == null)
		{
			return null;
		}

		String variableNameText = variableNameElement.getText();
		int delimiterIndex = variableNameText.lastIndexOf(':');
		return delimiterIndex == -1 ? null : PerlPackageUtil.getCanonicalPackageName(variableNameText.substring(0, delimiterIndex + 1));
	}

	@Override
	public PerlVariableNameElement getVariableNameElement()
	{
		return findChildByClass(PerlVariableNameElement.class);
	}

	@Override
	public boolean isBuiltIn()
	{
		if (getExplicitPackageName() != null)
		{
			return false;
		}

		String globName = getName();
		return globName != null && PerlGlobUtil.BUILT_IN.contains(globName);
	}

	@Override
	public boolean isDeprecated()
	{
		return false;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return getVariableNameElement();
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		PerlVariableNameElement variableNameElement = getVariableNameElement();
		if (variableNameElement != null)
		{
			PerlPsiUtil.renameElement(variableNameElement, name);
		}

		return this;
	}

	@NotNull
	@Override
	public String getCanonicalName()
	{
		return getPackageName() + PerlPackageUtil.PACKAGE_SEPARATOR + getName();
	}


	@Nullable
	@Override
	public Icon getIcon(int flags)
	{
		return PerlIcons.GLOB_GUTTER_ICON;
	}

	@Override
	public ItemPresentation getPresentation()
	{
		return new PerlItemPresentationSimple(this, getName());
	}

	@Override
	public boolean isLeftSideOfAssignment()
	{
		PerlGlobStub stub = getStub();
		if (stub != null)
		{
			return stub.isLeftSideOfAssignment();
		}
		return getParent() instanceof PsiPerlAssignExpr && getNextSibling() != null;
	}

	@Override
	public String getPresentableName()
	{
		return getName();
	}

	@Override
	public int getTextOffset()
	{
		PsiElement nameIdentifier = getNameIdentifier();

		return nameIdentifier == null
				? super.getTextOffset()
				: nameIdentifier.getTextOffset();
	}

}
