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

package com.perl5.lang.perl.parser.moose.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.perl5.lang.perl.psi.PsiPerlCommaSequenceExpr
import com.perl5.lang.perl.psi.PsiPerlParenthesisedExpr
import com.perl5.lang.perl.psi.PsiPerlStatement
import com.perl5.lang.perl.psi.references.PerlSubReferenceSuper

class PerlMooseStringSubReferenceProvider : PsiReferenceProvider() {
  override fun getReferencesByElement(
    element: PsiElement,
    context: ProcessingContext
  ): Array<out PsiReference> {
    val provider = PsiTreeUtil.getParentOfType(
      element, PerlMooseStatementWithSubReference::class.java, true, PsiPerlStatement::class.java
    ) ?: return PsiReference.EMPTY_ARRAY

    if (!provider.hasStringSubReference()) return PsiReference.EMPTY_ARRAY

    var expr: PsiElement? = provider.getExpr() ?: return PsiReference.EMPTY_ARRAY

    if (expr is PsiPerlParenthesisedExpr) {
      expr = expr.firstChild
      if (expr != null) {
        expr = expr.nextSibling
      }
    }

    if (expr is PsiPerlCommaSequenceExpr) {
      val lastElement: PsiElement? = expr.lastChild

      if (PsiTreeUtil.isAncestor(expr, element, true) && !PsiTreeUtil.isAncestor(lastElement, element, true)) {
        return arrayOf<PsiReference>(PerlSubReferenceSuper(element))
      }
    }

    return PsiReference.EMPTY_ARRAY
  }
}