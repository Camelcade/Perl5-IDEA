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
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlLabelDeclaration;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 04.03.2016.
 */
public class PerlLabelDeclarationMixin extends PerlCompositeElementImpl implements PerlLabelDeclaration {
  public PerlLabelDeclarationMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String getPresentableName() {
    return getName();
  }

  @Override
  public String getName() {
    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier != null) {
      return nameIdentifier.getText();
    }
    return null;
  }


  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    return getFirstChild();
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier != null) {
      PerlPsiUtil.renameElement(nameIdentifier, name);
    }
    return null;
  }

  @NotNull
  @Override
  public SearchScope getUseScope() {
    return new LocalSearchScope(getContainingFile());
  }
}
