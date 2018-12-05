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
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.types.PerlType;
import com.perl5.lang.perl.types.PerlTypeNamespace;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 01.06.2015.
 */
public abstract class PerlVariableDeclarationExprMixin extends PerlCompositeElementImpl implements PerlVariableDeclarationExpr {
  public PerlVariableDeclarationExprMixin(ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public PerlType getDeclarationType() {
    PerlNamespaceElement namespaceElement = getNamespaceElement();
    if (namespaceElement != null) {
      return PerlTypeNamespace.fromNamespace(namespaceElement.getCanonicalName());
    }

    // assignment
    PsiElement parent = getParent();
    if(parent instanceof PsiPerlAssignExpr){
      PsiElement rightSide = ((PsiPerlAssignExpr)parent).getRightSide();
      if(rightSide instanceof PsiPerlExpr){
        return PerlPsiUtil.getPerlExpressionNamespace(rightSide);
      }
    }

    return null;
  }

  @Nullable
  @Override
  public PerlNamespaceElement getNamespaceElement() {
    return findChildByClass(PerlNamespaceElement.class);
  }
}
