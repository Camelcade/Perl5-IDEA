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
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.parser.PerlParserUtil;
import com.perl5.lang.perl.psi.PerlAnnotation;
import com.perl5.lang.perl.psi.PerlLabel;
import com.perl5.lang.perl.psi.PsiPerlNyiStatement;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import com.perl5.lang.perl.util.PerlGlobUtil;
import org.jetbrains.annotations.NotNull;

public class PerlAnnotatorMisc extends PerlAnnotator
{
	private static final TokenSet QQ_STRINGS = TokenSet.create(HEREDOC_QQ, STRING_DQ);
	private static final TokenSet XQ_STRINGS = TokenSet.create(HEREDOC_QX, STRING_XQ);
	private static final TokenSet SQ_STRINGS = TokenSet.create(HEREDOC, STRING_SQ, STRING_BARE, STRING_LIST, TR_SEARCHLIST, TR_REPLACEMENTLIST);
	private static final TokenSet STRINGS = TokenSet.orSet(QQ_STRINGS, SQ_STRINGS, XQ_STRINGS);


	private void annotateStringContainerIfNotInjected(PsiElement element, AnnotationHolder holder)
	{
		if (element instanceof PsiLanguageInjectionHost && InjectedLanguageUtil.hasInjections((PsiLanguageInjectionHost) element))
		{
			return;
		}

		TextAttributesKey key = null;
		IElementType tokenType = element.getNode().getElementType();

		if (XQ_STRINGS.contains(tokenType))
		{
			key = PerlSyntaxHighlighter.PERL_DX_STRING;
		}
		else if (QQ_STRINGS.contains(tokenType))
		{
			key = PerlSyntaxHighlighter.PERL_DQ_STRING;
		}
		else
		{
			key = PerlSyntaxHighlighter.PERL_SQ_STRING;
		}

		PsiElement run = element.getFirstChild();
		int offset = run == null ? -1 : run.getTextOffset();

		while (run != null)
		{
			boolean isString = run instanceof PerlStringContentElementImpl;
			boolean isWhiteSpace = run instanceof PsiWhiteSpace;

			tokenType = run.getNode().getElementType();

			if (!(isString || isWhiteSpace || PerlParserUtil.ALL_QUOTES.contains(tokenType)))
			{
				int endOffset = run.getTextOffset();

				if (endOffset > offset)
				{
//					System.err.println("Annotating from " + offset + " to " + endOffset);
					holder.createInfoAnnotation(new TextRange(offset, endOffset), null).setTextAttributes(key);
				}
				offset = endOffset + run.getTextLength();
			}

			run = run.getNextSibling();
		}

		int endOffset = element.getTextOffset() + element.getTextLength();
		if (endOffset > offset && offset > -1)
		{
//			System.err.println("Annotating from " + offset + " to " + endOffset);
			holder.createInfoAnnotation(new TextRange(offset, endOffset), null).setTextAttributes(key);
		}
//		System.err.println("Done");
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
		else
		{
			IElementType tokenType = element.getNode().getElementType();
			if (STRINGS.contains(tokenType))
			{
				annotateStringContainerIfNotInjected(element, holder);
			}
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