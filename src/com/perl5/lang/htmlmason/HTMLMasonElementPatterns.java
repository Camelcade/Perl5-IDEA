/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.psi.PsiPerlNamedListExpr;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Created by hurricup on 08.03.2016.
 */
public interface HTMLMasonElementPatterns extends HTMLMasonElementTypes, PerlElementTypes
{
	PsiElementPattern.Capture<PsiElement> ATTR_OR_ARG_ELEMENT_PATTERN = psiElement().andOr(
			psiElement().inside(psiElement(HTML_MASON_ATTR_BLOCK)),
			psiElement().inside(psiElement(HTML_MASON_ARGS_BLOCK))
	);

	PsiElementPattern.Capture<PerlSubNameElement> HTML_MASON_TEMPLATE_CONTEXT_PATTERN =
			psiElement(PerlSubNameElement.class).withParent(
					psiElement(PsiPerlMethod.class)
							.withParent(
									psiElement(PsiPerlNamedListExpr.class).afterLeaf(psiElement(HTML_MASON_BLOCK_OPENER))
							)
			);

	// this is for corrupterd <%identifier
	PsiElementPattern.Capture<PsiElement> HTML_MASON_TEMPLATE_CONTEXT_PATTERN_BROKEN =
			psiElement(IDENTIFIER).afterLeaf(psiElement(HTML_MASON_BLOCK_OPENER)
			);
}
