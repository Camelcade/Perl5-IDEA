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

package com.perl5.lang.mason2.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mason2.psi.MasonAugmentMethodModifier;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseAugmentStatementImpl;
import com.perl5.lang.perl.parser.moose.stubs.augment.PerlMooseAugmentStatementStub;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.mason2.elementType.Mason2ElementTypes.MASON_METHOD_MODIFIER_NAME;


public class MasonAugmentMethodModifierImpl extends PerlMooseAugmentStatementImpl
  implements MasonAugmentMethodModifier {
  protected List<PerlVariableDeclarationElement> myImplicitVariables = null;

  public MasonAugmentMethodModifierImpl(ASTNode node) {
    super(node);
  }

  public MasonAugmentMethodModifierImpl(@NotNull PerlMooseAugmentStatementStub stub, @NotNull IElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public boolean hasStringSubReference() {
    return false;
  }

  protected List<PerlVariableDeclarationElement> buildImplicitVariables() {
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

  @Override
  protected @Nullable String getSubNameFromPsi() {
    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier != null) {
      return nameIdentifier.getText();
    }
    return null;
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    ASTNode node = getNode();
    ASTNode modifierNode = node.findChildByType(MASON_METHOD_MODIFIER_NAME);
    if (modifierNode != null) {
      return modifierNode.getPsi();
    }
    return null;
  }
}
