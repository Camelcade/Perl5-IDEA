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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mason2.psi.MasonFlagsStatement;
import com.perl5.lang.perl.psi.PsiPerlCommaSequenceExpr;
import com.perl5.lang.perl.psi.impl.PsiPerlStatementImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.COMMA;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.FAT_COMMA;


public class MasonFlagsStatementImpl extends PsiPerlStatementImpl implements MasonFlagsStatement {
  public MasonFlagsStatementImpl(ASTNode node) {
    super(node);
  }

  @Override
  public void changeParentsList(@NotNull List<? super String> currentList) {
    String extendsFlagsValue = getExtendsFlagValue();
    if (extendsFlagsValue != null) {
      currentList.add(extendsFlagsValue);
    }
  }


  private @Nullable String getExtendsFlagValue() {
    PsiElement expr = getExpr();
    // fixme this can be a prototype for scanning a hash to map
    if (expr instanceof PsiPerlCommaSequenceExpr) {
      PsiElement currentElement = expr.getFirstChild();
      while (currentElement != null) {
        PsiElement keyElement = currentElement;

        // comma between key and val
        if (isNullOrNotComma(currentElement = PerlPsiUtil.getNextSignificantSibling(currentElement))) {
          break;
        }

        PsiElement valElement = PerlPsiUtil.getNextSignificantSibling(currentElement);

        if (valElement != null && StringUtil.equals("extends", ElementManipulators.getValueText(keyElement))) {
          return ElementManipulators.getValueText(valElement);
        }

        // comma between pairs
        if (isNullOrNotComma(currentElement = PerlPsiUtil.getNextSignificantSibling(currentElement))) {
          break;
        }

        currentElement = PerlPsiUtil.getNextSignificantSibling(currentElement);
      }
    }

    return null;
  }

  @Contract("null -> true")
  private boolean isNullOrNotComma(@Nullable PsiElement element) {
    if (element == null) {
      return true;
    }

    IElementType elementType = element.getNode().getElementType();
    return elementType != COMMA && elementType != FAT_COMMA;
  }
}
