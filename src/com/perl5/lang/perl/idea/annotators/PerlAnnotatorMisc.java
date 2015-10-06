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

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import com.perl5.lang.perl.util.PerlGlobUtil;
import org.jetbrains.annotations.NotNull;

public class PerlAnnotatorMisc extends PerlAnnotator
{


	private void annotateStringContent(PerlStringContentElementImpl element, AnnotationHolder holder)
	{
		PsiElement parent = element.getParent();
		PsiElement grandParent = parent.getParent();

		if (parent instanceof PerlHeredocElementImpl)
		{
			if (!InjectedLanguageUtil.hasInjections((PerlHeredocElementImpl) parent))
			{
				Annotation annotation = holder.createInfoAnnotation((PsiElement) element, null);
				IElementType tokenType = parent.getNode().getElementType();

				if (tokenType == HEREDOC_QQ)
					annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_DQ_STRING);
				else if (tokenType == HEREDOC_QX) // executable
					annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_DX_STRING);
				else
					annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_SQ_STRING);

			}
		}
		else if (!(parent instanceof PsiPerlConstantName
				|| grandParent instanceof PsiPerlConstantName
		))
		{
			Annotation annotation = holder.createInfoAnnotation((PsiElement) element, null);

			if (parent instanceof PsiPerlStringDq) // interpolated
				annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_DQ_STRING);
			else if (parent instanceof PsiPerlStringXq) // executable
				annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_DX_STRING);
			else
				annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_SQ_STRING);
		}
	}


	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder)
	{

		if (element instanceof PsiPerlNyiStatement)
			decorateElement(
					holder.createInfoAnnotation(element, "Unimplemented statement"),
					CodeInsightColors.TODO_DEFAULT_ATTRIBUTES,
					true,
					false);
		else if (element instanceof PsiPerlAnnotation)
			decorateElement(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_ANNOTATION,
					false,
					false);
		else if (element instanceof PerlStringContentElementImpl)
			annotateStringContent((PerlStringContentElementImpl) element, holder);
//		else if( element instanceof PsiPerlTermExpr)
//			holder.createErrorAnnotation(element, "Term expression wrapper");
		else
		{
			IElementType tokenType = element.getNode().getElementType();
			if (tokenType == HANDLE)
				decorateElement(
						holder.createInfoAnnotation(element, null),
						PerlSyntaxHighlighter.PERL_GLOB,
						PerlGlobUtil.BUILT_IN.contains(element.getText()),
						false);
			else if (tokenType == SUB_ATTRIBUTE || tokenType == VAR_ATTRIBUTE)
				decorateElement(
						holder.createInfoAnnotation(element, null),
						PerlSyntaxHighlighter.PERL_SUB_ATTRIBUTE,
						false,
						false);
			else if (tokenType == SUB_PROTOTYPE_TOKEN)
				decorateElement(
						holder.createInfoAnnotation(element, null),
						PerlSyntaxHighlighter.PERL_SUB_PROTOTYPE_TOKEN,
						false,
						false);
			else if (tokenType == NUMBER_VERSION)
				decorateElement(
						holder.createInfoAnnotation(element, null),
						PerlSyntaxHighlighter.PERL_VERSION,
						false,
						false);
			else if (tokenType == REGEX_TOKEN)
				decorateElement(
						holder.createInfoAnnotation(element, null),
						PerlSyntaxHighlighter.PERL_REGEX_TOKEN,
						false,
						false);
		}
	}
}