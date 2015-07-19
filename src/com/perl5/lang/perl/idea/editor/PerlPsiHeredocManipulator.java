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

package com.perl5.lang.perl.idea.editor;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.resolve.reference.impl.manipulators.PsiCommentManipulator;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 10.06.2015.
 */
public class PerlPsiHeredocManipulator extends PsiCommentManipulator
{
	@Override
	public PsiComment handleContentChange(@NotNull PsiComment psiComment, @NotNull TextRange range, String newContent) throws IncorrectOperationException
	{
		String oldText = psiComment.getText();
		String newText = oldText.substring(0, range.getStartOffset()) + newContent + oldText.substring(range.getEndOffset());

		List<PsiElement> heredocElements = PerlElementFactory.createHereDocElements(psiComment.getProject(), '\'', "TEXT" + Math.random(), newText);
		assert heredocElements.size() == 4;

		return (PsiComment)psiComment.replace(heredocElements.get(1));
	}

}
