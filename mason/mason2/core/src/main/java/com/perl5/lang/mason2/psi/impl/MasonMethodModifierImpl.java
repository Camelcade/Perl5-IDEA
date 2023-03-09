/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.mason2.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.perl5.lang.mason2.psi.MasonMethodModifier;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseMethodModifierImpl;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class MasonMethodModifierImpl extends PerlMooseMethodModifierImpl implements MasonMethodModifier {
  protected List<PerlVariableDeclarationElement> myImplicitVariables = null;

  public MasonMethodModifierImpl(ASTNode node) {
    super(node);
  }


  @Override
  public @Nullable PsiReference[] getReferences(PsiElement element) {
    return null;
  }

  protected @NotNull List<PerlVariableDeclarationElement> buildImplicitVariables() {
    List<PerlVariableDeclarationElement> newImplicitVariables = new ArrayList<>();
    if (isValid()) {
      newImplicitVariables.add(PerlImplicitVariableDeclaration.createInvocant(this));
    }
    return newImplicitVariables;
  }


  @Override
  public @NotNull List<PerlVariableDeclarationElement> getImplicitVariables() {
    if (myImplicitVariables == null) {
      myImplicitVariables = buildImplicitVariables();
    }
    return myImplicitVariables;
  }
}
