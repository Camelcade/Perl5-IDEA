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
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.psi.PerlAnnotation;
import com.perl5.lang.perl.psi.impl.PerlAnnotationContainerImpl;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import org.jetbrains.annotations.NotNull;

public class PerlAnnotatorMisc extends PerlAnnotator
{
	private static final TokenSet QQ_STRINGS = TokenSet.create(HEREDOC_QQ, STRING_DQ);
	private static final TokenSet XQ_STRINGS = TokenSet.create(HEREDOC_QX, STRING_XQ);
	private static final TokenSet SQ_STRINGS = TokenSet.create(HEREDOC, STRING_SQ, STRING_BARE, STRING_LIST, TR_SEARCHLIST, TR_REPLACEMENTLIST);
	private static final TokenSet STRINGS = TokenSet.orSet(QQ_STRINGS, SQ_STRINGS, XQ_STRINGS);


	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder)
	{
		IElementType elementType = PsiUtilCore.getElementType(element);
		if (elementType == NYI_STATEMENT)
		{
			holder.createInfoAnnotation(element, "Unimplemented statement").setTextAttributes(CodeInsightColors.TODO_DEFAULT_ATTRIBUTES);
		}
		if (elementType == LABEL_DECLARATION || elementType == LABEL_EXPR)
		{
			holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_LABEL);
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
		else if (element instanceof PerlAnnotationContainerImpl)
		{
			PsiElement run = element.getFirstChild();
			while (run != null && !(run instanceof PsiComment || run instanceof PerlAnnotation))
			{
				if (!(run instanceof PsiWhiteSpace))
				{
					decorateElement(run, holder, PerlSyntaxHighlighter.PERL_COMMENT);
				}
				run = run.getNextSibling();
			}
		}
	}
}