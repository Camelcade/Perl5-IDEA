/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseAugmentStatement;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseMethodModifier;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseStatementWithSubReference;
import com.perl5.lang.perl.parser.moose.stubs.augment.PerlMooseAugmentStatementStub;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PerlMooseAugmentStatementImpl extends PerlStubBasedPsiElementBase<PerlMooseAugmentStatementStub>
  implements PerlMooseAugmentStatement, PerlMooseMethodModifier, PerlMooseStatementWithSubReference {
  public PerlMooseAugmentStatementImpl(ASTNode node) {
    super(node);
  }

  public PerlMooseAugmentStatementImpl(@NotNull PerlMooseAugmentStatementStub stub, @NotNull IElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public @Nullable String getSubName() {
    PerlMooseAugmentStatementStub stub = getGreenStub();
    if (stub != null) {
      return stub.getSubName();
    }
    return getSubNameFromPsi();
  }

  protected @Nullable String getSubNameFromPsi() {
    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier != null) {
      return ElementManipulators.getValueText(nameIdentifier);
    }

    return null;
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    PsiElement expr = getExpr();

    if (expr instanceof PsiPerlParenthesisedExpr) {
      expr = expr.getFirstChild();
      if (expr != null) {
        expr = expr.getNextSibling();
      }
    }

    if (expr instanceof PsiPerlCommaSequenceExpr) {
      PsiElement nameElement = expr.getFirstChild();
      if (nameElement instanceof PerlString) {
        return nameElement;
      }
    }
    return null;
  }

  @Override
  public int getTextOffset() {
    PsiElement nameIdentifier = getNameIdentifier();

    return nameIdentifier == null
           ? super.getTextOffset()
           : nameIdentifier.getTextOffset();
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    return PerlPsiUtil.renameNamedElement(this, name);
  }

  @Override
  public @Nullable PsiPerlExpr getExpr() {
    return PerlMooseStatementWithSubReference.super.getExpr();
  }

  @Override
  public @Nullable PsiPerlStatementModifier getStatementModifier() {
    return null;
  }
}
