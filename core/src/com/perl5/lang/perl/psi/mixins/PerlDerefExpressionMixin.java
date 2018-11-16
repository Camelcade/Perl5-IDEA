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
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.psi.PsiPerlDerefExpr;
import com.perl5.lang.perl.psi.impl.PsiPerlExprImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.OPERATOR_DEREFERENCE;

/**
 * Created by hurricup on 07.06.2015.
 */
public abstract class PerlDerefExpressionMixin extends PsiPerlExprImpl implements PsiPerlDerefExpr {
  public PerlDerefExpressionMixin(ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public String getPreviousElementNamespace(PsiElement methodElement) {
    // todo add some caching here
    if (methodElement == getFirstChild())    // first element
    {
      return PerlPackageUtil.getContextPackageName(this);
    }

    return getCurrentElementNamespace(methodElement.getPrevSibling());
  }

  @Nullable
  public String getCurrentElementNamespace(PsiElement currentElement) {
    IElementType currentElementType;
    while (PerlParserDefinition.WHITE_SPACE_AND_COMMENTS.contains(currentElementType = PsiUtilCore.getElementType(currentElement))
           || currentElementType == OPERATOR_DEREFERENCE) {
      currentElement = currentElement.getPrevSibling();
    }

    return PerlPsiUtil.getPerlExpressionNamespace(currentElement);
  }

  public String guessType() {
    PsiElement[] children = getChildren();
    if (children.length > 0) {
      return getCurrentElementNamespace(children[children.length - 1]);
    }
    return null;
  }
}
