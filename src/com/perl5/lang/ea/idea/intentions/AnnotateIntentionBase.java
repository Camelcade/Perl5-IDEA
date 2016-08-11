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

package com.perl5.lang.ea.idea.intentions;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.PsiNavigateUtil;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 11.08.2016.
 */
public abstract class AnnotateIntentionBase extends PsiElementBaseIntentionAction implements IntentionAction
{
	@Override
	public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException
	{
		PsiElement elementToAnnotate = getElementToAnnotate(element);
		if (elementToAnnotate != null)
		{
			PsiNavigateUtil.navigate(elementToAnnotate);
		}
	}

	@Nullable
	protected abstract PsiElement getElementToAnnotate(PsiElement namespaceElement);

	@Nls
	@NotNull
	@Override
	public String getFamilyName()
	{
		return PerlBundle.message("perl.ea.intention.family");
	}

}
