/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimpleDynamicLocation;
import com.perl5.lang.perl.idea.ui.PerlIconProvider;
import com.perl5.lang.perl.psi.PerlMethodModifier;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class PerlMethodModifierMixin extends PerlCompositeElementImpl implements PerlMethodModifier {
  public PerlMethodModifierMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public String getName() {
    PerlSubNameElement subNameElement = getSubNameElement();
    return subNameElement == null ? null : subNameElement.getText();
  }

  @Nullable
  private PerlSubNameElement getSubNameElement() {
    return PsiTreeUtil.getChildOfType(this, PerlSubNameElement.class);
  }

  @Override
  public Icon getIcon(int flags) {
    return PerlIconProvider.getIcon(this);
  }

  @Override
  public ItemPresentation getPresentation() {
    String name = getName();
    return name == null ? null : new PerlItemPresentationSimpleDynamicLocation(
      this,
      getModifierText() + " " + name + "()");
  }

  @NotNull
  @Override
  public PsiElement getNavigationElement() {
    PerlSubNameElement subNameElement = getSubNameElement();
    return subNameElement == null ? this : subNameElement;
  }
}
