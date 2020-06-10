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
import com.perl5.lang.perl.parser.PerlParserUtil;
import org.jetbrains.annotations.Nullable;

/**
 * Represents entity with quotes: string, strings list, regular expression (NYI)
 */
public interface PerlQuoted extends PsiElement {
  /**
   * @return open quote element if any
   */
  default @Nullable PsiElement getOpenQuoteElement() {
    PsiElement run = getFirstChild();

    while (run != null) {
      if (PerlParserUtil.OPEN_QUOTES.contains(PsiUtilCore.getElementType(run))) {
        return run;
      }
      run = run.getNextSibling();
    }
    return null;
  }

  /**
   * @return opening quote character if any or 0 if not found.
   */
  default char getOpenQuote() {
    PsiElement openQuoteElement = getOpenQuoteElement();
    if (openQuoteElement == null) {
      return 0;
    }
    CharSequence quoteChars = openQuoteElement.getNode().getChars();
    return quoteChars.length() == 1 ? quoteChars.charAt(0) : 0;
  }

  /**
   * @return close quote element if any
   */
  default @Nullable PsiElement getCloseQuoteElement() {
    PsiElement lastChild = getLastChild();
    return lastChild != null && PerlParserUtil.CLOSE_QUOTES.contains(PsiUtilCore.getElementType(lastChild)) ? lastChild : null;
  }
}
