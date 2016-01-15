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

package com.perl5.lang.embedded.idea.completion;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.perl5.lang.perl.lexer.PerlElementTypes;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Created by hurricup on 14.01.2016.
 */
public interface EmbeddedPerlPatterns extends PerlElementTypes
{
	PsiElementPattern.Capture<PsiElement> BROKEN_OPEN_MARKER_PATTERN =
			psiElement(QUESTION).withParent(
					psiElement(PsiErrorElement.class).afterSibling(
							psiElement(PsiErrorElement.class).withChild(psiElement(LEFT_ANGLE))
					)
			);
}
