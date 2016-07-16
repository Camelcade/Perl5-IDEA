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
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlVariable;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 10.08.2015.
 */
public class PerlAnnotatorNamespaces extends PerlAnnotator
{
	private void annotateNamespaceElement(PerlNamespaceElement namespaceElement, AnnotationHolder holder)
	{
		PsiElement parent = namespaceElement.getParent();

		if (parent instanceof PerlNamespaceDefinition)
		{
			decorateElement(namespaceElement, holder, PerlSyntaxHighlighter.PERL_PACKAGE_DEFINITION, namespaceElement.isDeprecated());
		}
		else
		{
			TextAttributesKey attributesKey = PerlSyntaxHighlighter.PERL_PACKAGE;

			if (namespaceElement.isPragma())
			{
				attributesKey = PerlSyntaxHighlighter.PERL_PACKAGE_PRAGMA;
			}
			else if (namespaceElement.isBuiltin())
			{
				attributesKey = PerlSyntaxHighlighter.PERL_PACKAGE_CORE;
			}

			decorateElement(namespaceElement, holder, attributesKey, namespaceElement.isDeprecated());
		}
	}

	@Override
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder)
	{
		PsiElement parent = element.getParent();
		if (element instanceof PerlNamespaceElement && !(parent instanceof PerlVariable || parent instanceof PerlGlobVariable))
		{
			annotateNamespaceElement((PerlNamespaceElement) element, holder);
		}
	}
}
