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

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlVariableNameElementContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 10.08.2015.
 */
public class PerlAnnotatorVariables extends PerlAnnotator
{
	public void decorateCastElement(@NotNull final PsiElement element, @NotNull AnnotationHolder holder, TextAttributesKey key)
	{
		// annotating sigil
		PsiElement sigilElement = element.getFirstChild();
		Annotation annotation = holder.createInfoAnnotation(sigilElement, null);
		annotation.setTextAttributes(key);

		// annotating braces
		PsiElement nextElement = sigilElement.getNextSibling();

		while (nextElement != null &&
				(nextElement instanceof PsiWhiteSpace || nextElement instanceof PsiComment))
			nextElement = nextElement.getNextSibling();

		if (nextElement != null && nextElement.getNode().getElementType() == LEFT_BRACE)
		{
			annotation = holder.createInfoAnnotation(nextElement, null);
			annotation.setTextAttributes(key);

			annotation = holder.createInfoAnnotation(element.getLastChild(), null);
			annotation.setTextAttributes(key);
		}
	}

	@Override
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder)
	{
		if (element instanceof PsiPerlScalarVariable || element instanceof PsiPerlArrayIndexVariable)
			decorateElement(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_SCALAR,
					((PerlVariable) element).isBuiltIn(),
					((PerlVariable) element).isDeprecated()
			);
		else if (element instanceof PsiPerlScalarCastExpr || element instanceof PsiPerlScalarIndexCastExpr)
			decorateCastElement(element, holder, PerlSyntaxHighlighter.PERL_SCALAR);
		else if (element instanceof PsiPerlHashVariable)
			decorateElement(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_HASH,
					((PerlVariable) element).isBuiltIn(),
					((PerlVariable) element).isDeprecated()
			);
		else if (element instanceof PsiPerlHashCastExpr)
			decorateCastElement(element, holder, PerlSyntaxHighlighter.PERL_HASH);
		else if (element instanceof PsiPerlArrayVariable)
			decorateElement(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_ARRAY,
					((PerlVariable) element).isBuiltIn(),
					((PerlVariable) element).isDeprecated()
			);
		else if (element instanceof PsiPerlArrayCastExpr)
			decorateCastElement(element, holder, PerlSyntaxHighlighter.PERL_ARRAY);
		else if (element instanceof PsiPerlGlobVariable)
			decorateElement(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_GLOB,
					((PerlVariableNameElementContainer) element).isBuiltIn(),
					false);
		else if (element instanceof PsiPerlConstantName)
			decorateElement(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_CONSTANT,
					false,
					false);
		else if (element instanceof PsiPerlGlobCastExpr)
			decorateCastElement(element, holder, PerlSyntaxHighlighter.PERL_GLOB);
		else if (element instanceof PsiPerlCodeVariable)
			decorateElement(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_SUB,
					((PerlVariableNameElementContainer) element).isBuiltIn(),
					false);
		else if (element instanceof PsiPerlCodeCastExpr)
			decorateCastElement(element, holder, PerlSyntaxHighlighter.PERL_SUB);

	}
}
