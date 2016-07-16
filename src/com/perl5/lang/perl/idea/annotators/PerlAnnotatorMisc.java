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

import com.intellij.lang.ASTNode;
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
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.parser.PerlParserUtil;
import com.perl5.lang.perl.psi.PerlAnnotation;
import com.perl5.lang.perl.psi.PerlConstantDefinition;
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

		if (element.getParent() instanceof PerlConstantDefinition && element.getPrevSibling() == null)
		{
			holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_CONSTANT);
			return;
		}

		TextAttributesKey key;
		IElementType tokenType = PsiUtilCore.getElementType(element);

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
		int offset = run == null ? -1 : run.getNode().getStartOffset();

		while (run != null)
		{
			boolean isString = run instanceof PerlStringContentElementImpl;
			boolean isWhiteSpace = run instanceof PsiWhiteSpace;

			ASTNode runNode = run.getNode();
			tokenType = runNode.getElementType();

			if (!(isString || isWhiteSpace || PerlParserUtil.ALL_QUOTES.contains(tokenType)))
			{
				int endOffset = runNode.getStartOffset();

				if (endOffset > offset)
				{
//					System.err.println("Annotating from " + offset + " to " + endOffset);
					holder.createInfoAnnotation(new TextRange(offset, endOffset), null).setTextAttributes(key);
				}
				offset = endOffset + run.getTextLength();
			}

			run = run.getNextSibling();
		}

		ASTNode elementNode = element.getNode();
		int endOffset = elementNode.getStartOffset() + elementNode.getTextLength();
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
		{
			holder.createInfoAnnotation(element, "Unimplemented statement").setTextAttributes(CodeInsightColors.TODO_DEFAULT_ATTRIBUTES);
		}
		else if (element instanceof PerlAnnotation)
		{
			holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_ANNOTATION);

			PsiElement lastChild = element.getLastChild();
			if (lastChild instanceof PerlStringContentElementImpl)
			{
				holder.createInfoAnnotation(lastChild, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SQ_STRING);
			}
		}
		else if (element instanceof PerlLabel)
		{
			holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_LABEL);
		}
		else
		{
			IElementType tokenType = element.getNode().getElementType();
			if (STRINGS.contains(tokenType))
			{
				annotateStringContainerIfNotInjected(element, holder);
			}
			if (tokenType == HANDLE)
			{
				decorateElement(
						element,
						holder,
						PerlGlobUtil.BUILT_IN.contains(element.getText()) ? PerlSyntaxHighlighter.PERL_HANDLE_BUILTIN : PerlSyntaxHighlighter.PERL_HANDLE
				);
			}
			else if (tokenType == ATTRIBUTE)
			{
				holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SUB_ATTRIBUTE);
			}
			else if (tokenType == SUB_PROTOTYPE_TOKEN)
			{
				holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SUB_PROTOTYPE_TOKEN);
			}
			else if (tokenType == NUMBER_VERSION)
			{
				holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_VERSION);
			}
			else if (tokenType == REGEX_TOKEN)
			{
				holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_REGEX_TOKEN);
			}
			else if (tokenType == LEFT_ANGLE || tokenType == RIGHT_ANGLE)
			{
				holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_ANGLE);
			}
			else if (PerlLexer.RESERVED_TOKENSET.contains(tokenType))
			{
				holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_KEYWORD);
			}
			else if (tokenType == SUB_SIGNATURE_ELEMENT_IGNORE)
			{
				decorateElement(
						holder.createInfoAnnotation(element.getFirstChild(), null),
						PerlSyntaxHighlighter.PERL_SCALAR,
						true);
			}
			else if (tokenType == COMMENT_LINE || tokenType == COMMENT_BLOCK)
			{
				holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_COMMENT);
			}
		}
	}
}