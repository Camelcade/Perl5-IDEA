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

package com.perl5.lang.perl.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlTokenSetsEx;
import com.perl5.lang.perl.psi.PerlStringList;
import com.perl5.lang.perl.psi.PsiPerlCommaSequenceExpr;
import com.perl5.lang.perl.psi.PsiPerlParenthesisedExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class PerlArrayUtilCore {
  private PerlArrayUtilCore() {
  }

  /**
   * Traversing root element, expanding lists and collecting all elements to the plain one:
   * ($a, $b, ($c, $d)), qw/bla bla/ -> $a, $b, $c, $d, bla, bla;
   *
   * @param rootElement top-level container or a single element
   * @return passed or new List of found PsiElements
   */
  public static @NotNull List<PsiElement> collectListElements(@Nullable PsiElement rootElement) {
    return collectListElements(rootElement, new ArrayList<>());
  }

  public static @NotNull List<PsiElement> collectListElements(@Nullable PsiElement rootElement, @NotNull List<PsiElement> result) {
    if (rootElement == null || PerlTokenSetsEx.getCOMMENTS().contains(PsiUtilCore.getElementType(rootElement))) {
      return result;
    }

    if (rootElement instanceof PsiPerlParenthesisedExpr ||
        rootElement instanceof PsiPerlCommaSequenceExpr ||
        rootElement instanceof PerlStringList) {
      for (PsiElement childElement : rootElement.getChildren()) {
        collectListElements(childElement, result);
      }
    }
    else if (rootElement.getNode() instanceof CompositeElement) {
      result.add(rootElement);
    }
    return result;
  }
}
