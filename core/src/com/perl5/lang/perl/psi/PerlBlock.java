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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.properties.PerlLoop;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.types.PerlType;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 04.03.2016.
 */
public interface PerlBlock extends PerlLoop, PerlLexicalScope {
  @Nullable
  @Override
  default PsiPerlContinueBlock getContinueBlock() {
    PsiElement potentialBlock = PerlPsiUtil.getNextSignificantSibling(this);
    return potentialBlock instanceof PsiPerlContinueBlock ? (PsiPerlContinueBlock)potentialBlock : null;
  }

  @Nullable
  default PsiPerlStatement getLastStatement(){
    PsiPerlStatement[] children = PsiTreeUtil.getChildrenOfType(this, PsiPerlStatement.class);
    if(children != null && children.length>0){
      return children[children.length-1];
    }
    return null;
  }

  @Nullable
  default PerlType guessReturnType(){
    // regards type of last expression in block as returned type
    PsiPerlStatement statement = getLastStatement();
    if (statement != null) {
      PsiPerlExpr expr = statement.getExpr();
      if (expr instanceof PsiPerlReturnExpr) {
        // fixme psiPerlReturnExpr.getExpr() should be auto generated
        expr = PsiTreeUtil.getChildOfType(expr, PsiPerlExpr.class);
      }
      return PerlPsiUtil.getPerlExpressionNamespace(expr);
    }
    return null;
  }
}
