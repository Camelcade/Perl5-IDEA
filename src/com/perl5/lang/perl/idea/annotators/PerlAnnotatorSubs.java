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
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 08.08.2015.
 */
public class PerlAnnotatorSubs extends PerlAnnotator
{
	@Override
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder)
	{
		if (element instanceof PerlSubNameElement)
		{
			if (((PerlSubNameElement) element).getConstantDefinitions().size() > 0)
			{
				Annotation annotation = holder.createInfoAnnotation(element, null);
				annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_CONSTANT);
			} else
			{

				PsiElement parent = element.getParent();

				if (parent instanceof PsiPerlSubDeclaration)
					holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SUB_DECLARATION);
				else if (parent instanceof PsiPerlSubDefinition)
					holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SUB_DEFINITION);
				else if (parent instanceof PsiPerlExpr) // fixme temporary solution for pre-defined expressions, suppose only built-ins
					decorateElement(
							holder.createInfoAnnotation(element, null),
							PerlSyntaxHighlighter.PERL_SUB,
							true,
							false
					);
				else if (parent instanceof PerlMethod)
				{
					decorateElement(
							holder.createInfoAnnotation(element, null),
							PerlSyntaxHighlighter.PERL_SUB,
							((PerlSubNameElement) element).isBuiltIn(),
							false
					);
				}
			}
		}
	}
}
