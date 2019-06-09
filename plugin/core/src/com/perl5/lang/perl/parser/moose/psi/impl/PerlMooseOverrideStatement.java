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
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.extensions.parser.PerlReferencesProvider;
import com.perl5.lang.perl.parser.moose.psi.PerlMoosePsiUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mixins.PerlSubDefinitionBase;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PerlMooseOverrideStatement extends PerlSubDefinitionBase implements PerlReferencesProvider {
  public PerlMooseOverrideStatement(@NotNull ASTNode node) {
    super(node);
  }

  public PerlMooseOverrideStatement(@NotNull PerlSubDefinitionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Nullable
  @Override
  public PsiReference[] getReferences(PsiElement element) {
    return PerlMoosePsiUtil.getModifiersNameReference(getExpr(), element);
  }


  @Override
  public PsiPerlBlock getSubDefinitionBody() {
    return null;
  }

  @Nullable
  @Override
  protected PsiElement getSignatureContainer() {
    return null;
  }


  @Nullable
  public PsiPerlExpr getExpr() {
    return findChildByClass(PsiPerlExpr.class);
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    PsiElement expr = getExpr();

    if (expr instanceof PsiPerlParenthesisedExpr) {
      expr = expr.getFirstChild();
      if (expr != null) {
        expr = expr.getNextSibling();
      }
    }

    if (expr instanceof PsiPerlCommaSequenceExpr) {
      PsiElement nameContainer = expr.getFirstChild();
      if (nameContainer instanceof PerlString) {
        return nameContainer;
      }
    }

    return null;
  }

  @Override
  protected String getSubNameHeavy() {
    PsiElement nameContainer = getNameIdentifier();

    if (nameContainer != null) {
      return ElementManipulators.getValueText(nameContainer);
    }

    return null;
  }

  @Override
  public boolean isMethod() {
    return true;
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    if (name.isEmpty()) {
      throw new IncorrectOperationException("You can't set an empty method name");
    }

    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier != null) {
      ElementManipulators.handleContentChange(nameIdentifier, name);
    }

    return this;
  }
}

