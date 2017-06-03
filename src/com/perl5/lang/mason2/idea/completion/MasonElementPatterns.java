/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.mason2.idea.completion;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.perl5.lang.mason2.psi.MasonCallStatement;
import com.perl5.lang.mason2.psi.MasonFlagsStatement;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PsiPerlCommaSequenceExpr;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Created by hurricup on 10.01.2016.
 */
public interface MasonElementPatterns extends PerlElementPatterns {
  PsiElementPattern.Capture<PerlString> MASON_EXTENDS_VALUE_PATTERN =
    psiElement(PerlString.class)
      .inside(MasonFlagsStatement.class)
      .withParent(PsiPerlCommaSequenceExpr.class)
      .afterLeaf(psiElement(PsiElement.class));

  PsiElementPattern.Capture<PsiElement> MASON_EXTENDS_VALUE_TEXT_PATTERN =
    psiElement(PsiElement.class)
      .withParent(MASON_EXTENDS_VALUE_PATTERN);

  PsiElementPattern.Capture<PerlStringContentElement> MASON_CALL_TEMPLATE_PATTERN =
    psiElement(PerlStringContentElement.class)
      .withParent(psiElement(PerlString.class).andOr(
        psiElement().withParent(MasonCallStatement.class),
        psiElement().withParent(
          psiElement(PsiPerlCommaSequenceExpr.class).withParent(MasonCallStatement.class))
      ).andNot(psiElement().afterSibling(psiElement())
      ));
}
