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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlAssignExpression;
import com.perl5.lang.perl.psi.impl.PsiPerlExprImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 30.04.2016.
 */
public class PerlAssignExpressionMixin extends PsiPerlExprImpl implements PerlAssignExpression {
  public PerlAssignExpressionMixin(ASTNode node) {
    super(node);
  }

  @NotNull
  @Override
  public PsiElement getLeftSide() {
    return getFirstChild();
  }

  @Nullable
  @Override
  public PsiElement getRightSide() {
    PsiElement lastChild = getLastChild();
    PsiElement firstChild = getFirstChild();

    if (lastChild == firstChild || lastChild == null || lastChild.getFirstChild() == null) {
      return null;
    }

    return lastChild;
  }
}
