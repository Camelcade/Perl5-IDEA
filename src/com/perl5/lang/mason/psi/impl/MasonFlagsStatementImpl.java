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

package com.perl5.lang.mason.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mason.MasonUtils;
import com.perl5.lang.mason.psi.MasonFlagsStatement;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PsiPerlCommaSequenceExpr;
import com.perl5.lang.perl.psi.impl.PsiPerlStatementImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 06.01.2016.
 */
public class MasonFlagsStatementImpl extends PsiPerlStatementImpl implements MasonFlagsStatement
{
	public MasonFlagsStatementImpl(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public String getParentNamespace()
	{
		String extendsFlagsValue = getExtendsFlagValue();
		PsiFile psiFile = getContainingFile();

		if (psiFile instanceof MasonFileImpl && StringUtil.isNotEmpty(extendsFlagsValue))
		{
			if (extendsFlagsValue.startsWith("" + VfsUtil.VFS_SEPARATOR_CHAR)) // abs path, see the Mason::Interp::_determine_parent_compc
			{
				return MasonUtils.getClassnameFromPath(extendsFlagsValue.substring(1));
			}
			else // relative path
			{
				VirtualFile containingFile = ((MasonFileImpl) psiFile).getRealContainingFile();
				VirtualFile currentComponentRoot = ((MasonFileImpl) psiFile).getComponentRoot();

				if (containingFile != null && currentComponentRoot != null)
				{
					String fullPath = containingFile.getParent().getPath() + VfsUtil.VFS_SEPARATOR_CHAR + extendsFlagsValue;
					String parentComponentPath = fullPath.substring(currentComponentRoot.getPath().length() + 1);
					return MasonUtils.getClassnameFromPath(parentComponentPath);
				}
			}
		}

		return null;
	}

	@Nullable
	private String getExtendsFlagValue()
	{
		PsiElement expr = getExpr();
		// fixme this can be a prototype for scanning a hash to map
		if (expr instanceof PsiPerlCommaSequenceExpr)
		{
			PsiElement currentElement = expr.getFirstChild();
			while (currentElement != null)
			{
				PsiElement keyElement = currentElement;

				// comma between key and val
				if (isNullOrNotComma(currentElement = PerlPsiUtil.getNextSignificantSibling(currentElement)))
				{
					break;
				}

				PsiElement valElement = PerlPsiUtil.getNextSignificantSibling(currentElement);

				if (keyElement instanceof PerlString && valElement instanceof PerlString && ((PerlString) keyElement).getStringContent().equals("extends"))
				{
					return ((PerlString) valElement).getStringContent();
				}

				// comma between pairs
				if (isNullOrNotComma(currentElement = PerlPsiUtil.getNextSignificantSibling(currentElement)))
				{
					break;
				}

				currentElement = PerlPsiUtil.getNextSignificantSibling(currentElement);
			}

		}

		return null;
	}

	private boolean isNullOrNotComma(@Nullable PsiElement element)
	{
		if (element == null)
			return false;

		IElementType elementType = element.getNode().getElementType();
		return elementType != OPERATOR_COMMA && elementType != OPERATOR_COMMA_ARROW;
	}
}
