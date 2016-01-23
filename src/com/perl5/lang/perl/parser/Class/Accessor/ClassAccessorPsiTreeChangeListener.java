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

package com.perl5.lang.perl.parser.Class.Accessor;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTreeChangeAdapter;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.Class.Accessor.psi.PerlClassAccessorFollowBestPractice;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 23.01.2016.
 */
public class ClassAccessorPsiTreeChangeListener extends PsiTreeChangeAdapter
{
	@Override
	public void childAdded(@NotNull PsiTreeChangeEvent event)
	{
		if (canHandle(event)) // myChild contains new element we should check psi element or if it's comment seek for FBP words
		{
			PsiElement element = event.getChild();

			if (element instanceof PsiComment && element.getText().contains("follow_best_practice") || isOrContainFBP(element))
				reScanAccessors();
		}
	}

	@Override
	public void childRemoved(@NotNull PsiTreeChangeEvent event)
	{
		if (canHandle(event)) // myChild contains with FBP
		{
			PsiElement element = event.getChild();

			if (isOrContainFBP(element))
				reScanAccessors();

		}
	}

	@Override
	public void childReplaced(@NotNull PsiTreeChangeEvent event)
	{
		if (canHandle(event)) // has old and new child for replacement
		{
			if (isOrContainFBP(event.getNewChild()) || isOrContainFBP(event.getOldChild()))
				reScanAccessors();
		}
	}

	protected boolean isOrContainFBP(PsiElement element)
	{
		if (element == null)
			return false;

		PsiElement currentElement = element;
		while (currentElement != null)
		{
			if (element instanceof PerlClassAccessorFollowBestPractice)
				return true;

			if (!(element instanceof LeafPsiElement) && isOrContainFBP(element.getFirstChild()))
				return true;

			currentElement = currentElement.getNextSibling();
		}

		return false;
	}


	protected void reScanAccessors()
	{
		System.err.println("Re-scan accessors");
	}

	protected boolean canHandle(@NotNull PsiTreeChangeEvent event)
	{
		return event.getChild() != null && event.getChild().getNode().getElementType().getLanguage().isKindOf(PerlLanguage.INSTANCE);
	}
}
