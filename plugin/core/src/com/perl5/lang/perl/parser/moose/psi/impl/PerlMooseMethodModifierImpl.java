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

package com.perl5.lang.perl.parser.moose.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseMethodModifier;
import com.perl5.lang.perl.parser.moose.psi.PerlMoosePsiUtil;
import com.perl5.lang.perl.psi.impl.PsiPerlStatementImpl;
import org.jetbrains.annotations.Nullable;


public class PerlMooseMethodModifierImpl extends PsiPerlStatementImpl implements PerlMooseMethodModifier {
  public PerlMooseMethodModifierImpl(ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public PsiReference[] getReferences(PsiElement element) {
    return PerlMoosePsiUtil.getModifiersNameReference(getExpr(), element);
  }
}
