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

package com.perl5.lang.htmlmason.idea.editor;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.folding.PerlFoldingBuilder;

import java.util.List;

/**
 * Created by hurricup on 09.03.2016.
 */
public abstract class AbstractMasonFoldingBuilder extends PerlFoldingBuilder
{
	public static void foldElement(PsiElement element, List<FoldingDescriptor> myDescriptors, Document myDocument)
	{
		PsiElement firstChild = element.getFirstChild();
		int leftMargin = firstChild == null || firstChild.getNextSibling() == null ? 0 : firstChild.getNode().getTextLength();

		PsiElement lastChild = element.getLastChild();
		int rightMargin = lastChild == null || lastChild == firstChild ? 0 : lastChild.getNode().getTextLength();

		addDescriptorFor(myDescriptors, myDocument, element, leftMargin, rightMargin, 0);

	}
}
