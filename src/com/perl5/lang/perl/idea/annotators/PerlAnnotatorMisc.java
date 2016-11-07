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

package com.perl5.lang.perl.idea.annotators;

/**
 * Created by hurricup on 25.04.2015.
 */

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import org.jetbrains.annotations.NotNull;

public class PerlAnnotatorMisc extends PerlAnnotator
{
	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder)
	{
		IElementType elementType = PsiUtilCore.getElementType(element);
		if (elementType == NYI_STATEMENT)
		{
			holder.createInfoAnnotation(element, "Unimplemented statement").setTextAttributes(CodeInsightColors.TODO_DEFAULT_ATTRIBUTES);
		}
		else if (elementType == LABEL_DECLARATION || elementType == LABEL_EXPR)
		{
			holder.createInfoAnnotation(element.getFirstChild(), null).setTextAttributes(PerlSyntaxHighlighter.PERL_LABEL);
		}
	}
}