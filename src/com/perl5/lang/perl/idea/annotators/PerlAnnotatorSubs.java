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

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.references.PerlSubReference;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 08.08.2015.
 */
public class PerlAnnotatorSubs extends PerlAnnotator
{
	@Override
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder)
	{
		if (element instanceof PerlSubDefinitionBase)
		{
			PsiElement nameIdentifier = ((PerlSubDefinitionBase) element).getNameIdentifier();
			if (nameIdentifier != null)
			{
				String subName = ((PerlSubDefinitionBase) element).getSubName();
				if (PerlSubUtil.SUB_AUTOLOAD.equals(subName))
				{
					holder.createInfoAnnotation(nameIdentifier, null).setTextAttributes(PerlSyntaxHighlighter.PERL_AUTOLOAD);
				}
				else
				{
					holder.createInfoAnnotation(nameIdentifier, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SUB_DEFINITION);
				}
			}
		}
		else if (element instanceof PerlSubDeclaration)
		{
			PsiElement nameIdentifier = ((PerlSubDeclaration) element).getNameIdentifier();
			if (nameIdentifier != null)
			{
				holder.createInfoAnnotation(nameIdentifier, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SUB_DECLARATION);
			}
		}
		if (PsiUtilCore.getElementType(element) == SUB) //  instanceof PerlSubNameElement
		{
			PsiElement parent = element.getParent();
			if (parent instanceof PerlMethod)
			{
				// fixme don't we need to take multiple references here?
				PsiElement grandParent = parent.getParent();
				PerlNamespaceElement methodNamespace = ((PerlMethod) parent).getNamespaceElement();

				if (
						!(grandParent instanceof PsiPerlNestedCall)    /// not ...->method fixme shouldn't we use isObjectMethod here?
								&& (methodNamespace == null || methodNamespace.isCORE())    // no explicit NS or it's core
								&& ((PerlSubNameElement) element).isBuiltIn()
						)
				{
					decorateElement(element, holder, PerlSyntaxHighlighter.PERL_SUB_BUILTIN);
				}
				else
				{

					PsiReference reference = element.getReference();

					if (reference instanceof PerlSubReference)
					{
						((PerlSubReference) reference).multiResolve(false);

						if (((PerlSubReference) reference).isConstant())
						{
							holder.createInfoAnnotation(element, "Constant").setTextAttributes(PerlSyntaxHighlighter.PERL_CONSTANT);
						}
						else if (((PerlSubReference) reference).isAutoloaded())
						{
							holder.createInfoAnnotation(element, "Auto-loaded sub").setTextAttributes(PerlSyntaxHighlighter.PERL_AUTOLOAD);
						}
						else if (((PerlSubReference) reference).isXSub())
						{
							holder.createInfoAnnotation(element, "XSub").setTextAttributes(PerlSyntaxHighlighter.PERL_XSUB);
						}
						else
						{
							decorateElement(
									element,
									holder,
									((PerlSubNameElement) element).isBuiltIn() ? PerlSyntaxHighlighter.PERL_SUB_BUILTIN : PerlSyntaxHighlighter.PERL_SUB
							);
						}
					}
				}
			}
		}
	}
}
