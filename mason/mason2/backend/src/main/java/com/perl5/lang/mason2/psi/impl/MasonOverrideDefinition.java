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
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.extensions.PerlImplicitVariablesProvider;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseOverrideStatement;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class MasonOverrideDefinition extends PerlMooseOverrideStatement implements PerlImplicitVariablesProvider {
  protected List<PerlVariableDeclarationElement> myImplicitVariables;

  public MasonOverrideDefinition(@NotNull ASTNode node) {
    super(node);
  }

  public MasonOverrideDefinition(@NotNull PerlSubDefinitionStub stub, @NotNull IElementType nodeType) {
    super(stub, nodeType);
  }

  protected @NotNull List<PerlVariableDeclarationElement> buildImplicitVariables() {
    List<PerlVariableDeclarationElement> newImplicitVariables = new ArrayList<>();
    if (isValid()) {
      newImplicitVariables.add(PerlImplicitVariableDeclaration.createInvocant(this));
    }
    return newImplicitVariables;
  }

  @Override
  public @NotNull PsiPerlBlock getSubDefinitionBody() {
    return findNotNullChildByClass(PsiPerlBlock.class);
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    return PsiTreeUtil.getChildOfType(this, PerlSubNameElement.class);
  }

  @Override
  protected @Nullable String getSubNameHeavy() {
    PsiElement subNameElement = getNameIdentifier();
    // fixme manipulator?
    return subNameElement == null ? null : subNameElement.getNode().getText();
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    if (name.isEmpty()) {
      throw new IncorrectOperationException("You can't set an empty method name");
    }

    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier instanceof LeafPsiElement leafPsiElement) {
      leafPsiElement.replaceWithText(name);
    }

    return this;
  }

  @Override
  public @NotNull List<PerlVariableDeclarationElement> getImplicitVariables() {
    if (myImplicitVariables == null) {
      myImplicitVariables = buildImplicitVariables();
    }
    return myImplicitVariables;
  }
}
