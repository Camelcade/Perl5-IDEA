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

package com.perl5.lang.tt2.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtil;
import com.perl5.lang.tt2.psi.TemplateToolkitString;
import com.perl5.lang.tt2.psi.impl.TemplateToolkitCompositeElementImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by hurricup on 15.06.2016.
 */
public class TemplateToolkitStringMixin extends TemplateToolkitCompositeElementImpl implements TemplateToolkitString
{
	protected static final TokenSet FILES_CONTAINERS = TokenSet.create(
			INSERT_DIRECTIVE,
			INCLUDE_DIRECTIVE,
			PROCESS_DIRECTIVE,
			WRAPPER_DIRECTIVE
	);
	protected static final PsiReference[] EMPTY = new PsiReference[0];

	protected AtomicNotNullLazyValue<PsiReference[]> myReferences;

	public TemplateToolkitStringMixin(@NotNull ASTNode node)
	{
		super(node);
		createReferences();
	}

	protected void createReferences()
	{
		myReferences = new AtomicNotNullLazyValue<PsiReference[]>()
		{
			@NotNull
			@Override
			protected PsiReference[] compute()
			{
				if (FILES_CONTAINERS.contains(PsiUtil.getElementType(getParent())))
				{
					return new FileReferenceSet(TemplateToolkitStringMixin.this)
					{
						@NotNull
						@Override
						public Collection<PsiFileSystemItem> computeDefaultContexts()
						{
							String path = getPathString();
							PsiFile containingFile = getContainingFile();
							if (StringUtil.startsWith(path, ".") && containingFile != null && containingFile.getParent() != null)
							{
								return Collections.<PsiFileSystemItem>singletonList(containingFile.getParent());
							}
							return super.computeDefaultContexts();
						}
					}.getAllReferences();
				}

				return EMPTY;
			}
		};
	}

	@Override
	public PsiReference getReference()
	{
		PsiReference[] references = getReferences();
		return references.length == 0 ? null : references[0];
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		return myReferences.getValue();
	}

	@Override
	public void subtreeChanged()
	{
		super.subtreeChanged();
		createReferences();
	}
}
