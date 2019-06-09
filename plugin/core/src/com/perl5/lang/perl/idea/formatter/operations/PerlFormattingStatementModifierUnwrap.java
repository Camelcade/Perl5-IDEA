/*
 * Copyright 2015-2019 Alexandr Evstigneev
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


public class PerlFormattingStatementModifierUnwrap extends PerlFormattingStatementModifierWrap {
  public PerlFormattingStatementModifierUnwrap(PsiPerlStatementModifier container) {
    super(container);
  }

  @Override
  public int apply() {
    int delta = 0;

    if (getMyModifier().isValid()) {
      PsiPerlExpr expression = PsiTreeUtil.getChildOfType(getMyModifier(), PsiPerlExpr.class);

      if (expression != null && expression instanceof PsiPerlParenthesisedExpr) {
        PsiPerlExpr nestedExpression = ((PsiPerlParenthesisedExpr)expression).getExpr();
        if (nestedExpression != null) {
          delta = nestedExpression.getNode().getTextLength() - expression.getNode().getTextLength();
          expression.replace(nestedExpression.copy());
        }
      }
    }
    return delta;
  }
}
