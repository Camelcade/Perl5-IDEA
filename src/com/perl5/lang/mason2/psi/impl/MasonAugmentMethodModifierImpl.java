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

package com.perl5.lang.mason2.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.mason2.elementType.Mason2ElementTypes;
import com.perl5.lang.mason2.psi.MasonAugmentMethodModifier;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseAugmentStatementImpl;
import com.perl5.lang.perl.parser.moose.stubs.augment.PerlMooseAugmentStatementStub;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 28.01.2016.
 */
public class MasonAugmentMethodModifierImpl extends PerlMooseAugmentStatementImpl
  implements MasonAugmentMethodModifier, Mason2ElementTypes {
  protected List<PerlVariableDeclarationElement> myImplicitVariables = null;

  public MasonAugmentMethodModifierImpl(ASTNode node) {
    super(node);
  }

  public MasonAugmentMethodModifierImpl(@NotNull PerlMooseAugmentStatementStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Nullable
  @Override
  public PsiReference[] getReferences(PsiElement element) {
    return null;
  }

  protected List<PerlVariableDeclarationElement> buildImplicitVariables() {
    List<PerlVariableDeclarationElement> newImplicitVariables = new ArrayList<>();

    if (isValid()) {
      newImplicitVariables.add(PerlImplicitVariableDeclaration.createDefaultInvocant(this));
    }
    return newImplicitVariables;
  }

  @NotNull
  @Override
  public List<PerlVariableDeclarationElement> getImplicitVariables() {
    if (myImplicitVariables == null) {
      myImplicitVariables = buildImplicitVariables();
    }
    return myImplicitVariables;
  }

  @Nullable
  @Override
  protected String getSubNameFromPsi() {
    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier != null) {
      return nameIdentifier.getText();
    }
    return null;
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    ASTNode node = getNode();
    ASTNode modifierNode = node.findChildByType(MASON_METHOD_MODIFIER_NAME);
    if (modifierNode != null) {
      return modifierNode.getPsi();
    }
    return null;
  }
}
