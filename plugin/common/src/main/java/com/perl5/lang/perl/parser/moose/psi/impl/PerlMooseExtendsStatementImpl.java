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

package com.perl5.lang.perl.parser.moose.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseExtendsStatement;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PsiPerlCommaSequenceExpr;
import com.perl5.lang.perl.psi.PsiPerlStringList;
import com.perl5.lang.perl.psi.impl.PsiPerlStatementImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class PerlMooseExtendsStatementImpl extends PsiPerlStatementImpl implements PerlMooseExtendsStatement {
  public PerlMooseExtendsStatementImpl(ASTNode node) {
    super(node);
  }

  @Override
  public void changeParentsList(@NotNull List<? super String> currentList) {
    currentList.clear();
    currentList.addAll(getParentsList());
  }

  protected @NotNull List<String> getParentsList() {
    List<String> result = new ArrayList<>();

    PsiElement expr = getExpr();
    if (expr == null) {
      return result;
    }
    switch (expr) {
      case PerlString perlString -> {
        String content = ElementManipulators.getValueText(perlString);
        if (!content.isEmpty()) {
          result.add(content);
        }
      }
      case PsiPerlCommaSequenceExpr sequenceExpr -> {
        PsiElement element = sequenceExpr.getFirstChild();
        while (element != null) {
          if (element instanceof PerlString) {
            String content = ElementManipulators.getValueText(element);
            if (!content.isEmpty()) {
              result.add(content);
            }
          }
          element = element.getNextSibling();
        }
      }
      case PsiPerlStringList stringList -> {
        for (PsiElement element : PerlPsiUtil.collectStringElements(stringList.getFirstChild())) {
          String content = element.getText();
          if (!content.isEmpty()) {
            result.add(content);
          }
        }
      }
      default -> {
        // todo we need to somehow mark statement as bad
      }
    }

    return result;
  }

}
