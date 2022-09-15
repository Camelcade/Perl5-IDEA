/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import org.jetbrains.annotations.Nullable;

/**
 * Common parent for m/s/qr regular expression expressions
 */
public interface PerlRegexExpression extends PsiElement, PerlQuoted {
  /**
   * @return first opening quote element of the regex
   */
  @Override
  default @Nullable PsiElement getOpenQuoteElement() {
    PsiElement run = getFirstChild();
    while (run != null) {
      if (PerlTokenSets.REGEX_QUOTE_OPEN.contains(PsiUtilCore.getElementType(run))) {
        return run;
      }
      run = run.getNextSibling();
    }
    return null;
  }

  @Override
  default @Nullable PsiElement getCloseQuoteElement() {
    PsiElement run = getFirstChild();
    while (run != null) {
      if (PerlTokenSets.QUOTE_CLOSE_FIRST_ANY.contains(PsiUtilCore.getElementType(run))) {
        return run;
      }
      run = run.getNextSibling();
    }
    return null;
  }

  @Nullable
  PsiPerlPerlRegexModifiers getPerlRegexModifiers();
}
