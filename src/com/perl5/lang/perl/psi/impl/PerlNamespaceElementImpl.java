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

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.references.PerlNamespaceFileReference;
import com.perl5.lang.perl.psi.references.PerlNamespaceReference;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 25.05.2015.
 *
 */
public class PerlNamespaceElementImpl extends LeafPsiElement implements PerlNamespaceElement
{
	public PerlNamespaceElementImpl(@NotNull IElementType type, CharSequence text) {
		super(type, text);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor) {
		if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitNamespaceElement(this);
		else super.accept(visitor);
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		String currentName = getText();

		boolean currentTail = currentName.endsWith("::");
		boolean newTail = name.endsWith("::");

		if (newTail && !currentTail)
			name = name.replaceAll(":+$", "");
		else if (!newTail && currentTail)
			name = name + "::";

		PerlNamespaceElementImpl newName = PerlElementFactory.createPackageName(getProject(), name);

		if( newName != null )
			replace(newName);

		return this;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return this;
	}

	@NotNull
	@Override
	public String getName()
	{
		return PerlPackageUtil.getCanonicalPackageName(this.getText());
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		return ReferenceProvidersRegistry.getReferencesFromProviders(this);
	}

	@Override
	public boolean isBuiltin()
	{
		return PerlPackageUtil.isBuiltIn(getName());
	}

	@Override
	public boolean isPragma()
	{
		return PerlPackageUtil.BUILT_IN_PRAGMA.contains(getName());
	}

	// todo implement deprecation support for user packages and take it into account
	@Override
	public boolean isDeprecated()
	{
		return PerlPackageUtil.BUILT_IN_DEPRECATED.contains(getName());
	}

	@Override
	public List<PerlNamespaceDefinition> getNamespaceDefinitions()
	{
		List<PerlNamespaceDefinition> namespaceDefinitions = new ArrayList<>();

		PsiReference[] references = getReferences();

		for (PsiReference reference : references)
		{
			if( reference instanceof PerlNamespaceReference)
			{
				ResolveResult[] results = ((PerlNamespaceReference) reference).multiResolve(false);

				for (ResolveResult result : results)
				{
					PsiElement targetElement = result.getElement();
					assert targetElement != null;
					assert targetElement instanceof PerlNamespaceDefinition;

					namespaceDefinitions.add((PerlNamespaceDefinition)targetElement);
				}
			}
		}
		return namespaceDefinitions;
	}

	@Override
	public List<PerlFileElement> getNamespaceFiles()
	{
		List<PerlFileElement> namespaceFiles = new ArrayList<>();

		PsiReference[] references = getReferences();

		for (PsiReference reference : references)
		{
			if( reference instanceof PerlNamespaceFileReference)
			{
				ResolveResult[] results = ((PerlNamespaceFileReference) reference).multiResolve(false);

				for (ResolveResult result : results)
				{
					PsiElement targetElement = result.getElement();
					assert targetElement != null;
					assert targetElement instanceof PerlFileElement;

					namespaceFiles.add((PerlFileElement) targetElement);
				}
			}
		}
		return namespaceFiles;
	}

	@Override
	public TextRange getTextRange()
	{
		if( getText().endsWith("::"))
		{
			int start = getStartOffset();
			return new TextRange(start, start + getTextLength()-2);
		}
		return super.getTextRange();
	}


}
