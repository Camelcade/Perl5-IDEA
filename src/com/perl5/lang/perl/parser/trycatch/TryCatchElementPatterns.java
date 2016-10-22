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

package com.perl5.lang.perl.parser.trycatch;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.psi.PsiPerlStatement;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Created by hurricup on 17.04.2016.
 */
public interface TryCatchElementPatterns extends PerlElementPatterns
{
	PsiElementPattern.Capture<PsiElement> ELEMENT_AFTER_TRY_CATCH =
			psiElement().inside(
					psiElement(PsiPerlStatement.class).afterSiblingSkipping(
							WHITE_SPACE_AND_COMMENTS,
							psiElement().andOr(
									psiElement(TRY_EXPR),
									psiElement(CATCH_EXPR))

					));

	PsiElementPattern.Capture<PsiElement> PACKAGE_IN_CATCH =
			psiElement(IDENTIFIER).withParent(
					psiElement(CATCH_CONDITION)
			);
}
