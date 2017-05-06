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

package com.perl5.lang.perl.idea.formatter.operations;

import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.PsiPerlParenthesisedExpr;
import com.perl5.lang.perl.psi.PsiPerlStatementModifier;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;

/**
 * Created by hurricup on 15.11.2015.
 */
public class PerlFormattingStatementModifierWrap implements PerlFormattingOperation {
  private final PsiPerlStatementModifier myModifier;

  public PerlFormattingStatementModifierWrap(PsiPerlStatementModifier container) {
    myModifier = container;
  }

  @Override
  public int apply() {
    int delta = 0;

    if (getMyModifier().isValid()) {
      PsiPerlExpr expression = PsiTreeUtil.getChildOfType(getMyModifier(), PsiPerlExpr.class);

      if (expression != null && !(expression instanceof PsiPerlParenthesisedExpr)) {
        PsiPerlParenthesisedExpr parenthesisedExpression = PerlElementFactory.createParenthesisedExpression(getMyModifier().getProject());
        parenthesisedExpression.addAfter(expression.copy(), parenthesisedExpression.getFirstChild());
        delta = expression.getNode().getTextLength() - parenthesisedExpression.getNode().getTextLength();
        expression.replace(parenthesisedExpression);
      }
    }
    return delta;
  }

  public PsiPerlStatementModifier getMyModifier() {
    return myModifier;
  }
}
