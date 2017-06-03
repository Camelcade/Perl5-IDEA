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

package com.perl5.lang.perl.idea.formatter.operations;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PsiPerlScalarVariable;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;

/**
 * Created by evstigneev on 16.11.2015.
 * Converts $var->{key} to $$var{key}
 */
public class PerlFormattingScalarDerefCollapse implements PerlFormattingOperation {
  protected final PsiPerlScalarVariable myScalarElement;
  protected final PsiElement myIndexElement;

  public PerlFormattingScalarDerefCollapse(PsiPerlScalarVariable scalarElement, PsiElement indexElement) {
    myScalarElement = scalarElement;
    myIndexElement = indexElement;
  }

  @Override
  public int apply() {
    int delta = 0;

    if (myScalarElement.isValid() && myIndexElement.isValid()) {
      String newCode = "$" + myScalarElement.getNode().getText() + myIndexElement.getNode().getText() + ";";
      PerlFileImpl newFile = PerlElementFactory.createFile(myScalarElement.getProject(), newCode);
      PsiPerlStatement statement = PsiTreeUtil.findChildOfType(newFile, PsiPerlStatement.class);

      if (statement != null) {
        delta = new PerlFormattingReplace(myScalarElement, myIndexElement, statement.getFirstChild(), statement.getFirstChild()).apply();
      }
    }

    return delta;
  }
}
