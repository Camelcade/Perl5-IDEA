/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.perl5.lang.perl.idea.codeInsight.typeInferrence.value.PerlStaticValue;
import com.perl5.lang.perl.idea.codeInsight.typeInferrence.value.PerlValue;
import com.perl5.lang.perl.psi.PsiPerlPackageExpr;
import com.perl5.lang.perl.psi.impl.PsiPerlExprImpl;
import com.perl5.lang.perl.psi.properties.PerlValuableEntity;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

public abstract class PerlPackageExpression extends PsiPerlExprImpl implements PsiPerlPackageExpr, PerlValuableEntity {
  public PerlPackageExpression(ASTNode node) {
    super(node);
  }

  @NotNull
  @Override
  public PerlValue computePerlValue() {
    String elementText = getText();
    return PerlPackageUtil.__PACKAGE__.equals(elementText)
           ? PerlPackageUtil.getContextType(this)
           : PerlStaticValue.create(PerlPackageUtil.getCanonicalName(elementText));
  }
}
