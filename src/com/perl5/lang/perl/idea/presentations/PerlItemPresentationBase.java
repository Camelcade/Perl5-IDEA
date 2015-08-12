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

package com.perl5.lang.perl.idea.presentations;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 05.06.2015.
 */
public abstract class PerlItemPresentationBase implements ItemPresentation
{
	PsiElement myElement;

	public PerlItemPresentationBase(PsiElement element)
	{
		myElement = element;
	}

	@Nullable
	@Override
	public String getLocationString()
	{
		PsiFile file = myElement.getContainingFile();
		Document document = PsiDocumentManager.getInstance(myElement.getProject()).getCachedDocument(file);

		String suffix = "";
		if (document != null)
			suffix = ", line " + (document.getLineNumber(myElement.getTextOffset()) + 1);

		return getElement().getContainingFile().getName() + suffix;
	}

	@Nullable
	@Override
	public Icon getIcon(boolean unused)
	{
		return myElement.getIcon(0);
	}

	public PsiElement getElement()
	{
		return myElement;
	}
}
