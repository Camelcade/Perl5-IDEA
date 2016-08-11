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

package com.perl5.lang.ea.psi;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.perl5.lang.ea.PerlExternalAnnotationsParserDefinition;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Created by hurricup on 11.08.2016.
 */
public interface PerlExternalAnnotationsElementPatterns extends PerlExternalAnnotationsElementTypes
{
	PsiElementPattern.Capture<PsiElement> FILE_PATTERN = psiElement(PerlExternalAnnotationsParserDefinition.FILE);

	PsiElementPattern.Capture<PsiElement> NAMESPACE_PATTERN = psiElement(PSEUDO_NAMESPACE).withParent(FILE_PATTERN);

	PsiElementPattern.Capture<PsiElement> NAMESPACE_ELEMENT_PATTERN = psiElement(PACKAGE).withParent(NAMESPACE_PATTERN);

	PsiElementPattern.Capture<PsiElement> SUB_ELEMENT_PATTERN =
			psiElement(PSEUDO_SUB_NAME).withParent(
					psiElement(PSEUDO_DECLARATION).withParent(
							psiElement(NAMESPACE_CONTENT).withParent(NAMESPACE_PATTERN)
					)
			);

	PsiElementPattern.Capture<PsiElement> KEYWORD_ELEMENT_PATTERN = psiElement(IDENTIFIER).inside(FILE_PATTERN);

}
