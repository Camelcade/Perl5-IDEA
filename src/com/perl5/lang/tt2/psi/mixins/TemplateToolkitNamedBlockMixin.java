/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.tt2.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.tt2.psi.PsiBlockName;
import com.perl5.lang.tt2.psi.TemplateToolkitNamedBlock;
import com.perl5.lang.tt2.psi.impl.TemplateToolkitCompositeElementImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 15.06.2016.
 */
public abstract class TemplateToolkitNamedBlockMixin extends TemplateToolkitCompositeElementImpl implements TemplateToolkitNamedBlock {
  public TemplateToolkitNamedBlockMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    PsiBlockName blockName = getBlockDirective().getBlockName();
    if (blockName == null) {
      return null;
    }

    return blockName.getFirstChild();
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    if (StringUtil.isEmpty(name)) {
      throw new IncorrectOperationException(PerlBundle.message("ttk2.error.empty.block.name"));
    }

    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier instanceof LeafPsiElement) {
      ((LeafPsiElement)nameIdentifier).replaceWithText(name);
    }

    return this;
  }

  @NotNull
  @Override
  public SearchScope getUseScope() {
    return super.getUseScope();
  }

  @Override
  public String getName() {
    PsiElement nameIdentifier = getNameIdentifier();
    return nameIdentifier == null ? super.getName() : nameIdentifier.getText();
  }

  @Override
  public int getTextOffset() {
    PsiElement nameIdentifier = getNameIdentifier();
    return nameIdentifier == null ? super.getTextOffset() : nameIdentifier.getTextOffset();
  }

  @Nullable
  @Override
  public Icon getIcon(int flags) {
    return PerlIcons.TTK2_BLOCK_ICON;
  }
}
