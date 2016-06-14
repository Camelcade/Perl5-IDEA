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

package com.perl5.lang.perl.psi.impl;

import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.references.PerlNamespaceFileReference;
import com.perl5.lang.perl.psi.references.PerlNamespaceReference;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 25.05.2015.
 */
public class PerlNamespaceElementImpl extends LeafPsiElement implements PerlNamespaceElement
{
	protected AtomicNotNullLazyValue<PsiReference[]> myReferences;

	public PerlNamespaceElementImpl(@NotNull IElementType type, CharSequence text)
	{
		super(type, text);
		createMyReferences();
	}

	private void createMyReferences()
	{
		myReferences = new AtomicNotNullLazyValue<PsiReference[]>()
		{
			@NotNull
			@Override
			protected PsiReference[] compute()
			{
				PerlNamespaceElement element = PerlNamespaceElementImpl.this;
				PsiElement nameSpaceContainer = element.getParent();

				if (nameSpaceContainer instanceof PsiPerlUseStatement
						|| nameSpaceContainer instanceof PsiPerlRequireExpr
						)
				{
					return new PsiReference[]{new PerlNamespaceFileReference(element, null)};
				}
				else if (nameSpaceContainer instanceof PerlNamespaceDefinition)
				{
					return PsiReference.EMPTY_ARRAY;
				}
				else
				{
					return new PsiReference[]{new PerlNamespaceReference(element, null)};
				}
			}
		};
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PerlVisitor)
		{
			((PerlVisitor) visitor).visitNamespaceElement(this);
		}
		else
		{
			super.accept(visitor);
		}
	}

	@NotNull
	@Override
	public String getName()
	{
		return this.getText();
	}

	public String getCanonicalName()
	{
		return PerlPackageUtil.getCanonicalPackageName(getName());
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		return myReferences.getValue();
	}

	@Override
	public PsiReference getReference()
	{
		return myReferences.getValue().length > 0 ? myReferences.getValue()[0] : null;
	}

	@Override
	public boolean isBuiltin()
	{
		return PerlPackageUtil.isBuiltIn(getCanonicalName());
	}

	@Override
	public boolean isPragma()
	{
		return PerlPackageUtil.BUILT_IN_PRAGMA.contains(getCanonicalName());
	}

	@Override
	public boolean isSUPER()
	{
		return PerlPackageUtil.isSUPER(getCanonicalName());
	}

	@Override
	public boolean isMain()
	{
		return PerlPackageUtil.isMain(getCanonicalName());
	}

	@Override
	public boolean isCORE()
	{
		return PerlPackageUtil.isCORE(getCanonicalName());
	}

	@Override
	public boolean isUNIVERSAL()
	{
		return PerlPackageUtil.isUNIVERSAL(getCanonicalName());
	}

	@Override
	public boolean isDeprecated()
	{
		return PerlPackageUtil.isDeprecated(getProject(), getCanonicalName());
	}

	@Override
	public List<PerlNamespaceDefinition> getNamespaceDefinitions()
	{
		List<PerlNamespaceDefinition> namespaceDefinitions = new ArrayList<PerlNamespaceDefinition>();

		PsiReference[] references = getReferences();

		for (PsiReference reference : references)
		{
			if (reference instanceof PerlNamespaceReference)
			{
				ResolveResult[] results = ((PerlNamespaceReference) reference).multiResolve(false);

				for (ResolveResult result : results)
				{
					PsiElement targetElement = result.getElement();
					assert targetElement != null;
					assert targetElement instanceof PerlNamespaceDefinition;

					namespaceDefinitions.add((PerlNamespaceDefinition) targetElement);
				}
			}
		}
		return namespaceDefinitions;
	}

	@Override
	public List<PerlFileImpl> getNamespaceFiles()
	{
		List<PerlFileImpl> namespaceFiles = new ArrayList<PerlFileImpl>();

		PsiReference[] references = getReferences();

		for (PsiReference reference : references)
		{
			if (reference instanceof PerlNamespaceFileReference)
			{
				ResolveResult[] results = ((PerlNamespaceFileReference) reference).multiResolve(false);

				for (ResolveResult result : results)
				{
					PsiElement targetElement = result.getElement();
					assert targetElement != null;

					if (targetElement instanceof PerlFileImpl)
					{
						namespaceFiles.add((PerlFileImpl) targetElement);
					}
				}
			}
		}
		return namespaceFiles;
	}

	@Override
	public TextRange getTextRange()
	{
		return PerlPackageUtil.getPackageRangeFromOffset(getStartOffset(), getText());
	}

	@Override
	public void clearCaches()
	{
		super.clearCaches();
		createMyReferences();
	}
}
