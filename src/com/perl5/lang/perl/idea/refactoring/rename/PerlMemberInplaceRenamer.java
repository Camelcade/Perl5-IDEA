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

package com.perl5.lang.perl.idea.refactoring.rename;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.refactoring.rename.inplace.MemberInplaceRenamer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created by hurricup on 07.04.2016.
 */
public class PerlMemberInplaceRenamer extends MemberInplaceRenamer
{
	public PerlMemberInplaceRenamer(@NotNull PsiNamedElement elementToRename, PsiElement substituted, Editor editor)
	{
		super(elementToRename, substituted, editor);
	}

	public PerlMemberInplaceRenamer(@NotNull PsiNamedElement elementToRename, PsiElement substituted, Editor editor, String initialName, String oldName)
	{
		super(elementToRename, substituted, editor, initialName, oldName);
	}

	@Override
	protected boolean notSameFile(@Nullable VirtualFile file, @NotNull PsiFile containingFile)
	{
		final PsiFile currentFile = PsiDocumentManager.getInstance(myProject).getPsiFile(myEditor.getDocument());
		if (currentFile == null)
		{
			return true;
		}
		InjectedLanguageManager manager = InjectedLanguageManager.getInstance(containingFile.getProject());
		PsiFile topLevelFile = manager.getTopLevelFile(containingFile);
		PsiFile topLevelFile1 = manager.getTopLevelFile(currentFile);
		return topLevelFile == null || topLevelFile1 == null || topLevelFile.getViewProvider() != topLevelFile1.getViewProvider();
	}

	@Override
	protected boolean appendAdditionalElement(Collection<PsiReference> refs, Collection<Pair<PsiElement, TextRange>> stringUsages)
	{
		boolean b = super.appendAdditionalElement(refs, stringUsages);

		for (PsiReference ref : refs)
		{
			if (ref instanceof PsiPolyVariantReference)
			{
				for (ResolveResult resolveResult : ((PsiPolyVariantReference) ref).multiResolve(false))
				{
					appendAdditionalElement(resolveResult.getElement(), stringUsages);
				}
			}
			else
			{
				appendAdditionalElement(ref.resolve(), stringUsages);
			}
		}

		return b;
	}

	private void appendAdditionalElement(@Nullable PsiElement psiElement, Collection<Pair<PsiElement, TextRange>> stringUsages)
	{
		if (psiElement == null || psiElement.equals(myElementToRename) || !(psiElement instanceof PsiNameIdentifierOwner))
		{
			return;
		}

		PsiElement nameIdentifier = ((PsiNameIdentifierOwner) psiElement).getNameIdentifier();
		if (nameIdentifier == null)
		{
			return;
		}

		stringUsages.add(Pair.create(nameIdentifier, ElementManipulators.getValueTextRange(nameIdentifier)));
	}

	@NotNull
	@Override
	protected TextRange getRangeToRename(@NotNull PsiElement element)
	{
		return ElementManipulators.getValueTextRange(element);
	}
}
