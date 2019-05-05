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
import com.perl5.lang.perl.psi.PerlCastExpression;
import com.perl5.lang.perl.psi.PsiPerlScalarVariable;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;


public class PerlFormattingSimpleDereferenceUnwrap implements PerlFormattingOperation {
  protected final PerlCastExpression myCastElement;
  protected final PsiPerlScalarVariable myVariableElement;

  public PerlFormattingSimpleDereferenceUnwrap(PerlCastExpression myCastElement, PsiPerlScalarVariable myVariableElement) {
    this.myCastElement = myCastElement;
    this.myVariableElement = myVariableElement;
  }

  @Override
  public int apply() {
    int delta = 0;

    if (myCastElement.isValid() && myVariableElement.isValid()) {
      String newCode = getCode();
      PerlFileImpl newFile = PerlElementFactory.createFile(myCastElement.getProject(), newCode);
      PerlCastExpression newCastExpression = PsiTreeUtil.findChildOfType(newFile, PerlCastExpression.class);
      if (newCastExpression != null) {
        delta = new PerlFormattingReplace(myCastElement, newCastExpression).apply();
      }
    }

    return delta;
  }

  protected String getCode() {
    return myCastElement.getFirstChild().getNode().getText() + myVariableElement.getNode().getText();
  }
}
