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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.psi.PerlAnnotation;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlConstantDefinition;
import com.perl5.lang.perl.psi.impl.PerlSubDefinitionWithTextIdentifier;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * Created by hurricup on 29.08.2015.
 */
public abstract class PerlConstantDefinitionMixin extends PerlSubDefinitionWithTextIdentifier implements PsiPerlConstantDefinition {
  public PerlConstantDefinitionMixin(ASTNode node) {
    super(node);
  }

  public PerlConstantDefinitionMixin(@NotNull PerlSubDefinitionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  /**
   * Returns constant defenition value PsiElement
   *
   * @return PsiElement
   */
  public PsiElement getValueExpression() {
    PsiElement[] children = getChildren();
    if (children.length > 1) {
      return children[children.length - 1];
    }
    return null;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) {
      ((PerlVisitor)visitor).visitConstantDefinition(this);
    }
    else {
      super.accept(visitor);
    }
  }

  @NotNull
  @Override
  public List<PerlAnnotation> getAnnotationList() {
    List<PerlAnnotation> annotationList = super.getAnnotationList();
    if (!annotationList.isEmpty()) {
      return annotationList;
    }

    return PerlPsiUtil.collectAnnotations(PsiTreeUtil.getParentOfType(this, PerlUseStatement.class));
  }

  @Override
  public boolean isStatic() {
    return true;
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    return PerlPsiUtil.getFirstContentTokenOfString(getFirstChild());
  }

  @Override
  public ItemPresentation getPresentation() {
    return new PerlItemPresentationSimple(this, getName());
  }

  @Nullable
  @Override
  public Icon getIcon(int flags) {
    return PerlIcons.CONSTANT_GUTTER_ICON;
  }

  @Override
  public String getPresentableName() {
    return getName();
  }
}
