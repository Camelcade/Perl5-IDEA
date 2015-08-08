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

package com.perl5.lang.perl.idea;

/**
 * Created by hurricup on 25.04.2015.
 */

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import com.perl5.lang.perl.psi.properties.PerlVariableNameElementContainer;
import com.perl5.lang.perl.util.PerlGlobUtil;
import org.jetbrains.annotations.NotNull;

public class PerlAnnotatorSyntax implements Annotator, PerlElementTypes
{
	EditorColorsScheme currentScheme = EditorColorsManager.getInstance().getGlobalScheme();

	private void decorateCastElement(@NotNull final PsiElement element, @NotNull AnnotationHolder holder, TextAttributesKey key)
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

	private void decorateElement(Annotation annotation, TextAttributesKey key, boolean builtin, boolean deprecated)
	{
		TextAttributes attrs = currentScheme.getAttributes(key);

		if (builtin)
			attrs = TextAttributes.merge(attrs, PerlSyntaxHighlighter.BOLD);
		if (deprecated)
			attrs = TextAttributes.merge(attrs, PerlSyntaxHighlighter.STROKE);

		annotation.setEnforcedTextAttributes(attrs);
	}

	private void annotateNamespaceElement(PerlNamespaceElement namespaceElement, AnnotationHolder holder)
	{
		PsiElement parent = namespaceElement.getParent();

		if (parent instanceof PerlNamespaceDefinition)
		{
			holder.createInfoAnnotation(namespaceElement, null).setTextAttributes(PerlSyntaxHighlighter.PERL_PACKAGE_DEFINITION);
		} else if (namespaceElement.isPragma())
		{
			Annotation annotation = holder.createInfoAnnotation(namespaceElement, null);
			annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_PACKAGE);
			annotation.setEnforcedTextAttributes(PerlSyntaxHighlighter.BOLD_ITALIC);
		} else if (!(parent instanceof PerlVariable || parent instanceof PerlGlobVariable))
			decorateElement(
					holder.createInfoAnnotation(namespaceElement, null),
					PerlSyntaxHighlighter.PERL_PACKAGE,
					namespaceElement.isBuiltin(),
					namespaceElement.isDeprecated()
			);
	}

	private void annotateSubNameElement(PerlSubNameElement perlSubNameElement, AnnotationHolder holder)
	{
		PsiElement parent = perlSubNameElement.getParent();

		if (parent instanceof PsiPerlSubDeclaration)
			holder.createInfoAnnotation(perlSubNameElement, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SUB_DECLARATION);
		else if (parent instanceof PsiPerlSubDefinition)
			holder.createInfoAnnotation(perlSubNameElement, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SUB_DEFINITION);
		else if (parent instanceof PsiPerlExpr) // fixme temporary solution for pre-defined expressions, suppose only built-ins
			decorateElement(
					holder.createInfoAnnotation(perlSubNameElement, null),
					PerlSyntaxHighlighter.PERL_SUB,
					true,
					false
			);
		else if (parent instanceof PerlMethod)
		{
			decorateElement(
					holder.createInfoAnnotation(perlSubNameElement, null),
					PerlSyntaxHighlighter.PERL_SUB,
					perlSubNameElement.isBuiltIn(),
					false
			);
		}
	}

	private void annotateStringContent(PerlStringContentElementImpl element, AnnotationHolder holder)
	{
		PsiElement parent = element.getParent();

		if (!(parent instanceof PsiPerlConstantName || parent.getParent() instanceof PsiPerlConstantName))
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
		else if (element instanceof PsiPerlNyiStatement)
			decorateElement(
					holder.createInfoAnnotation(element, "Unimplemented statement"),
					CodeInsightColors.TODO_DEFAULT_ATTRIBUTES,
					true,
					false);
		else if (element instanceof PsiPerlCodeCastExpr)
			decorateCastElement(element, holder, PerlSyntaxHighlighter.PERL_SUB);
		else if (element instanceof PsiPerlAnnotation)
			decorateElement(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_ANNOTATION,
					false,
					false);
		else if (element instanceof PerlNamespaceElement)
			annotateNamespaceElement((PerlNamespaceElement) element, holder);
		else if (element instanceof PerlStringContentElementImpl)
			annotateStringContent((PerlStringContentElementImpl) element, holder);
		else if (element instanceof PerlSubNameElement)
			annotateSubNameElement((PerlSubNameElement) element, holder);
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
		}
	}
}