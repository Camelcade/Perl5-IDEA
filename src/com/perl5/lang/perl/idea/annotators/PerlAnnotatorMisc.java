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
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.psi.PerlAnnotation;
import com.perl5.lang.perl.psi.PerlLabel;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PsiPerlNyiStatement;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlStringListImpl;
import com.perl5.lang.perl.util.PerlGlobUtil;
import org.jetbrains.annotations.NotNull;

public class PerlAnnotatorMisc extends PerlAnnotator
{
	private void annotateStringContainerIfNotInjected(PsiElement element, AnnotationHolder holder)
	{
		if (element instanceof PsiLanguageInjectionHost && InjectedLanguageUtil.hasInjections((PsiLanguageInjectionHost) element))
		{
			return;
		}

		TextAttributesKey key = null;
		IElementType tokenType = element.getNode().getElementType();

		if (tokenType == HEREDOC_QX || tokenType == STRING_XQ)
		{
			key = PerlSyntaxHighlighter.PERL_DX_STRING;
		}
		else if (tokenType == HEREDOC_QQ || tokenType == STRING_DQ)
		{
			key = PerlSyntaxHighlighter.PERL_DQ_STRING;
		}
		else if (tokenType == HEREDOC || tokenType == STRING_LIST || tokenType == STRING_BARE || tokenType == STRING_SQ)
		{
			holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SQ_STRING);
			return;
		}

		if (key == null)
		{
			System.err.println("Don known how to highlight: " + tokenType);
			return;
		}

		PsiElement run = element.getFirstChild();
		while (run != null)
		{
			if (run instanceof PerlStringContentElementImpl)
			{
				holder.createInfoAnnotation(run, null).setTextAttributes(key);
			}
			run = run.getNextSibling();
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
		else if (element instanceof PerlAnnotation)
		{
			decorateElement(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_ANNOTATION,
					false,
					false);

			PsiElement lastChild = element.getLastChild();
			if (lastChild instanceof PerlStringContentElementImpl)
			{
				decorateElement(
						holder.createInfoAnnotation(lastChild, null),
						PerlSyntaxHighlighter.PERL_SQ_STRING,
						false,
						false
				);
			}
		}
		else if (element instanceof PerlLabel)
		{
			decorateElement(
					holder.createInfoAnnotation(element, null),
					PerlSyntaxHighlighter.PERL_LABEL,
					false,
					false
			);
		}
		else if (element instanceof PerlString || element instanceof PerlHeredocElementImpl || element instanceof PsiPerlStringListImpl)
		{
			annotateStringContainerIfNotInjected(element, holder);
		}
		else
		{
			IElementType tokenType = element.getNode().getElementType();
			if (tokenType == HANDLE)
				decorateElement(
						holder.createInfoAnnotation(element, null),
						PerlSyntaxHighlighter.PERL_GLOB,
						PerlGlobUtil.BUILT_IN.contains(element.getText()),
						false);
			else if (tokenType == ATTRIBUTE)
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
			else if (tokenType == LEFT_ANGLE || tokenType == RIGHT_ANGLE)
				decorateElement(
						holder.createInfoAnnotation(element, null),
						PerlSyntaxHighlighter.PERL_ANGLE,
						false,
						false);
			else if (PerlLexer.RESERVED_TOKENSET.contains(tokenType))
				decorateElement(
						holder.createInfoAnnotation(element, null),
						PerlSyntaxHighlighter.PERL_KEYWORD,
						true,
						false);
			else if (tokenType == SUB_SIGNATURE_ELEMENT_IGNORE)
				decorateElement(
						holder.createInfoAnnotation(element.getFirstChild(), null),
						PerlSyntaxHighlighter.PERL_SCALAR,
						true,
						false);
		}
	}
}