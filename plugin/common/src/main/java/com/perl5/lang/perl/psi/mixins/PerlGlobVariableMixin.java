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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.psi.PerlStubBasedPsiElementBase;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import com.perl5.lang.perl.psi.PsiPerlAssignExpr;
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.psi.stubs.globs.PerlGlobStub;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlGlobUtilCore;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public abstract class PerlGlobVariableMixin extends PerlStubBasedPsiElementBase<PerlGlobStub>
  implements PsiPerlGlobVariable {
  public PerlGlobVariableMixin(@NotNull ASTNode node) {
    super(node);
  }

  public PerlGlobVariableMixin(@NotNull PerlGlobStub stub, @NotNull IElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public @Nullable String getNamespaceName() {
    PerlGlobStub stub = getGreenStub();
    if (stub != null) {
      return stub.getNamespaceName();
    }

    String namespace = getExplicitNamespaceName();

    if (namespace == null) {
      namespace = getContextNamespaceName();
    }

    return namespace;
  }

  @Override
  public String getName() {
    return getGlobName();
  }

  @Override
  public @Nullable String getGlobName() {
    PerlGlobStub stub = getGreenStub();
    if (stub != null) {
      return stub.getGlobName();
    }

    PerlVariableNameElement variableNameElement = getVariableNameElement();
    if (variableNameElement == null) {
      return null;
    }
    String variableNameText = variableNameElement.getText();
    int delimiterIndex = variableNameText.lastIndexOf(':');
    return delimiterIndex == -1 ? variableNameText : variableNameText.substring(delimiterIndex + 1);
  }

  protected @Nullable String getContextNamespaceName() {
    return PerlPackageUtilCore.getContextNamespaceName(this);
  }

  @Override
  public @Nullable String getExplicitNamespaceName() {
    PerlVariableNameElement variableNameElement = getVariableNameElement();
    if (variableNameElement == null) {
      return null;
    }

    String variableNameText = variableNameElement.getText();
    int delimiterIndex = variableNameText.lastIndexOf(':');
    return delimiterIndex == -1 ? null : PerlPackageUtilCore.getCanonicalNamespaceName(variableNameText.substring(0, delimiterIndex + 1));
  }

  @Override
  public PerlVariableNameElement getVariableNameElement() {
    return findChildByClass(PerlVariableNameElement.class);
  }

  @Override
  public boolean isBuiltIn() {
    if (getExplicitNamespaceName() != null) {
      return false;
    }

    String globName = getName();
    return globName != null && PerlGlobUtilCore.BUILT_IN.contains(globName);
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    return getVariableNameElement();
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    return PerlPsiUtil.renameNamedElement(this, name);
  }

  @Override
  public @Nullable String getCanonicalName() {

    String packageName = getNamespaceName();
    if (packageName == null) {
      return null;
    }
    return packageName + PerlPackageUtilCore.NAMESPACE_SEPARATOR + getName();
  }


  @Override
  public @Nullable Icon getIcon(int flags) {
    return PerlIcons.GLOB_GUTTER_ICON;
  }

  @Override
  public ItemPresentation getPresentation() {
    return new PerlItemPresentationSimple(this, getName());
  }

  @Override
  public boolean isLeftSideOfAssignment() {
    PerlGlobStub stub = getGreenStub();
    if (stub != null) {
      return stub.isLeftSideOfAssignment();
    }
    return getParent() instanceof PsiPerlAssignExpr && getNextSibling() != null;
  }


  @Override
  public int getTextOffset() {
    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier == null) {
      return super.getTextOffset();
    }

    return nameIdentifier.getTextOffset() + ElementManipulators.getOffsetInElement(nameIdentifier);
  }
}
